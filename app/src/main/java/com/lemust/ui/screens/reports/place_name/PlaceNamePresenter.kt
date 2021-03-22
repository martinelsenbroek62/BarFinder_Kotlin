package com.lemust.ui.screens.reports.place_name

import android.app.Activity.RESULT_OK
import com.lemust.ui.base.BaseActivity
import com.squareup.otto.Bus


class PlaceNamePresenter(var view: PlaceNameContract.View, var eventBus: Bus, var activity: BaseActivity, var name: String) : PlaceNameContract.Presenter {


    init {
        initData()
        initAction()
    }

    private fun initAction() {
        view.onApplyAction().subscribe {
            if (view.getCurrentName().isNotEmpty()) {
                activity.intent.putExtra(PlaceNameActivity.PLACE_NAME_KEY, view.getCurrentName())
                activity.setResult(RESULT_OK, activity.intent)
                activity.finish()
            } else {
                view.showError()
            }

        }
    }

    private fun initData() {
        view.setCurrentName(name)
    }


}