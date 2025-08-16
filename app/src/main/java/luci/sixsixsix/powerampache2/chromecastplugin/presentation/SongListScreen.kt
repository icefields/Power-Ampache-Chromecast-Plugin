package luci.sixsixsix.powerampache2.chromecastplugin.presentation

import android.content.Context
import android.view.LayoutInflater
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.appbar.MaterialToolbar

@Composable
fun SongListScreen(viewModel: ChromecastViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val queue = viewModel.queueStateFlow.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chromecast Player") },
                actions = {
                    CastActionButton(viewModel.needsConnection())
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(queue.value) { song ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.playSong(song) }
                        .padding(16.dp)
                ) {
                    Text(song.name, style = MaterialTheme.typography.subtitle1)
                    Text(song.artist.name, style = MaterialTheme.typography.body1)
                }
                Divider()
            }
        }
    }
}

@Composable
fun CastActionButton(needsConnection: Boolean) {
    AndroidView(
        modifier = Modifier.size(48.dp),
        factory = { ctx ->
            androidx.mediarouter.app.MediaRouteButton(ctx).apply {
                CastButtonFactory.setUpMediaRouteButton(ctx, this)

                // Trigger dialog if not connected
                if (needsConnection) {
                    post { performClick() }
                }
            }
        }
    )
}
