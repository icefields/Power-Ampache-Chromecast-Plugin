/**
 * Copyright (C) 2025  Antonio Tari
 *
 * This file is a part of Power Ampache 2
 * Ampache Android client application
 * @author Antonio Tari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package luci.sixsixsix.powerampache2.chromecastplugin.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.mediarouter.app.MediaRouteButton
import coil.compose.AsyncImage
import com.google.android.gms.cast.framework.CastButtonFactory
import luci.sixsixsix.powerampache2.chromecastplugin.R

val mainFontSize = 16.sp
val smallFontSize = 12.sp
val screenPadding
    @Composable get() = dimensionResource(R.dimen.screen_padding)

val surfaceVariantLight = Color(0xFFDFE5E3)

@Composable
fun SongListScreen(viewModel: ChromecastViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val queue = viewModel.queueStateFlow.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
            .padding(bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()),
        topBar = {
            TopAppBar(
                //backgroundColor = MaterialTheme.colors.primary,
                title = {
                    Text(
                        text = stringResource(R.string.app_name_topBar),
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = screenPadding),//.basicMarquee(),
                        textAlign = TextAlign.Start
                    )
                },
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colors.secondary.copy(alpha = 0.5f),
                        modifier = Modifier.wrapContentSize()
                    ) {
                        CastActionButton(viewModel.needsConnection())
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                cutoutShape = null
            ) {
                uiState.value.currentSong?.let { currentSong ->
                    AsyncImage(currentSong.imageUrl, contentDescription = null)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentSong.name,
                            style = MaterialTheme.typography.subtitle1,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = currentSong.artist.name,
                            style = MaterialTheme.typography.body2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }


                IconButton(onClick = { viewModel.prev() }) {
                    Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                }
                IconButton(onClick = { viewModel.togglePlayPause() }) {
                    Icon(
                        if (uiState.value.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (uiState.value.isPlaying) "Pause" else "Play"
                    )
                }
                IconButton(onClick = { viewModel.next() }) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next")
                }
            }
        }
    ) { padding ->

        if (queue.value.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.mainScreen_emptyQueue_title),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center)
                Text(stringResource(R.string.mainScreen_emptyQueue_subtitle),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center)
            }
        }

        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(queue.value) { song ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .fillMaxWidth()
                        .background(
                            if (uiState.value.currentIndex == queue.value.indexOf(song)) {
                                colorResource(R.color.surfaceVariant)
                            } else { Color.Transparent }
                        )
                        .clickable { viewModel.playSong(song) }
                ) {
                    AsyncImage(
                        song.imageUrl,
                        modifier = Modifier.fillMaxWidth(0.2f).aspectRatio(1f),
                        contentDescription = null
                    )
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(song.name, style = MaterialTheme.typography.body1)
                        Text(song.artist.name, style = MaterialTheme.typography.caption)
                    }
                }
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
