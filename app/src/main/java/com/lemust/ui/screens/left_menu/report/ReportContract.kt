package com.lemust.ui.screens.left_menu.report

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


interface ReportContract {
    interface View : BaseViewContract {
        public fun getEmail(): String
        public fun getText(): String
        public fun sendReportAction(): Observable<Any>
        fun getSequenceEmail(): Observable<CharSequence>
        fun getSequenceText(): Observable<CharSequence>
        fun hideEmailFild()
        fun isEnableButtonSent(isEnabled:Boolean)
        fun dismiss()
        public fun setEmail(email:String)


        fun showEmailKeyboard()
        fun showTextKeyboard()



    }

    interface Presenter {
//        fun changePlaceType(placeId: Int)
//        fun initAction()
    }


}