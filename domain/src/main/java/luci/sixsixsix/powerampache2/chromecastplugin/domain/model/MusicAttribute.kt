package luci.sixsixsix.powerampache2.chromecastplugin.domain.model

import java.util.UUID

data class MusicAttribute(
    val id: String,
    val name: String
) {
    companion object {
        fun emptyInstance(): MusicAttribute = MusicAttribute("","")
        fun randomInstance(): MusicAttribute =
            MusicAttribute(UUID.randomUUID().toString(), UUID.randomUUID().toString().drop(24))

    }
}
