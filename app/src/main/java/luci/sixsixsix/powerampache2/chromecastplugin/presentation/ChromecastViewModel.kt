package luci.sixsixsix.powerampache2.chromecastplugin.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import luci.sixsixsix.powerampache2.chromecastplugin.ChromecastManager
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song
import luci.sixsixsix.powerampache2.chromecastplugin.domain.usecase.GetQueueUseCase
import luci.sixsixsix.powerampache2.chromecastplugin.domain.usecase.QueueStateFlow
import javax.inject.Inject

@HiltViewModel
class ChromecastViewModel @Inject constructor(
    queueStateFlowUseCase: QueueStateFlow,
    private val chromecastManager: ChromecastManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val queueStateFlow = queueStateFlowUseCase()
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf())


    init {
        //chromecastManager.initialize()
        loadSongs()
        updateConnectionStatus()
        viewModelScope.launch {
            queueStateFlow.collect {
                println("collected aaaa ${it.size}")
            }
        }
    }

    fun loadSongs() {
//        viewModelScope.launch {
//            val queue = getQueueUseCase()
//            _uiState.value = _uiState.value.copy(songs = queue)
//        }
    }

    fun updateConnectionStatus() {
        _uiState.value = _uiState.value.copy(isConnected = chromecastManager.isConnected())
    }

    fun connectChromecast() {
        chromecastManager.connect()
    }

    fun playSong(song: Song) {
        chromecastManager.playSong(song.songUrl, song.name, song.artist.name)
    }

    fun needsConnection() = !chromecastManager.isConnected()
}
