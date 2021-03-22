package com.lemust.ui.screens.auth.forgot_password

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class ForgotContract {
    interface View : BaseViewContract {
        fun onSendAction(): Observable<Any>
        fun getEmail(): String
        fun isShowProgressLoader(isShow: Boolean)
        fun showEmailError(error: String)
        fun finish()
        fun hideKeyboard()
        fun showKeyboard()
        fun onBackAction(): Observable<Any>
        fun back()
//        fun showPrivacyPolicy()
//        fun openNextActivity()

    }

    interface Presenter {
        fun getEmailError(email: String): String?

    }
}
