package com.lemust.ui.screens.profile.occupation

import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.profile.occupation.step1.adapter.OccupationItem
import io.reactivex.Observable


class Step1OccupationContract {
    interface View : BaseViewContract {
        fun setOccupations(items: List<OccupationItem>)
        fun getItems(): ArrayList<OccupationItem>
        fun onNextAction(): Observable<Any>
        fun onApplyAction(): Observable<Any>
        fun onItemClickedAction(): Observable<Any>
        fun onBackAction(): Observable<Any>
        fun isShowProgressLoader(isShow: Boolean)
        fun isEnableButtonSent(isEnabled: Boolean)
        fun closeScreen()
        fun isClicableFragmatnt(isClicable: Boolean)
        fun changeTextInProgressBar(text: String)
    }

    interface Presenter {

    }
}
