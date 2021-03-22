package com.lemust.ui.screens.profile.edit_user

import android.content.Context
import android.support.v7.app.AlertDialog
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.ui.base.BaseView
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import com.lemust.utils.TextHelper
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class EditUserPresenter(var view: EditUserContract.View, var context: Context, var eventBus: Bus, var isVisibleFirstName: Boolean) : EditUserContract.Presenter {

    var user = AppHelper.preferences.getUser()!!

    var firstName = user.firstName
    var lastName = user.lastName
    var currentName = ""
    var postName = ""

    init {
        initData()
        initAction()
    }

    private fun initAction() {
        view.onBackAction().subscribe {
            //validate()
            view.finish()

        }
        view.onApplyAction().subscribe {
            saveUserData()

        }
    }


    private fun saveUserData() {
        view.hideKeyboard()
        var firstName = view.getFirstNameText()
        var secondName = view.getLastText()

        currentName = if (isVisibleFirstName) {
            firstName
        } else {
            secondName

        }



        if (postName != currentName) {
            view.changeTextInProgressBar(context.resources.getString(R.string.string_updating))
            view.isShowProgressLoader(true)

            if (!firstName.isEmpty()) {
                firstName = TextHelper.cap1stChar(firstName)
            }

            if (!secondName.isEmpty()) {
                secondName = TextHelper.cap1stChar(secondName)
            }

            if (isVisibleFirstName) {
                AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(firstName = firstName)).subscribe(response)
            } else {
                AppHelper.api.changeUserInformation(AppHelper.preferences.getToken(), AppHelper.preferences.getUserId(), PathUserDTO(lastName = secondName)).subscribe(response)

            }
        } else {
            view.finish()
        }


    }

    private fun initData() {
        if (isVisibleFirstName) {
            view.setTitle(view.getAppContext().resources.getString(R.string.title_first_name))
            view.setDescription(view.getAppContext().resources.getString(R.string.title_name_description))

            if (firstName != null) {
                view.setFirstName(firstName!!)
                postName = firstName!!

            }
            view.isVisibleFirstName(true)
            view.isVisibleLastName(false)

        } else {
            view.setTitle(view.getAppContext().resources.getString(R.string.title_last_name))
            view.setDescription(view.getAppContext().resources.getString(R.string.title_famile_description))

            if (lastName != null) {
                view.setLastName(lastName!!)
                postName = lastName!!

            }
            view.isVisibleFirstName(false)
            view.isVisibleLastName(true)

        }


    }


    var response = object : Observer<UserDTO> {
        override fun onComplete() {

        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(t: UserDTO) {

            AppHelper.preferences.saveUserObj(t)

            view.isShowProgressLoader(false)
            view.finish()

        }

        override fun onError(e: Throwable) {
            try {
                view.isShowProgressLoader(false)
                var errorMessage = ErrorUtils(e, true, view.getAppContext())
                errorMessage.addCustomTitle("first_name", view.getAppContext().resources.getString(R.string.title_first_name))
                errorMessage.addCustomTitle("last_name", view.getAppContext().resources.getString(R.string.title_last_name))
                errorMessage.parse()

                view.showDialogWithOneButtons(errorMessage.titleError, errorMessage.bodyError, view.getAppContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                dialog.dismiss()
                            }
                        })
            } catch (e: Exception) {
                System.out.print(e.localizedMessage)
            }
        }


    }

}