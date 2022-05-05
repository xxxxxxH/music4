package win.hapsunday.mplayer.widget

import android.app.Dialog
import android.view.View
import android.widget.TextView
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import win.hapsunday.mplayer.R

class UsD():AAH_FabulousFragment() {
    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.us, null)
        v.findViewById<TextView>(R.id.confirm).let {
            it.setOnClickListener { dismiss() }
        }
        v.findViewById<TextView>(R.id.cancel).let {
            it.setOnClickListener { dismiss() }
        }
        setViewMain(v.findViewById(R.id.main))
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }
}