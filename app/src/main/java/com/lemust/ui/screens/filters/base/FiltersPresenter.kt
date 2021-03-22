package com.lemust.ui.screens.filters.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.lemust.repository.models.filters.Filter
import com.lemust.repository.models.filters.FilterData
import com.lemust.repository.models.filters.OptionDTO
import com.lemust.repository.models.rest.request.*
import com.lemust.ui.screens.filters.base.adapter.sub_filters.SubFiltersItem
import com.lemust.ui.screens.filters.base.adapter.sub_filters.fieldTypeBoolKey
import com.lemust.ui.screens.filters.base.adapter.sub_filters.fieldTypeChoiceKey
import com.lemust.ui.screens.filters.base.adapter.sub_filters.fieldTypeMultiKey
import com.lemust.ui.screens.filters.base.adapter.type_place.PlaceTypeItem
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.main.map.MainFragment
import com.lemust.utils.AppHelper
import com.lemust.utils.Tools
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


class FiltersPresenter(var view: FiltersContract.View, var eventBus: Bus) : FiltersContract.Presenter {


   // var lifeCircleHelper = LifeCircleSettingHelper()

    private var loadedFilters = ArrayList<Filter>()
    private var currentPlaceTypeId: PlaceTypeItem? = null
    private var onBackDisposable: Disposable? = null

    private var placeTypeId = -1
//    private var currentCityId = -1

    private var subFiltersPager = SubFilters()
    private var restoreSubFiltersPager: SubFilters? = null
    private var pager = HashMap<Int, SubFilters>()

    private var lastInitEvent:MainActivity.InitFilters?=null


    init {
        eventBus.register(this)
        initAction()
        view.setVisibleContentFilter(false)
//        lifeCircleHelper.getAction().subscribe {
//            lifeCircleHelper.getAction().onComplete()
//            if (LifeCircleSettingHelper.LifeCircleAction.RESTORE_STATE == it) {
//                loadedFilters.addAll(lifeCircleHelper.loadedFilters)
//                currentPlaceTypeId = lifeCircleHelper.currentPlaceTypeId
//                placeTypeId = lifeCircleHelper.placeId
//                subFiltersPager = lifeCircleHelper.subFiltersPager
//                restoreSubFiltersPager = subFiltersPager.copy()
//                pager = lifeCircleHelper.pager
//
//                showFilters(loadedFilters)
//                //Handler(Looper.getMainLooper()).postDelayed(Runnable() {
//                //   setCurrentCheckeds(tmp)
//                //  },500);
//
//
//            }
//        }


    }


    override fun onSaveState(savedInstanceState: Bundle) {
       // lifeCircleHelper.saveState(savedInstanceState, loadedFilters, currentPlaceTypeId, subFiltersPager, pager)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
       // lifeCircleHelper.restoreState(savedInstanceState)
    }

    override fun onDestroy() {
        if (onBackDisposable != null)
            if (!onBackDisposable!!.isDisposed) {
                onBackDisposable!!.dispose()
            }
    }

    override fun initAction() {
        view.languageChanged().subscribe {
            view.updateLocaleResources()
            loadFilters(true)

        }
        view.onApplyFilters().subscribe {
            applyFilters()
        }
        view.onChangedPlaceTypeId().subscribe {
            view.setVisibleLoader(false)
            changePlaceType(it, false)

        }
        view.onOpenSubFiltersAction().subscribe {
            openSubFilters(it)
        }
        view.onResetFilters().subscribe {
            resetFilters()
        }
        view.onReloadFilter().subscribe {
view.setVisibleErrorMsg(false)
            view.setVisibleContent(true)

            view.setVisibleLoader(true)
            loadFilters()

        }
    }

    private fun resetFilters() {
        if (currentPlaceTypeId != null) {
            clearFilters()
            var request = MarkerFilterRequest(AppHelper.preferences.getCurrentCity().id.toInt(), currentPlaceTypeId!!.slug, ArrayList())
            eventBus.post(MainFragment.ApplyFilters(currentPlaceTypeId!!.id, request))
        }
    }

