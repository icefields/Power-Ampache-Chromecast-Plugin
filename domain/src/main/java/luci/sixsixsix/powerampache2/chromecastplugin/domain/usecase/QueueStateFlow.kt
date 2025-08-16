package luci.sixsixsix.powerampache2.chromecastplugin.domain.usecase

import luci.sixsixsix.powerampache2.chromecastplugin.domain.MusicFetcher
import javax.inject.Inject

class QueueStateFlow @Inject constructor(private val musicFetcher: MusicFetcher) {
    operator fun invoke() = musicFetcher.currentQueueFlow
}
