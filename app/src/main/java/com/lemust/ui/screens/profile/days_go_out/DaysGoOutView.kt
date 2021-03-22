package com.lemust.ui.screens.profile.days_go_out

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.profile.days_go_out.adapter.WeekDayItem
import com.lemust.ui.screens.profile.days_go_out.adapter.WeekDaysAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_days_go_out.view.*
import kotlinx.android.synthetic.main.loader.view.*


class DaysGoOutView(var fragment: BaseActivity, var root: View) : DaysGoOutContract.View, BaseView(fragment!!) {

    private var recyclerMusicTypes = root.findViewById<RecyclerView>(R.id.rv_days)
    private var musicItemsItems = ArrayList<WeekDayItem>()
    private var placeTypeAdapter: WeekDaysAdapter? = null

    init {
        initRecycler()

        root.iv_close.setOnClickListener { fragment.onBackPressed() }
    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }

    override fun getDays(): ArrayList<WeekDayItem> {
        return placeTypeAdapter!!.itemsData
    }

    private fun saveDays() {

    }

    //    override fun onBackAction(): Observable<Any> {
//        return RxView.clicks(root.iv_close)
//    }
    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }

    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(fragment!!, LinearLayoutManager.VERTICAL, false)
        recyclerMusicTypes.layoutManager = managerSubFilters
        placeTypeAdapter = WeekDaysAdapter(musicItemsItems)
        recyclerMusicTypes.adapter = placeTypeAdapter
        recyclerMusicTypes.isNestedScrollingEnabled = false;

    }

    override fun setMusicItems(items: List<WeekDayItem>) {
        musicItemsItems.clear()
        musicItemsItems.addAll(items)
        placeTypeAdapter!!.notifyDataSetChanged()
    }

    override fun isShowProgressLoader(isShow: Boolean) {
        (fragment).showDefaultProgressLoader(isShow)

    }
}