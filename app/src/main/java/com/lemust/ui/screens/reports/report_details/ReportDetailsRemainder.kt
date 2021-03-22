package com.lemust.ui.screens.rports.report_details

import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.content.Intent
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.screens.reports.place_name.PlaceNameActivity
import com.lemust.ui.screens.reports.report_details.ReportDetailsContract
import com.lemust.ui.screens.reports.report_details.adapter.PlaceDetailsAdapter
import com.lemust.ui.screens.reports.report_details.new_item.NewItemActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


class ReportDetailsRemainder(var fragment: BaseActivity) : ReportDetailsContract.Remainder, LifecycleObserver {

    val EDIT_NAME_RESULT = 1
    val ADD_NEW_ITEM_RESULT = 2

    var onEditPlaceNameAction = PublishSubject.create<String>()
    var onNewItemAction = PublishSubject.create<PlaceDetailsAdapter.NewItem>()


    override fun getContext(): Context {
        return fragment.baseContext
    }

    override fun openEditNameScreen(name: String) {
        PlaceNameActivity.start(fragment, EDIT_NAME_RESULT, name)
    }


    init {
        (fragment as LifecycleOwner).lifecycle.addObserver(this)
        initAction()
    }


    private fun initAction() {
        fragment.onActivityResultListener.subscribe {

            when (it.requestCode) {
                EDIT_NAME_RESULT -> {
                    if (it.resultCode == RESULT_OK)
                        handleEditNameResult(it.data)
                }
                ADD_NEW_ITEM_RESULT -> {
                    if (it.resultCode == RESULT_OK)
                        handleNewItemResult(it.data)

                }
            }


        }
    }

    private fun handleNewItemResult(data: Intent?) {
        var newItem = data!!.getSerializableExtra(NewItemActivity.NEW_ITEM_KEY) as PlaceDetailsAdapter.NewItem
        onNewItemAction.onNext(newItem)
    }

    private fun handleEditNameResult(data: Intent?) {
        var placeName = data!!.getStringExtra(PlaceNameActivity.PLACE_NAME_KEY)
        onEditPlaceNameAction.onNext(placeName)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        onNewItemAction.onComplete()
        onEditPlaceNameAction.onComplete()

    }

    override fun dismiss() {
        fragment.finish()

    }

    override fun onNewItemResultAction(): Observable<PlaceDetailsAdapter.NewItem> {
        return onNewItemAction
    }

    override fun openAddItemScreen(item: PlaceDetailsAdapter.NewItem) {
        NewItemActivity.start(fragment, ADD_NEW_ITEM_RESULT, item)

    }

    override fun onEditPlaceNameResultAction(): Observable<String> {
        return onEditPlaceNameAction
    }


}