    private fun openSubFilters(it: SubFiltersItem) {
        when (it.filter.fieldType) {
            fieldTypeChoiceKey -> {
                view.openSubScreen(false, it.filter.id.toInt(), it.filter.options!!, subFiltersPager.subFiltersSingle[it.filter.id.toInt()], it.filter.name!!)

            }
            fieldTypeMultiKey -> {
                view.openSubScreen(true, it.filter.id.toInt(), it.filter.options!!, subFiltersPager.subFiltersMulti[it.filter.id.toInt()], it.filter.name!!)

            }
            fieldTypeBoolKey -> {
                if (it.isSelected) {
                    pager[currentPlaceTypeId!!.id]!!.subFiltersBool.put(it.filter.id.toInt(), it.isSelected)
                } else {
                    pager[currentPlaceTypeId!!.id]!!.subFiltersBool.remove(it.filter.id.toInt())

                }
            }


        }
    }

    private fun changePlaceType(it: PlaceTypeItem?, isHideAnimation: Boolean = true) {
        checkBoolItems()
        this.currentPlaceTypeId = it
        subFiltersPager = pager[currentPlaceTypeId!!.id]!!
        showSubFilterSection(isHideAnimation)
        Log.d("sh_sb", "subs: changePlaceType: ")

    }

    private fun applyFilters() {
        checkBoolItems()
        var filters = ArrayList<BaseFilter>()

        subFiltersPager.subFiltersSingle.forEach {
            if (it.value.isNotEmpty())
                filters.add(SingleChoiceFilter(it.key, it.value[0].id))
        }
        subFiltersPager.subFiltersMulti.forEach { element ->
            var ids = element.value.map { it.id }
            filters.add(MultiChoiceFilter(element.key, ids))

        }
        subFiltersPager.subFiltersBool.forEach {
            filters.add(BoolFilter(it.key, it.value))
        }

        var request = MarkerFilterRequest(AppHelper.preferences.getCurrentCity().id.toInt(), currentPlaceTypeId!!.slug, filters)

        eventBus.post(MainFragment.ApplyFilters(currentPlaceTypeId!!.id, request))
        view.hideFilters()
        // eventBus.post(MainActivity.HideFiltersScreen())


    }

    private fun clearFilters(isUpdateResources: Boolean = false) {
        subFiltersPager.clear()
        if (currentPlaceTypeId != null)
            view.setSubFilterItems(currentPlaceTypeId!!.filters.map {
                SubFiltersItem(it, false, arrayListOf())
            })
    }

    private fun checkBoolItems() {
        view.getAdapterDate().forEach {
            if (it.filter.fieldType == fieldTypeBoolKey)
                if (it.isSelected) {
                    subFiltersPager.subFiltersBool.put(it.filter.id.toInt(), it.isSelected)
                } else {
                    subFiltersPager.subFiltersBool.remove(it.filter.id.toInt())

                }
        }
    }


