package com.lemust.ui.screens.reports.place_name

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


interface PlaceNameContract {
    interface View :BaseViewContract{
        public fun onApplyAction():Observable<Any>
        public fun setCurrentName(name:String)
        public fun getCurrentName():String
        public fun showError()
    }

    interface Presenter {
//        fun changePlaceType(placeId: Int)
//        fun initAction()
    }


}