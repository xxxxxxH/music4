package win.hapsunday.mplayer.page

import androidx.core.view.GravityCompat
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_bottom.*
import kotlinx.android.synthetic.main.main_title.*
import kotlinx.android.synthetic.main.nv_main.*
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage
import win.hapsunday.mplayer.utils.BannerAdapter
import win.hapsunday.mplayer.utils.buildFloatActionButton
import win.hapsunday.mplayer.utils.next
import win.hapsunday.mplayer.widget.ExitD
import win.hapsunday.mplayer.widget.SleepD
import win.hapsunday.mplayer.widget.UsD

class MainPage : BasicPage(R.layout.activity_main) {

    private val exit by lazy {
        ExitD(this)
    }

    private val fab by lazy {
        buildFloatActionButton()
    }

    private val usD by lazy {
        UsD()
    }

    private val sleepD by lazy {
        SleepD()
    }

    override fun go() {
        eq.setOnClickListener { next(EqPage::class.java) }
        menu.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
        about.setOnClickListener {
            usD.setParentFab(fab)
            usD.show(supportFragmentManager, "")
        }
        sleep.setOnClickListener {
            sleepD.setParentFab(fab)
            sleepD.show(supportFragmentManager, "")
        }
        local.setOnClickListener {
            showInsertAd()
            next(LMPage::class.java)
        }
        initBanner()
    }

    private fun initBanner(){
        val array = arrayOf(
            R.mipmap.main2,
            0
        )
        val adapter = BannerAdapter(array.toMutableList(), this)
        banner.addBannerLifecycleObserver(this)
            .setAdapter(adapter)
            .indicator = CircleIndicator(this)
    }

    override fun onBackPressed() {
        exit.setParentFab(fab)
        exit.show(supportFragmentManager, "")
    }

    override fun onStart() {
        super.onStart()
        banner.start()
    }

    override fun onStop() {
        super.onStop()
        banner.stop()
    }

    override fun onDestroy() {
        banner.destroy();
        super.onDestroy()
    }
}