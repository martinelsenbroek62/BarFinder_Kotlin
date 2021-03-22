package com.lemust.ui.screens.profile.settings

import android.app.Activity.RESULT_OK
import android.support.v7.app.AlertDialog
import com.hairdresser.services.socials.facebook.FacebookAuthHelper
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.profile.ProfileRemainder
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody


class SettingPresenter(var view: SettingContract.View, var ctx: BaseActivity, var eventBus: Bus) : SettingContract.Presenter {
    override fun onBackPressed() {
        if (currentLanguage != AppHelper.locale.getLanguage(ctx)) {
            var intent = ctx.intent
            intent.putExtra(ProfileRemainder.LANGUAGE_CHANGED_KEY, true)
            ctx.setResult(RESULT_OK, intent)
            ctx.finish()

        } else {
            ctx.finish()
        }
    }

    var currentLanguage = AppHelper.locale.getLanguage(ctx)

    init {
        initAction()
        eventBus.register(this)


    }

    private fun initAction() {
        view.onBackAction().subscribe {
            ctx.onBackPressed()
        }
        view.onLanguageAction().subscribe {
            view.openLanguageScreen()
        }
        view.onFeedbackAction().subscribe {
            view.openFeedbackScreen()
        }

        view.onLogoutAction().subscribe {
            view.isShowProgressLoader(true)
            AppHelper.api.logout(AppHelper.preferences.getToken()).subscribe(object : Observer<ResponseBody> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: ResponseBody) {
                    logout()

                }

                override fun onError(e: Throwable) {
                    logout()

                }
            })


        }
        view.onRemoveAccount().subscribe {
            view.showRemoveAccountDialog(ctx.getString(R.string.title_remove_account), "", ctx.getString(R.string.title_delet), ctx.getString(R.string.title_cancel)).subscribe {
                view.isShowProgressLoader(true)
                AppHelper.api.deleteUser().subscribe(object : Observer<Any> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Any) {
                        FacebookAuthHelper.disconnectFromFacebook()
                        AppHelper.preferences.clearToken()
                        view.openAuthScreen()
                    }

                    override fun onError(e: Throwable) {
                        view.isShowProgressLoader(false)
                        var errorMessage = ErrorUtils(e, false, view.getActivityContext())
                        errorMessage.parse()
                        view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getActivityContext().resources.getString(R.string.title_ok),
                                object : BaseView.DialogController1 {
                                    override fun action1(dialog: AlertDialog) {
                                        dialog.dismiss()
                                    }
                                })


                    }
                })

            }
        }
        view.onPrivacyPolicyAction().subscribe { view.openPrivacyPolicy() }
        view.onTermsConditionsAction().subscribe { view.openTermsConditions() }
    }

    fun logout() {
        FacebookAuthHelper.disconnectFromFacebook()
        AppDataHolder.skipCurrentCity()
        AppHelper.preferences.clearToken()
        view.openAuthScreen()
        AppDataHolder.actualUserPhoto = ""
        view.isShowProgressLoader(false)
    }

    @Subscribe
    fun onEvent(event: MainActivity.UpdateResources) {
        view.updateResources()
    }
//
//    @Subscribe
//    fun onEvent(event: MainActivity.ShowNoInternetDialog) {
//        view.showDialogWithOneButtons(LeMustApp.instance.resources.getString(R.string.no_internet_connection),"", LeMustApp.instance.resources.getString(R.string.title_ok),
//                object : BaseView.DialogController1 {
//                    override fun action1(dialog: AlertDialog) {
//                        dialog.dismiss()
//                    }
//                })
//    }

}