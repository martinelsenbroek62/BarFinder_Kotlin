package com.lemust.ui.screens.main.map

import android.app.Activity
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.lemust.LeMustApp
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.ui.AppConst
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.main.MainActivity.Companion.ON_ACTIVITY_RESULT_LOCALE
import com.lemust.ui.screens.sharing.SharingActivity
import com.lemust.utils.AppHelper
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject


class MainRemainder(var activity: MainActivity) : MainContract.Remainder, LifecycleObserver {
    override fun onTakeScreenAction(): Observable<Any> {
        return activity.onTakeScreenAction
    }

    override fun onSearchAction(): Observable<SearchItemDTO> {
        return onSearchAction
    }

    override fun onLanguageChanged(): Observable<Any> {
        return activity.onLanguageAction
    }

    override fun onCityChanged(): Observable<Any> {
        return onCityChanged
    }

    val ON_ACTIVITY_RESULT_NO_INTERNET = 3
    var ON_ACTIVITY_SHARING_RESULT_KEY = 22

    var onLocationResultAction = BehaviorSubject.create<Any>()
    var onInternetResultAction = PublishSubject.create<Any>()
    var onSharingResetAction = PublishSubject.create<Any>()
    var onCityChanged = PublishSubject.create<Any>()
    var onSearchAction = PublishSubject.create<SearchItemDTO>()
    //  var onLanguageChanged = PublishSubject.create<Any>()


    override fun onSharingResetAction(): Observable<Any> {
        return onSharingResetAction
    }

    override fun openScreenshotPreviewActivity() {
        var intent=Intent(activity, SharingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        activity.startActivityForResult(intent, ON_ACTIVITY_SHARING_RESULT_KEY)
    }

    companion object {
        const val RESULT_CODE_RESET = 23

    }


    override fun onDestroy() {
        onLocationResultAction.onComplete()
        onInternetResultAction.onComplete()
        onSharingResetAction.onComplete()
        (activity as LifecycleOwner).lifecycle.removeObserver(this)

    }

    init {
        (activity as LifecycleOwner).lifecycle.addObserver(this)
        initAction()
    }


    private fun initAction() {
        activity.onActivityResultListener.subscribe {
            when (it.requestCode) {
                ON_ACTIVITY_RESULT_LOCALE -> {
                    onLocationResultAction.onNext(Any())
                }
                ON_ACTIVITY_RESULT_NO_INTERNET -> {
                    onInternetResultAction.onNext(Any())

                }

                AppConst.ACTIVITY_AVAILABLE_CITY_RESULT -> {
                    if (it.resultCode == Activity.RESULT_OK)
                        onCityChanged.onNext(Any())

                }

                AppConst.ACTIVITY_AVAILABLE_LANGUAGE_RESULT -> {
                    if (it.resultCode == Activity.RESULT_OK)
                        activity.onLanguageAction.onNext(Any())

                }

                AppConst.ACTIVITY_SEARCH_RESULT -> {
                    if (it.resultCode == Activity.RESULT_OK) {
                        var place = AppHelper.preferences.getSearchedItem()
                        place?.let {
                            onSearchAction.onNext(it)


                        }

                    }

                }
                ON_ACTIVITY_SHARING_RESULT_KEY -> {
                    if (it.resultCode == RESULT_CODE_RESET) {
                        onSharingResetAction.onNext(Any())
                    }

                }

            }
        }
    }

//    override fun onStateScreenAction(): Observable<NavigationController.StateScreen> {
//        return (fragment as NavigationController).getNavigationScreenState()
//    }


    override fun getContext(): Context {
        return activity.baseContext
    }


    override fun onLocationAction(): Observable<Any> {
        return onLocationResultAction
    }

    override fun onInternetAction(): Observable<Any> {
        return onInternetResultAction
    }

}

