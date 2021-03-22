package com.lemust.ui.screens.profile.occupation

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.profile.occupation.step1.adapter.OccupationAdapter
import com.lemust.ui.screens.profile.occupation.step1.adapter.OccupationItem
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_step1_ocuppation.view.*
import kotlinx.android.synthetic.main.loader.view.*


class Step1OccupationView(var fragment: OccupationActivity, var root: View) : Step1OccupationContract.View, BaseView(fragment!!) {
    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }
//    override fun openNextActivity(isClicable: Boolean) {
//        var t = Step3OccupationFragment.newInstance(isClicable, "")
//        ( fragment.activity as OccupationActivity).addFragmet(t)
//    }

    override fun changeTextInProgressBar(text: String) {
        root.title_loading.text = text
    }
    override fun isClicableFragmatnt(isClicable: Boolean) {
        if (isClicable) {
            root.isClickable = true
        } else {
            root.isClickable = false

        }
    }

    override fun closeScreen() {
        fragment!!.finish()
    }

    override fun onBackAction(): Observable<Any> {
        return fragment.onBackPressedListener
    }

    override fun onItemClickedAction(): Observable<Any> {
        return placeTypeAdapter!!.clickAction
    }

    override fun getItems(): ArrayList<OccupationItem> {
        return placeTypeAdapter!!.itemsData
    }


    override fun onNextAction(): Observable<Any> {
        return RxView.clicks(root.btn_next)
    }

    override fun isEnableButtonSent(isEnabled: Boolean) {
        if (isEnabled) {
            root.btn_next.isEnabled = true
            root.btn_next.setBackgroundResource(R.drawable.button_filter_selector)
        } else {
            root.btn_next.isEnabled = false
            root.btn_next.setBackgroundResource(R.drawable.background_preview_pressed)


        }
    }

    private var recyclerMusicTypes = root.findViewById<RecyclerView>(R.id.rv_occupation)
    private var musicItemsItems = ArrayList<OccupationItem>()
    private var placeTypeAdapter: OccupationAdapter? = null

    init {
        initRecycler()
        root.iv_close.setOnClickListener {
            fragment!!.onBackPressed()
        }


    }

    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(fragment!!, LinearLayoutManager.VERTICAL, false)
        recyclerMusicTypes.layoutManager = managerSubFilters
        placeTypeAdapter = OccupationAdapter(musicItemsItems)
        recyclerMusicTypes.adapter = placeTypeAdapter
        recyclerMusicTypes.isNestedScrollingEnabled = false;

    }

    override fun isShowProgressLoader(isShow: Boolean) {
        if (isShow) {
            root.pb_map!!.visibility = View.VISIBLE
        } else {
            root.pb_map!!!!.visibility = View.GONE
        }

    }

    override fun setOccupations(items: List<OccupationItem>) {
        musicItemsItems.clear()
        musicItemsItems.addAll(items)
        placeTypeAdapter!!.notifyDataSetChanged()
    }

}