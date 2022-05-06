package win.hapsunday.mplayer.page

import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.theme_center.*
import kotlinx.android.synthetic.main.theme_top.*
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage

class ThemePage:BasicPage(R.layout.theme_main) {
    override fun go() {
        themeBack.setOnClickListener { finish() }
    }

    fun click(view:View){
        when(view.contentDescription){
            "1"->{
                imageCenter.setImageResource(R.mipmap.large_1)
            }
            "2" -> {
                imageCenter.setImageResource(R.mipmap.large_2)
            }
            "3" -> {
                imageCenter.setImageResource(R.mipmap.large_3)
            }
            "4" -> {
                imageCenter.setImageResource(R.mipmap.large_4)
            }
        }
    }
}