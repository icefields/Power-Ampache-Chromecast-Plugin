package luci.sixsixsix.powerampache2.chromecastplugin

import android.content.Context
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.common.api.Status
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class ChromecastManager @Inject constructor(@ApplicationContext context: Context): SessionManagerListener<CastSession> {

//    private val castContext: CastContext = CastContext.getSharedInstance(context)
//    private val sessionManager = castContext.sessionManager

    private var currentSession: CastSession? = null

    private val castContext: CastContext by lazy {
        CastContext.getSharedInstance(context)
    }

    private val sessionManager by lazy {
        castContext.sessionManager
    }

    init {
        sessionManager.addSessionManagerListener(this, CastSession::class.java)

        // Attempt to restore existing session on startup
        currentSession = sessionManager.currentCastSession
    }

    fun isConnected(): Boolean = currentSession?.isConnected == true

    fun connect() {
        // Cast button UI triggers device chooser
        castContext.sessionManager.currentCastSession?.let {
            currentSession = it
        }
    }

    fun playSong(url: String, title: String, artist: String) {
        val metadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK).apply {
            putString(MediaMetadata.KEY_TITLE, title)
            putString(MediaMetadata.KEY_ARTIST, artist)
        }

        val mediaInfo = MediaInfo.Builder(url)
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("audio/mp3")
            .setMetadata(metadata)
            .build()

        currentSession?.remoteMediaClient?.load(
            MediaLoadRequestData.Builder()
                .setMediaInfo(mediaInfo)
                .build()
        )
    }

    override fun onSessionStarted(session: CastSession, sessionId: String) {
        currentSession = session
    }

    override fun onSessionEnded(session: CastSession, error: Int) {
        currentSession = null
    }

    override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
        currentSession = session
    }

    override fun onSessionResuming(p0: CastSession, p1: String) {}

    override fun onSessionResumeFailed(session: CastSession, error: Int) {}
    override fun onSessionStarting(session: CastSession) {}
    override fun onSessionEnding(session: CastSession) {}
    override fun onSessionSuspended(session: CastSession, reason: Int) {}
    override fun onSessionStartFailed(session: CastSession, error: Int) {}
}
