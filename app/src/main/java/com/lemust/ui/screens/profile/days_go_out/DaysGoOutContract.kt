package com.lemust.ui.screens.profile.days_go_out

import android.content.Context
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.profile.days_go_out.adapter.WeekDayItem
import io.reactivex.Observable


class DaysGoOutContract {
    interface View : BaseViewContract {
        fun setMusicItems(items: List<WeekDayItem>)
        fun getDays(): ArrayList<WeekDayItem>
        //fun onBackAction(): Observable<Any>
        fun isShowProgressLoader(isShow: Boolean)
        fun onApplyAction():Observable<Any>
        fun changeTextInProgressBar(text: String)

    }

    interface Presenter

    interface Remainder {

        fun finish()
        fun cancel()
        fun getContext(): Context
        fun onResume()
        fun onDestroy()
        fun rewriteData():Observable<Any>
        fun onBackPressedAction():Observable<Any>
        fun generateBackPressed()


    }
}
