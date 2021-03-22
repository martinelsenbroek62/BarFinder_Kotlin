package com.lemust.ui.screens.left_menu

import android.graphics.BitmapFactory
import com.lemust.ui.base.navigation.NavigationController
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.main.map.MainFragment
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import com.lemust.utils.NetworkTools
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe


class LeftMenuPresenter(var view: LeftMenuContract.View) : LeftMenuContract.Presenter {
    var user = AppHelper.preferences.getUser()


    init {
        initUserProfile()
        initAction()
    }

    private fun initUserProfile() {
        var firstName = ""
        var secondName = ""
        var email = ""

        handleInitPhoto()

        if (user!!.firstName != null) {
            firstName = user!!.firstName!!
        }

        if (user!!.lastName != null) {
            secondName = user!!.lastName!!

        }

        if (user!!.email != null) {
            email = user!!.email!!
        }

        view.setUseInfo("$firstName $secondName", email)

    }

    private fun handleInitPhoto() {
        try {
            AppDataHolder.actualUserPhoto?.let {
                if (it.isEmpty()) {
                    if (user!!.image != null) {
                        view.setUserAvatar(user!!.image!!)
                    } else {
                        view.setDefaultAvatar()
                    }
                } else {
                    view.setUserAvatar(BitmapFactory.decodeFile(it))
                }


            }
        } catch (e: Exception) {
            System.err.print(e.localizedMessage)
        }


    }


    private fun initAction() {

//            view.onChangeLanguage().subscribe {
//                view.updateResources()
//            }
        view.onBackPressed().subscribe {
            if (view.isVisibleLeftMenu())
                view.hide()
        }
        view.onProfileItemAction().subscribe {
            view.openProfile()
        }
        view.onChangeLanguage().subscribe {
            //                view.isClickableItems(false)
            view.showLanguageScreen()
        }

        view.onChangeLocation().subscribe {
            //                view.isClickableItems(false)
            view.showLocationScreen()
        }

        view.onShare().subscribe {
            // view.shareApplication()
            view.onTakeScreen()
            //  eventBus.post(MainFragment.ShowScreenShotType())
            view.hideWithoutAnimation()
            //  eventBus.post(MainActivity.HideFiltersScreen(false))

        }
        view.onReport().subscribe {
            view.showReportScreen()
        }

        view.onProfileReloadDataAction().subscribe {
            user = AppHelper.preferences.getUser()

            initUserProfile()
            if (it) {
                view.updateResources()
                if (NetworkTools.isOnline())
                //  eventBus.post(MainActivity.UpdateResources())
                    view.updateResources()
            }

        }
        view.onFAQ().subscribe {
            view.openFAQ()
        }

    }


//    @Subscribe
//    fun onEvent(event: MainActivity.UpdateResources) {
//        view.updateResources()
//    }

//    @Subscribe
//    fun onEvent(event: MainActivity.OnDialogDismissed) {
////        view.isClickableItems(true)
//
//    }


}