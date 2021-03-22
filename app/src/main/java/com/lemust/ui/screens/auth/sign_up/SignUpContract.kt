package com.lemust.ui.screens.auth.sign_up

import com.lemust.ui.base.BaseViewContract
import io.reactivex.Observable


class SignUpContract {
    interface View : BaseViewContract {
        fun onSignInAction(): Observable<Any>
        fun showSignInScreen()
        fun isShowProgressLoader(isShow:Boolean)
        fun openNextActivity()
        fun hideKeyboard()

        fun registrationViaEmailPasswordAction(): Observable<Any>
        fun registrationViaFacebookAction(): Observable<Any>
        fun getEmail(): String
        fun getPassword(): String
        fun showProgressViewState(show: Boolean)
        fun showEmailError(error: String)
        fun showPasswordError(error: String)
        fun isEnabledScreen(isEnabled:Boolean)

        fun cleanFields()



    }

    interface Presenter {
        fun getEmailError(email: String):String?
        fun getPasswordError(password: String): String?
        fun registrationViaFacebook(token: String)
        fun registrationViaEmailPassword(email: String, password: String)
    }
}
