package com.lemust.ui.screens.details

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.view.WindowManager
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.reports.report_details.ReportDetailsActivity
import com.lemust.ui.screens.reports.report_frequentation.ReportStatisticActivity
import com.lemust.utils.Tools
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class PlaceDetailsRemainder(var activity: BaseActivity) : PlaceDetailsContract.Remainder, LifecycleObserver {
    override fun onShareAction(): Observable<Any> {
        return shareAction
    }

    val ON_ACTIVITY_RESULT_CHOOSER = 77

    var shareAction = PublishSubject.create<Any>()


    init {
        (activity as LifecycleOwner).lifecycle.addObserver(this)
        initStatusBar()

        activity.onActivityResultListener.subscribe {
                    shareAction.onNext(Any())



        }
    }

    private fun initStatusBar() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            Tools.setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            Tools.setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            activity.window.statusBarColor = Color.TRANSPARENT
        }
    }


    override fun finish() {
        activity.setResult(Activity.RESULT_OK, activity.intent)
        activity.finish()
    }

    override fun openReportStatistic(placeId: Int) {
        ReportStatisticActivity.start(activity, placeId)
    }

    override fun openPlaceDetailsReport(placeId: Int, placeTypeId: String) {
        ReportDetailsActivity.start(activity, placeId, placeTypeId)
    }

    override fun getContext(): Context {
        return activity.baseContext
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        shareAction.onComplete()

    }


}