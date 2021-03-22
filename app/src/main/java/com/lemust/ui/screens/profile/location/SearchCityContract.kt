package com.lemust.ui.screens.profile.location

import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.profile.location.adapter.SearchCitytem
import io.reactivex.Observable


interface SearchCityContract {
    interface View :BaseViewContract{
        fun setVisibleLoader(isVisible: Boolean)
        fun setVisibleContent(isVisible: Boolean)
        fun onTouchItemEvent(): Observable<SearchCitytem>
        fun onClearAction(): Observable<Any>
        fun onScrollDown(): Observable<Any>
        fun onResetAction(): Observable<Any>
        fun clearCity()
        fun getPlacesName(): Observable<CharSequence>
        fun setData(data: List<SearchCitytem>)
        fun hideKeyboard()
        fun setDefaultCirt(city:String)
        fun dismiss()
        fun clearList()
        fun changeTextInProgressBar(text: String)
    }

    interface Presenter {
//        fun changePlaceType(placeId: Int)
//        fun initAction()
    }


}