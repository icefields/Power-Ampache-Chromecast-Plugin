package luci.sixsixsix.powerampache2.chromecastplugin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import luci.sixsixsix.powerampache2.chromecastplugin.presentation.SongListScreen

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SongListScreen()
        }
    }
}
