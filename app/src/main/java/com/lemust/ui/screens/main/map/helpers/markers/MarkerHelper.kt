package com.lemust.ui.screens.main.map.helpers.markers

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.lemust.repository.models.rest.Features
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.PlaceType
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.ui.screens.main.map.adapter.HourItem
import com.lemust.ui.screens.main.map.helpers.filters.PlaceTypeHelper
import com.lemust.ui.screens.main.map.helpers.hours.HoursHelper
import com.lemust.ui.screens.main.map.helpers.markers.models.MarkerItem
import com.lemust.ui.screens.main.map.helpers.markers.models.MarkerItemConfig
import com.lemust.ui.screens.main.map.helpers.markers.tools.PlaceManager
import com.lemust.utils.Tools
import io.reactivex.disposables.Disposable

class MarkerHelper(var placeTypeHelper: PlaceTypeHelper, var hoursHelper: HoursHelper) {
    private var observerIconGenerationDispos: Disposable? = null
    private var observerIconGenerationDispos1: Disposable? = null


    var currentPlaceMarkers: ArrayList<Place> = ArrayList()
    var currentHiddenMarkers: ArrayList<Place> = ArrayList<Place>()
    var currentShownMarkers: ArrayList<Place> = ArrayList<Place>()

    var lastGeneratedMarkerItem: ArrayList<MarkerItem> = ArrayList<MarkerItem>()
    var savedCurrentPlaceMarkers: ArrayList<Place> = ArrayList()


    var isEnabledZoomPlacesManager = true
    var screenState = ScreenState.MAP
    var previousScreenState = ScreenState.MAP


    fun skip() {
        screenState = ScreenState.MAP
        previousScreenState = ScreenState.MAP
        isEnabledZoomPlacesManager=true
    }

    private var savedZoomLevel = 0
    private var savedCenterMap = LatLng(0.0, 0.0)


    var zoomLevel = 0
    var currentCenterMap = LatLng(0.0, 0.0)

    fun initScreenState(state: ScreenState) {
        previousScreenState = screenState
        screenState = state
    }

    fun resetScreenState() {
        screenState = previousScreenState
    }

    private var searchStateListener: SearchStateListener? = null
    fun onDestroy() {
        if (observerIconGenerationDispos != null)
            if (!observerIconGenerationDispos!!.isDisposed)
                observerIconGenerationDispos!!.dispose()

        if (observerIconGenerationDispos1 != null)
            if (!observerIconGenerationDispos1!!.isDisposed)
                observerIconGenerationDispos1!!.dispose()

        if (currentPlaceMarkers != null)
            currentPlaceMarkers!!.clear()

        if (currentHiddenMarkers != null)
            currentHiddenMarkers!!.clear()

        if (currentShownMarkers != null)
            currentShownMarkers!!.clear()

        if (lastGeneratedMarkerItem != null)
            lastGeneratedMarkerItem!!.clear()

        if (savedCurrentPlaceMarkers != null)
            savedCurrentPlaceMarkers!!.clear()

        searchStateListener = null

    }

    fun initMarkers(markers: List<Place>) {
        markers.forEach {
            if (it.googleRating == null) {
                it.googleRating = 0.0
            }
        }
        currentPlaceMarkers!!.addAll(markers)
    }


    fun setCurrentMapParametrs(zoomLevel: Int, currentCenterPosition: LatLng) {
        this.zoomLevel = zoomLevel
        this.currentCenterMap = currentCenterPosition
    }

    fun addSearchStateListener(listener: SearchStateListener) {
        this.searchStateListener = listener
    }

    fun clearMarkers() {
        currentPlaceMarkers!!.clear()
        currentHiddenMarkers!!.clear()
        currentShownMarkers!!.clear()

//        lastGeneratedMarkerItem?.clear()
    }

    fun prepareUpdatedMarkerByHour(hour: HourItem, placeType: Int, listener: UpdateMarkersListener) {
        if (currentPlaceMarkers.isNotEmpty()) {
            var config = MarkerItemConfig(currentPlaceMarkers!!, placeType, hour.hourInt, Tools.getTodayDayOfWeek(), Tools.getTomorrowDayOfWeek(), hour.dayPosition)
            if (observerIconGenerationDispos != null)
                if (!observerIconGenerationDispos!!.isDisposed) {
                    observerIconGenerationDispos!!.dispose()
                }
            observerIconGenerationDispos = PlaceManager().generateMarkers(config).subscribe {
                Handler(Looper.getMainLooper()).post {
                    listener?.markerUpdated(it)

                }

            }
        }
    }


