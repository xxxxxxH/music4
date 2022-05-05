package win.hapsunday.mplayer.basic

import android.app.Activity
import android.app.Application
import android.content.Context
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdListener
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import org.xutils.x
import win.hapsunday.mplayer.BuildConfig
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.utils.printMsg

class BasicApp : Application() {
    companion object {
        var instance: BasicApp? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        IU.initialize(this)
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        IU.getInstance().initStartUp()
        x.Ext.init(this)
        x.Ext.setDebug(BuildConfig.DEBUG)
    }


    fun insertAd(id: String, ac: Activity): MaxInterstitialAd {
        return MaxInterstitialAd(id, IU.getInstance().lovinSdk, ac)
    }

    fun nativeAd(id: String): MaxNativeAdLoader {
        return MaxNativeAdLoader(id, IU.getInstance().lovinSdk, this)
    }

    fun bannerAd(id: String): MaxAdView {
        return MaxAdView(id, IU.getInstance().lovinSdk, this)
    }

    fun openAd(id: String, listener: ATSplashAdListener?): ATSplashAd {
        return ATSplashAd(this, id, listener)
    }
}