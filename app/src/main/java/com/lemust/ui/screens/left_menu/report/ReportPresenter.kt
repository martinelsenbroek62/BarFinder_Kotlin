package com.lemust.ui.screens.left_menu.report

import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.report.ReportDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.BaseView.Companion.DIALOG_KEY_EVENT_DISMISS
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.main.MainActivity
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.lemust.utils.NetworkTools
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class ReportPresenter(var view: ReportContract.View, var eventBus: Bus, var activity: BaseActivity) : ReportContract.Presenter {
    var isEmailIsEmpty = true
    var isTextIsEmpty = true
    var user = AppHelper.preferences.getUser()!!


    init {
        initData()
        view.isEnableButtonSent(false)

        initAction()
    }

    private fun initData() {
        user.let {
            if (it.email.isNullOrEmpty()) {
                view.showTextKeyboard()
                view.showEmailKeyboard()
            } else {
                view.setEmail(user!!.email!!)
                view.hideEmailFild()
            }

        }


    }


    fun getEmailError(email: String): String? {
        return if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            null
        } else {
            view.getViewContext().getString(R.string.error_invalid_email)
        }
    }

    private fun handleLogin(): Boolean {
        var email = view.getEmail()

        var isReady = true
        var emailError = getEmailError(email)
        if (emailError != null) {
            // view.showEmailError(emailError)
            isReady = false
        } else {
            //view.showEmailError("")
        }
        return isReady
    }


    private fun initAction() {
        view.sendReportAction().subscribe {
            sendReport()

        }


        view.getSequenceEmail().subscribe {
            isEmailIsEmpty = it.count() <= 4
            checkIsEmptyField()
        }
        view.getSequenceText().subscribe {
            isTextIsEmpty = it.count() < 1
            checkIsEmptyField()

        }


    }

    fun sendReport() {
        if (NetworkTools.isOnline()) {
            view.isEnableButtonSent(false)

            if (handleLogin()) {
                var version = "-1";
                try {
                    val pInfo = LeMustApp.instance.getPackageManager().getPackageInfo(LeMustApp.instance.getPackageName(), 0)
                    version = pInfo.versionName
                } catch (e: NameNotFoundException) {
                    e.printStackTrace()
                }

                var report = ReportDTO(view.getEmail(), view.getText(), version)
                AppHelper.api.sendReport(report).subscribe(object : Observer<ReportDTO> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: ReportDTO) {
                        activity.hideKeyboard()

                        view.showPositiveDialogOkCallback(view.getViewContext().getString(R.string.feedback_was_sent), view.getViewContext().getString(R.string.thanks_for_you_feedback)).subscribe {
                            if (it == DIALOG_KEY_EVENT_DISMISS) {
                                view.dismiss()
                            } else {
                                view.isEnableButtonSent(true)
                            }

                        }
                    }

                    override fun onError(e: Throwable) {
                        // view.isShowProgressLoader(false)
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


            } else {
                view.isEnableButtonSent(true)
                view.showPositiveDialog(view.getViewContext().getString(R.string.title_email), view.getViewContext().getString(R.string.error_enter_valid_email))


            }
        } else {
            //                eventBus.post(MainActivity.ShowNoInternetDialog())
            //                view.dismiss()
            activity.hideKeyboard()
            Handler(Looper.getMainLooper()).post {
                view.showDialog(DialogModel().build(activity, activity.resources.getString(R.string.no_internet_connection))
                        .showLastButton(activity.resources.getString(R.string.title_reload))
                        .showFirstButton(activity.resources.getString(R.string.title_cancel))
                        .single(true)).subscribe {
                    if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                        it.dialog.dismiss()

                    }
                    if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                        it.dialog.dismiss()
                        sendReport()


                    }
                }
            }


        }
    }

    private fun checkIsEmptyField() {
        if (!isEmailIsEmpty && !isTextIsEmpty) {
            view.isEnableButtonSent(true)
        } else {
            view.isEnableButtonSent(false)

        }

    }


}