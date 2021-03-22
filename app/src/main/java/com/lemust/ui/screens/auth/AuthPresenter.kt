package com.lemust.ui.screens.auth

import android.support.v7.app.AlertDialog
import com.lemust.R
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.utils.AppHelper
import com.lemust.utils.NetworkTools
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException


class AuthPresenter(var view: AuthContract.View, var eventBus: Bus, var authActivity: AuthActivity, var remainder: AuthContract.Remainder, var tokenNotValid: Boolean) : AuthContract.Presenter {
    override fun onStop() {
        view.closeDialogs()
    }

    override fun login() {
        handleAuth()
    }

    init {
        initAction()
        cheekInternetConnection()
        handleState()

    }

    private fun handleState() {
        if (tokenNotValid) {
            view.showDialog(DialogModel().build(view.getViewContext(), view.getViewContext().getString(R.string.title_session_expidred)).isCancable(true)
                    .showFirstButton(view.getViewContext()
                            .resources.getString(R.string.title_ok)).single(true).isAutoCloseFirstButton(true))
        }
    }


    private fun initAction() {
        view.onPrivacyPolicyAction().subscribe {
            view.showPrivacyPolicy()
        }
//        view.onReloadCityAction().subscribe {
//            handlePermission()
//        }
        view.onPrivacyTermsAction().subscribe {
            view.showTerms()
        }
//        view.onLoadCityWithNotLocationAction().subscribe {
//
//            loadCities(language = AppHelper.locale.getLanguage(LeMustApp.instance))
//
//        }
//        remainder!!.onLocationAction().subscribe {
//            var manager = remainder.getContext().getSystemService(Context.LOCATION_SERVICE) as CustomLocationManager
//            if (manager.isProviderEnabled(CustomLocationManager.GPS_PROVIDER)) {
////                handlePermission()
//            } else {
//                // view.showGpsSettingDialog()
//            }
//
//        }


    }

    private fun cheekInternetConnection() {
        if (NetworkTools.isOnline()) {
            handleAuth()
        } else {
            view.showNoInternetConnection()
        }
    }

    private fun handleAuth() {
        if (!AppHelper.preferences.getToken().isEmpty()) {
            AppHelper.api.getUser(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId()).subscribe(object : Observer<UserDTO> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: UserDTO) {
                    AppHelper.preferences.saveUserObj(t)
                    view.openNextActivity()


                    //   handlePermission()
                }

                override fun onError(e: Throwable) {
                    val error = e as HttpException
                    val errorBody = error.response().errorBody()!!.string()
                    view.showDialogWithOneButtons(e.message().toString(), errorBody, view.getViewContext().resources.getString(R.string.title_ok),
                            object : BaseView.DialogController1 {
                                override fun action1(dialog: AlertDialog) {
                                    dialog.dismiss()
                                }
                            })
                }
            })
        }
    }


//    @Subscribe
//    fun onEvent(event: AuthActivity.Login) {
//        handleAuth()
//    }


}