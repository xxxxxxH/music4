package win.hapsunday.mplayer.basic

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.IATSplashEyeAd
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.utils.*

abstract class BasicPage(id: Int) : AppCompatActivity(id) {
    private var isBackground = false
    private var openAd: ATSplashAd? = null
    private var insertAd: MaxInterstitialAd? = null
    private var openId = ""
    private var insertId = ""
    private var nativeId = ""
    private var bannerId = ""
    private val openAdListener = object : BasicAdListener.openAdLisenter {
        override fun onAdLoaded() {
            "open onAdLoaded $openAd".printMsg()
        }

        override fun onNoAdError(p0: AdError?) {
            "open $p0".printMsg()
            getOpenAd()
        }

        override fun onAdDismiss(p0: ATAdInfo?, p1: IATSplashEyeAd?) {
            onSplashAdHidden()
            getOpenAd()
        }

    }
    private val insertAdListener = object : BasicAdListener.inertAdListener {
        override fun onAdLoaded(ad: MaxAd?) {
            "insert onAdLoaded $insertAd".printMsg()
        }

        override fun onAdHidden(ad: MaxAd?) {
            adLastTime = System.currentTimeMillis()
            getInsertAd()
            onInterstitialAdHidden()
        }


        override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
            getInsertAd()
        }

        override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
            getInsertAd()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openId = getString(R.string.top_on_open_ad_id)
        insertId = getString(R.string.lovin_insert_ad_id)
        nativeId = getString(R.string.lovin_native_ad_id)
        bannerId = getString(R.string.lovin_banner_ad_id)
        openAd = BasicApp.instance!!.openAd(openId, openAdListener)
        openAd!!.loadAd()
        openAd.printMsg()

        insertAd = BasicApp.instance!!.insertAd(insertId, this)
        insertAd!!.setListener(insertAdListener)
        insertAd!!.loadAd()
        insertAd.printMsg()

        go()
        addBannerAd()
    }

    abstract fun go()

    open fun onInterstitialAdHidden() {}

    open fun onSplashAdHidden() {}

    private fun getOpenAd() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            openAd?.onDestory()
            openAd = BasicApp.instance!!.openAd(openId, openAdListener)
            openAd?.loadAd()
        }
    }

    private fun getInsertAd() {
        lifecycleScope.launch(Dispatchers.IO) {
            insertAd?.destroy()
            delay(3500)
            insertAd = BasicApp.instance!!.insertAd(insertId, this@BasicPage)
            insertAd!!.setListener(insertAdListener)
            insertAd!!.loadAd()
        }

    }


    override fun onStop() {
        super.onStop()
        isBackground = isInBackground()
    }

    override fun onResume() {
        super.onResume()
        if (isBackground) {
            isBackground = false
            val content = findViewById<ViewGroup>(android.R.id.content)
            (content.getTag(R.id.open_ad_view_id) as? FrameLayout)?.let {
                showOpenAd(it)
            } ?: kotlin.run {
                FrameLayout(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    content.addView(this)
                    content.setTag(R.id.open_ad_view_id, this)
                    showOpenAd(this)
                }
            }
        }
    }

    private fun addBannerAd() {
        val content = findViewById<ViewGroup>(android.R.id.content)
        val frameLayout = FrameLayout(this)
        val p = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.layoutParams = p

        val linearLayout = LinearLayout(this)
        val p1 = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        linearLayout.layoutParams = p1

        val banner = BasicApp.instance!!.bannerAd(bannerId)
        "banner $banner".printMsg()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            banner.loadAd()
            withContext(Dispatchers.Main) {
                val p2 =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp2px(this@BasicPage, 50f)
                    )
                p2.gravity = Gravity.BOTTOM
                banner.layoutParams = p2
                linearLayout.addView(banner)
                frameLayout.addView(linearLayout)
                content.addView(frameLayout)
            }
        }
    }

    fun showOpenAd(itt: ViewGroup, isForce: Boolean = false): Boolean {
        return if (configModel.isOpenAdReplacedByInsertAd()) {
            showInsertAd(isForce = isForce)
        } else {
            showOpenAdImpl(itt)
        }
    }


    fun showInsertAd(
        showByPercent: Boolean = false,
        isForce: Boolean = false,
        tag: String = ""
    ): Boolean {
        if (isForce) {
            return showInsertAdImpl()
        } else {
            if (configModel.isCanShowInsertAd()) {
                if ((showByPercent && configModel.isCanShowByPercent()) || (!showByPercent)) {
                    if (System.currentTimeMillis() - adLastTime > configModel.insertAdOffset() * 1000) {
                        var s = false
                        if (adShownList.getOrNull(adShownIndex) == true) {
                            s = showInsertAdImpl(tag)
                        }
                        adShownIndex++
                        if (adShownIndex >= adShownList.size) {
                            adShownIndex = 0
                        }
                        return s
                    }
                }
            }
            return false
        }
    }

    private fun showInsertAdImpl(tag: String = ""): Boolean {
        insertAd?.let {
            if (it.isReady) {
                it.showAd(tag)
                return true
            }
        }
        return false
    }

    fun showOpenAdImpl(viewGroup: ViewGroup, tag: String = ""): Boolean {
        openAd?.let {
            if (it.isAdReady) {
                it.show(this, viewGroup)
                return true
            }
        }
        return false
    }

    fun showNativeAd(show: (MaxNativeAdView?) -> Unit) {
        val nativeAd = BasicApp.instance!!.nativeAd(nativeId)
        nativeAd.loadAd()
        nativeAd.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                super.onNativeAdLoaded(p0, p1)
                show(p0)
            }

            override fun onNativeAdLoadFailed(p0: String?, p1: MaxError?) {
                super.onNativeAdLoadFailed(p0, p1)
                p0.printMsg()
                p1.printMsg()
            }
        })
    }
}