    private fun loadFilters(isUpdateResources: Boolean = false) {
        view.setVisibleContent(false)
        view.setVisibleLoader(true)
        var cityId = AppHelper.preferences.getCurrentCity().id.toInt()
        AppHelper.api.getPlacesFiltered(cityId.toLong(), cityId).subscribe(object : Observer<List<Filter>> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                view.setVisibleLoader(false)
                view.setVisibleErrorMsg(true)
               // view.showFilterException(e)
                //todo add dialog
            }

            override fun onNext(t: List<Filter>) {
                view.setVisibleErrorMsg(false)

                AppHelper.preferences.savePlaceIdForFilters(cityId)
                handleResponse(t, isUpdateResources)
            }
        })
    }

    private fun handleResponse(t: List<Filter>, isUpdateResources: Boolean = false) {
        var availableFilter = t.filter {
            it.id.toInt() == placeTypeId
        }

        if (availableFilter.isNotEmpty()) {
            var newFilter = removeEmptyFilters(t)
            loadedFilters.clear()
            loadedFilters.addAll(newFilter)
            showFilters(newFilter)
            view.hideAnimateLoader()
        } else {
            if (!isUpdateResources) {
                view.hideFilters()
                // eventBus.post(MainActivity.HideFiltersScreen())
                eventBus.post(MainActivity.FilterChangedToServer())
            }
        }

    }

    private fun removeEmptyFilters(t: List<Filter>): List<Filter> {
        t.forEach {
            val newIterator = ((it.filters as ArrayList).clone()) as ArrayList<FilterData>
            val iterator = newIterator.iterator()
            while (iterator.hasNext()) {
                val it = iterator.next()

                if (it.fieldType == fieldTypeChoiceKey) {
                    if (it.options == null) {
                        iterator.remove()
                    }
                    if (it.options != null) {
                        if (it.options!!.isEmpty())
                            iterator.remove()
                    }
                }


            }
            it.filters = newIterator


        }


        return t

    }

    fun showFilters(list: List<Filter>) {
        Thread(Runnable {
            var placeTypeItems = ArrayList<PlaceTypeItem>()
            list.forEach { filter ->
                pager.put(filter.id.toInt(), SubFilters())
                if (placeTypeId == filter.id.toInt()) {
                    var it = PlaceTypeItem(filter.id.toInt(), filter.name!!, filter.slug!!, Tools.getIconForTypePlaces(filter.slug!!), filter.filters!!, true)
                    this.currentPlaceTypeId = it
                    changePlaceType(it, false)
                    showSubFilterSection()
                    placeTypeItems.add(it)

                } else {
                    var it = PlaceTypeItem(filter.id.toInt(), filter.name!!, filter.slug!!, Tools.getIconForTypePlaces(filter.slug!!), filter.filters!!, false)
                    placeTypeItems.add(it)
                }
            }

            Handler(Looper.getMainLooper()).post({
                view.setFilterPlaceTypeItems(placeTypeItems)
                view.showAnimateContent()
                view.hideAnimateLoader()
                view.setVisibleContentFilter(true)

            })

        }).start()

    }


    private fun showSubFilterSection(isHideAnimation: Boolean = true) = Thread(Runnable {
        var subFilters = ArrayList<SubFiltersItem>()
        var subFiltersIsEmpty = false
        subFilters.addAll(currentPlaceTypeId!!.filters.map { SubFiltersItem(it, false, arrayListOf()) })
        if (restoreSubFiltersPager != null) {
            subFilters.forEach { sub ->
                restoreSubFiltersPager!!.subFiltersBool.filter {
                    it.key == sub.filter.id.toInt()
                }.map {
                    sub.isSelected = true
                }

                restoreSubFiltersPager!!.subFiltersMulti.filter {
                    it.key == sub.filter.id.toInt()
                }.map {
                    sub.selectedSubFilters = it.value
                }

                restoreSubFiltersPager!!.subFiltersSingle.filter {
                    it.key == sub.filter.id.toInt()
                }.map {
                    sub.selectedSubFilters = it.value
                    subFiltersPager.subFiltersSingle[sub.filter.id.toInt()] = it.value

                }

            }

        }

        if (restoreSubFiltersPager == null)
            for (i in 0 until subFilters.size) {
                var it = subFilters[i]

                if (subFiltersPager.subFiltersMulti.containsKey(it.filter.id.toInt())) {

                    it.selectedSubFilters.clear()
                    it.selectedSubFilters.addAll((subFiltersPager.subFiltersMulti[it.filter.id.toInt()]!!))
                }

                if (subFiltersPager.subFiltersSingle.containsKey(it.filter.id.toInt())) {
                    it.selectedSubFilters.clear()
                    var value = subFiltersPager.subFiltersSingle[it.filter.id.toInt()]!!
                    if (value.isNotEmpty())
                        it.selectedSubFilters.add(value[0])

                }


                if (subFiltersPager.subFiltersBool.containsKey(it.filter.id.toInt())) {

                    it.isSelected = true
                }
            }

        if (subFilters.isEmpty()) {
            subFiltersIsEmpty = true
        }


        Handler(Looper.getMainLooper()).post {
            //   view.showSubFiltersEmptyMessage(subFiltersIsEmpty)
            view.setSubFilterItems(subFilters)
            //   view.setVisibleContent(true)
            if (isHideAnimation)
                view.hideAnimateLoader()
            // view.isVisibleApplyButton(true)
        }


    }).start()


    @Subscribe
    fun onEvent(event: FiltersFragment.ChangeSubFilters) {
        if (event.isMultiChoice) {
            if (event.data.isEmpty()) {
                if (subFiltersPager.subFiltersMulti.containsKey(event.id)) {
                    subFiltersPager.subFiltersMulti.remove(event.id)
                }
            } else {
                subFiltersPager.subFiltersMulti.put(event.id, event.data)

            }

        } else {
            subFiltersPager.subFiltersSingle.put(event.id, event.data)
        }
        view.showContent()
        view.updateItemSubFilters(event.id, event.data)
    }

    @Subscribe
    fun onEvent(event: FiltersFragment.ShowContent) {
        view.showContent()
    }

