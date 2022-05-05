package win.hapsunday.mplayer.utils

import androidx.appcompat.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.internal.ActivityLifecycleTracker
import win.hapsunday.mplayer.model.ConfigModel
import win.hapsunday.mplayer.model.UpdateModel
import win.hapsunday.mplayer.page.fromBase64
import win.hapsunday.mplayer.page.isBase64

fun handleResult1(s: String): String? {
    return try {
        StringBuffer(s).replace(1, 2, "").toString()
    } catch (e: Exception) {
        e.fillInStackTrace()
        null
    }
}

fun handleResult2(s: String): String? {
    return if (s.isBase64()) {
        s.toByteArray().fromBase64().decodeToString()
    } else {
        null
    }
}

fun handleResult3(s: String): ConfigModel? {
    return gson.fromJson(s, ConfigModel::class.java)
}

fun AppCompatActivity.handleResult4(pojo: ConfigModel): String? {
    configModel = pojo
    if (configModel.insertAdInvokeTime() != adInvokeTime || configModel.insertAdRealTime() != adRealTime) {
        adInvokeTime = configModel.insertAdInvokeTime()
        adRealTime = configModel.insertAdRealTime()
        adShownIndex = 0
        adLastTime = 0
        adShownList = mutableListOf<Boolean>().apply {
            if (adInvokeTime >= adRealTime) {
                (0 until adInvokeTime).forEach { _ ->
                    add(false)
                }
                (0 until adRealTime).forEach { index ->
                    set(index, true)
                }
            }
        }
    }
    if (configModel.faceBookId().isNotBlank()) {
        initFaceBook()
    }
    return pojo.info
}

fun handleResult5(s: String): String? {
    return if (s.isBase64()) {
        s.toByteArray().fromBase64().decodeToString()
    } else {
        null
    }
}

fun handleResult6(s: String): UpdateModel? {
    return gson.fromJson(s, UpdateModel::class.java)
}

fun handleResult7(pojo: UpdateModel) {
    updateModel = pojo
}

fun AppCompatActivity.initFaceBook() {
    FacebookSdk.apply {
        setApplicationId(configModel.faceBookId())
        sdkInitialize(this@initFaceBook)
        ActivityLifecycleTracker.apply {
            onActivityCreated(this@initFaceBook)
            onActivityResumed(this@initFaceBook)
        }
    }
}