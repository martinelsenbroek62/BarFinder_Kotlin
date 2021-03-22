package com.lemust.ui.screens.left_menu

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.graphics.Bitmap
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.base.navigation.NavigationController
import io.reactivex.Observable


class LeftMenuContract {
    interface View : BaseViewContract, LifecycleObserver {
        public fun onChangeLocation(): Observable<Any>
        public fun onChangeLanguage(): Observable<Any>
        public fun onReport(): Observable<Any>
        public fun onShare(): Observable<Any>
        public fun onFAQ(): Observable<Any>
        public fun onProfileItemAction(): Observable<Any>
        fun getRootView():android.view.View

//        fun isClickableItems(isClickable: Boolean)
        fun setUserAvatar(img: Bitmap)

        public fun setUserAvatar(ava: String)
        public fun setDefaultAvatar()

        public fun setUseInfo(name: String, email: String)

        public fun updateResources()
        fun onDestroy()
        public fun showLanguageScreen()
        public fun showLocationScreen()
        public fun showReportScreen()
        //        public fun shareApplication()
        public fun openAuthScreen()
        fun getActivityContext(): Context
        fun openProfile()
        fun openFAQ()
        fun onProfileReloadDataAction(): Observable<Boolean>
        fun onBackPressed(): Observable<Any>
        fun onLanguageChanged(): Observable<Any>
        fun hide()
        fun isVisibleLeftMenu():Boolean
        fun hideWithoutAnimation()
        fun onTakeScreen()


    }

    interface Presenter {

    }


//    interface Remainder {
//
//
//    }
}
