package win.hapsunday.mplayer.utils

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hjq.permissions.XXPermissions
import org.xutils.common.Callback
import org.xutils.http.RequestParams
import org.xutils.x
import win.hapsunday.mplayer.basic.BasicApp


fun dp2px(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun isInBackground(): Boolean {
    val activityManager = BasicApp.instance!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
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

fun SeekBar.setCurrent(){
    val seek = (10..100).random()
    progress = seek
}

fun AppCompatActivity.next(clazz: Class<*>, isFinish: Boolean = false) {
    startActivity(Intent(this, clazz))
    if (isFinish) {
        finish()
    }
}