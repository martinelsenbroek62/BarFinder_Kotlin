package com.lemust.ui.screens.reports.report_frequentation

import android.content.Context
import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class ReportStatisticContract {
    interface View : BaseViewContract {
        public fun onChangeProgress():Observable<Int>
        public fun onSendReportAction():Observable<Any>
        public fun onChangeStatistic(progress:Int)
        public fun setSeekBarColor(color:Int)
        public fun setProgress(progress:Int)

        fun isShowProgressLoader(isShow: Boolean)

    }

    interface Presenter {

    }

    interface Remainder {
        fun getContext(): Context
        fun dismiss()



    }
}
