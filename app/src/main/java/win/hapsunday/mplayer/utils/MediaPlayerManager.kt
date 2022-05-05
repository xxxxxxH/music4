package win.hapsunday.mplayer.utils

import android.media.AudioManager
import android.media.MediaPlayer


class MediaPlayerManager {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    var musicPath: String? = null

    companion object {
        private var i: MediaPlayerManager? = null
            get() {
                field ?: run {
                    field = MediaPlayerManager()
                }
                return field
            }

        @Synchronized
        fun get(): MediaPlayerManager {
            return i!!
        }
    }

    fun getMediaPlayer(): MediaPlayer {
        return mediaPlayer
    }

    fun setPath() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.reset()
        mediaPlayer.setDataSource(musicPath)
        mediaPlayer.prepare()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    fun start() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun resume() {
        mediaPlayer.start()
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    fun duration(): Int {
        return mediaPlayer.duration
    }

    fun getCurrentPercent(): Int {
        val total = mediaPlayer.duration
        val current = mediaPlayer.currentPosition
        return current * 100 / total
    }

}