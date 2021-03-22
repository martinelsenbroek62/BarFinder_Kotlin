package com.lemust.ui.screens.left_menu.localization

import android.content.Context
import com.lemust.ui.screens.left_menu.localization.adapter.LanguageItem
import io.reactivex.Observable


interface LanguageContract {
    interface View {
        fun setLanguages(hours: ArrayList<LanguageItem>)
        fun changeLanguage(code:String)
        fun dismiss()
        fun onApplyAction(): Observable<Any>
        fun onClickAction(): Observable<LanguageItem>
        fun getContext():Context
        fun finish()



    }

    interface Presenter {
//        fun changePlaceType(placeId: Int)
//        fun initAction()
    }


}