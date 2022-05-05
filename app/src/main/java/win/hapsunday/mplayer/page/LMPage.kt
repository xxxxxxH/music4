package win.hapsunday.mplayer.page

import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.local_bottom.*
import kotlinx.android.synthetic.main.local_main.*
import kotlinx.android.synthetic.main.local_top.*
import net.idik.lib.slimadapter.SlimAdapter
import net.idik.lib.slimadapter.SlimInjector
import net.idik.lib.slimadapter.viewinjector.IViewInjector
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage
import win.hapsunday.mplayer.model.SongModel
import win.hapsunday.mplayer.utils.MediaPlayerManager
import win.hapsunday.mplayer.utils.getAllSongs

class LMPage :BasicPage(R.layout.local_main), MediaPlayer.OnCompletionListener{
    private val mediaPlayerManager by lazy {
        MediaPlayerManager.get()
    }
    override fun go() {
        localBack.setOnClickListener { finish() }
        recycler.layoutManager = LinearLayoutManager(this)
        getAllSongs {
            SlimAdapter.create().register(R.layout.item_song, object :SlimInjector<SongModel>{
                override fun onInject(
                    data: SongModel,
                    injector: IViewInjector<out IViewInjector<*>>
                ) {
                    val image = injector.findViewById<ImageView>(R.id.cover)
                    Glide.with(this@LMPage).load(data.img_uri).into(image)
                    injector.text(R.id.name, data.title)
                        .text(R.id.author, data.artist)
                        .clicked(R.id.itemRoot) {
                            songName.text = data.title
                            songAuthor.text = data.artist
                            Glide.with(this@LMPage).load(data.img_uri).into(coverImage)
                            mediaPlayerManager.getMediaPlayer().setOnCompletionListener(this@LMPage)
                            mediaPlayerManager.musicPath = data.path
                            mediaPlayerManager.setPath()
                            mediaPlayerManager.start()
                            play.setImageResource(R.mipmap.lo9)
                        }
                }

            }).attachTo(recycler).updateData(it)
        }
        play.setOnClickListener {
            if (mediaPlayerManager.isPlaying()){
                mediaPlayerManager.pause()
                play.setImageResource(R.mipmap.lo10)
            }else{
                mediaPlayerManager.resume()
                play.setImageResource(R.mipmap.lo9)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayerManager.isPlaying()){
            mediaPlayerManager.stop()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {

    }
}