    fun prepareNewMarkerByHourAndCheckZoomLevel(hour: HourItem, placeType: Int, currentCenterMapPosition: LatLng, zoomLevel: Int, listener: MarkersListener) {
        if (currentPlaceMarkers!!.isNotEmpty()) {
            var config = MarkerItemConfig(currentPlaceMarkers!!, placeType, hour.hourInt, Tools.getTodayDayOfWeek(), Tools.getTomorrowDayOfWeek(), hour.dayPosition)

            if (observerIconGenerationDispos != null)
                if (!observerIconGenerationDispos!!.isDisposed) {
                    observerIconGenerationDispos!!.dispose()
                }
            observerIconGenerationDispos = PlaceManager().generateMarkers(config).subscribe {
                Handler(Looper.getMainLooper()).post {
                    prepareZoomMarker(zoomLevel, currentCenterMapPosition, object : ZoomMarkersListener {
                        override fun markerUpdated(showMarkers: List<Place>, hideMarkers: List<Place>) {
                            listener.markerCreated(it, showMarkers, hideMarkers)

                        }
                    })
                }

            }
        }
    }

    var threadInterceptorZoom = 0
    fun prepareZoomMarkerAsync(it: Int, currentCenterMapPosition: LatLng, zoomListener: ZoomMarkersListener? = null) {
        var state = PlaceManager().getPlacesCurrentZoomState(it.toInt())
        threadInterceptorZoom++;
        Thread(Runnable {
            var innerIdentity = threadInterceptorZoom
            synchronized(this) {
                if (innerIdentity < threadInterceptorZoom) return@Runnable
                val currentPlaceMarkersClone = (currentPlaceMarkers!!.clone()) as ArrayList<Place>
                var t = PlaceManager().getDisplayStatesPlaces(state.zoomStateLogic, currentPlaceMarkersClone!!, state.countPlaces, currentCenterMapPosition)
                if (innerIdentity < threadInterceptorZoom) return@Runnable
                currentHiddenMarkers!!.clear()
                currentShownMarkers!!.clear()
                currentShownMarkers!!.addAll(t.showPlaces)
                currentHiddenMarkers!!.addAll(t.hidePlaces)
                if (isEnabledZoomPlacesManager) {
                    Handler(Looper.getMainLooper()).post {

                        if (zoomListener != null) {
                            zoomListener.markerUpdated(t.showPlaces, t.hidePlaces)
                        } else {
                            zoomListener!!.markerUpdated(currentPlaceMarkers!!, t.hidePlaces)

                        }
                    }

                }
            }
        }).start()
    }


    fun prepareZoomMarker(it: Int, currentCenterMapPosition: LatLng, zoomListener: ZoomMarkersListener? = null) {
        if (screenState != ScreenState.SEARCH) {
            var state = PlaceManager().getPlacesCurrentZoomState(it.toInt())
            Log.d("test_type_req", "onClickFilterAction:  START THREAD prepareZoomMarker")

            val currentPlaceMarkersClone = (currentPlaceMarkers!!.clone()) as ArrayList<Place>
//
//            PlaceManager().getDisplayStatesPlaces(state.zoomStateLogic, currentPlaceMarkersClone!!, state.countPlaces, currentCenterMapPosition).subscribe(object : Observer<PlaceManager.PlacesDisplayStates> {
//                override fun onComplete() {}
//                override fun onSubscribe(d: Disposable) {
//                    if (observerPlaceDisplayStateDispos != null)
//                        if (!observerPlaceDisplayStateDispos!!.isDisposed) {
//                            observerPlaceDisplayStateDispos!!.dispose()
//                            Log.d("test_type_req","onClickFilterAction:  START THREAD prepareZoomMarker dispose")
//
//                        }
//                    observerPlaceDisplayStateDispos = d
//
//
//
//
//
//
//                    Log.d("test_type_req","onClickFilterAction:  START THREAD prepareZoomMarker onSubscribe")
//
//                }
//
//                override fun onNext(t: PlaceManager.PlacesDisplayStates) {
//                    Log.d("test_type_req","onClickFilterAction:  START THREAD prepareZoomMarker onNext")
            var t = PlaceManager().getDisplayStatesPlaces(state.zoomStateLogic, currentPlaceMarkersClone!!, state.countPlaces, currentCenterMapPosition)
            currentHiddenMarkers!!.clear()
            currentShownMarkers!!.clear()
            currentShownMarkers!!.addAll(t.showPlaces)
            currentHiddenMarkers!!.addAll(t.hidePlaces)
            if (isEnabledZoomPlacesManager) {
                if (zoomListener != null) {
                    zoomListener.markerUpdated(t.showPlaces, t.hidePlaces)
                } else {
                    zoomListener!!.markerUpdated(currentPlaceMarkers!!, t.hidePlaces)

                }

            }
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.d("test_type_req","onClickFilterAction:  START THREAD prepareZoomMarker onError")
//
//                }
            // })
        }
    }


