package win.hapsunday.mplayer.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class SongModel(
    var songId: Long = 0,
    var album: String = "",
    var albumId: Long = 0,
    var artist: String = "",
    var duration: Long = 0,
    var img_uri: Uri? = null,
    var path: String = "",
    var title: String = "",
    var history_date: String = "",
    var queue_id: Int = 0,
    var size: String = "",
    var trackNumber: Int = 0,
    var artistId: Long = 0,
    var date: Int = 0
) : Serializable,Parcelable
