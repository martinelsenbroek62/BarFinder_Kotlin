package com.lemust.ui.screens.reports.report_frequentation

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.lemust.ui.base.BaseActivity
import io.reactivex.subjects.PublishSubject


class ReportStatisticRemainder(var fragment: BaseActivity) : ReportStatisticContract.Remainder, LifecycleObserver {

    val POFILEE_RESULT = 12

    var onProfuleResultAction = PublishSubject.create<Any>()


    override fun getContext(): Context {
        return fragment.baseContext
    }


    init {
        (fragment as LifecycleOwner).lifecycle.addObserver(this)
        initAction()
    }


    private fun initAction() {
        fragment.onActivityResultListener.subscribe {

            when (it.requestCode) {
                POFILEE_RESULT -> {
                    onProfuleResultAction.onNext(Any())
                }
            }


        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        onProfuleResultAction.onComplete()

    }
    override fun dismiss() {
        fragment.finish()

    }


}