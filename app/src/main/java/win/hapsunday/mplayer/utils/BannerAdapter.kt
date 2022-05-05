package win.hapsunday.mplayer.utils

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import win.hapsunday.mplayer.basic.BasicPage


class BannerAdapter(data: List<Int>, activity: Activity) : BannerAdapter<Int, win.hapsunday.mplayer.utils.BannerAdapter.BannerViewHolder>(data) {

    private var activity:Activity?=null

    init {
        this.activity = activity
    }

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout = view as RelativeLayout
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val layout = RelativeLayout(parent!!.context)
        layout.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return BannerViewHolder(layout)
    }

    override fun onBindView(holder: BannerViewHolder, data: Int, position: Int, size: Int) {
        when (data) {
            0 -> {
                (activity as BasicPage).showNativeAd {
                    it?.let {
                        it.layoutParams = getParams()
                        holder.layout.addView(it)
                    }
                }

            }
            else -> {
                val image = ImageView(holder.layout.context)
                image.setImageResource(data)
                image.layoutParams = getParams()
                holder.layout.addView(image)
            }
        }
    }

    private fun getParams(): RelativeLayout.LayoutParams {
        return RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
    }
}