package com.lemust.ui.screens.profile.occupation

import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.api.RestManagerImplNull
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.repository.models.rest.user.reset.OccupationResetDTO
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.profile.occupation.step1.adapter.OccupationItem
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.lemust.utils.NetworkTools
import com.lemust.utils.TextHelper
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ProtocolException


class Step1OccupationPresenter(var view: Step1OccupationContract.View, var context: Context, var eventBus: Bus, var occupation: HashMap<String, String>) : Step1OccupationContract.Presenter {
    var user = AppHelper.preferences.getUser()!!
    var oldOccupationType = user.occupation

    init {
        initAction()
        initData()
        view.changeTextInProgressBar(context.resources.getString(R.string.string_updating))
    }

    private fun initAction() {


        view.onItemClickedAction().subscribe {
            var selected = view.getItems().filter { it.isSelected }

            if (selected.isEmpty()) {
                view.isEnableButtonSent(false)
            } else {
                view.isEnableButtonSent(true)

            }

        }
        view.onBackAction().subscribe {
            view.closeScreen()
        }
        view.onApplyAction().subscribe {
            if (oldOccupationType != null) {
                var selected = view.getItems().filter { it.isSelected }

                if (selected.isEmpty()) {
                    view.isShowProgressLoader(true)
                    RestManagerImplNull().api.changeUserOccupation(OccupationResetDTO(null, "")).subscribe(object : Observer<UserDTO> {
                        override fun onComplete() {
                        }

                        override fun onSubscribe(d: Disposable) {
                        }

                        override fun onNext(t: UserDTO) {
                            view.isShowProgressLoader(false)
                            view.closeScreen()

                        }

                        override fun onError(e: Throwable) {
                            if(e is ProtocolException){
                                Log.d("is_prot_e","protocol ex")
                            }
                            try {
                                var errorMessage = ErrorUtils(e, true, view.getViewContext())
                                errorMessage.parse()
                                view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getViewContext().resources.getString(R.string.title_ok),
                                        object : BaseView.DialogController1 {
                                            override fun action1(dialog: AlertDialog) {
                                                if (errorMessage.isInternetError)
                                                    dialog.dismiss()
                                            }
                                        })
                            } catch (e: java.lang.Exception) {
                                System.out.print(e.localizedMessage)
                            }
                        }
                    })
                } else
                    saveOccupation()
            } else saveOccupation()


        }
    }

    private fun saveOccupation() {
        var selected = view.getItems().filter { it.isSelected }

        if (selected.isEmpty()) {
            view.closeScreen()
        } else {
            when {
                user.occupation == null -> {
                    view.isShowProgressLoader(true)
                    sendRequest(selected[0].id)
                }
                user.occupation.toString() != selected[0].id -> {
                    view.isShowProgressLoader(true)
                    sendRequest(selected[0].id)
                }
                else -> {
                    if (NetworkTools.isOnline())
                        view.closeScreen() else {

                        view.showDialogWithOneButtons(context.resources.getString(R.string.no_internet_connection), "", context.resources.getString(R.string.title_ok),
                                object : BaseView.DialogController1 {
                                    override fun action1(dialog: AlertDialog) {
                                        dialog.dismiss()
                                    }
                                })


                    }
                }

            }
        }

    }

    private fun initData() {
        view.isEnableButtonSent(false)
        var items = ArrayList<OccupationItem>()
        occupation.forEach {
            if (user.occupation.toString() == it.key) {
                view.isEnableButtonSent(true)
                items.add(OccupationItem(it.key, TextHelper.cap1stChar(it.value), true))
            } else {
                items.add(OccupationItem(it.key, TextHelper.cap1stChar(it.value), false))
            }

        }
        view.setOccupations(items)

    }


    private fun sendRequest(occupationId: String?) {
        AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(occupation = occupationId)).subscribe(object : Observer<UserDTO> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: UserDTO) {
                view.isShowProgressLoader(false)
                //       view.openStep2Screen()
//                view.openNextActivity(AppDataHolder.currentUser!!.occupation != oldOccupationType)
                view.closeScreen()

            }

            override fun onError(e: Throwable) {
                try {
                    view.isShowProgressLoader(false)
                    var errorMessage = ErrorUtils(e, false)
                    errorMessage.parse()
                    view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, context.resources.getString(R.string.title_ok),
                            object : BaseView.DialogController1 {
                                override fun action1(dialog: AlertDialog) {
                                    dialog.dismiss()
                                }
                            })
                } catch (e: Exception) {
                    System.out.print(e.localizedMessage)
                }

            }
        })
    }
}