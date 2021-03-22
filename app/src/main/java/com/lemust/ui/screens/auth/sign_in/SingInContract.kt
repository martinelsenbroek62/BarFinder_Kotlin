package com.lemust.ui.screens.auth.sign_in

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class SingInContract {
    interface View : BaseViewContract {
        fun onSignOutAction(): Observable<Any>
        fun showSignUpScreen()
        fun openNextActivity()
        fun isShowProgressLoader(isShow:Boolean)
        fun isShowContent(isShow:Boolean)

        fun loginViaEmailPasswordAction(): Observable<Any>
        fun forgotPasswordAction():Observable<Any>
        fun onForgotPasword():Observable<Any>
        fun loginViaFacebookAction(): Observable<Any>
        fun getEmail(): String
        fun getPassword(): String
        fun showProgressViewState(show: Boolean)
        fun showEmailError(error: String)
        fun showPasswordError(error: String)
        fun showForgotPasswordScreen()
        fun isEnabledScreen(isEnabled:Boolean)
        fun hideKeyboard()


    }

    interface Presenter {
        fun getEmailError(email: String):String?
        fun getPasswordError(password: String): String?
        fun loginViaFacebook(token: String)
        fun loginViaEmailPassword(email: String, password: String)

    }
}
