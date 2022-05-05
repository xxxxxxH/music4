package win.hapsunday.mplayer.page

import kotlinx.android.synthetic.main.eq_center.*
import kotlinx.android.synthetic.main.eq_top.*
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage
import win.hapsunday.mplayer.utils.setCurrent

class EqPage:BasicPage(R.layout.eq_main) {
    override fun go() {
        eqBack.setOnClickListener { finish() }
        seek1.setCurrent()
        seek2.setCurrent()
        seek3.setCurrent()
        seek4.setCurrent()
        seek5.setCurrent()
        seek6.setCurrent()
        seek7.setCurrent()
    }
}