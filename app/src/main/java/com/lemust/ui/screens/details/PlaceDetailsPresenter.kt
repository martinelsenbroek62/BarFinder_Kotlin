package com.lemust.ui.screens.details

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.facebook.FacebookSdk.getApplicationContext
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.place_details.PeriodsDTO
import com.lemust.repository.models.rest.place_details.PlaceDetails
import com.lemust.repository.models.rest.place_details.PlaceImage
import com.lemust.ui.AppConst
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.details.adapters.PlaceDetailsSubCategoryItem
import com.lemust.utils.*
import com.squareup.otto.Bus
import com.steelkiwi.simplehistogram.TimeData
import io.paperdb.Paper
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import locationprovider.davidserrano.com.LocationProvider
import java.lang.StringBuilder
import java.util.*


class PlaceDetailsPresenter(var view: PlaceDetailsContract.View, var eventBus: Bus, var location: LatLng?, var placeType: Int?, var placeId: Int?, var context: Context?, var remainder: PlaceDetailsContract.Remainder) : PlaceDetailsContract.Presenter {


    override fun onBackPressed() {
        remainder.finish()
    }

    var placeDisposable: Disposable? = null

    override fun onDestroy() {
        if (placeDisposable != null) {
            if (!placeDisposable!!.isDisposed) {
                placeDisposable!!.dispose()
            }
        }
        AppDataHolder.photos.clear()


    }

    var currentPlace: PlaceDetails? = null
    var workPeriod = ArrayList<String>()
    var fragmentIsActive = false


    init {
        initStartState()
        initAction()
        loadData()
        initMarker()
    }

    override fun onResume() {
        fragmentIsActive = true
    }

    override fun onPause() {
        fragmentIsActive = false
    }

    private fun initStartState() {
        view.isVisibleButtonRepport(false)
        view.isVisibleFrequentationContainer(false)
    }