    fun prepareNewMarkersByHourForPlaceType(place: List<Place>, hour: HourItem, placeType: Int, currentCenterMapPosition: LatLng, zoomLevel: Int, markerCreatorListener: MarkersListener) {
        Thread(Runnable {
            var config = MarkerItemConfig(place, placeType, hour.hourInt, Tools.getTodayDayOfWeek(), Tools.getTomorrowDayOfWeek(), hour.dayPosition)
            if (observerIconGenerationDispos1 != null)
                if (!observerIconGenerationDispos1!!.isDisposed) {
                    observerIconGenerationDispos1!!.dispose()
                }
            observerIconGenerationDispos1 = PlaceManager().generateMarkers(config).subscribe {
                if (screenState == ScreenState.SEARCH) {
                    markerCreatorListener.markerCreated(it, currentPlaceMarkers!!, ArrayList())
                } else {
                    prepareZoomMarker(zoomLevel, currentCenterMapPosition, object : ZoomMarkersListener {
                        override fun markerUpdated(showMarkers: List<Place>, hideMarkers: List<Place>) {
                            if (markerCreatorListener != null) {
                                Handler(Looper.getMainLooper()).post {
                                    markerCreatorListener.markerCreated(it, showMarkers, hideMarkers)
                                }


                            }

                        }


                    })


                }


            }
        }).start()
    }

    fun enableMapState() {

    }

    fun enableSearchState(place: SearchItemDTO, generatedMarkers: List<MarkerItem>) {
        this.savedZoomLevel = zoomLevel
        this.savedCenterMap = currentCenterMap
        lastGeneratedMarkerItem!!.clear()
        lastGeneratedMarkerItem!!.addAll(generatedMarkers)
        screenState = ScreenState.SEARCH
//        isSearchState = true
//        savedDefaultPlaceTypeId = placeTypeHelper!!.currentPlaceTypeId
        savedCurrentPlaceMarkers!!.clear()
        savedCurrentPlaceMarkers!!.addAll(currentPlaceMarkers!!)
        currentPlaceMarkers!!.clear()
        isEnabledZoomPlacesManager = false
        var selectedItem = place!!
        var isCreateMarker = placeTypeHelper!!.currentPlaceTypeId != selectedItem.placeType!!.id
        placeTypeHelper!!.useTemporaryPlaceType(selectedItem.placeType!!.id!!)
        var features: Features? = null
        when (placeTypeHelper!!.currentPlaceTypeId) {
            1 ->
                features = Features(null, selectedItem.place!!.features, null, null)

            2 ->
                features = Features(selectedItem.place!!.features, null, null, null)

            3 ->
                features = Features(null, null, selectedItem.place!!.features, null)

            4 ->
                features = Features(null, null, null, selectedItem.place!!.features)
        }


        var place = Place(selectedItem.place!!.id!!, selectedItem.place!!.name!!, selectedItem.place!!.longDescription!!, selectedItem.place!!.placeImagesCount, features, selectedItem.place!!.googlePlaceId, selectedItem.place!!.placeTypes as ArrayList<PlaceType>?, selectedItem.place!!.googleRating, selectedItem.place!!.location, selectedItem.place!!.isGayFriendly, selectedItem.place!!.popularTimes, selectedItem.place!!.shortDescription)
        currentPlaceMarkers!!.add(place)


        //  if (isCreateMarker) {
        prepareNewMarkersByHourForPlaceType(currentPlaceMarkers!!, hoursHelper.currrentHourItem!!, placeTypeHelper!!.currentPlaceTypeId, currentCenterMap, zoomLevel, object : MarkerHelper.MarkersListener {
            override fun markerCreated(markerItems: List<MarkerItem>, showMarkers: List<Place>, hideMarkers: List<Place>) {

                Handler(Looper.getMainLooper()).post({
                    if (searchStateListener != null) {
                        searchStateListener!!.stateActivated(markerItems, currentPlaceMarkers!!)
                    }
                })

            }

        })

//        } else {
//            prepareUpdatedMarkerByHour(hoursHelper.startPopularTime, placeTypeHelper!!.currentPlaceTypeId,
//                    object : MarkerHelper.UpdateMarkersListener {
//                        override fun markerUpdated(markers: List<MarkerItem>) {
//                            if (searchStateListener != null) {
//                                searchStateListener!!.stateActivated(markers,currentPlaceMarkers!!)
//
//                            }
//                        }
//                    })
//
//
//        }

//        view.showMarkers(currentPlaceMarkers!!)

//        createMarkerPlaceByHourForCurrentPlaceType(currentPlaceMarkers!!, hoursHelper.startPopularTime, selectedItem.placeType!!.id!!, !isCreateMarker)


    }

    fun enableReportState() {

    }

    fun closeSearchState() {
        // this.zoomLevel = this.savedZoomLevel
        //   this.currentCenterMap=this.savedCenterMap
        currentPlaceMarkers!!.clear()
        currentPlaceMarkers!!.addAll(savedCurrentPlaceMarkers!!)


    }


    interface MarkersListener {
        fun markerCreated(markerItems: List<MarkerItem>, showMarkers: List<Place>, hideMarkers: List<Place>)
    }

    interface UpdateMarkersListener {
        fun markerUpdated(markers: List<MarkerItem>)
    }

    interface ZoomMarkersListener {
        fun markerUpdated(showMarkers: List<Place>, hideMarkers: List<Place>)
    }

    interface SearchStateListener {
        fun stateActivated(markerItems: List<MarkerItem>, list: List<Place>)
    }

    enum class ScreenState {
        SEARCH,
        MAP,
        REPORT

    }
}
