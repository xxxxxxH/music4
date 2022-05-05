package win.hapsunday.mplayer.widget

import android.app.Dialog
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import net.idik.lib.slimadapter.SlimAdapter
import net.idik.lib.slimadapter.SlimInjector
import net.idik.lib.slimadapter.viewinjector.IViewInjector
import win.hapsunday.mplayer.R
import win.hapsunday.mplayer.model.SleepModel
import win.hapsunday.mplayer.utils.getSleepTimeData

class SleepD : AAH_FabulousFragment() {

    var adapter:SlimAdapter?=null

    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.sleep_time, null)
        val recyclerView = v.findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val d = getSleepTimeData()
        adapter =
            SlimAdapter.create().register(R.layout.item_sleep, object : SlimInjector<SleepModel> {
                override fun onInject(
                    data: SleepModel,
                    injector: IViewInjector<out IViewInjector<*>>
                ) {
                    injector.text(R.id.itemSleepText, data.text)
                        .visibility(R.id.itemSleepSelect, if (data.select) View.VISIBLE else View.GONE)
                        .clicked(R.id.itemRoot) {
                            val p = d.indexOf(data)
                            for ((index, item) in d.withIndex()){
                                item.select = index == p
                            }
                            adapter?.notifyDataSetChanged()
                        }
                }

            })
        adapter!!.attachTo(recyclerView)
        adapter!!.updateData(d)
        setViewMain(v.findViewById(R.id.main))
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }
}