package com.lemust.ui.screens.filters.base

import android.os.Bundle
import android.util.Log
import com.lemust.repository.models.filters.Filter
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.place.PlaceTypeDTO
import com.lemust.repository.models.rest.request.BaseFilter
import com.lemust.ui.screens.filters.base.adapter.type_place.PlaceTypeItem
import com.lemust.utils.AppHelper
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*

class LifeCircleSettingHelper {
    private val IS_RESTORE = "IS_RESTORE_SETTING"
    private var isRestoredState = false
    private var lifeCircleAction = BehaviorSubject.create<LifeCircleAction>()

    var loadedFilters = ArrayList<Filter>()
    var currentPlaceTypeId: PlaceTypeItem? = null
    var subFiltersPager = FiltersPresenter.SubFilters()
    var pager = HashMap<Int, FiltersPresenter.SubFilters>()
    public var placeId = 0


    public fun getAction(): BehaviorSubject<LifeCircleAction> {
        return lifeCircleAction
    }

    fun saveState(savedInstanceState: Bundle, loadedFilters: ArrayList<Filter>, currentPlaceTypeId: PlaceTypeItem?,
                  ubFiltersPager: FiltersPresenter.SubFilters, pager: HashMap<Int, FiltersPresenter.SubFilters>) {


        currentPlaceTypeId?.let {
            savedInstanceState.putBoolean(IS_RESTORE, true)
            AppHelper.preferences.saveFilters(loadedFilters)
            AppHelper.preferences.saveCurrentFilterPlacesType(it)
            AppHelper.preferences.saveCurrentSubFilters(ubFiltersPager)
            AppHelper.preferences.saveCurrentSubFiltersPager(pager)
        }


    }

    public fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {

            var source = AppHelper.preferences
            if (isRestoredState) {
                loadedFilters.addAll(source.getFilters())
                currentPlaceTypeId = source.getCurrentFilterPlacesType()
                subFiltersPager = source.getCurrentSubFilters()
                pager = source.getCurrentSubFiltersPager()
                placeId = AppHelper.preferences.getCurrentTypeId()

                lifeCircleAction.onNext(LifeCircleAction.RESTORE_STATE)
            }

        }

    }

    enum class LifeCircleAction {
        RESTORE_STATE,
        RELOAD_STATE,
    }
}