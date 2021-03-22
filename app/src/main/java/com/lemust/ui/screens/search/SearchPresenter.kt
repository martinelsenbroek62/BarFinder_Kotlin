package com.lemust.ui.screens.search

import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.main.map.MainFragment
import com.lemust.utils.AppHelper
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class SearchPresenter(var view: SearchContract.View, var cityId: Int?) : SearchContract.Presenter {
    override fun onStart() {

    }


    var searchDisposable: Disposable? = null


    init {
        initAction()
    }

    private fun initAction() {
        view.onScrollDown().subscribe {
            view.hideKeyboard()
        }


        view.onClearAction().subscribe {
            cancelLoadLocations()
            view.setVisibleContent(true)
            view.setVisibleLoader(false)
            view.setVisibleEmptyMessage(false)

        }
        view.getPlacesName().subscribe { text ->
            handleLoadPlaces(text.toString())
        }
        view.onTouchItemEvent().subscribe {
            if (!MainFragment.isScreenReady) view.dismiss()
            view.showPlace(it)
            view.dismiss()
        }

    }

    private fun handleLoadPlaces(text: String) {
        if (!text.trim().isEmpty()) {
            view.setVisibleRightEditTextIcon(true)
            view.setVisibleEmptyMessage(false)
            loadPlaces(text.trim().toString())
        } else {
            cancelLoadLocations()
            view.setVisibleRightEditTextIcon(false)
            view.setData(ArrayList())
            view.setVisibleContent(false)
            view.setVisibleLoader(false)
            view.setVisibleEmptyMessage(false)


        }
    }

    private fun cancelLoadLocations() {
        if (searchDisposable != null)
            if (!searchDisposable!!.isDisposed) {
                searchDisposable!!.dispose()
            }
    }

    public fun loadPlaces(name: String) {
        view.setVisibleContent(false)
        view.setVisibleLoader(true)
        AppHelper.api.searchPlaces(cityId!!.toLong(), name).subscribe(placesObservable)

    }

    var placesObservable = object : Observer<List<SearchItemDTO>> {
        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
            if (searchDisposable != null)
                if (!searchDisposable!!.isDisposed) {
                    searchDisposable!!.dispose()
                }
            searchDisposable = d

        }

        override fun onNext(t: List<SearchItemDTO>) {
            if (t.isEmpty()) {
                view.setVisibleEmptyMessage(true)
            } else {
                view.setVisibleEmptyMessage(false)

            }
            view.setData(t)
            view.setVisibleContent(true)
            view.setVisibleLoader(false)

        }

        override fun onError(e: Throwable) {

            view.hideKeyboard()

            view.setVisibleContent(true)
            view.setVisibleLoader(false)

            view.showDialogException(e).subscribe {
                if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                    it.dialog.dismiss()

                }
                if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                    it.dialog.dismiss()

                    handleLoadPlaces(view.getSearchText())

                }

            }
        }
    }

    override fun onDestroy() {
        view.hideKeyboard()
        view.unregister()
        cancelLoadLocations()
    }

    override fun onPause() {
        view.hideKeyboard()
    }

}