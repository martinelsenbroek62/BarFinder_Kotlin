package com.lemust.ui.screens.reports.report_details.new_item

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


interface NewItemContract {
    interface View :BaseViewContract{
        public fun onApplyAction():Observable<Any>
        public fun setCurrentName(name:String)
        public fun setTitle(name:String)
        public fun setSubTitle(name:String)
        public fun setTextHint(name:String)
        public fun getCurrentName():String
        public fun showError()

    }

    interface Presenter {
//        fun changePlaceType(placeId: Int)
//        fun initAction()
    }


}