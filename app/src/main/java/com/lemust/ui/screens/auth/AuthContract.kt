package com.lemust.ui.screens.auth

import android.content.Context
import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class AuthContract {
    interface View : BaseViewContract {
        fun onPrivacyPolicyAction(): Observable<Any>
        fun onPrivacyTermsAction(): Observable<Any>
        fun onReloadCityAction(): Observable<Any>
        fun showPrivacyPolicy()
        fun showTerms()
        fun openNextActivity()
        fun showNoInternetConnection()
        fun onDestroy()
        fun closeDialogs()
        fun showRequestError(title: String, message: String)

    }

    interface Presenter {
        fun login()
        fun onStop()

    }


    interface Remainder {
        fun getContext(): Context


        fun onResume()
        fun onDestroy()

//        fun showGallery()
//        fun openCamera()

        fun onLocationAction(): Observable<Any>
        fun onInternetAction(): Observable<Any>
//        fun onFavoritePlacesRewriteAction(): Observable<Any>
//        fun onOccupationRewriteAction(): Observable<Any>
//        fun onSearchRewriteAction(): Observable<Any>
//        fun onNewImageAction(): Observable<String>
//        fun onUserEditRewriteAction(): Observable<Any>
//        fun onSettingRewriteAction(): Observable<Boolean>


    }
}