//    @Subscribe
//    fun onEvent(event: FiltersFragment.HideContent) {
//        view.setVisibleContent(false)
//        view.setVisibleLoader(true)    }

    @Subscribe
    fun onEvent(event: FiltersFragment.CleanScreens) {
        view.hideScreens()
    }

    @Subscribe
    fun onEvent(event: FiltersFragment.ClearFilters) {
        clearFilters(true)
    }

    @Subscribe
    fun onEvent(event: MainActivity.InitFilters) {
        lastInitEvent=event
//        if (restoreSubFiltersPager != null)
//            restoreSubFiltersPager!!.clear()
        initFilters(event)
    }

    private fun initFilters(event: MainActivity.InitFilters) {
        handleFilterShowing(event)
        if (currentPlaceTypeId != null) {
            changePlaceType(currentPlaceTypeId, false)
        }
    }


    private fun handleFilterShowing(event: MainActivity.InitFilters) {
        view.onBackPressed().subscribe(object : Observer<Any> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                onBackDisposable = d
            }

            override fun onNext(t: Any) {
                if (!view.filterDetailsIsShowed()) {
                    view.hideFilters()
                    onBackDisposable!!.dispose()
                }


            }

            override fun onError(e: Throwable) {

            }
        })

        if (event.cityId != AppHelper.preferences.getPlaceIdForFilters() || loadedFilters.isEmpty()) {
            placeTypeId = event.placeTypeId
            //  defaultPlaceTypeId = event.defaultPlaceTypeId
            loadFilters()
        } else {
            currentPlaceTypeId = view.selectFilterTypeById(event.placeTypeId)
            if (currentPlaceTypeId == null) {
                //filters changed
                view.hideFilters()
                //  eventBus.post(MainActivity.HideFiltersScreen())
                eventBus.post(MainActivity.FilterChangedToServer())
            } else {

                Log.d("sh_sb", "subs: handleFilterShowing: ")

                // showSubFilterSection(false)
            }


        }
    }


    public class SubFilters() {
        var subFiltersSingle = HashMap<Int, ArrayList<OptionDTO>>()
        var subFiltersMulti = HashMap<Int, ArrayList<OptionDTO>>()
        var subFiltersBool = HashMap<Int, Boolean>()

        fun clear() {
            subFiltersBool.clear()
            subFiltersSingle.clear()
            subFiltersMulti.clear()
        }

        public fun copy(): SubFilters {
            var copySelected = SubFilters()
            copySelected.subFiltersBool = subFiltersBool.clone() as HashMap<Int, Boolean>
            copySelected.subFiltersSingle = subFiltersSingle.clone() as HashMap<Int, ArrayList<OptionDTO>>
            copySelected.subFiltersMulti = subFiltersMulti.clone() as HashMap<Int, ArrayList<OptionDTO>>
            return copySelected
        }
    }

//    @Subscribe
//    fun onEvent(event: MainActivity.UpdateResources) {
//
//
//    }


}