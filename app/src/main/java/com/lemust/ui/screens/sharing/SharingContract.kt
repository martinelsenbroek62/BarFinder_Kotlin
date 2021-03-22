package com.lemust.ui.screens.sharing

import android.graphics.Bitmap
import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class SharingContract {
    interface View : BaseViewContract {
        fun setPreviewScreenshot(img: Bitmap)
        fun onApplyAction(): Observable<Any>
        fun onResetAction(): Observable<Any>
        fun onSentAction(): Observable<Any>
        fun cancel()
        fun shareApplication(img:Bitmap)
        fun finish()


        fun showPhoto(img:Bitmap)
        fun onPhotoAction(): Observable<Any>

        fun isShowProgressLoader(isShow: Boolean)
//        fun getFirstNameText(): String
//        fun getLastText(): String
//        fun setFirstName(firstName: String)
//        fun setLastName(lastName: String)
//        fun finish()
//
//        fun showFirstNameError(error: String)
//        fun showSeconNameError(error: String)
//        fun disableFirstNameError()
//        fun disableSecondNameError()
//
//        fun onBackAction():Observable<Any>
//
//        fun setTitle(text:String)
//
//        fun  isVisibleFirstName(isVisible:Boolean)
//        fun  isVisibleLastName(isVisible:Boolean)

    }

    interface Presenter {

    }
}
