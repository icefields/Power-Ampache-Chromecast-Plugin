package luci.sixsixsix.powerampache2.chromecastplugin.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.mediarouter.app.MediaRouteButton
import coil.compose.AsyncImage
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.material.appbar.MaterialToolbar
import luci.sixsixsix.powerampache2.chromecastplugin.R

val mainFontSize = 16.sp
val smallFontSize = 12.sp
val screenPadding
    @Composable get() = dimensionResource(R.dimen.screen_padding)

@Composable
fun SongListScreen(viewModel: ChromecastViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val queue = viewModel.queueStateFlow.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding()),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    Text(
                        text = stringResource(R.string.app_name_topBar),
                        fontSize = 17.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = screenPadding).basicMarquee(),
                        textAlign = TextAlign.Start
                    )
                },
                actions = { CastActionButton(viewModel.needsConnection()) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(queue.value) { song ->
                Row(modifier = Modifier.fillMaxWidth()) {

                    AsyncImage(
                        song.imageUrl,
                        modifier = Modifier.fillMaxWidth(0.2f),
                        contentDescription = null
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.playSong(song) }
                            .padding(16.dp)
                    ) {
                        Text(song.name, style = MaterialTheme.typography.subtitle1)
                        Text(song.artist.name, style = MaterialTheme.typography.body1)
                    }
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
            MediaRouteButton(ctx).apply {
                CastButtonFactory.setUpMediaRouteButton(ctx, this)
                // Trigger dialog if not connected
                if (needsConnection) { post { performClick() } }
            }
        }
    )
}

@Composable
fun MainTopBar(modifier: Modifier = Modifier) {
    Column(modifier) {
        Spacer(Modifier
            .fillMaxWidth()
            .height(WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
            .background(MaterialTheme.colors.onBackground)
        )

        Spacer(Modifier.Companion.height(screenPadding))
        Text(
            text = stringResource(R.string.app_name_topBar),
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.fillMaxWidth().padding(horizontal = screenPadding),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.Companion.height(screenPadding))
    }
}