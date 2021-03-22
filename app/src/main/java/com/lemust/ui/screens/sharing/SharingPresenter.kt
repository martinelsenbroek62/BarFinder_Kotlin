package com.lemust.ui.screens.sharing

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import com.lemust.utils.AppDataHolder
import com.squareup.otto.Bus
import io.reactivex.disposables.Disposable


class SharingPresenter(var view: SharingContract.View, var context: Context, var eventBus: Bus) : SharingContract.Presenter, LifecycleObserver {
    var bitmap: Bitmap? = null
    var i = 0


    init {
        initAction()
        view.isShowProgressLoader(false)
        loadPhoto()
    }

    private fun loadPhoto() {
        var bitma = AppDataHolder.screenshot

        if (bitma == null) {
            view.cancel()
        } else {
            view.setPreviewScreenshot(bitma!!)
            bitmap = AppDataHolder.screenshot!!
        }
    }

    private fun initAction() {

        view.onApplyAction().subscribe {
            view.shareApplication(bitmap!!)
        }
        view.onSentAction().subscribe {
          //  view.finish()
            view.cancel()

        }
        view.onResetAction().subscribe {
            view.cancel()
        }
//        view.onPhotoAction().subscribe {
//            view.showPhoto(bitmap!!)
//        }
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//    fun onDestroy() {
//        if (screenshotDisposable != null) {
//            if (!screenshotDisposable!!.isDisposed) {
//                screenshotDisposable!!.dispose()
//            }
//        }
//
//    }


}