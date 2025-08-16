package luci.sixsixsix.powerampache2.chromecastplugin.domain

import kotlinx.coroutines.flow.StateFlow
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.MusicAttribute
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song

interface MusicFetcher {
    val currentQueueFlow: StateFlow<List<Song>>

    fun parseQueue(responseStr: String): List<Song>
}
