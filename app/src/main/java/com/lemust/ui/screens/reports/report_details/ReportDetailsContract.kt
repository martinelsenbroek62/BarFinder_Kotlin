package com.lemust.ui.screens.reports.report_details

import android.content.Context
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.reports.report_details.adapter.Base
import com.lemust.ui.screens.reports.report_details.adapter.PlaceDetailsAdapter
import com.lemust.ui.screens.reports.report_details.adapter.TextItem
import io.reactivex.Observable


class ReportDetailsContract {
    interface View : BaseViewContract {
        fun setDetails(hours: ArrayList<Base>)
        fun isShowProgressLoader(isShow: Boolean)
        fun onSendReportAction(): Observable<Any>
        fun onEditNameAction(): Observable<Any>
        fun getItems(): ArrayList<Base>
        fun setPlaceName(name: String)
        fun isVisibleContent(isVisible: Boolean)
        fun onNewItemAction():Observable<PlaceDetailsAdapter.NewItem>
        fun addNewItem(item: TextItem,position:Int)
        fun onDestroy()
    }

    interface Presenter {
        fun onDestroy()

    }

    interface Remainder {
        fun getContext(): Context
        fun dismiss()
        fun openEditNameScreen(name: String)
        fun openAddItemScreen(item:PlaceDetailsAdapter.NewItem)
        fun onEditPlaceNameResultAction(): Observable<String>
        fun onNewItemResultAction(): Observable<PlaceDetailsAdapter.NewItem>


    }
}
