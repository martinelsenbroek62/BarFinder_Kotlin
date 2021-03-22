package com.lemust.ui.screens.auth.forgot_password

import android.support.v7.app.AlertDialog
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.auth.EmailDTO
import com.lemust.repository.models.rest.auth.reset.ResetDTO
import com.lemust.ui.base.BaseView
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class ForgotPresenter(var view: ForgotContract.View, var eventBus: Bus) : ForgotContract.Presenter {
    init {
        initAction()
    }//

    private fun initAction() {
        view.onSendAction().subscribe {
            handleLoginViaEmailPassword()
        }
        view.onBackAction().subscribe { view.back() }

    }

    override fun getEmailError(email: String): String? {
        return if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            null
        } else {
            view.getViewContext().getString(R.string.error_auth_invalid_email)
        }
    }
    private fun handleLoginViaEmailPassword() {
        view.isShowProgressLoader(true)
        var email = view.getEmail()

        var isReady = true
        var emailError = getEmailError(email)
        if (emailError != null) {
            view.showEmailError(emailError)
            isReady = false
        } else {
            view.showEmailError("")
        }


        if (isReady) {
            forgotPassword(email)
        }else{
            view.isShowProgressLoader(false)

        }
    }

    private fun forgotPassword(email: String) {
        view.hideKeyboard()
        AppHelper.api.resetPassword(EmailDTO(email)).subscribe(object :Observer<ResetDTO>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: ResetDTO) {
                view.isShowProgressLoader(false)

                view.showDialogWithOneButtons(t.detail!!, "", view.getViewContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                view.finish()
                                dialog.dismiss()
                            }
                        })
            }

            override fun onError(e: Throwable) {
                view.isShowProgressLoader(false)
                var errorMessage = ErrorUtils(e, false)
                errorMessage.parse()
                view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getViewContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                dialog.dismiss()
                                view.showKeyboard()
                            }
                        })

            }
        })
    }

}