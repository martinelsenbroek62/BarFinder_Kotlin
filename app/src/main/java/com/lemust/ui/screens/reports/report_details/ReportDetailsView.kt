package com.lemust.ui.screens.reports.report_details

//import com.stfalcon.frescoimageviewer.ImageViewer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.reports.report_details.ReportDetailsPresenter.Companion.EDIT_ITEM_ID
import com.lemust.ui.screens.reports.report_details.adapter.Base
import com.lemust.ui.screens.reports.report_details.adapter.EditPlaceName
import com.lemust.ui.screens.reports.report_details.adapter.PlaceDetailsAdapter
import com.lemust.ui.screens.reports.report_details.adapter.TextItem
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_report_place_details.view.*
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


class ReportDetailsView(var ctx: BaseActivity, var root: View) : ReportDetailsContract.View, BaseView(ctx) {


    private var recyclerDetils = root.findViewById<RecyclerView>(R.id.rv_place_details)
    private var detailsItems = ArrayList<Base>()
    private var detailsAdapter: PlaceDetailsAdapter? = null

    init {
        root.iv_close.setOnClickListener {
            ctx.finish()
        }
        initRecycler()
        OverScrollDecoratorHelper.setUpOverScroll(recyclerDetils, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);


    }

    override fun onDestroy() {
        detailsAdapter!!.onAddNewItem.onComplete()
        detailsAdapter!!.onEditPlaceNameScreen.onComplete()
    }

    private fun initRecycler() {
        recyclerDetils.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        detailsAdapter = PlaceDetailsAdapter(detailsItems)
        recyclerDetils.adapter = detailsAdapter
        recyclerDetils.isNestedScrollingEnabled = false;

    }

    override fun isShowProgressLoader(isShow: Boolean) {
        ctx.showDefaultProgressLoader(isShow)

    }


    override fun setDetails(hours: ArrayList<Base>) {
        detailsItems.addAll(hours)
        detailsAdapter!!.notifyDataSetChanged()
    }

    override fun onSendReportAction(): Observable<Any> {
        return RxView.clicks(root.tv_send_report)
    }

    override fun addNewItem(item: TextItem, position: Int) {

        detailsItems.add(position, item)
        detailsAdapter!!.notifyDataSetChanged()
    }

    override fun onNewItemAction(): Observable<PlaceDetailsAdapter.NewItem> {
        return detailsAdapter!!.onAddNewItem
    }

    override fun setPlaceName(name: String) {
        detailsItems.filter { it.id == EDIT_ITEM_ID }.map { (it as EditPlaceName).currentName = name }
        detailsAdapter!!.notifyDataSetChanged()
    }

    override fun onEditNameAction(): Observable<Any> {
        return detailsAdapter!!.onEditPlaceNameScreen
    }

    override fun isVisibleContent(isVisible: Boolean) {
        if (isVisible) {
            root.content.visibility = View.VISIBLE
        } else {
            root.content.visibility = View.INVISIBLE

        }
    }

    override fun getItems(): ArrayList<Base> {
        return detailsAdapter!!.itemsData
    }

}