package luci.sixsixsix.powerampache2.chromecastplugin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import luci.sixsixsix.powerampache2.chromecastplugin.presentation.SongListScreen
import luci.sixsixsix.powerampache2.chromecastplugin.presentation.theme.PowerAmpachePluginTheme

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PowerAmpachePluginTheme(darkTheme = true, dynamicColor = true) {
                SongListScreen()
            }
        }
    }
}
