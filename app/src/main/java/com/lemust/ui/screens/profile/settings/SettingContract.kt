package com.lemust.ui.screens.profile.settings

import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class SettingContract {
    interface View : BaseViewContract , LifecycleObserver {
        fun onLanguageAction(): Observable<Any>
        fun onFeedbackAction(): Observable<Any>
        fun onPrivacyPolicyAction(): Observable<Any>
        fun onTermsConditionsAction(): Observable<Any>
        fun onLogoutAction(): Observable<Any>
        fun onRemoveAccount(): Observable<Any>
        fun onBackAction(): Observable<Any>
        fun showRemoveAccountDialog(title: String, message: String, positiveTitle: String, negativeString: String): Observable<Any>
        fun isShowProgressLoader(isShow: Boolean)

        fun openLanguageScreen()
        fun openFeedbackScreen()
        fun openAuthScreen()
        fun openPrivacyPolicy()
        fun openTermsConditions()
        fun getActivityContext(): Context
        fun updateResources()
    }

    interface Presenter {
        fun onBackPressed()

    }
}
