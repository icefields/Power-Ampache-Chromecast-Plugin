package luci.sixsixsix.powerampache2.chromecastplugin.data.dto

import com.google.gson.annotations.SerializedName
import luci.sixsixsix.powerampache2.chromecastplugin.domain.model.Song

data class QueueDto(
    @SerializedName("queue")
    val queue: List<Song> = listOf()
)
