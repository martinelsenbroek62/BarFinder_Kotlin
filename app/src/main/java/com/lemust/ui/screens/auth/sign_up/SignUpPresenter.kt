package com.lemust.ui.screens.auth.sign_up

import android.support.v7.app.AlertDialog
import com.facebook.AccessToken
import com.hairdresser.services.socials.facebook.FacebookAuthHelper
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.auth.VerificationDTO
import com.lemust.repository.models.rest.auth.registaration.AuthFacebookDTO
import com.lemust.repository.models.rest.auth.registaration.AuthResponseDTO
import com.lemust.repository.models.rest.auth.registaration.RegistrationDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.auth.AuthActivity
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class SignUpPresenter(var view: SignUpContract.View, var eventBus: Bus, var fragment: BaseFragment) : SignUpContract.Presenter {
    init {
        initAction()
    }

    private fun initAction() {
        view.onSignInAction().subscribe {
            view.hideKeyboard()
            view.showSignInScreen()
        }

        view.registrationViaEmailPasswordAction().subscribe {
            view.hideKeyboard()
            handleLoginViaEmailPassword()
        }
        view.registrationViaFacebookAction().subscribe { handleFacebookLogin() }

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
            registrationViaEmailPassword(email, password)
        } else {
            view.isShowProgressLoader(false)

        }
    }

    private fun handleFacebookLogin() {
        FacebookAuthHelper.disconnectFromFacebook()
        view.isShowProgressLoader(true)
        view.isEnabledScreen(false)
        FacebookAuthHelper.startFacebookLogin(LeMustApp.instance.applicationContext).subscribe(object : Observer<AccessToken> {
            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {
                view.isShowProgressLoader(false)
                view.isEnabledScreen(true)
                e.message?.let {
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
                registrationViaFacebook(t.token.trim())
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

    override fun registrationViaFacebook(token: String) {
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
                view.isShowProgressLoader(false)
                view.isEnabledScreen(true)

                view.showDialogWithOneButtons(e.message.toString(), "", view.getViewContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                dialog.dismiss()
                            }
                        })
            }
        })
    }


    override fun registrationViaEmailPassword(email: String, password: String) {
        AppHelper.api.registration(RegistrationDTO(email, password, password)).subscribe(object : Observer<VerificationDTO> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: VerificationDTO) {
                view.isShowProgressLoader(false)
                view.showDialog(DialogModel().build(LeMustApp.instance, fragment.activity!!.getString(R.string.title_verification))
                        .showFirstButton(LeMustApp.instance.resources.getString(R.string.title_ok))
                        .single(true)).subscribe {
                    if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                        it.dialog.dismiss()
                        view.showSignInScreen()

                    }
                }


            }

            override fun onError(e: Throwable) {
                view.isEnabledScreen(true)
                view.isShowProgressLoader(false)
                var errorMessage = ErrorUtils(e, false)
                errorMessage.parse()
                view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getViewContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                dialog.dismiss()
                            }
                        })


            }
        })
    }


    private fun getUser() {
        (fragment.activity as AuthActivity).login()

    }

}