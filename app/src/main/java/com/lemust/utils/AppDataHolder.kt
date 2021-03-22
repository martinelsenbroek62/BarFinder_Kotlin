package com.lemust.utils

import android.graphics.Bitmap
import com.lemust.repository.models.rest.City
import io.reactivex.subjects.BehaviorSubject

object AppDataHolder {
    var photos = ArrayList<Bitmap>()
    //var cities = mutableListOf<City>()
    // var currentCity: City? = null
    var citiesLanguageChanged = false
    var isOpenedSubScreens = false
    var isScreenshotReady = BehaviorSubject.create<Bitmap>()
    var actualUserPhoto = ""
    var sharePlaceDetailsData: PlaceDetailsShareData? = null
    var timeDelay: Int = 0
    var screenshot: Bitmap? = null
    var cancelShareMode=false


   // var isCalledRestoreSaveMethodMapScreen = false


    //var isSelectCurrentTime = true


    //Config
    var isLocationAvailable = false
    var isCancableLocation = false


    fun skipCurrentCity() {

    }

    fun savePhoto(list: ArrayList<Bitmap>) {
        photos = list
    }


    fun saveScreenshot(bitmap: Bitmap) {
        screenshot = bitmap
        isScreenshotReady.onNext(bitmap)
    }

    fun getSavedPhotos(): ArrayList<Bitmap> {
        return photos
    }

    fun clean() {
        photos.clear()
        isScreenshotReady.onComplete()

    }

    public class PlaceDetailsShareData(var placeTypeId: Int, var placeId: Int, var cityId: Int, var isActive: Boolean)

}