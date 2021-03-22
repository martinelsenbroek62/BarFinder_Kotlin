package com.lemust.ui.screens.filters.sub

import com.lemust.repository.models.filters.OptionDTO
import com.lemust.ui.screens.filters.base.FiltersFragment
import com.lemust.ui.screens.filters.sub.adapter.SubItem
import com.squareup.otto.Bus
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class SubFiltersPresenter(var view: SubFiltersContract.View, var data: ArrayList<OptionDTO>, var parentFilterId: Int, var bus: Bus, var isMultiChoice: Boolean?, var selectedItems: ArrayList<OptionDTO>?, var screenName: String?) : SubFiltersContract.Presenter {
    init {
        initAction()
        initData()
    }

    private fun initAction() {

        var disposable:Disposable?=null
        view.onBackAction().subscribe (object :Observer<Any>{
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                disposable=d
            }

            override fun onNext(t: Any) {
                var filters = FiltersFragment.ChangeSubFilters(view.getAdapterData().filter { it.isSelected }.map { it.name } as ArrayList<OptionDTO>, parentFilterId, isMultiChoice!!)


                when {
                    selectedItems == null -> {
                        bus.post(filters)
                        view.closeScreen()
                    }
                    filters.data.hashCode() == selectedItems!!.hashCode() -> view.closeScreen()
                    else -> {
                        bus.post(filters)
                        view.closeScreen()
                    }
                }
            disposable!!.dispose()
            }

            override fun onError(e: Throwable) {
                disposable!!.dispose()
            }
        })
    }

    private fun initData() {
        var items = ArrayList<SubItem>()
        items.addAll(data.map { SubItem(it, false) })
        view.setTitleScreen(screenName!!)

        //TO DO refactored models and check to selected item
        if (selectedItems != null)
            selectedItems!!.forEach { selected ->
                items.filter { selected.option == it.name.option }.map { it.isSelected = true }
            }
        view.setData(items)

    }


}