package luci.sixsixsix.powerampache2.chromecastplugin.domain.usecase

import luci.sixsixsix.powerampache2.chromecastplugin.domain.MusicFetcher
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song
import javax.inject.Inject

class GetQueueUseCase @Inject constructor(private val musicFetcher: MusicFetcher) {
    operator fun invoke(responseStr: String): List<Song> = musicFetcher.getQueue(responseStr)
}
