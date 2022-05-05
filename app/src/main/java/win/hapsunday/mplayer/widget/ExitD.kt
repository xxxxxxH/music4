package win.hapsunday.mplayer.widget

import android.app.Dialog
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.basic.BasicPage

class ExitD(private val activity: AppCompatActivity) : AAH_FabulousFragment() {
    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.exist, null)
        val adView = v.findViewById<FrameLayout>(R.id.adView)
        activity.lifecycleScope.launch(Dispatchers.IO) {
            (activity as BasicPage).showNativeAd {
                it?.let {
                    adView.removeAllViews()
                    adView.addView(it)
                }
            }
        }
        v.findViewById<TextView>(R.id.confirm).let {
            it.setOnClickListener {
                activity.finish()
            }
        }
        v.findViewById<TextView>(R.id.cancel).let {
            it.setOnClickListener { dismiss() }
        }
        isCancelable = false
        setViewMain(v.findViewById(R.id.main))
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }
}