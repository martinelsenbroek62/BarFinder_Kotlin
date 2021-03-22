package com.lemust.ui.screens.auth.sign_in

import android.support.v7.app.AlertDialog
import com.facebook.AccessToken
import com.hairdresser.services.socials.facebook.FacebookAuthHelper
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.auth.login.LoginDTO
import com.lemust.repository.models.rest.auth.registaration.AuthFacebookDTO
import com.lemust.repository.models.rest.auth.registaration.AuthResponseDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class SignInPresenter(var view: SingInContract.View, var eventBus: Bus, var fragment: BaseFragment) : SingInContract.Presenter {
    val NON_FIELD_ERROR = "non_field_errors"

    init {
        initAction()

    }

    private fun initAction() {
        view.onForgotPasword().subscribe {
            view.showForgotPasswordScreen()
        }
        view.onSignOutAction().subscribe {
            view.hideKeyboard()
            view.showSignUpScreen()
        }
        view.loginViaEmailPasswordAction().subscribe {
            view.hideKeyboard()
            handleLoginViaEmailPassword()
        }
        view.loginViaFacebookAction().subscribe {
            view.hideKeyboard()

            handleFacebookLogin()
        }
    }


    private fun handleLoginViaEmailPassword() {
        view.isShowProgressLoader(true)
        var email = view.getEmail()
        var password = view.getPassword()

        var isReady = true
        var emailError = getEmailError(email)
        if (emailError != null) {
            view.showEmailError(emailError)
            isReady = false
        } else {
            view.showEmailError("")
        }


        var passwordError = getPasswordError(password)
        if (passwordError != null) {
            view.showPasswordError(passwordError)
            isReady = false
        } else {
            view.showPasswordError("")
        }

        if (isReady) {
            loginViaEmailPassword(email, password)
        } else {
            view.isShowProgressLoader(false)

        }
    }

    private fun handleFacebookLogin() {
        view.isShowProgressLoader(true)
        view.isEnabledScreen(false)
        FacebookAuthHelper.startFacebookLogin(LeMustApp.instance.applicationContext).subscribe(object : Observer<AccessToken> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                view.isEnabledScreen(true)
                view.isShowProgressLoader(false)

                e.message?.let {
                    //view.showToast(e.message.toString())
                    view.isShowProgressLoader(false)
                    view.showDialogWithOneButtons(e.message.toString(), "", view.getViewContext().resources.getString(R.string.title_ok),
                            object : BaseView.DialogController1 {
                                override fun action1(dialog: AlertDialog) {
                                    dialog.dismiss()
                                }
                            })
                }
            }

            override fun onComplete() {

            }

            override fun onNext(t: AccessToken) {
                loginViaFacebook(t.token.trim())
            }
        })
    }

    override fun getEmailError(email: String): String? {
        return if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            null
        } else {
            view.getViewContext().getString(R.string.error_auth_invalid_email)
        }
    }

    override fun getPasswordError(password: String): String? {
        return if (password.length in 6..128) {
            null
        } else {
            view.getViewContext().getString(R.string.error_invalid_password)
        }
    }

    override fun loginViaFacebook(token: String) {
        view.isShowContent(false)
        AppHelper.api.loginFacebook(AuthFacebookDTO(token, "string")).subscribe(object : Observer<AuthResponseDTO> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: AuthResponseDTO) {
                AppHelper.preferences.saveAccessToken(t.key!!)
                AppHelper.preferences.saveUserId(t.userId!!)
                getUser()

//                view.isShowProgressLoader(false)

            }

            override fun onError(e: Throwable) {
                view.isEnabledScreen(true)

                view.isShowProgressLoader(false)
                view.showDialogWithOneButtons(e.message.toString(), "", view.getViewContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                dialog.dismiss()
                            }
                        })
            }
        })
    }

    override fun loginViaEmailPassword(email: String, password: String) {
        AppHelper.api.login(LoginDTO(email, password)).subscribe(object : Observer<AuthResponseDTO> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: AuthResponseDTO) {
                AppHelper.preferences.saveAccessToken(t.key!!)
                AppHelper.preferences.saveUserId(t.userId!!)
                getUser()

            }

            override fun onError(e: Throwable) {
                view.isEnabledScreen(true)

                var errorMessage = ErrorUtils(e, false)
                errorMessage.parse()
                view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getViewContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                dialog.dismiss()
                            }
                        })
                view.isShowProgressLoader(false)
            }
        })
    }

    private fun getUser() {
        (fragment.activity as AuthActivity).login()
    }
}