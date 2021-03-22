package com.lemust.ui.screens.main.map.helpers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.place.PlaceTypeDTO
import com.lemust.repository.models.rest.request.BaseFilter
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.main.map.MainContract
import com.lemust.ui.screens.main.map.helpers.hours.HoursHelper
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class LifeCircleHelper {
    private val IS_RESTORE = "IS_RESTORE"
    private var isRestoredState = false
    private var lifeCircleAction = BehaviorSubject.create<LifeCircleAction>()

    public var places = ArrayList<Place>()
    public var placeTypeModels = ArrayList<PlaceTypeDTO>()
    public var filters = HashMap<Int, ArrayList<BaseFilter>>()
    public var placeId = 0

    private var isFocusCity = false


    public fun getAction(): BehaviorSubject<LifeCircleAction> {
        return lifeCircleAction
    }

    fun saveState(savedInstanceState: Bundle, placesMapState: List<Place>, placesBufferMapState: List<Place>, filters: HashMap<Int, ArrayList<BaseFilter>>,
                  currentPlaceId: Int, placeTypeModels: List<PlaceTypeDTO>) {
        Log.d("test_lifecir_1", "saveState  +++")
        Log.d("test_lifecir_1", "placesMapState  +++: "+placesMapState.size)
        Log.d("test_lifecir_1", "placesBufferMapState  +++:  "+placesBufferMapState.size)
        Log.d("test_lifecir_11", "save placeType  +++:  "+placeTypeModels.size)




        var saveResultPlaces = ArrayList<Place>()

        if (placesMapState.size > placesBufferMapState.size) {
            saveResultPlaces.addAll(placesMapState)
        } else {
            saveResultPlaces.addAll(placesBufferMapState)

        }


        if (saveResultPlaces.isNotEmpty()) {
            Log.d("test_lifecir_1", "savedInstanceState: SAVE")

            savedInstanceState.putBoolean(IS_RESTORE, true)

            placeTypeModels.forEach {
                it.isDefault = false
                if (it.id == currentPlaceId) {
                    it.isDefault = true
                }
            }


            AppHelper.preferences.saveCurrentPlaces(saveResultPlaces)
            AppHelper.preferences.saveTypeModels(placeTypeModels)
            AppHelper.preferences.saveCurrentFilters(filters)
            AppHelper.preferences.saveCurrentTypeId(currentPlaceId)


        } else {
            savedInstanceState.putBoolean(IS_RESTORE, false)

        }

    }

    public fun restoreState(savedInstanceState: Bundle?) {

        Log.d("test_lifecir_1", "savedInstanceState: "+savedInstanceState)
        Log.d("test_lifecir_1", "isLanguageChangedForMap: "+AppHelper.preferences.isLanguageChangedForMap())
        Log.d("test_lifecir_1", "AppHelper.preferences.isChangedCity(): "+AppHelper.preferences.isChangedCity())

        if (savedInstanceState != null) {
            isRestoredState = savedInstanceState.getBoolean(IS_RESTORE, false)

            if (AppHelper.preferences.isLanguageChangedForMap()) {
                isFocusCity = true
                changLanguage()
            } else
                if (!AppHelper.preferences.isChangedCity()) {
                    if (isRestoredState) {
                        places.addAll(AppHelper.preferences.getCurrentPlaces())
                        filters = AppHelper.preferences.getCurrentFilters()
                        placeId = AppHelper.preferences.getCurrentTypeId()
                        placeTypeModels = AppHelper.preferences.getTypeModels() as ArrayList<PlaceTypeDTO>
                        lifeCircleAction.onNext(LifeCircleAction.RESTORE_STATE)
                        Log.d("test_lifecir_1", "RESTORE_STATE")



                    } else {
                        lifeCircleAction.onNext(LifeCircleAction.RELOAD_STATE)

                    }
                } else {
                    changCity()


                }
        } else {
            prepareLocationAndPermission()
           // lifeCircleAction.onNext(LifeCircleAction.RELOAD_STATE)

        }

    }


    fun changCity() {
        Log.d("test_lifecir_1", "changCity")
        lifeCircleAction.onNext(LifeCircleAction.CHANGE_CITY_STATE)


    }

    fun prepareLocationAndPermission() {
        Log.d("test_lifecir_1", "prepareLocationAndPermission")

        lifeCircleAction.onNext(LifeCircleAction.RELOAD_STATE)


    }

    fun changLanguage() {
        AppHelper.preferences.setLanguageChangedForMap(false)

        if (isFocusCity)
            lifeCircleAction.onNext(LifeCircleAction.CHANGE_LANGUAGE_FOCUS)
        else
            lifeCircleAction.onNext(LifeCircleAction.CHANGE_LANGUAGE)

    }

    enum class LifeCircleAction {
        RESTORE_STATE,
        RELOAD_STATE,
        CHANGE_CITY_STATE,
        CHANGE_LANGUAGE,
        CHANGE_LANGUAGE_FOCUS,
    }

    fun resume(context: Context, isSelectedCurrentTime: Boolean, hoursHelper: HoursHelper, view: MainContract.View) {

        AppDataHolder.screenshot = null
        if (isSelectedCurrentTime || hoursHelper.hours.isEmpty()) {
            hoursHelper.hours.clear()
            var times = hoursHelper.getTimewheelHours(context)
            hoursHelper.setCurrentHourItem(times.hour)
            view.setHours(times.hours, times.currentHourPosition!!)
            view.setDisplayTimePickerDate(times.hours[times.currentHourPosition].hour + " " + times.hours[times.currentHourPosition].day)

            var oldHour = hoursHelper.currrentHourItem!!
            var newHour = (oldHour.dayPosition * hoursHelper.HOURS) + oldHour.hourInt + oldHour.dayPosition
            view.updateTimeWheelPosition(newHour)
        }

        hoursHelper.callback = object : HoursHelper.Callback {
            override fun update() {
                var oldHour = hoursHelper.currrentHourItem!!
                var newHour = (oldHour.dayPosition * hoursHelper.HOURS) + oldHour.hourInt + oldHour.dayPosition

                hoursHelper.hours.clear()
                var times = hoursHelper.getTimewheelHours(context)
                hoursHelper.setCurrentHourItem(times.hour)
                view.setHours(times.hours, times.currentHourPosition!!)
                view.updateTimeWheelPosition(newHour)

            }
        }

        hoursHelper.startWatchingForHourChanges()

        hoursHelper.isWatchingForHourChanges = true
    }


}