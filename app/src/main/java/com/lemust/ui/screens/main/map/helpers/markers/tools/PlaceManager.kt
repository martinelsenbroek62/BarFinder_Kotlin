package com.lemust.ui.screens.main.map.helpers.markers.tools

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.Place
import com.lemust.ui.screens.main.map.helpers.markers.models.MarkerItem
import com.lemust.ui.screens.main.map.helpers.markers.models.MarkerItemConfig
import com.lemust.utils.Tools
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList


class PlaceManager {

    public fun generateMarkers(config: MarkerItemConfig): Observable<ArrayList<MarkerItem>> {
        var itemsReadyObserver = PublishSubject.create<ArrayList<MarkerItem>>()

        Thread(Runnable {
            var list = ArrayList<MarkerItem>()

            val newIterator = ((config.listPlaces as ArrayList<Place>).clone()) as ArrayList<Place>
            val iterator = newIterator.iterator()
            var i = 0
            System.gc()
            while (iterator.hasNext()) {
                val it = iterator.next()
                var icon: Bitmap? = null
                if (it.popularTimes == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        icon = getIconByTimeStatistics(config.currentPlaceType, 0f, it.name)
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        icon = getIconByTimeStatistics(config.currentPlaceType, it.popularTimes!![config.dayPosition].data[config.hour].toFloat(), it.name)
                    }
                }
                var raring = 0.0;
                if (it.googleRating != null)
                    raring = it.googleRating!!


                var marker = MarkerItem(it, icon!!, raring)

                list.add(marker)

            }
            itemsReadyObserver.onNext(list)
            itemsReadyObserver.onComplete()
        }).start()
        return itemsReadyObserver
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getIconByTimeStatistics(currentPlaceType: Int, popularTimes: Float, placeName: String): Bitmap {
        val drawable: Drawable


        var color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle10)
        var alpha = 100
        var sizeCircle = 0f


        when (popularTimes) {
            in 0..0 -> {
                alpha = 0
            }
            in 1..10 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle10)
                sizeCircle = 0.1f

            }
            in 11..20 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle20)
                sizeCircle = 0.2f

            }
            in 21..30 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle30)
                sizeCircle = 0.3f
            }
            in 31..40 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle40)
                sizeCircle = 0.4f

            }
            in 41..50 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle50)
                sizeCircle = 0.5f

            }
            in 51..60 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle60)
                sizeCircle = 0.6f

            }
            in 61..70 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle70)
                sizeCircle = 0.7f

            }
            in 71..80 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle80)
                sizeCircle = 0.8f

            }
            in 81..90 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle90)
                sizeCircle = 0.9f

            }
            in 91..100 -> {
                color = LeMustApp.instance.resources.getColor(R.color.colorPinCircle100)
                sizeCircle = 1f
            }
        }
        return IconGenerator.getRoundedCroppedBitmap(Tools.getIconForMarker(currentPlaceType), sizeCircle, color)
    }


    public fun sortPlacesByRating(places: ArrayList<Place>, takeCount: Int): List<Place> {
        Collections.sort(places) { x1, x2 -> x2.googleRating!!.compareTo(x1.googleRating!!) }
        return places!!.take(takeCount)
    }

    public fun sortPlacesByDistance(places: ArrayList<Place>, takeCount: Int, point: LatLng): List<Place> {

        places.sortWith(Comparator { x1, x2 ->
            SphericalUtil.computeDistanceBetween(point, LatLng(x1.location!!.lat, x1.location!!.lng))
                    .compareTo(SphericalUtil.computeDistanceBetween(point, LatLng(x2.location!!.lat, x2.location!!.lng)))
        })
        return places!!.take(takeCount)
    }


    public fun getPlacesCurrentZoomState(currentZoom: Int): PlacesCurrentZoomState {
        var isRatingSort: Boolean
        var numberOfDisplaysMarkers: Int

        Log.d("GOOGLE_ZOOM_MAP", "GoogleMapZoom: $currentZoom")
        when (currentZoom) {
            in 0..6 -> {
                isRatingSort = true
                numberOfDisplaysMarkers = 5
            }
            in 6..7 -> {
                isRatingSort = true
                numberOfDisplaysMarkers = 10
            }
            in 7..8 -> {
                isRatingSort = true
                numberOfDisplaysMarkers = 15
            }
            in 8..9 -> {
                isRatingSort = true
                numberOfDisplaysMarkers = 25
            }
            in 9..11 -> {
                isRatingSort = true
                numberOfDisplaysMarkers = 30
            }
            in 11..13 -> {
                isRatingSort = true
                numberOfDisplaysMarkers = 50
            }
            in 13..15 -> {
                isRatingSort = true
                numberOfDisplaysMarkers = 70
            }
            else -> {
                Log.d("GOOGLE_ZOOM_MAP", "GoogleMapZoom: isRatingSort=false 100")
                isRatingSort = false
                numberOfDisplaysMarkers = 100
            }
        }
        return PlacesCurrentZoomState(isRatingSort, numberOfDisplaysMarkers)
    }


    public fun getDisplayStatesPlaces(zoomStateLogic: Boolean, places: ArrayList<Place>, takeCount: Int, relativeCoordinates: LatLng): PlacesDisplayStates {
//        var isReady = PublishSubject.create<PlacesDisplayStates>()
//
//        Log.d("test_type_req","onClickFilterAction:  START THREAD prepareZoomMarker getDisplayStatesPlaces")
//
//        Thread(Runnable {
            var showPlaces = ArrayList<Place>()
            if (zoomStateLogic) {
                showPlaces.addAll(sortPlacesByRating(places, takeCount))
            } else {
                showPlaces.addAll(sortPlacesByDistance(places, takeCount, relativeCoordinates))

            }

            var hidePlaces = ArrayList<Place>()
            hidePlaces.addAll(places!!)
            hidePlaces.removeAll(showPlaces)

            Log.d("test_type_req","onClickFilterAction:  START THREAD prepareZoomMarker  getDisplayStatesPlaces onNext")

//            isReady.onNext(PlacesDisplayStates(hidePlaces, showPlaces))
//            isReady.onComplete()
            Log.d("test_type_req","onClickFilterAction:  START THREAD prepareZoomMarker  getDisplayStatesPlaces onNext")


//        }).start()
        return PlacesDisplayStates(hidePlaces, showPlaces)

    }

     data class PlacesDisplayStates(var hidePlaces: List<Place>, var showPlaces: List<Place>)
     data class PlacesCurrentZoomState(var zoomStateLogic: Boolean, var countPlaces: Int)

}