package com.lemust.ui.screens.auth

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.lemust.ui.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class AuthRemainder(var fragment: BaseActivity) : AuthContract.Remainder, LifecycleObserver {

//
    val ON_ACTIVITY_RESULT_LOCATION = 2
    val ON_ACTIVITY_RESULT_NO_INTERNET = 3
//    val FAVORITE_MUSIC_RESULT = 5
//    val FAVORITE_PLACES_RESULT = 6
//    val SEARCH_CITY_RESULT = 7
//    val OCCUPATION_RESULT = 8
//    val GALLERY_RESULT = 9
//    val CAMERA_RESULT = 10
//    val EDIT_USER_RESULT = 11
//    val SETTING_RESULT = 12
//
//
    var onLocationResultAction = PublishSubject.create<Any>()
    var onInternetResultAction = PublishSubject.create<Any>()
//    var onPlacesResult = PublishSubject.create<Any>()
//    var onOccupationResult = PublishSubject.create<Any>()
//    var onSearchResult = PublishSubject.create<Any>()
//    var onNewImageReady = PublishSubject.create<String>()
//    var onUserEditReady = PublishSubject.create<Any>()
//    var onSettingReady = PublishSubject.create<Boolean>()
    private var resume = PublishSubject.create<Any>()




    init {
        (fragment as LifecycleOwner).lifecycle.addObserver(this)
        initAction()
    }

    companion object {
        val LANGUAGE_CHANGED_KEY = "language_changed_key"

    }


    private fun initAction() {
        fragment.onActivityResultListener.subscribe {
//
            when (it.requestCode) {
                ON_ACTIVITY_RESULT_LOCATION -> {
                    onLocationResultAction.onNext(Any())
                }
                ON_ACTIVITY_RESULT_NO_INTERNET -> {
                    onInternetResultAction.onNext(Any())

                }}

        }
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        onLocationResultAction.onComplete()
        onInternetResultAction.onComplete()
        resume.onComplete()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        resume.onNext(Any())

    }



    override fun getContext(): Context {
        return fragment.baseContext
    }


        override fun onLocationAction(): Observable<Any> {
            return onLocationResultAction
        }

        override fun onInternetAction(): Observable<Any> {
            return onInternetResultAction
        }

}

