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
package luci.sixsixsix.powerampache2.chromecastplugin.data

import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import luci.sixsixsix.powerampache2.chromecastplugin.data.dto.QueueDto
import luci.sixsixsix.powerampache2.chromecastplugin.domain.MusicFetcher
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicFetcherImpl @Inject constructor(): MusicFetcher {
    private val _currentQueueFlow = MutableStateFlow<List<Song>>(emptyList())
    override val currentQueueFlow: StateFlow<List<Song>> = _currentQueueFlow
    private val gson = Gson()

    override fun parseQueue(responseStr: String): List<Song> =
        gson.fromJson(responseStr, QueueDto::class.java).queue.also { _currentQueueFlow.value = it }
}
