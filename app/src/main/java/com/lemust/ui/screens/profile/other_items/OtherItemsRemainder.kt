package com.lemust.ui.screens.profile.other_items

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.lemust.ui.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class OtherItemsRemainder(var fragment: BaseActivity) : OtherItemsContract.Remainder, LifecycleObserver {



    private var resume = PublishSubject.create<Any>()
    private var onBackPressedAction = PublishSubject.create<Any>()

    init {
        (fragment as LifecycleOwner).lifecycle.addObserver(this)
    }

    override fun generateBackPressed() {
        onBackPressedAction.onNext(Any())
    }


    override fun onBackPressedAction(): Observable<Any> {
        return onBackPressedAction

    }


    override fun rewriteData(): Observable<Any> {
        return resume
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        resume.onComplete()
        onBackPressedAction.onComplete()


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        resume.onNext(Any())

    }


    override fun getContext(): Context {
        return fragment.baseContext
    }

    override fun finish() {
        fragment.setResult(RESULT_OK,fragment.intent)
        fragment.finish()
    }

    override fun cancel() {
        fragment.setResult(RESULT_CANCELED,fragment.intent)
        fragment.finish()    }

}