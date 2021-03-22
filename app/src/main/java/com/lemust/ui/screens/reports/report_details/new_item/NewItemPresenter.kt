package com.lemust.ui.screens.reports.report_details.new_item

import android.app.Activity.RESULT_OK
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.reports.report_details.adapter.PlaceDetailsAdapter
import com.squareup.otto.Bus


class NewItemPresenter(var view: NewItemContract.View, var eventBus: Bus, var activity: BaseActivity, var item: PlaceDetailsAdapter.NewItem) : NewItemContract.Presenter {


    init {
        initData()
        initAction()
    }

    private fun initAction() {
        view.onApplyAction().subscribe {
            if (view.getCurrentName().isNotEmpty()) {

                item.newItemName = view.getCurrentName()
                activity.intent.putExtra(NewItemActivity.NEW_ITEM_KEY, item)
                activity.setResult(RESULT_OK, activity.intent)
                activity.finish()
            } else {
                view.showError()
            }

        }
    }

    private fun initData() {
        view.setTitle(activity.resources.getString(R.string.title_add) + " " + item.title)
        view.setSubTitle( item.title)
        view.setTextHint(item.title)
    }


}