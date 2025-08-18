package luci.sixsixsix.powerampache2.chromecastplugin

import android.content.Context
import androidx.core.net.toUri
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.MediaQueue
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import luci.sixsixsix.powerampache2.chromecastplugin.domain.common.defaultContentType
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song
import javax.inject.Inject

class ChromecastManager @Inject constructor(@ApplicationContext context: Context): /*MediaQueue.Callback(),*/ SessionManagerListener<CastSession> {
    private var currentSession: CastSession? = null
    private val castContext: CastContext by lazy { CastContext.getSharedInstance(context) }
    private val sessionManager by lazy { castContext.sessionManager }
    var onPlaybackStateChanged: ((Boolean) -> Unit)? = null
    var onConnectionStateChanged: ((Boolean) -> Unit)? = null
    //var onCurrentQueueIndexChanged: ((Int) -> Unit)? = null

    private val _currentQueueIndexStateFlow = MutableStateFlow<Int>(0)
    val currentQueueIndexStateFlow: StateFlow<Int> = _currentQueueIndexStateFlow

//    private val _remoteQueueSizeStateFlow = MutableStateFlow<Int>(0)
//    val remoteQueueSizeStateFlow: StateFlow<Int> = _remoteQueueSizeStateFlow

    // Remember last queue in case we need to (re)load on toggle
    private var lastQueue: List<Song> = emptyList()
    private var lastStartIndex: Int = 0

    private val remoteMediaCallback = object : RemoteMediaClient.Callback() {
        override fun onQueueStatusUpdated() {
            super.onQueueStatusUpdated()
            val client = currentSession?.remoteMediaClient ?: return
            client.currentItem?.media?.metadata?.getInt(MediaMetadata.KEY_QUEUE_ITEM_ID)?.let { itemId ->
                println("aaaa onQueueStatusUpdated $itemId")
                lastStartIndex = lastQueue.indexOfFirst { s -> s.mediaId == itemId.toString() }
                _currentQueueIndexStateFlow.value = lastStartIndex
            }
        }
        override fun onStatusUpdated() {
            super.onStatusUpdated()
            val client = currentSession?.remoteMediaClient ?: return
            onPlaybackStateChanged?.invoke(client.isPlaying)

            val status = client.mediaStatus ?: return
            if (status.playerState == MediaStatus.PLAYER_STATE_IDLE &&
                status.idleReason == MediaStatus.IDLE_REASON_FINISHED
            ) {
                if (lastQueue.isNotEmpty()) {
                    val nextIndex = (lastStartIndex + 1).coerceAtMost(lastQueue.lastIndex)
                    lastStartIndex = nextIndex
                    _currentQueueIndexStateFlow.value = lastStartIndex
                }
            }
        }
    }

    init {
        sessionManager.addSessionManagerListener(this, CastSession::class.java)
        currentSession = sessionManager.currentCastSession
        onConnectionStateChanged?.invoke(isConnected())

        currentSession?.remoteMediaClient?.let { client ->
            client.registerCallback(remoteMediaCallback)
            //client.mediaQueue.registerCallback(this)
        }
    }

    fun isConnected(): Boolean = currentSession?.isConnected == true

    fun connect() {
        castContext.sessionManager.currentCastSession?.let { currentSession = it }
        onConnectionStateChanged?.invoke(isConnected())
    }

    fun loadQueue(songs: List<Song>, startIndex: Int = 0) {
        if (startIndex < 0) return

        lastQueue = songs
        lastStartIndex = startIndex

        val items = songs.map { song ->
            val metadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK).apply {
                putInt(MediaMetadata.KEY_QUEUE_ITEM_ID, song.mediaId.toInt())
                putString(MediaMetadata.KEY_TITLE, song.name)
                putString(MediaMetadata.KEY_ARTIST, song.artist.name)
                addImage(WebImage(song.imageUrl.toUri()))
            }

            val mediaInfo = MediaInfo.Builder(song.songUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(song.mime?.ifBlank { defaultContentType } ?: defaultContentType)
                .setMetadata(metadata)
                .build()

            MediaQueueItem.Builder(mediaInfo)
                .setAutoplay(true)
                .setPreloadTime(5.0)
                .build()
        }.toTypedArray()

        currentSession?.remoteMediaClient?.apply {
            queueLoad(
                items,
                startIndex,
                MediaStatus.REPEAT_MODE_REPEAT_OFF,
                null
            )
            registerCallback(remoteMediaCallback)
        }
    }

    fun togglePlayPause() {
        val client = currentSession?.remoteMediaClient ?: return
        println("aaaa client.isPlaying ${client.isPlaying}")
        if (client.isPlaying) client.pause() else client.play()
        currentSession?.remoteMediaClient?.let { client ->
            if (client.mediaQueue.itemCount == 0 && lastQueue.isNotEmpty()) {
                loadQueue(lastQueue, lastStartIndex)
            }
            println("aaaa ${client.mediaQueue.itemCount} ${lastQueue.size}")
            //_remoteQueueSizeStateFlow.value = client.mediaQueue.itemCount
        }
    }

    fun queueNext() {
        currentSession?.remoteMediaClient?.queueNext(null)
    }

    fun queuePrev() {
        currentSession?.remoteMediaClient?.queuePrev(null)
    }

    fun pause() {
        currentSession?.remoteMediaClient?.pause()
        //onPlaybackStateChanged?.invoke(false)
    }

// ----- SessionManagerListener<CastSession> -----

    override fun onSessionStarted(session: CastSession, sessionId: String) {
        currentSession = session
        currentSession?.remoteMediaClient?.registerCallback(remoteMediaCallback)
        onConnectionStateChanged?.invoke(true)
    }

    override fun onSessionEnded(session: CastSession, error: Int) {
        currentSession = null
        onConnectionStateChanged?.invoke(false)
    }

    override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
        currentSession = session
        currentSession?.remoteMediaClient?.registerCallback(remoteMediaCallback)
        onConnectionStateChanged?.invoke(true)
    }

    override fun onSessionSuspended(session: CastSession, reason: Int) {
        currentSession = session
        onConnectionStateChanged?.invoke(false)
    }

    override fun onSessionResuming(p0: CastSession, p1: String) {}
    override fun onSessionResumeFailed(session: CastSession, error: Int) {}
    override fun onSessionStarting(session: CastSession) {}
    override fun onSessionEnding(session: CastSession) {}
    override fun onSessionStartFailed(session: CastSession, error: Int) {}

//    override fun mediaQueueChanged() {
//        super.mediaQueueChanged()
//        println("aaaa prev ${_remoteQueueSizeStateFlow.value}")
//        currentSession?.remoteMediaClient?.let { client ->
//            println("aaaa ${client.mediaQueue.itemCount}")
//
//            _remoteQueueSizeStateFlow.value = client.mediaQueue.itemCount
//        }
//    }
}
