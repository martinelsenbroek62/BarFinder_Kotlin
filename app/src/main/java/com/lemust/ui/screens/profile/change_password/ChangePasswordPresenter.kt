package com.lemust.ui.screens.profile.change_password

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.auth.reset.ChangePsswordDTO
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.utils.AppHelper
import com.lemust.utils.NetworkTools
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class ChangePasswordPresenter(var view: ChangePasswordContract.View, var context: Context, var eventBus: Bus) : ChangePasswordContract.Presenter {
    private val minSizePassword = 6
    private var isVisibleOldPassword = true
    var user = AppHelper.preferences.getUser()!!


    init {
        initCurrentState()
        initAction()
    }


    private fun initCurrentState() {
//        view.isEnableButtonSent(false)
        isVisibleOldPassword = user.hasPassword!!
        if (!isVisibleOldPassword) {
            view.isVisibleOldPassword(false)
        }
    }

    private fun initAction() {
        view.onPasswordChangeAction().subscribe {
            handleField()
        }
        view.getNewPasswordAction().subscribe {
            handleButtonState()
        }
        view.getOldPasswordAction().subscribe {
            handleButtonState()
        }
        view.getRepeatPasswordAction().subscribe {
            handleButtonState()
        }
    }

    private fun handleButtonState() {
//        if (isFieldsCorrect() && isFieldsCorrect() && isPasswordsMatch()) {
//            view.isEnableButtonSent(true)
//        } else {
//            view.isEnableButtonSent(false)
//
//        }
    }

    private fun handleField() {
        var isFieldsNotEmpty = isFieldsNotEmpty()
        var isFieldsCorrect = isFieldsCorrect()
        var isPasswordsMatch = isPasswordsMatch()

        if (isFieldsCorrect && isFieldsNotEmpty && isPasswordsMatch) {
            handleNewPassword()
        } else {
            handleErrors(isFieldsNotEmpty, isFieldsCorrect, isPasswordsMatch)
        }
    }

    private fun handleNewPassword() {
        view.changeTextInProgressBar(context.resources.getString(R.string.string_updating))
        view.isShowProgressLoader(true)
        view.hideKeyboard()
        AppHelper.api.changePassword(AppHelper.preferences.getToken(), ChangePsswordDTO(view.getCurrentPassword(),
                view.getNewPassword(), view.getRepeatPassword())).subscribe(object : Observer<ChangePsswordDTO> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: ChangePsswordDTO) {
                view.isShowProgressLoader(false)

                view.showDialog(DialogModel().build(context, context.resources.getString(R.string.titlr_password_changed)
                )
                        .showFirstButton(context.resources.getString(R.string.title_ok))
                        .single(true)).subscribe {
                    if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                        it.dialog.dismiss()
                        view.finish()


                    }
                }
            }

            override fun onError(e: Throwable) {
                var err = NetworkTools.parseThrowableErrorByRetrofit(e)
                if (err != null) {
                    if (err.oldPassword != null) {
                        if (err.oldPassword!!.isNotEmpty()) {
                            view.isShowProgressLoader(false)
                            view.showDialogWithOneButtons(view.getViewContext().getString(R.string.title_current_password), err.oldPassword!![0], view.getViewContext().resources.getString(R.string.title_ok),
                                    object : BaseView.DialogController1 {
                                        override fun action1(dialog: AlertDialog) {
                                            dialog.dismiss()
                                        }
                                    })

                        } else {
                            view.isShowProgressLoader(false)
                            view.showDialog(context.getString(R.string.title_error), e.message.toString())
                        }


                    } else {
                        view.isShowProgressLoader(false)
                        view.showDialog(context.getString(R.string.title_error), e.message.toString())
                    }
                } else
                    Handler(Looper.getMainLooper()).post {
                        view.showDialog(DialogModel().build(view.getViewContext(), view.getViewContext().resources.getString(R.string.no_internet_connection))
                                .showFirstButton(view.getViewContext().resources.getString(R.string.title_ok))
                                .single(true)).subscribe {
                            if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                                it.dialog.dismiss()
                                view.isShowProgressLoader(false)


                            }
                        }
                    }

            }
        })

    }

    private fun handleErrors(fieldsNotEmpty: Boolean, fieldsCorrect: Boolean, passwordsMatch: Boolean) {
        if (!fieldsNotEmpty) {
            generateErrorMessage(context.getString(R.string.title_cant_be_empty), 0)
        } else if (!fieldsCorrect) {
            generateErrorMessage(context.getString(R.string.title_error_password_size), 1)
        } else if (!passwordsMatch) {
            generateErrorMessage(context.getString(R.string.title_password_dont_match), 2)
        }
    }

    private fun generateErrorMessage(message: String, code: Int) {
        view.hideKeyboard()
        when (code) {
            0 -> when {
                view.getCurrentPassword().isEmpty() -> view.showPositiveDialog(context.getString(R.string.title_password), message)
                view.getNewPassword().isEmpty() -> view.showPositiveDialog(context.getString(R.string.title_new_password), message)
                view.getRepeatPassword().isEmpty() -> view.showPositiveDialog(context.getString(R.string.title_repeat_password), message)
            }
            1 -> when {
                view.getCurrentPassword().length < minSizePassword -> view.showPositiveDialog(context.getString(R.string.title_password), message)
                (view.getNewPassword().length < minSizePassword) -> view.showPositiveDialog(context.getString(R.string.title_new_password), message)
                (view.getRepeatPassword().length < minSizePassword) -> view.showPositiveDialog(context.getString(R.string.title_repeat_password), message)
            }
            2 -> when {
                (view.getRepeatPassword() != view.getNewPassword()) -> view.showPositiveDialog(context.getString(R.string.title_repeat_password), message)

            }
        }
    }

    private fun isPasswordsMatch(): Boolean {
        return view.getNewPassword() == view.getRepeatPassword()
    }

    private fun isFieldsCorrect(): Boolean {
        return if (isVisibleOldPassword) view.getCurrentPassword().length >= minSizePassword && view.getNewPassword().length >= minSizePassword && view.getRepeatPassword().length >= minSizePassword
        else view.getNewPassword().length >= minSizePassword && view.getRepeatPassword().length >= minSizePassword

    }

    private fun isFieldsNotEmpty(): Boolean {
        return if (isVisibleOldPassword)
            !view.getCurrentPassword().isEmpty() && !view.getNewPassword().isEmpty() && !view.getRepeatPassword().isEmpty()
        else !view.getNewPassword().isEmpty() && !view.getRepeatPassword().isEmpty()


    }

}