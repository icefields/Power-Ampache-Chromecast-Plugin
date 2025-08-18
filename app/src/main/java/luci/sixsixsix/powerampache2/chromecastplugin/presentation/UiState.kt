package luci.sixsixsix.powerampache2.chromecastplugin.presentation

import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song

data class UiState(
    val isConnected: Boolean = false,
    val isPlaying: Boolean = false,
    val currentIndex: Int = 0,
    val currentSong: Song? = null
)
