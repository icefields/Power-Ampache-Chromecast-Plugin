package luci.sixsixsix.powerampache2.chromecastplugin.domain

import kotlinx.coroutines.flow.StateFlow
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.MusicAttribute
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song

interface MusicFetcher {
    val currentQueueFlow: StateFlow<List<Song>>

    fun getQueue(responseStr: String): List<Song>

    companion object {
        val mockList = listOf(
            Song(
                id = "1",
                mediaId = "1",
                title = "Forced Birther",
                artist = MusicAttribute("1", "Sentiment Dissolve"),
                songUrl = "https://tari.ddns.net/play/index.php?ssid=a271b1d33a283bd7ed00d6a5a0dbcc2e&type=song&oid=24179&uid=18&player=api&name=Sentiment%20Dissolve%20-%20Forced%20Birther.flac",
                imageUrl = ""
            ),
            Song(
                id = "1",
                mediaId = "1",
                title = "Tolitarian Doctrine",
                artist = MusicAttribute("1", "Sentiment Dissolve"),
                songUrl = "https://tari.ddns.net/play/index.php?ssid=a271b1d33a283bd7ed00d6a5a0dbcc2e&type=song&oid=24183&uid=18&player=api&name=Sentiment%20Dissolve%20-%20Tolitarian%20Doctrine.flac",
                imageUrl = ""
            )
        )
    }
}
