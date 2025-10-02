/**
 * Copyright (C) 2025  Antonio Tari
 *
 * This file is a part of Power Ampache 2
 * Ampache Android client application
 * @author Antonio Tari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package luci.sixsixsix.powerampache2.chromecastplugin.presentation

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import luci.sixsixsix.powerampache2.chromecastplugin.ChromecastManager
import luci.sixsixsix.powerampache2.chromecastplugin.R
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song
import luci.sixsixsix.powerampache2.chromecastplugin.domain.usecase.QueueStateFlow
import javax.inject.Inject

@HiltViewModel
class ChromecastViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    queueStateFlowUseCase: QueueStateFlow,
    private val chromecastManager: ChromecastManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val queueStateFlow = queueStateFlowUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())

    init {
        updateConnectionStatus()
        observePlayback()
        observeConnectionState()
    }

    private fun observeConnectionState() {
        // Observe connection state
        chromecastManager.onConnectionStateChanged = { connected ->
            _uiState.value = _uiState.value.copy(isConnected = connected)
        }
    }

    private fun loadQueue(
        songs: List<Song> = queueStateFlow.value,
        startIndex: Int = uiState.value.currentIndex.takeIf { it >= 0 } ?: 0
    ) = chromecastManager.loadQueue(songs, startIndex)

    private fun observePlayback() {
        chromecastManager.onPlaybackStateChanged = { playing ->
            _uiState.value = _uiState.value.copy(isPlaying = playing)
        }

        // observe index changes
        viewModelScope.launch {
            chromecastManager.currentQueueIndexStateFlow.collectLatest { index ->
                _uiState.value = _uiState.value.copy(
                    currentIndex = index.takeIf { it >= 0 } ?: 0,
                    currentSong = queueStateFlow.value.getOrNull(index) ?: _uiState.value.currentSong
                        .also { println("aaaa queueStateFlow.value.getOrNull 83 ${it?.name ?: "NULL"}   index $index") }
                )
            }
        }

        // observe queue changes
        viewModelScope.launch {
            queueStateFlow.collectLatest {
                _uiState.value = _uiState.value.copy(
                    currentIndex = 0,
                    currentSong = it.getOrNull(0)
                        .also { println("aaaa queueStateFlow.value.getOrNull 94 ${it?.name ?: "NULL"}") }
                )
                loadQueue()
            }
        }
    }

    private fun checkWarnConnection() =
        (uiState.value.isConnected == true).also { isConnected ->
            if (!isConnected) Toast.makeText(context, R.string.toast_noConnection_warning, Toast.LENGTH_LONG).show()
        }

    /**
     * Start playback at this song by sending the whole queue to Chromecast and
     * telling it to start at this index. Cast will auto-advance afterwards.
     */
    fun playSong(song: Song) {
        val queue = queueStateFlow.value
        val index = queue.indexOf(song).takeIf { it >= 0 } ?: 0
        _uiState.value = _uiState.value.copy(currentIndex = index)

        if (!checkWarnConnection()) return

        loadQueue(queue, startIndex = index)
    }

    /**
     * Bottom bar play/pause: if nothing is loaded yet, load queue & start;
     * otherwise toggle. This fixes the “first press does nothing” issue.
     */
    fun togglePlayPause() {
        if (!checkWarnConnection()) return
        if (uiState.value.currentSong == null) return

        if (uiState.value.isPlaying) {
            chromecastManager.pause()
        } else {
            chromecastManager.togglePlayPause()
        }
    }

    fun next() {
        if (!checkWarnConnection()) return
        val lastIndex = (queueStateFlow.value.size - 1).coerceAtLeast(0)
        val nextIndex = (_uiState.value.currentIndex + 1).coerceAtMost(lastIndex)
        _uiState.value = _uiState.value.copy(currentIndex = nextIndex)
        chromecastManager.queueNext()
    }

    fun prev() {
        if (!checkWarnConnection()) return
        val prevIndex = (_uiState.value.currentIndex - 1).coerceAtLeast(0)
        _uiState.value = _uiState.value.copy(currentIndex = prevIndex)
        chromecastManager.queuePrev()
    }

    fun updateConnectionStatus() {
        _uiState.value = _uiState.value.copy(isConnected = chromecastManager.isConnected())
    }

    fun connectChromecast() {
        chromecastManager.connect()
    }

    fun needsConnection() = !chromecastManager.isConnected()
}
