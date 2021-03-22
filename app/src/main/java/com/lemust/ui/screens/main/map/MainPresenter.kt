package com.lemust.ui.screens.main.map

import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.City
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.description.DescriptionPlaceDTO
import com.lemust.repository.models.rest.place.PlaceTypeDTO
import com.lemust.repository.models.rest.request.BaseFilter
import com.lemust.repository.models.rest.request.MarkerFilterRequest
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.ui.AppConst
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.filters.base.*
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.main.map.helpers.LifeCircleHelper
import com.lemust.ui.screens.main.map.helpers.filters.PlaceTypeHelper
import com.lemust.ui.screens.main.map.helpers.hours.HoursHelper
import com.lemust.ui.screens.main.map.helpers.markers.MarkerHelper
import com.lemust.ui.screens.main.map.helpers.markers.models.MarkerItem
import com.lemust.ui.screens.main.map.helpers.markers.tools.IconGenerator
import com.lemust.ui.screens.main.map.helpers.markers.tools.IconGeneratorBuffer
import com.lemust.utils.*
import com.squareup.otto.Bus
import com.squareup.otto.Subscribe
import io.nlopez.smartlocation.SmartLocation
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import locationprovider.davidserrano.com.LocationProvider

    
class MainPresenter(var view: MainContract.View, var eventBus: Bus, var remainder: MainContract.Remainder?) : MainContract.Presenter, LifecycleObserver {
    private var typeCity = ""
    var lifeCircleHelper = LifeCircleHelper()

    private var filters = HashMap<Int, ArrayList<BaseFilter>>()
    private var placeType = mutableListOf<PlaceTypeDTO>()
    private var tmpPlaces = mutableListOf<Place>()
    var dialog: Dialog? = null

    private var mRequestDisposable: Disposable? = null
    private var itemPlaceDisposable: Disposable? = null
    private var cityDisposable: Disposable? = null
    var trackСhangesOnTimewheelDisposble: Disposable? = null

    var locationComposite = CompositeDisposable()
    var fragmentIsActive = false
    var locationProvider:LocationProvider?=null

    val hoursHelper = HoursHelper()
    var placeTypeHelper: PlaceTypeHelper = PlaceTypeHelper()
    var markerHelper = MarkerHelper(placeTypeHelper, hoursHelper)

    var possiblePlaceDetails: MarkerItem? = null

    var isOpenMap = false;
    var isСoordinatesReady = false


