package win.hapsunday.mplayer.page

import android.view.View
import kotlinx.android.synthetic.main.index_layout.*
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage
import win.hapsunday.mplayer.utils.*

class IndexPage : BasicPage(R.layout.index_layout) {
    private var showMax = false
    override fun go() {
        per {
            if (it) {
                getConfig({
//                    if (isLogin) {
//                        showOpenAd(root, isForce = true)
//                        return@getConfig
//                    }
////                    if (configModel.needLogin()){
////                        login.visibility = View.VISIBLE
////                        return@getConfig
////                    }
//                    showOpen()
                }, {
//                    showOpen()
                })
            } else {

            }
        }
        login.setOnClickListener { next(SignInPage::class.java) }
    }

    private fun showOpen(){
        if (showOpenAd(root, isForce = true)){
            showMax = true
            return
        }
//        next(MainPage::class.java, true)
    }

    override fun onInterstitialAdHidden() {
        super.onInterstitialAdHidden()
//        if (configModel.isOpenAdReplacedByInsertAd()) {
//            if (showMax){
//                showMax = !showMax
//                next(MainPage::class.java, true)
//            }
//
//        }
    }

    override fun onSplashAdHidden() {
        super.onSplashAdHidden()
//        if (showMax){
//            showMax = !showMax
//            next(MainPage::class.java, true)
//        }
    }
}