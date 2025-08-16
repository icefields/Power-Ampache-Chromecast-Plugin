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

    override fun getQueue(responseStr: String): List<Song> =
        gson.fromJson(responseStr, QueueDto::class.java).queue.also { _currentQueueFlow.value = it }
}
