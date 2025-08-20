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
package luci.sixsixsix.powerampache2.chromecastplugin.domain.model

import luci.sixsixsix.powerampache2.chromecastplugin.domain.common.ERROR_INT

data class Song(
    val mediaId: String,
    val id: String = mediaId,
    val title: String,
    val album: MusicAttribute = MusicAttribute.emptyInstance(),
    val artist: MusicAttribute = MusicAttribute.emptyInstance(),
    val albumArtist: MusicAttribute = MusicAttribute.emptyInstance(),
    val songUrl: String = "",
    val imageUrl: String = "",
    val bitrate: Int = ERROR_INT,
    val streamBitrate: Int = ERROR_INT,
    val catalog: Int = ERROR_INT,
    val channels: Int = ERROR_INT,
    val composer: String = "",
    val filename: String = "",
    val genre: List<MusicAttribute> = listOf(),
    val mime: String? = null,
    val playCount: Int = ERROR_INT,
    val playlistTrackNumber: Int = ERROR_INT,
    val rateHz: Int = ERROR_INT,
    val size: Int = ERROR_INT,
    val time: Int = ERROR_INT,
    val trackNumber: Int = ERROR_INT,
    val year: Int = ERROR_INT,
    val name: String = "",
    val mode: String? = null,
    val artists: List<MusicAttribute> = listOf(),
    val flag: Int = 0,
    val streamFormat: String? = null,
    val format: String? = null,
    val streamMime: String? = null,
    val publisher: String? = null,
    val replayGainTrackGain: Float? = null,
    val replayGainTrackPeak: Float? = null,
    val disk: Int = ERROR_INT,
    val diskSubtitle: String = "",
    val mbId: String = "",
    val comment: String = "",
    val language: String = "",
    val lyrics: String = "",
    val albumMbId: String = "",
    val artistMbId: String = "",
    val albumArtistMbId: String = "",
    val averageRating: Float = 0f,
    val preciseRating: Float = 0f,
    val rating: Float = 0f,
): Comparable<Song> {
    override fun compareTo(other: Song): Int = mediaId.compareTo(other.mediaId)
}