    private fun loadData() {


        view.isVisibleMainContent(false)
        view.isVisibleRatingBar(false)
        view.isVisiblePriceLevel(false)

        LeMustApp.instance.api.getPlaceById(placeId!!, Tools.getPlaceTypeKeyById(placeType!!)).subscribe(object : Observer<PlaceDetails> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                if (placeDisposable != null) {
                    if (!placeDisposable!!.isDisposed) {
                        placeDisposable!!.dispose()
                    }
                }

                placeDisposable = d
            }

            override fun onNext(t: PlaceDetails) {
                currentPlace = t
                initPlaceDetailsInfo(currentPlace)

                handlePhoto(t)

            }

            override fun onError(e: Throwable) {
                view.showDialogWithOneButtons(e.message.toString(), "", remainder.getContext().resources.getString(R.string.title_ok),
                        object : BaseView.DialogController1 {
                            override fun action1(dialog: AlertDialog) {
                                view.isVisibleLoader(false)
                                dialog.dismiss()
                            }
                        })
            }
        })
    }

    private fun handlePhoto(t: PlaceDetails) {
        if (t.placeImages!!.isEmpty()) {
            var man = PhotoTools()
            man.getGooglePhotos(t.googlePlaceId!!, placeId!!)
            man.photosLoadedAction.subscribe {
                loadedPhotos.addAll(it.images)
                view.initPhotosPreview(loadedPhotos)
                view.isVisibleLoader(false)
                view.isVisibleButtonShare(true)


            }
            // getGooglePhotos(t.googlePlaceId!!)
        } else
            getServerPhoto(t.placeImages!!)

    }

    var loadedPhotos = ArrayList<Bitmap>()

    private fun getServerPhoto(placeImages: List<PlaceImage>) {
        var photoNotValidCount = 0
        placeImages.forEach {
            Glide.with(getApplicationContext()).asBitmap()
                    .load(it.image).listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                            photoNotValidCount++
                            if ((loadedPhotos.size + photoNotValidCount) == placeImages.size) {
                                Handler().post {
                                    view.initPhotosPreview(loadedPhotos)


                                }
                            }

                            return true
                        }

                        override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return true
                        }
                    })
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            loadedPhotos.add(resource)

                            if (loadedPhotos.size == placeImages.size) {
                                Handler().post {
                                    view.initPhotosPreview(loadedPhotos)
                                    view.isVisibleMainContent(true)
                                    view.isVisibleButtonRepport(true)
                                    view.isVisibleButtonShare(true)
                                    view.isVisibleLoader(false)
                                    view.isVisibleButtonShare(true)


                                }

                            }
                        }
                    })
        }


    }

    private fun initTime(openHours: List<PeriodsDTO>?) {
        var isOpenedAlways = false

        if (openHours!!.size == 1) {
            if (openHours[0].close == null) {
                isOpenedAlways = true
            }
        }

        workPeriod.clear()
        if (!isOpenedAlways) {
            var dayPeriods = ArrayList<Tools.Day>()
            if (openHours != null) {
                openHours.forEachIndexed { index, periodsDTO ->

                    if (periodsDTO.close == null && periodsDTO.open != null) {
                        dayPeriods.add(Tools.getDayWeek(periodsDTO.open!!.day!!, null, periodsDTO.open!!.time!!, null, true))
                        // workPeriod.add(Tools.getDayWeek(periodsDTO.open!!.day!!, null, periodsDTO.open!!.time!!, null, true))
                    }

                    if (periodsDTO.close != null && periodsDTO.open == null) {
                        dayPeriods.add(Tools.getDayWeek(null, periodsDTO.close!!.day!!, null, periodsDTO.close!!.time!!, true))
                    }

                    if (periodsDTO.close != null && periodsDTO.open != null) {
                        dayPeriods.add(Tools.getDayWeek(periodsDTO.open!!.day!!, periodsDTO.close!!.day!!, periodsDTO.open!!.time!!, periodsDTO.close!!.time!!, true))
                    }
                }
            }

            var day = HashMap<Int, String>()

            for (i in 0 until 7) {
                var periods = dayPeriods.filter { it.dayPosition == i }
                if (!periods.isEmpty()) {

                    periods.forEachIndexed { index, dayPeriod ->
                        if (index == 0) {
                            day.put(i, TimeHelper.getDayWeekName(i, context!!) + " " + dayPeriod.time)
                        } else {
                            var currentText = day.get(i)
                            day.put(i, currentText + ", " + dayPeriod.time)

                        }
                    }

                } else {
                    day.put(i, TimeHelper.getDayWeekName(i, context!!) + ": " + context!!.resources.getString(R.string.title_closed))
                }
            }
            for (i in 1 until 7) {
                workPeriod.add(day.get(i)!!)
            }
            workPeriod.add(day.get(0)!!.toString())
        } else {
            for (i in 1 until 7) {
                workPeriod.add(TimeHelper.getDayWeekName(i, context!!) + ": " + context!!.getString(R.string.text_open_24))
            }
            workPeriod.add(TimeHelper.getDayWeekName(0, context!!) + ": " + context!!.getString(R.string.text_open_24))

        }

        if (workPeriod.isNotEmpty()) {
            view.isVisibleTimeWork(true)
        } else {
            view.isVisibleTimeWork(false)
        }

        view.setStaticsDay(workPeriod[Tools.getTodayDayOfWeek()])
    }


    private fun handelStaticPager() {
        if (currentPlace!!.popularTimes == null) {
            view.isVisibleStatisticsView(false)
            view.setUserReportStatisticText(context!!.getString(R.string.how_busy_now))

        } else {
            view.isVisibleStatisticsView(true)
            createStaticsPage(currentPlace!!)
        }
        view.isVisibleFrequentationContainer(true)
    }

    private fun initPlaceDetailsInfo(currentPlace: PlaceDetails?) {
        var rating = 0f;
        var description = "";

        location = LatLng(currentPlace!!.location!!.lat, currentPlace!!.location!!.lng)

        view.initMarker(Tools.getIconForMarker(placeType!!), location!!)

        currentPlace?.isGayFriendly?.let {
            view.isVisibleGayFrendly(it)
        }
        if (currentPlace!!.googleRating != null) {
            rating = currentPlace.googleRating!!.toFloat()
        }

        if (currentPlace!!.shortDescription != null) {
            description = currentPlace!!.shortDescription!!
        }


        initBaseInformation(currentPlace, description, rating)

        if (currentPlace.googlePriceLevel != null) {
            view.setPriceLevel(currentPlace.googlePriceLevel!!)
        } else {
            view.setPriceLevel(0)

        }
        view.isVisiblePriceLevel(true)

        if (currentPlace.openHours != null) {
            if (currentPlace.openHours!!.periods != null) {
                initTime(currentPlace.openHours!!.periods)
            }
            if (currentPlace.openHours!!.weekdayText != null) {
                var time = StringBuilder("")
                workPeriod.forEach {
                    time.append(it + "\n")
                }
                view.setTime(time.toString(), workPeriod[Tools.getTodayDayOfWeek()])
            }
        }

        if (currentPlace.address !== null) {
            if (!currentPlace.address!!.isEmpty()) {
                view.isVisibleAddress(true)
                view.setAddress(currentPlace.address!!)
            }
        } else {
            view.isVisibleAddress(false)
        }


        var subCategoryList = ArrayList<PlaceDetailsSubCategoryItem>()
        if (currentPlace.placeFields != null) {
            currentPlace.placeFields!!.map!!.forEach {
                subCategoryList.add(PlaceDetailsSubCategoryItem(it.key, it.value))
            }

        }



        handleSubCategoryAndFeatures(subCategoryList)

        handleVisibilityPhone(currentPlace)

        handleVisibilityWeb(currentPlace)

        handelStaticPager()

        //view.isVisibleReportUserStatistic(true)


        if (currentPlace.aboutPlace!!.isEmpty()) {
            view.isAboutPlaceTextView(false)
        }

        view.isVisibleRatingBar(true)

        view.isVisibleMainContent(true)
        view.isVisibleButtonRepport(true)


    }

    private fun initBaseInformation(currentPlace: PlaceDetails, description: String, rating: Float) {
        var features = ""
        if (currentPlace.features != null)
            if (currentPlace.features!!.isNotEmpty()) {
                features = Tools.appendStrings(currentPlace.features!!, "•")
                if (!features.isEmpty())
                    features = " • $features"
            }

        view.initPlaceDetails(currentPlace.name!!, Tools.getPlaceTypeNameById(placeType!!, context!!) + features, description, rating)
    }

    private fun handleSubCategoryAndFeatures(subCategoryList: ArrayList<PlaceDetailsSubCategoryItem>) {

        view.setSubCategory(subCategoryList.filter { it.value.isNotEmpty() } as ArrayList<PlaceDetailsSubCategoryItem>)
        var featuresArray = subCategoryList.filter { it.value.isEmpty() }.map { it.title }

        if (featuresArray.isNotEmpty()) {
            var features = Tools.appendStrings(featuresArray)
            view.setFeatures(features)

        }


    }

    private fun handleVisibilityWeb(currentPlace: PlaceDetails) {
        if (currentPlace.website !== null) {
            if (currentPlace.website!!.isNotEmpty()) {
                view.isVisibleWebButton(true)
            } else {
                view.isVisibleWebButton(false)
            }
        } else {
            view.isVisibleWebButton(false)

        }
    }

    private fun handleVisibilityPhone(currentPlace: PlaceDetails) {
        if (currentPlace.phone !== null) {
            if (!currentPlace.phone!!.trim()!!.isEmpty()) {
                view.isVisiblePhone(true)
                view.isVisiblePhoneButton(true)
                view.setPhone(currentPlace.phone!!)
            } else {
                view.isVisiblePhone(false)
                view.isVisiblePhoneButton(false)

            }
        } else {
            view.isVisiblePhone(false)
            view.isVisiblePhoneButton(false)

        }
    }


    private fun initAction() {
        view.onBackPressed().subscribe {
            remainder.finish()
        }

        remainder.onShareAction().subscribe {
            //  view.isShowProgressLoader(false)
            view.isClickableButtonShare(true)

        }
        view.onShareAction().subscribe {
            view.isClickableButtonShare(false)
            view.shareLinkAndMap(placeId!!, AppHelper.preferences.getCurrentCity().id.toInt(), Tools.getPlaceTypeKeyById(placeType!!))
        }
        view.onCallAction().subscribe {
            view.call(currentPlace?.phone!!)
        }
        view.onDirectionsAction().subscribe {
            view.showDialog(DialogModel().build(view.getViewContext(), view.getViewContext().getString(R.string.Map))
                    .showMessage(view.getViewContext().getString(R.string.title_confirm_location))
                    .showFirstButton(view.getViewContext().resources.getString(R.string.title_cancel))
                    .showLastButton(view.getViewContext().resources.getString(R.string.title_ok))
                    .isCancable(false)).subscribe {
                if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                    it.dialog.dismiss()

                }
                if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                    it.dialog.dismiss()
                    createRoute()


                }
            }

        }


        view.onWebAction().subscribe {
            view.openWeb(currentPlace?.website!!)
        }
        view.mapIsReady().subscribe {
            initMarker()
        }
        view.currentDayListener().subscribe {
            if (it < workPeriod.size)
                view.setStaticsDay(workPeriod[it])
            else
                view.setStaticsDay("")

        }
        view.onClickGallery().subscribe {
            view.openGallery(loadedPhotos, currentPlace!!.name!!)
        }
        view.onGraphicContainer().subscribe {
            view.isShowGraphic()
        }
        view.onPhotoPreviewClick().subscribe {
            view.showPhoto(loadedPhotos, it, currentPlace!!.name!!)
        }

        view.onReportStatisticAction().subscribe {
            remainder.openReportStatistic(placeId!!)
        }

        view.onPlaceDetailsReportAction().subscribe {
            remainder.openPlaceDetailsReport(placeId!!, placeType.toString())
        }

    }

    val callback = object : LocationProvider.LocationCallback {
        override fun onNewLocationAvailable(lat: Float, lon: Float) {
            handleOpenMap(lat, lon)
            Log.d("Location_test", "onNewLocationAvailable")

        }

        override fun locationServicesNotEnabled() {
            view.showGPSEnabledSettingDialog()
        }

        override fun updateLocationInBackground(lat: Float, lon: Float) {
            handleOpenMap(lat, lon)
            Log.d("Location_test", "updateLocationInBackground")


        }

        override fun networkListenerInitialised() {
        }
    }


    var isOpenMap = false;
    var isСoordinatesReady = false

    private fun createRoute() {
        isOpenMap = true
        isСoordinatesReady = false
        view.getPermissionLocation(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!isСoordinatesReady) {
                        showDialogGetingСoordinates()
                    }

                }, 4000)


                val locationProvider = LocationProvider.Builder()
                        .setContext(view.getViewContext())
                        .setListener(callback)
                        .create()

                locationProvider.requestLocation()

            }


            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                isOpenMap = false
                view.showDialog(DialogModel().build(view.getViewContext(), view.getViewContext().getString(R.string.title_permi_to_locale)).isAutoCloseFirstButton(true)
                        .showFirstButton(view.getViewContext().resources.getString(R.string.title_ok)))


            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                token!!.continuePermissionRequest();
            }
        })
    }


    private fun showDialogGetingСoordinates() {
        view.showDialog(DialogModel().build(view.getViewContext(),
                view.getViewContext().getString(R.string.title_getting_coordinate_header), true)
                .showMessage(view.getViewContext().getString(R.string.title_getting_location))
                .showFirstButton(view.getViewContext().resources.getString(R.string.title_cancel))
                .isCancable(false)).subscribe {

            if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                it.dialog.dismiss()
                isOpenMap = false
                isСoordinatesReady = false

            }


        }


    }

    private fun handleOpenMap(lat: Float, lon: Float) {

        closeDialogs()



        if (fragmentIsActive && isOpenMap) {
            isOpenMap = false
            isСoordinatesReady = true
            var with = LatLng(lat.toDouble(), lon.toDouble())
            view.openRout(LatLng(currentPlace!!.location!!.lat, currentPlace!!.location!!.lng), with)
        }
    }

    private fun closeDialogs() {
        view.closeDialog()
    }

    private fun initMarker() {
        if (location != null)
            view.initMarker(Tools.getIconForMarker(placeType!!), location!!)
    }


    private fun createStaticsPage(place: PlaceDetails) {
        var items = ArrayList<ArrayList<TimeData>>()


        place!!.popularTimes!!.forEachIndexed { index, popularTimes ->
            var dayStatics = ArrayList<TimeData>()
            for (i in 6 until popularTimes.data.size) {
                dayStatics.add(TimeData(i, popularTimes.data[i], Tools.getColorByPercentage(popularTimes.data[i])))
            }

            if (index < place!!.popularTimes!!.size - 1) {
                for (i in 0 until 6) {
                    dayStatics.add(TimeData(i, place!!.popularTimes!![index + 1].data[i], Tools.getColorByPercentage(place!!.popularTimes!![index + 1].data[i])))
                }
            } else {
                for (i in 0 until 6) {
                    dayStatics.add(TimeData(i, place!!.popularTimes!![0].data[i], Tools.getColorByPercentage(place!!.popularTimes!![0].data[i])))
                }
            }



            items.add(dayStatics)
        }

        view.initViewPager(items)


    }
}