    private fun focusMyLocation() {
        isOpenMap = true
        isСoordinatesReady = false
        view.getPermissionLocation(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!isСoordinatesReady) {
                        showDialogGetingСoordinates()
                    }

                }, 2000)

                locationProvider?.requestLocation()

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
                .showMessage(view.getViewContext().getString(R.string.title_getting_location)).showFirstButton(view.getViewContext().resources.getString(R.string.title_cancel))
                .isCancable(false)).subscribe {

            if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                it.dialog.dismiss()
                isOpenMap = false
                isСoordinatesReady = false

            }


        }
    }


    val callback = object : LocationProvider.LocationCallback {
        override fun onNewLocationAvailable(lat: Float, lon: Float) {
            handleOpenMap(lat, lon)
            Log.d("Mapt_test","onNewLocationAvailable")

        }

        override fun locationServicesNotEnabled() {
            view.showGPSEnabledSettingDialog(false)
            Log.d("Mapt_test","locationServicesNotEnabled")

        }

        override fun updateLocationInBackground(lat: Float, lon: Float) {
            handleOpenMap(lat, lon)
            Log.d("Mapt_test","updateLocationInBackground")



        }

        override fun networkListenerInitialised() {
            Log.d("Mapt_test","updateLocationInBackground")

        }
    }


    private fun handleOpenMap(lat: Float, lon: Float) {

        view.closeDialog()
        view.isEnableProcessingLocation(true)

        if (fragmentIsActive && isOpenMap) {
            isOpenMap = false
            isСoordinatesReady = true
            var with = LatLng(lat.toDouble(), lon.toDouble())
            view.focusMyLocation(with)
        }
    }

    init {
        initServices()
        initAction()
        checkInternet()
        view.isVisibleButtonLocation(true)

    }

    private fun initServices() {
        eventBus.register(this)
        lifeCircleHelper.resume(view.getCtx(), view.isSelectedCurrentTime(), hoursHelper, view)
        locationProvider = LocationProvider.Builder()
                .setContext(view.getViewContext())
                .setListener(callback)
                .create()
    }

    private fun checkInternet() {
        view.isActiveSearchAndFilters(false)
        if (!NetworkTools.isOnline()) {
            view.setVisibleLoader(false)
            view.setVisibleButtonReload(true)
        }
    }

    private fun initLifeCircleAction(it: LifeCircleHelper.LifeCircleAction?) {
        when (it) {
            LifeCircleHelper.LifeCircleAction.CHANGE_CITY_STATE -> {
                changeCity(AppHelper.preferences.getCurrentCity())
            }
            LifeCircleHelper.LifeCircleAction.RELOAD_STATE -> {
                prepareLocationAndPermission()

            }
            LifeCircleHelper.LifeCircleAction.CHANGE_LANGUAGE -> {
                updateResources()

            }
            LifeCircleHelper.LifeCircleAction.CHANGE_LANGUAGE_FOCUS -> {
                view.focusMapOnCity(AppHelper.preferences.getCurrentCity().latitude!!, AppHelper.preferences.getCurrentCity().longitude!!)
                updateResources()

            }
            LifeCircleHelper.LifeCircleAction.RESTORE_STATE -> {
                tmpPlaces.clear()
                tmpPlaces.addAll(lifeCircleHelper.places)
                var city = AppHelper.preferences.getCurrentCity()
                view.focusMapOnCity(city.latitude!!, city.longitude!!)
                initTypeFiltersUI(lifeCircleHelper.placeTypeModels)
                placeTypeHelper.changeCurrentPlaceType(lifeCircleHelper.placeId)
                prepareMarkersForMap(lifeCircleHelper.places)

            }
        }


    }


    override fun onSaveState(savedInstanceState: Bundle) {
        lifeCircleHelper.saveState(savedInstanceState, tmpPlaces, markerHelper.savedCurrentPlaceMarkers, filters, placeTypeHelper.currentPlaceTypeId, placeTypeHelper.placesTypeDTO)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        lifeCircleHelper.restoreState(savedInstanceState)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        hoursHelper.isWatchingForHourChanges = true
        fragmentIsActive = true

        if (!view.isTimeWheelSelected()) {
            var hour = hoursHelper.currrentHourItem
            hour?.let {
                view.updateTimeWheelPosition(it.hourInt + it.dayPosition)

            }

        }


        if (AppDataHolder.cancelShareMode) {
            AppDataHolder.cancelShareMode = false
            view.closeReportState()
            view.isEnabledTakeSnapshotButton(true)
            markerHelper.skip()

        }

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        fragmentIsActive = false

        hoursHelper.isWatchingForHourChanges = false
        AppDataHolder.timeDelay = TimeHelper.getCurrentHour()
    }


    @SuppressLint("CheckResult")
    override fun initAction() {
        view.onMapReady().subscribe {
            view.initMap()
            lifeCircleHelper.getAction().subscribe {
                initLifeCircleAction(it)

            }

            remainder!!.onLocationAction().subscribe {
                lifeCircleHelper.prepareLocationAndPermission()
            }

        }
        view.onOpenPlaceDetailsActivity().subscribe {
            possiblePlaceDetails?.let {
                view.openPlaceDetails(it.place.id, it.place.location!!.lat, it.place.location!!.lng, placeTypeHelper.currentPlaceTypeId)
                view.closePreviewDialog()
            }

        }

        remainder!!.onTakeScreenAction().subscribe {
            enabledReportType()

        }


        remainder!!.onSearchAction().subscribe {
            enableSearchState(it)
        }
        remainder!!.onCityChanged().subscribe {
            lifeCircleHelper.changCity()
        }

        remainder!!.onLanguageChanged().subscribe {
            lifeCircleHelper.changLanguage()
        }



        view.onLoadCityWithNotLocationAction().subscribe {
            AppDataHolder.isCancableLocation = true
            loadCities()
        }


        //Navigation
        view.onCloseReportStateAction().subscribe {
            markerHelper.resetScreenState()
            view.closeReportState()

        }
        view.onTakeScreenshotAction().subscribe {
            view.isEnabledTakeSnapshotButton(false)
            view.getMap().snapshot {
                AppDataHolder.saveScreenshot(view.takeSnapshot(it))
                remainder!!.openScreenshotPreviewActivity()
                view.isEnabledTakeSnapshotButton(true)


            }
        }



        remainder!!.onSharingResetAction().subscribe {
            markerHelper.resetScreenState()
            view.closeReportState()

        }


        view.onLeftMenuAction().subscribe {
            view.closePreviewDialog()
            view.showLeftMenu()
            view.closePreviewDialog()
            itemPlaceDisposable?.let {
                if (!it.isDisposed) {
                    view.setVisibleLoader(false)
                    it.dispose()

                }
            }

        }
        view.onSearchButtonAction().subscribe {
            onSearch()
        }
        view.onClickFilterAction().subscribe {
            if (it != placeTypeHelper!!.currentPlaceTypeId) {
                indexUpdateMarkersThread++

                IconGeneratorBuffer.map.clear()
                placeTypeHelper!!.changeCurrentPlaceType(it)
                view.selectItem(it)
                view.setVisibleLoader(true)
                view.clearMarkers()
                var nawFilters = ArrayList<BaseFilter>()
                if (filters.containsKey(placeTypeHelper!!.currentPlaceTypeId)) {
                    nawFilters.addAll(filters.get(placeTypeHelper!!.currentPlaceTypeId)!!)
                }

                var slug = ""
                var currentSlug = placeType.filter { placeTypeHelper!!.currentPlaceTypeId == it.id }[0].slug
                if (currentSlug != null) {
                    slug = currentSlug!!
                }

                var request = MarkerFilterRequest(AppHelper.preferences.getCurrentCity()!!.id.toInt(), slug, nawFilters)
                if (request.filter!!.size > 0) {
                    view.isActiveFilterButton(true)
                } else {
                    view.isActiveFilterButton(false)

                }
                markerHelper.clearMarkers()
                AppHelper.api.getPlacesByFilter(request).subscribe(placeObserver())
            }
        }
        view.onReloadAction().subscribe {
            view.removeFilters()
            if (!AppDataHolder.isLocationAvailable) {
                prepareLocationAndPermission()
            } else {
                view.focusMapOnCity(AppHelper.preferences.getCurrentCity()!!.latitude!!, AppHelper.preferences.getCurrentCity()!!.longitude!!)
                view.setVisibleLoader(true)
                view.removeFilters()
                loadFiltersByCity()
            }
        }

        view.onFiltersAction().subscribe {
            view.closePreviewDialog()
            eventBus.post(FiltersFragment.CleanScreens())
            view.showRightMenu()
            eventBus.post(MainActivity.InitFilters(AppHelper.preferences.getCurrentCity()!!.id.toInt(), placeTypeHelper!!.currentPlaceTypeId))
        }

        trackСhangesOnTimewheelDisposble = view.onTimeChangedAction().subscribe {
            if (placeTypeHelper.isReady) {
                hoursHelper.setCurrentHourItem(it)
                view.setDisplayTimePickerDate(it.hour + " " + it.day)
                updateMarkers()
            }

        }
        view.onZoomAction().subscribe {
            markerHelper.prepareZoomMarkerAsync(it.toInt(), view.getCurrentCenterPosition(), object : MarkerHelper.ZoomMarkersListener {
                override fun markerUpdated(showMarkers: List<Place>, hideMarkers: List<Place>) {
                    view.showMarkers(showMarkers)
                    view.hideMarkers(hideMarkers)
                }
            })
        }
        view.onMarckerClick().subscribe {
            if (markerHelper.screenState != MarkerHelper.ScreenState.REPORT)
                handleOpenedPlacePreview(it)
        }
        view.onShowMyLocationAction().subscribe {
            checkMyPlacementButton()

            if (view.isAvailableLocation())
                focusMyLocation() else
                view.showGPSEnabledSettingDialog(false)
        }

    }

    private fun prepareLocationAndPermission() {
        if (AppDataHolder.isCancableLocation) {
            loadCities()
        } else
            view.getPermissionLocation(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    handleLocationGranted()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    AppDataHolder.isLocationAvailable = false
                    loadCities()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    token!!.continuePermissionRequest();
                }
            })
    }

    private fun handleLocationGranted() {
        AppDataHolder.isLocationAvailable = true
        try {

            if (view.isAvailableLocation()) {
                view.getLocation().subscribe {
                    if (it.location != null) {
                        loadCities(it.location!!.latitude, it.location!!.longitude)
                    } else {
                        loadCities()
                    }
                }

            } else {
                view.showGPSEnabledSettingDialog()
            }
        } catch (e: Exception) {
            System.out.print(e.localizedMessage)
            loadCities()
        }
    }


    fun loadCities(lat: Double? = null, lon: Double? = null) {
        AppHelper.api.updateCitiesLanguage(lat, lon, languageCode = AppHelper.locale.getLanguage(LeMustApp.instance))
                .subscribe(object : Observer<List<City>> {
                    override fun onError(e: Throwable) {
                        view.setVisibleLoader(false)
                        view.setVisibleButtonReload(true)
                        view.showRequestError(e)
                    }

                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: List<City>) {
                        AppDataHolder.citiesLanguageChanged = false
                        AppHelper.preferences.saveAvailableCities(t)
                        AppHelper.preferences.updateCurrentCity()
                        startApp()

                    }

                })

    }

    private fun onSearch() {
        if (markerHelper.screenState == MarkerHelper.ScreenState.SEARCH) {
            closeSearchState()
        } else {
            view.closePreviewDialog()
            view.showSearchScreen(AppHelper.preferences.getCurrentCity()!!.id.toInt())
        }
    }

    private fun handleOpenedPlacePreview(it: MarkerItem) {
        view.setVisibleLoader(true)
        AppHelper.api.getDescriptionPlace(it.place.id, AppHelper.preferences.getCurrentCity().id.toInt(), placeTypeHelper!!.currentPlaceTypeId).subscribe(object : Observer<DescriptionPlaceDTO> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                if (itemPlaceDisposable != null) {
                    if (!itemPlaceDisposable!!.isDisposed) {
                        itemPlaceDisposable!!.dispose()

                    } else {
                        itemPlaceDisposable = d

                    }
                } else {
                    itemPlaceDisposable = d

                }

            }

            override fun onNext(t: DescriptionPlaceDTO) {
                showPreviewDialog(it, t)


            }

            override fun onError(e: Throwable) {
                view.setVisibleLoader(false)
                view.showRequestError(e)
                view.closePreview()
            }
        })
    }

    private fun showPreviewDialog(it: MarkerItem, t: DescriptionPlaceDTO) {
        if (!view.isVisibleLeftMenu()) {
            var placeNamePreview = ""
            var shortDescription = ""
            var typeString = ""
            var placeType = ""
            var preview = ""
            var currentIcon = 0
            var rating = 0.0
            var isGayFriendly = false
            var googleId = ""

            t.googlePlaceId?.let {
                googleId = it
            }
            t.name?.let {
                placeNamePreview = it
            }
            t.preview?.let {
                preview = it
            }
            t.features?.let {
                when (Tools.getPlaceTypeKeyById(placeTypeHelper.currentPlaceTypeId)) {
                    nightClubKey -> {
                        it.nightClub?.let {
                            typeString = Tools.appendStrings(it, "•")
                        }
                        currentIcon = R.mipmap.icn_place_main_nightclub
                    }
                    barKey -> {
                        it.bar?.let {
                            typeString = Tools.appendStrings(it, "•")
                        }
                        currentIcon = R.mipmap.icn_place_main_bars
                    }
                    restaurantKey -> {
                        it.restaurant?.let {
                            typeString = Tools.appendStrings(it, "•")
                        }
                        currentIcon = R.mipmap.icn_place_main_restaurants
                    }
                    karaokeKey -> {
                        it.karaoke?.let {
                            typeString = Tools.appendStrings(it, "•")
                        }
                        currentIcon = R.mipmap.icn_place_main_karaoke
                    }
                }
            }

            if (!typeString.isEmpty())
                typeString = " • $typeString"

            placeType = Tools.getPlaceTypeNameById(placeTypeHelper.currentPlaceTypeId, view.getViewContext()) + typeString

            t.shortDescription?.map?.let {
                var key = Tools.getPlaceTypeKeyById(placeTypeHelper.currentPlaceTypeId)
                if (it.containsKey(key)) {
                    shortDescription = it[key]!!
                }
            }

            t.googleRating?.let {
                rating = it
            }

            t.isGayFriendly?.let {
                isGayFriendly = it
            }

            possiblePlaceDetails = it
            view.showPreview(placeNamePreview, shortDescription, typeString, placeType, preview, currentIcon, rating, isGayFriendly, googleId)
        }
        view.setVisibleLoader(false)

    }

    private fun updateMarkers() {
        markerHelper.prepareUpdatedMarkerByHour(hoursHelper.currrentHourItem!!, placeTypeHelper!!.currentPlaceTypeId,
                object : MarkerHelper.UpdateMarkersListener {
                    override fun markerUpdated(markers: List<MarkerItem>) {
                        updateMarkersToMap(markers)
                        view.setVisibleLoader(false)
                        view.showMarkers(markerHelper.currentShownMarkers!!)
                        if (markerHelper.screenState == MarkerHelper.ScreenState.MAP)
                            view.setVisibleContent(true)
                        view.setVisibleButtonReload(false)
                    }
                })
    }


    private fun startApp() {
        if (isActiveSharingPlaceDetails()) {
            var cities = AppHelper.preferences.getAvailableCities()
            cities.filter {
                AppDataHolder.sharePlaceDetailsData!!.cityId == it.id.toInt()
            }.map {
                AppHelper.preferences.saveCurrentCity(it)
            }
        }
        var city = AppHelper.preferences.getCurrentCity()
        view.focusMapOnCity(city!!.latitude!!, city!!.longitude!!)
        loadFiltersByCity()
    }


    private fun checkMyPlacementButton() {

        if (ContextCompat.checkSelfPermission(view.getViewContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            view.showDialog(DialogModel().build(view.getViewContext(), view.getViewContext().getString(R.string.title_permi_to_locale)).isAutoCloseFirstButton(true)
                    .showFirstButton(view.getViewContext().resources.getString(R.string.title_ok)))
            return;
        }

        if (SmartLocation.with(view.getViewContext()).location().state().locationServicesEnabled()) {
            view.isEnableProcessingLocation(true)

        } else {

            view.isEnableProcessingLocation(false)

        }


    }

    private fun loadFiltersByCity() {
        view.isVisibleFiltersButton(false)
        System.gc()
        var currentCity = AppHelper.preferences.getCurrentCity()

        AppHelper.api.getPlaceTypes(currentCity.id.toInt(), currentCity.id.toInt()).subscribe(object : Observer<List<PlaceTypeDTO>> {
            override fun onComplete() = Unit
            override fun onSubscribe(d: Disposable) = Unit
            override fun onNext(t: List<PlaceTypeDTO>) {
                loadPlaces(t)
            }

            override fun onError(e: Throwable) {
                view.setVisibleLoader(false)
                view.setVisibleContent(false)
                view.setVisibleButtonReload(true)
                view.showRequestError(e)
            }
        })
    }


    fun loadPlaces(t: List<PlaceTypeDTO>) {
        if (t.isNotEmpty()) {
            initTypeFiltersUI(t)
            handleLoadPlacesForCity(AppHelper.preferences.getCurrentCity().id.toInt(), placeTypeHelper!!.currentPlaceTypeId)
        } else {
            view.showPositiveDialogOkCallback(view.getCtx().getString(R.string.title_sorry_no_result), "")
            view.isActiveTimeWheel(false)
            view.isActiveSearchButton(false)
            view.setVisibleLoader(false)

        }


    }

    private fun initTypeFiltersUI(t: List<PlaceTypeDTO>) {
        view.removeFilters()
        placeType.clear()
        placeType.addAll(t)
        handleCurrentPlaceTypes(t)
        if (isActiveSharingPlaceDetails()) {
            view.selectItem(AppDataHolder.sharePlaceDetailsData!!.placeTypeId)
            placeTypeHelper!!.changeCurrentPlaceType(AppDataHolder.sharePlaceDetailsData!!.placeTypeId)
        }
        placeTypeHelper.placesTypeDTO.clear()
        placeTypeHelper.placesTypeDTO.addAll(t)
        view.isActiveTimeWheel(true)
        view.isActiveSearchAndFilters(true)
        view.isClickableMenu(true)
    }

    private fun handleCurrentPlaceTypes(t: List<PlaceTypeDTO>) {
        view.removeFilters()
        placeTypeHelper.clear()
        placeTypeHelper.init(t, remainder!!.getContext())
        view.addItemsFilterInContainer(placeTypeHelper!!.placeTypeFilters)

    }


    fun handleLoadPlacesForCity(placeId: Int, typeId: Int) {
        view.setVisibleContent(false)
        view.setVisibleLoader(true)
        AppHelper.api.getPlacesByCity(placeId, typeId).subscribe(placeObserver())

    }


    override fun changePlaceType(placeId: Int) {
        IconGeneratorBuffer.map.clear()
        view.setVisibleLoader(true)
        this.placeTypeHelper!!.changeCurrentPlaceType(placeId)
        handleLoadPlacesForCity(AppHelper.preferences.getCurrentCity()!!.id.toInt(), placeTypeHelper!!.currentPlaceTypeId)
        view.clearMarkers()

    }


    @Subscribe
    fun onEvent(event: MainFragment.ApplyFilters) {
        applyFilters(event)
    }

    @Subscribe
    fun onEvent(event: MainActivity.UpdateResources) {
        updateResources()
    }

    @Subscribe
    fun onEvent(event: MainActivity.FilterChangedToServer) {
        view.showChangedFilterServerDialog().subscribe {
            if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                it.dialog.dismiss()
                eventBus.post(FiltersFragment.ClearFilters())
                changeCity(AppHelper.preferences.getCurrentCity())

            }

        }

    }

    private fun enabledReportType() {
        markerHelper.initScreenState(MarkerHelper.ScreenState.REPORT)
        view.isEnabledReportType(true)
        view.isClickableMenu(false)
        view.isActiveSearchAndFilters(false)
    }

    private fun applyFilters(event: MainFragment.ApplyFilters) {
        view.setVisibleLoader(true)
        view.clearMarkers()
        placeTypeHelper!!.changeCurrentPlaceType(event.placeTypeId!!)
        view.selectItem(event.placeTypeId)
        if (filters.containsKey(event.placeTypeId)) filters[event.placeTypeId]!!.clear()
        filters.put(event.placeTypeId, event.request.filter!!)
        if (event.request.filter!!.size > 0) {
            view.isActiveFilterButton(true)
        } else {
            view.isActiveFilterButton(false)

        }
        AppHelper.api.getPlacesByFilter(event.request).subscribe(placeObserver())
    }


    private fun changeCity(city: City) {
        view.closePreview()
        IconGeneratorBuffer.map.clear()
        eventBus.post(FiltersFragment.ClearFilters())
        placeType.clear()
        placeTypeHelper.clear()
        view.clearMarkers()
        view.removeFilters()

        view.clearMarkers()
        markerHelper.clearMarkers()
        eventBus.post(FiltersFragment.HideContent())
        checkMyPlacementButton()
        placeTypeHelper!!.placeTypeFilters.clear()
        if (markerHelper.screenState == MarkerHelper.ScreenState.SEARCH)
            closeSearchState()
        filters.clear()
        view.isActiveFilterButton(false)
        view.setVisibleLoader(true)
        this.typeCity = city.currentCity
        if (this.typeCity == null) {
            typeCity = String("null".toByteArray())
        }
        view.setVisibleLoader(true)
        view.initMap()
        view.focusMapOnCity(city.latitude!!, city.longitude!!)
        //  this.currentCity = event.city
        AppHelper.preferences.saveCurrentCity(city)
        loadFiltersByCity()
    }

    private fun placeObserver(): Observer<List<Place>> {
        return object : Observer<List<Place>> {
            override fun onNext(t: List<Place>) {


                if (!filters.containsKey(placeTypeHelper.currentPlaceTypeId)) {
                    tmpPlaces.clear()
                    tmpPlaces.addAll(t)
                }

                if (filters.containsKey(placeTypeHelper.currentPlaceTypeId)) {
                    if (filters[placeTypeHelper.currentPlaceTypeId]!!.isEmpty()) {
                        tmpPlaces.clear()
                        tmpPlaces.addAll(t)
                    }
                }






                prepareMarkersForMap(t)
                if (t.isEmpty()) {
                    view.showPositiveDialogOkCallback(view.getCtx().getString(R.string.title_sorry_no_result), "")
                    view.setVisibleLoader(false)
                }
            }

            override fun onComplete() = Unit
            override fun onSubscribe(d: Disposable) {
                if (mRequestDisposable != null)
                    if (!mRequestDisposable!!.isDisposed) {
                        mRequestDisposable!!.dispose()
                    }
                mRequestDisposable = d

            }

            override fun onError(e: Throwable) {
                view.removeFilters()
                view.setVisibleLoader(false)
                view.setVisibleContent(false)
                view.setVisibleButtonReload(true)
                view.showRequestError(e)
            }
        }
    }

    private fun prepareMarkersForMap(t: List<Place>) {
        view.setVisibleButtonReload(false)
        markerHelper.clearMarkers()
        markerHelper.initMarkers(t)
        handlePlaceShareFunctionality()

        markerHelper.prepareNewMarkersByHourForPlaceType(t, hoursHelper.currrentHourItem!!, placeTypeHelper!!.currentPlaceTypeId, view.getCurrentCenterPosition(), view.getCurrentZoomLevel(), object : MarkerHelper.MarkersListener {
            override fun markerCreated(markerItems: List<MarkerItem>, showMarkers: List<Place>, hideMarkers: List<Place>) {
                addMarkersToMap(markerItems, showMarkers)
                if (placeTypeHelper!!.placeTypeFilters.isNotEmpty()) {
                    view.isVisibleFiltersButton(true)
                } else {
                    view.isVisibleFiltersButton(false)
                }
            }

        })
    }

    var indexSetMarkersThread = 0
    private fun addMarkersToMap(markerItems: List<MarkerItem>, showMarkers: List<Place>) {
        System.gc()
        indexSetMarkersThread++
        Thread(Runnable {
            try {
                var listBitmap = mutableListOf<BitmapDescriptor>()
                var listMarker = mutableListOf<MarkerItem>()
                var listVisibleState = mutableListOf<Boolean>()


                var localIndex = indexSetMarkersThread
                if (resetOldThread(localIndex, listBitmap, listMarker, listVisibleState)) return@Runnable



                for (i in 0 until markerItems.size) {
                    if (resetOldThread(localIndex, listBitmap, listMarker, listVisibleState)) return@Runnable
                    var bitmap = BitmapDescriptorFactory.fromBitmap(IconGenerator.drawCenter(markerItems[i]!!.iconBitmap, markerItems[i]!!.place.name))
                    var objects = showMarkers.filter { it.id == markerItems[i].place.id }

                    listMarker.add(markerItems[i])
                    listBitmap.add(bitmap)
                    listVisibleState.add(objects.isNotEmpty())

                }

                if (resetOldThread(localIndex, listBitmap, listMarker, listVisibleState)) return@Runnable

                Handler(Looper.getMainLooper()).post {
                    view.setVisibleLoader(false)
                    view.setMarkers(listMarker, listBitmap, listVisibleState)
                };


            } catch (e: Exception) {
                Log.e("Error", e.localizedMessage)
            }
        }).start()


    }

    private fun resetOldThread(localIndex: Int, listBitmap: MutableList<BitmapDescriptor>, listMarker: MutableList<MarkerItem>, listVisibleState: MutableList<Boolean>): Boolean {
        if (localIndex < indexSetMarkersThread) {
            if (resetShowingMarkers(listBitmap, listMarker, listVisibleState)) return true

        }
        return false
    }

    private fun resetShowingMarkers(listBitmap: MutableList<BitmapDescriptor>, listMarker: MutableList<MarkerItem>, listVisibleState: MutableList<Boolean>): Boolean {
        synchronized(listBitmap) { listBitmap.clear() }
        synchronized(listBitmap) { listMarker.clear() }
        synchronized(listBitmap) { listVisibleState.clear() }
        System.gc()

        return true
    }

    var indexUpdateMarkersThread = 0
    private fun updateMarkersToMap(list: List<MarkerItem>) {
        indexUpdateMarkersThread++
        Thread(Runnable {
            try {
                var localIndex = indexUpdateMarkersThread
                var listBitmap = mutableListOf<BitmapDescriptor>()
                var listMarker = mutableListOf<Marker>()
                var availableMarkers = view.getAvailableMarkers()

                for (i in 0 until list.size) {
                    if (localIndex < indexUpdateMarkersThread) return@Runnable
                    if (availableMarkers.containsKey(list[i].place.id)) {
                        var bitmap = BitmapDescriptorFactory.fromBitmap(IconGenerator.drawCenter(list[i].iconBitmap, list[i].place.name))
                        var marker = availableMarkers[list[i].place.id]
                        if (bitmap != null && marker != null) {
                            listBitmap.add(bitmap)
                            listMarker.add(marker!!)
                        }


                    }
                }
                if (localIndex < indexUpdateMarkersThread) return@Runnable
                Handler(Looper.getMainLooper()).post {
                    view.updateMarkers(listMarker, listBitmap)
                };
            } catch (e: Exception) {
                System.out.print(e.localizedMessage)
            }
        }).start()


    }


    private fun handlePlaceShareFunctionality() {
        if (isActiveSharingPlaceDetails()) {
            AppDataHolder.sharePlaceDetailsData!!.isActive = false
            view.openPlaceDetails(AppDataHolder!!.sharePlaceDetailsData!!.placeId, null, null, AppDataHolder!!.sharePlaceDetailsData!!.placeTypeId)
        }
    }

    public fun isActiveSharingPlaceDetails(): Boolean {
        if (AppDataHolder.sharePlaceDetailsData != null)
            if (AppDataHolder.sharePlaceDetailsData!!.isActive)
                return true
        return false
    }

    private fun updateResources() {
        eventBus.post(FiltersFragment.ClearFilters())
        if (markerHelper.screenState == MarkerHelper.ScreenState.SEARCH)
            closeSearchState()

        hoursHelper.updateLanguageForItems(view.getCtx())
        view.setDisplayTimePickerDate(hoursHelper.currrentHourItem!!.hour + " " + hoursHelper.currrentHourItem!!.day)
        view.updateLocaleResources()
        filters.clear()
        view.isActiveFilterButton(false)
        view.setVisibleLoader(true)
        view.clearMarkers()
        markerHelper.clearMarkers()

        view.setVisibleLoader(true)
        view.initMap()
        loadFiltersByCity()
    }


    private fun enableSearchState(place: SearchItemDTO) {
        view.isSearchState(true)
        view.setVisibleContent(false)
        view.isVisibleFiltersButton(false)
        markerHelper.setCurrentMapParametrs(view.getCurrentZoomLevel(), view.getCurrentCenterPosition())
        markerHelper.enableSearchState(place, view.getCurrentPlaceItemMarkers())

        markerHelper.addSearchStateListener(object : MarkerHelper.SearchStateListener {
            override fun stateActivated(markerItems: List<MarkerItem>, list: List<Place>) {
                view.focusMapOnCity(place.place!!.location!!.lat, place.place!!.location!!.lng)
                view.clearMarkers()
                // view.setMarkers(markerItems, list)
                addMarkersToMap(markerItems, list)

//                  view.showMarkers(list)

            }
        })
    }

    private fun closeSearchState() {
        markerHelper.closeSearchState()
        view.clearMarkers()
        view.isSearchState(false)
        view.setVisibleContent(true)

        placeTypeHelper!!.resetTemporaryPlaceType()
        markerHelper.screenState = MarkerHelper.ScreenState.MAP
        markerHelper.isEnabledZoomPlacesManager = true


        var currentPlaceMarkers = markerHelper.currentPlaceMarkers

        if (currentPlaceMarkers.isEmpty()) {
            changePlaceType(placeTypeHelper.currentPlaceTypeId)
        } else {
            markerHelper.prepareNewMarkerByHourAndCheckZoomLevel(hoursHelper.currrentHourItem!!, placeTypeHelper.currentPlaceTypeId, view.getCurrentCenterPosition(), view.getCurrentZoomLevel(), object : MarkerHelper.MarkersListener {
                override fun markerCreated(markerItems: List<MarkerItem>, showMarkers: List<Place>, hideMarkers: List<Place>) {
                    addMarkersToMap(markerItems, showMarkers)
                }
            })
        }

        if (placeTypeHelper!!.placeTypeFilters.isNotEmpty()) {
            view.isVisibleFiltersButton(true)

        }


    }

    override fun onDestroy() {
        if (mRequestDisposable != null)
            if (!mRequestDisposable!!.isDisposed)
                mRequestDisposable!!.dispose()

        if (trackСhangesOnTimewheelDisposble != null)
            if (!trackСhangesOnTimewheelDisposble!!.isDisposed)
                trackСhangesOnTimewheelDisposble!!.dispose()

        if (itemPlaceDisposable != null)
            if (!itemPlaceDisposable!!.isDisposed)
                itemPlaceDisposable!!.dispose()

        if (cityDisposable != null)
            if (!cityDisposable!!.isDisposed)
                cityDisposable!!.dispose()

        filters.clear()
        placeType.clear()
        hoursHelper.onDestroy()
        markerHelper.onDestroy()
        locationComposite.dispose()
        lifeCircleHelper.getAction().onComplete()

    }
}


