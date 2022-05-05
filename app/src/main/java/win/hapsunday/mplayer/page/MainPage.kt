package win.hapsunday.mplayer.page

import androidx.core.view.GravityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_title.*
import kotlinx.android.synthetic.main.nv_main.*
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage
import win.hapsunday.mplayer.utils.buildFloatActionButton
import win.hapsunday.mplayer.utils.next
import win.hapsunday.mplayer.widget.ExitD

class MainPage : BasicPage(R.layout.activity_main) {

    private val exit by lazy {
        ExitD(this)
    }

    private val fab by lazy {
        buildFloatActionButton()
    }

    override fun go() {
    eq.setOnClickListener { next(EqPage::class.java) }
        menu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
    }

    override fun onBackPressed() {
        exit.setParentFab(fab)
        exit.show(supportFragmentManager, "")
    }
}