package com.lemust.ui.screens.main.map

import android.Manifest
import android.arch.lifecycle.Lifecycle
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.hairdresser.ui.main.customer.reward_points.adapter.HourlyStatisticsAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.description.DescriptionPlaceDTO
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.base.BaseView
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.base.navigation.NavigationController
import com.lemust.ui.screens.details.PlaceDetailsActivity
import com.lemust.ui.screens.main.MainActivity
import com.lemust.ui.screens.main.MainActivity.Companion.ON_ACTIVITY_RESULT_LOCALE
import com.lemust.ui.screens.main.MainActivity.Companion.ON_ACTIVITY_RESULT_PLACE_DETAILS
import com.lemust.ui.screens.main.map.adapter.HourItem
import com.lemust.ui.screens.main.map.adapter.HourlyLayoutManager
import com.lemust.ui.screens.main.map.adapter.RecyclerViewDisabler
import com.lemust.ui.screens.main.map.helpers.filters.TypeFilterModel
import com.lemust.ui.screens.main.map.helpers.markers.models.MarkerItem
import com.lemust.ui.screens.search.SearchActivity
import com.lemust.utils.*
//import com.lemust.ui.screens.search.SearchFragmentDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.item_main_screen_filter.view.*
import kotlinx.android.synthetic.main.loader.view.*
import kotlinx.android.synthetic.main.view_preview.view.*
import java.util.concurrent.TimeUnit


class MainView(var activity: MainActivity, var root: View) : MainContract.View, BaseView(activity!!), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    override fun isVisibleLeftMenu(): Boolean {
        return activity.navigation.isShowedLeftMenu
    }

    override fun hideMenuWithoutAnimation() {
        activity.navigation.hideMenuWithoutAnimated()
    }

    override fun isTimeWheelSelected(): Boolean {
        return isTimewheelSelected
    }

    override fun onFilterException(): Observable<Throwable> {
        return activity.onFilterExeptionAction
    }

    override fun isSelectedCurrentTime(): Boolean {
        return activity.isSelectCurrentTime
    }

    override fun showChangedFilterServerDialog(): Observable<DialogModel.OnDialogResult> {
        return showDialog(DialogModel().build(activity!!, "There were changes to filters on the server")
                .showMessage("Filters was be changed")
                .showFirstButton(activity!!.resources.getString(R.string.title_reload))
                .single(true))
    }

    override fun getCtx(): Context {
        return activity
    }

    override fun showRequestError(e: Throwable) {
        var errorMessage = ErrorUtils(e, false, activity)
        errorMessage.parse()

        Handler(Looper.getMainLooper()).post {
            try {
                showDialog(DialogModel().build(activity, errorMessage.titleError).showMessage(errorMessage.bodyError)
                        .showFirstButton(activity.resources.getString(R.string.title_ok))
                        .isAutoCloseFirstButton(true)
                        .single(true))
            } catch (e: Exception) {
                System.out.print(e.localizedMessage)
            }
        }
    }

    override fun onOpenPlaceDetailsActivity(): Observable<Any> {
        return RxView.clicks(root.frameLayout)
    }


    override fun closePreview() {
        root.post {
            root.dialog_preview.visibility = View.GONE
            cleenPreviewFields()
        }


    }

    fun cleenPreviewFields() {
        root.tv_place_name_preview1.text = ""
        root.iv_place_type.setImageResource(0)
        root.tv_place_types.text = ""
        root.tv_description.text = ""
        root.ratingBar.setRating(0f)
        root.iv_place.setImageResource(0)
        root.iv_flag.visibility = View.INVISIBLE

    }

    override fun showPreview(placeNamePreview: String, shortDescription: String, typeString: String, placeType: String, preview: String, currentIcon: Int, rating: Double, isGayFriendly: Boolean, googleId: String) {
        cleenPreviewFields()
        root.dialog_preview.visibility = View.VISIBLE
        root.tv_place_name_preview1.text = placeNamePreview
        root.iv_place_type.setImageResource(currentIcon)
        root.tv_place_types.text = placeType
        root.tv_description.text = shortDescription
        root.ratingBar.setRating(rating.toFloat())

        if (preview.isNotEmpty()) {
            Glide.with(context!!)
                    .load(preview)
                    .into(root.iv_place)
        } else if (googleId.isNotEmpty()) {
            PhotoTools().initPhotoByGooglePLaceId(googleId, root.iv_place)
        }

        if (isGayFriendly) {
            root.iv_flag.visibility = View.VISIBLE
        } else {
            root.iv_flag.visibility = View.INVISIBLE

        }

    }

    override fun isAvailableLocation(): Boolean {
        return GpsTracker(activity).isAvailableLocation()
    }

    override fun getPermissionLocation(permission: PermissionListener) {
        activity.getPermissionLocation(permission)
    }

    override fun getLocation(): Observable<GpsTracker.LocationResult> {
        return GpsTracker(activity).getCurrentLocation()
    }


    override fun showLeftMenu() {
        activity.navigation.showLeftMenu()

    }

    override fun showRightMenu() {
        activity.navigation.showRightMenu()
    }

    override fun hideMenu() {
        activity.navigation.hideMenu()
    }


    private var filtersItemViewIdAndPlaceTypeKey = HashMap<Int, Int>()
    private var filtersItemPlaceTypeKeyAndViewId = HashMap<Int, View>()
    private var actionFilterClick = PublishSubject.create<Int>()
    private var actionZoom = PublishSubject.create<Float>()
    private var previousZoomLevel = 14f
    private var onMapObserver = PublishSubject.create<Any>()
    private var onTimeChangedObserver = PublishSubject.create<HourItem>()
    private var onMarkerClickObserver = PublishSubject.create<MarkerItem>()
    private var onReloadAction = PublishSubject.create<Any>()
    private var recycler = root.findViewById<RecyclerView>(R.id.rv_hourly_statistics)
    private var adapter: HourlyStatisticsAdapter? = null
    private var hours = ArrayList<HourItem>()
    private var markerList = HashMap<Int, Marker>()
    private var snapHelper = LinearSnapHelper()
    private var googleMap: GoogleMap? = null
    // private val previewDialog = PreviewDialog()
    // private var searchFragment: SearchFragmentDialog? = null
    private var loadCitiesWithNotLocation = PublishSubject.create<Any>()!!
    private var mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    private var lastSelectedItemPosition = 0
    private var manager: HourlyLayoutManager? = null
    private var isTimewheelSelected = true


    init {
        initRecycler()
        initAction()


    }


    override fun getAvailableMarkers(): HashMap<Int, Marker> {
        return markerList
    }

    fun initAction() {
        root.btn_reload.setOnClickListener {
            onReloadAction.onNext(Any())
        }

        root.dialog_preview.setOnClickListener {
            closePreview()
        }


    }


    override fun onDestroy() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap!!.isMyLocationEnabled = false
                googleMap!!.clear()
            }


        }
        filtersItemViewIdAndPlaceTypeKey.clear()
        filtersItemPlaceTypeKeyAndViewId.clear()
        actionFilterClick.onComplete()
        actionZoom.onComplete()
        onMapObserver.onComplete()
        onTimeChangedObserver.onComplete()
        onMarkerClickObserver.onComplete()
        onReloadAction.onComplete()
        adapter!!.onClickObservable.onComplete()
        markerList.clear()
        loadCitiesWithNotLocation.onComplete()
        removeFilters()
        clearMarkers()
        markerList.clear()


    }


    override fun getMap(): GoogleMap {
        return googleMap!!
    }

    override fun takeSnapshot(map: Bitmap): Bitmap {
        return Tools.loadBitmapFromView(root.ui_container, map)
    }

    override fun isEnabledTakeSnapshotButton(isEnabled: Boolean) {
        root.btn_camera.isClickable = isEnabled

    }

    override fun onTakeScreenshotAction(): Observable<Any> {
        return RxView.clicks(root.btn_camera)
                .debounce(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
    }

    override fun onCloseReportStateAction(): Observable<Any> {
        return RxView.clicks(root.btn_report_reset)
    }

    override fun closeReportState() {
        root.layout_report_mode.visibility = View.INVISIBLE
        root.navigation_report_container.visibility = View.INVISIBLE
        root.menuButton.isClickable = true
        isActiveSearchAndFilters(true)

    }


    override fun isClickableMenu(isEnbled: Boolean) {
        root.menuButton.isClickable = isEnbled
    }

    override fun isEnabledReportType(isEnbled: Boolean) {
        if (isEnbled) {
            root.layout_report_mode.visibility = View.VISIBLE
            root.navigation_report_container.visibility = View.VISIBLE
        } else {
            root.layout_report_mode.visibility = View.INVISIBLE
            root.navigation_report_container.visibility = View.INVISIBLE

        }
    }

    override fun closePreviewDialog() {
        root.dialog_preview.visibility = View.GONE
    }


    override fun isVisibleButtonLocation(isVisible: Boolean) {
        if (isVisible) {
            root.btn_city_location.visibility = View.VISIBLE
        } else {
            root.btn_city_location.visibility = View.GONE


        }
    }

    override fun isActiveSearchAndFilters(isActive: Boolean) {
        if (isActive) {
            root.iv_filter_btn.isEnabled = true
            root.iv_filter_btn.isClickable = true
            root.btn_search.isEnabled = true
            root.btn_search.isClickable = true
            root.menuButton.isClickable = true
        } else {
            root.menuButton.isClickable = false
            root.iv_filter_btn.isEnabled = false
            root.iv_filter_btn.isClickable = false
            root.btn_search.isEnabled = false
            root.btn_search.isClickable = false
        }
    }


    override fun isActiveSearchButton(isActive: Boolean) {
        root.btn_search.isEnabled = isActive

    }

    var disabler: RecyclerView.OnItemTouchListener = RecyclerViewDisabler()

    override fun isActiveTimeWheel(isActive: Boolean) {
        if (isActive) {
            recycler.removeOnItemTouchListener(disabler);
        } else {
            recycler.addOnItemTouchListener(disabler);
        }
    }

    override fun onLeftMenuAction(): Observable<Any> {
        return RxView.clicks(root.menuButton)
    }

    override fun showGPSEnabledSettingDialog(isLoadDefaultCity: Boolean) {
        Handler(Looper.getMainLooper()).post {
            showDialog(DialogModel().build(activity, activity.getString(R.string.title_GPS_settings))
                    .showMessage(activity.getString(R.string.title_PS_is_not_enabled))
                    .showLastButton(activity.resources.getString(R.string.title_setting))
                    .showFirstButton(activity.resources.getString(R.string.title_cancel))
                    .single(true)).subscribe {
                if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                    dialog!!.dismiss()
                    if (isLoadDefaultCity)
                        loadCitiesWithNotLocation.onNext(Any())

                }
                if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                    dialog!!.dismiss()
                    if (isLoadDefaultCity) {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        activity.startActivityForResult(intent, ON_ACTIVITY_RESULT_LOCALE)
                    } else {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        activity.startActivity(intent)
                    }


                }
            }
        }


    }


    override fun isSearchState(isSearchState: Boolean) {
        if (isSearchState) {
            root.btn_search.setImageResource(R.drawable.ic_close_white_24dp)
        } else {
            root.btn_search.setImageResource(R.drawable.ic_search_white_24dp)

        }
    }


    override fun showSearchScreen(cityId: Int) {
        SearchActivity.start(activity, cityId)
    }


    override fun onSearchButtonAction(): Observable<Any> {
        return RxView.clicks(root.btn_search)
    }


    override fun getCurrentTimePosition(): Int? {
        val centerView = snapHelper.findSnapView(manager)
        val pos = manager?.getPosition(centerView)
        return pos
    }


    override fun hideMarkers(list: List<com.lemust.repository.models.rest.Place>) {
        list.forEach {
            if (markerList.containsKey(it.id))
                markerList[it.id]!!.isVisible = false
        }
    }


    override fun showMarkers(list: List<com.lemust.repository.models.rest.Place>) {
        val newList = mutableListOf(*list.toTypedArray())
        newList.forEach {
            Handler(Looper.getMainLooper()).post(Runnable() {
                if (markerList.containsKey(it.id))
                    markerList[it.id]!!.isVisible = true
            });
        }
    }

    override fun removeFilters() {
        root.container_for_filters.removeAllViews()
        filtersItemPlaceTypeKeyAndViewId.clear()
        filtersItemViewIdAndPlaceTypeKey.clear()
    }


    override fun addItemsFilterInContainer(types: List<TypeFilterModel>) {
        types.forEach {
            addItemFilterInContainer(it.isCurrent, it.placeId, it.name, it.currentItemPosition, it.sizeFilters)
        }
    }

    private fun addItemFilterInContainer(isCurrent: Boolean, placeId: Int, name: String, currentItemPosition: Int, sizeFilters: Int) {
        var vi = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val child = vi.inflate(R.layout.item_main_screen_filter, null)
        child.id = Tools.generateViewId(placeId)
        child.tag = currentItemPosition
        filtersItemViewIdAndPlaceTypeKey.put(child.id, placeId)
        filtersItemPlaceTypeKeyAndViewId.put(placeId, child)

        child.iv_filter_icon.setImageResource(Tools.getIconForFilter(placeId))
        child.iv_filter_icon_un_selected.setImageResource(Tools.getIconForFilter(placeId))
        child.tv_filter_type_name.text = name

        child.ll_selected_item.setBackgroundResource(Tools.getCoverBackground(placeId))
        child.iv_filter_icon_un_selected.setBackgroundResource(R.drawable.background_filter_un_selected_item)



        child.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                actionFilterClick.onNext(filtersItemViewIdAndPlaceTypeKey!![v!!.id]!!)
            }
        })

        var wm = LeMustApp.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var display = wm.getDefaultDisplay()
        if (isCurrent) {
            createSelectedItem(child, currentItemPosition, sizeFilters)
        } else {
            createUnSelectedItem(child, display, currentItemPosition, sizeFilters)
        }
        root.container_for_filters.addView(child)

    }

    private fun createUnSelectedItem(child: View, display: Display, currentItemPosition: Int, sizeFilters: Int) {
        child.ll_selected_item.visibility = View.INVISIBLE
        child.iv_filter_icon_un_selected.visibility = View.VISIBLE

        var params = LinearLayout.LayoutParams(Tools.convertDpToPixel(32f, activity).toInt(), LinearLayout.LayoutParams.MATCH_PARENT)
        if (currentItemPosition == 0) {
            params.setMargins(0, 0, 8, 0)
        } else if (currentItemPosition == sizeFilters - 1) {
            params.setMargins(8, 0, 0, 0)
        } else {
            params.setMargins(8, 0, 8, 0)

        }
        params.gravity = Gravity.CENTER
        child.layoutParams = params
    }


    private fun createSelectedItem(child: View, currentItemPosition: Int, sizeFilters: Int) {
        child.ll_selected_item.visibility = View.VISIBLE
        child.iv_filter_icon_un_selected.visibility = View.INVISIBLE

        var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        if (currentItemPosition == 0) {
            params.setMargins(0, 0, 8, 0)
        } else if (currentItemPosition == sizeFilters - 1) {
            params.setMargins(8, 0, 0, 0)
        } else {
            params.setMargins(8, 0, 8, 0)

        }
        params.weight = 1F
        params.gravity = Gravity.CENTER
        child.layoutParams = params
    }

    override fun selectItem(placeId: Int) {
        //   currentPlaceId = Tools.getPlaceTypeKeyById(placeId)
        if (filtersItemPlaceTypeKeyAndViewId!!.containsKey(placeId)) {
            var view = filtersItemPlaceTypeKeyAndViewId!![placeId]!!
            var wm = LeMustApp.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            var display = wm.defaultDisplay
            filtersItemPlaceTypeKeyAndViewId.forEach {
                if (it.key == placeId) {
                    createSelectedItem(view, view.tag as Int, filtersItemPlaceTypeKeyAndViewId.size)
                } else {
                    createUnSelectedItem(it.value, display, it.value.tag as Int, filtersItemPlaceTypeKeyAndViewId.size)
                }
            }
        }
    }

    override fun onZoomAction(): Observable<Float> {
        return actionZoom
    }

    override fun updateTimeWheelPosition(pos: Int) {
        var center = ((Int.MAX_VALUE / adapter!!.itemsData.size) / 2) * adapter!!.itemsData.size
//        updateTimeObjects(hours)

        var currentPosition = center + pos

        recycler!!.scrollToPosition(currentPosition)
        recycler.smoothScrollBy(1, 0);

    }

    override fun setHours(hours: ArrayList<HourItem>, currentPosition1: Int) {

        updateTimeObjects(hours)

        var center = ((Int.MAX_VALUE / adapter!!.itemsData.size) / 2) * adapter!!.itemsData.size
        recycler!!.scrollToPosition(center + currentPosition1)
    }


    private fun updateTimeObjects(hours: ArrayList<HourItem>) {
        this.hours.clear()
        this.hours.addAll(hours)
        adapter!!.notifyDataSetChanged();
    }


    override fun getCurrentTime(): HourItem {
        return hours[getCurrentTimePosition()!!]
    }

    private fun initRecycler() {
        manager = HourlyLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false, recycler)
        recycler.setHasFixedSize(true);

        recycler.layoutManager = manager
        adapter = HourlyStatisticsAdapter(hours)

        recycler.adapter = this.adapter
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                try {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        val centerView = snapHelper.findSnapView(manager)
                        val pos = manager!!.getPosition(centerView)
                        if (lastSelectedItemPosition != pos) {
                            onTimeChangedObserver.onNext(hours[pos % adapter!!.itemsData.size])
                        }
                        lastSelectedItemPosition = pos


                    }
                } catch (e: Exception) {
                    isTimewheelSelected = false
                    System.out.print(e.localizedMessage)
                }
            }
        });


        recycler.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                recycler.viewTreeObserver.removeOnPreDrawListener(this)
                snapHelper.attachToRecyclerView(recycler)
                return true

            }
        })

        val displayMetrics = DisplayMetrics()
        activity.windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)

        root.rv_hourly_statistics.layoutParams.width = (displayMetrics.widthPixels - displayMetrics.widthPixels / 3.2).toInt()

        root.view_width_divider.layoutParams.width = displayMetrics.widthPixels / 8
    }


    override fun initMap() {
        try {
            val success = googleMap!!.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(activity, R.raw.style))

            if (!success) {
                Log.e(ContentValues.TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(ContentValues.TAG, "Can't find style. Error: ", e)
        }
        googleMap!!.isBuildingsEnabled = false
        googleMap!!.uiSettings.isTiltGesturesEnabled = false;
        googleMap!!.uiSettings.isCompassEnabled = false;
        googleMap!!.isIndoorEnabled = false
        previousZoomLevel = googleMap!!.cameraPosition.zoom
        googleMap!!.setOnCameraChangeListener {
            if (previousZoomLevel != it.zoom) {
                actionZoom.onNext(it.zoom)
            }
            previousZoomLevel = it.zoom;
        }
        googleMap!!.setOnMarkerClickListener(this);


    }

    override fun focusMyLocation(location: LatLng) {

        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location!!.longitude), 14f))
    }


    override fun onMarkerClick(p0: Marker?): Boolean {
        onMarkerClickObserver.onNext(p0!!.tag as MarkerItem)
        return true
    }


    override fun focusMapOnCity(lat: Double, lon: Double, zoomLevel: Int) {
        val place = LatLng(lat, lon)


        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(place, zoomLevel.toFloat()))

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        onMapObserver.onNext(Any())
        onMapObserver.onComplete()


    }


    override fun onMapReady(): Observable<Any> {
        return onMapObserver
    }

    override fun setMarkers(list: List<MarkerItem>, icons: List<BitmapDescriptor>, visibleStates: List<Boolean>) {
        list.forEachIndexed { index, markerItem ->
            var marker = googleMap!!.addMarker(MarkerOptions().position(markerItem.position).visible(visibleStates[index]).anchor(0.5f, 0.6f).icon(icons[index]))
            marker.tag = markerItem
            markerList.put(markerItem.place.id, marker)
        }
    }

    override fun updateMarkers(list: List<Marker>, icons: List<BitmapDescriptor>) {
        try {
            list.forEachIndexed { index, marker ->
                var icon = icons[index]
                marker.setIcon(icon)
            }
        } catch (e: Exception) {
            System.out.print(e.localizedMessage)
        }
    }


    override fun getCurrentPlaceItemMarkers(): List<MarkerItem> {
        return markerList.map { it.value.tag as MarkerItem }
    }


    override fun onTimeChangedAction(): Observable<HourItem> {
        return onTimeChangedObserver
    }

    override fun onFiltersAction(): Observable<Any> {
        return RxView.clicks(root.iv_filter_btn)
    }

    override fun clearMarkers() {
        markerList.clear()
        if (googleMap != null)
            googleMap!!.clear()

    }


    override fun setVisibleLoader(isVisible: Boolean) {
        if (isVisible) {
            root.pb_map.post {
                root.pb_map.visibility = View.VISIBLE
            }
        } else {
            root.pb_map.post {
                root.pb_map.visibility = View.GONE
            }

        }
    }

    override fun setVisibleContent(isVisible: Boolean) {
        if (isVisible) {
            root.container_for_filters.visibility = View.VISIBLE
        } else {
            root.container_for_filters.visibility = View.GONE

        }

    }

    override fun setVisibleButtonReload(isVisible: Boolean) {
        if (isVisible) {
            root.btn_reload.visibility = View.VISIBLE
        } else {
            root.btn_reload.visibility = View.GONE

        }
    }


    override fun onClickFilterAction(): Observable<Int> {
        return actionFilterClick
    }

    override fun setDisplayTimePickerDate(date: String) {
        root.tv_date.text = date
    }

    override fun getCurrentZoomLevel(): Int {
        return googleMap!!.cameraPosition.zoom.toInt()
    }


    override fun getCurrentCenterPosition(): LatLng {
        return LatLng(googleMap!!.cameraPosition.target.latitude, googleMap!!.cameraPosition.target.longitude)
    }

    override fun onReloadAction(): Observable<Any> {
        return onReloadAction
    }

//    override fun showPlacePreviewDialog(markerItem: MarkerItem, placeTypeId: Int, typeCity: String, descriptionPlaceDTO: DescriptionPlaceDTO) {
////        if (searchFragment == null) {
////                if (fragment.isVisible)
////                    showPreviewDialog(markerItem, placeTypeId, descriptionPlaceDTO)
////        } else if (!searchFragment!!.isVisible) {
////                if (fragment.isVisible)
////                    showPreviewDialog(markerItem, placeTypeId, descriptionPlaceDTO)
////
////        }
//    }
//
//    private fun showPreviewDialog(markerItem: MarkerItem, placeTypeId: Int, descriptionPlaceDTO: DescriptionPlaceDTO) {
////        if (!previewDialog.isVisible)
////            previewDialog.showDialog(fragment.childFragmentManager, markerItem, placeTypeId, descriptionPlaceDTO, object : PreviewDialog.DialogPlaceListener {
////                override fun onOpenDetailsScreen(location: LatLng, placeType: Int, placeId: Int) {
////                    if (NetworkTools.isOnline()) {
////                        openPlaceDetails(markerItem.place.id, markerItem.place.location!!.lat, markerItem.place.location!!.lng, placeType)
////                    } else {
////
////                        showDialogWithOneButtons(LeMustApp.instance.resources.getString(R.string.no_internet_connection), "", LeMustApp.instance.resources.getString(R.string.title_ok),
////                                object : DialogController1 {
////                                    override fun action1(dialog: AlertDialog) {
////                                        dialog.dismiss()
////                                    }
////                                })
////                    }
////
////
////                }
////            })
//    }

    override fun openPlaceDetails(placeId: Int, lat: Double?, lon: Double?, placeType: Int) {
        var intent = Intent(activity, PlaceDetailsActivity::class.java)
        if (lat != null && lon != null)
            intent.putExtra(PlaceDetailsActivity.PLACE_LAT_LON_KEY, LatLng(lat, lon))
        intent.putExtra(PlaceDetailsActivity.PLACE_TYPE_ID_KEY, placeType)
        intent.putExtra(PlaceDetailsActivity.PLACE_ID_KEY, placeId)
        intent.putExtra(PlaceDetailsActivity.PLACE_CITY_TYPE, placeType)
        activity!!.startActivityForResult(intent, ON_ACTIVITY_RESULT_PLACE_DETAILS)


    }

    override fun isPermissionGranted(): Boolean {
        return activity.isPermissionGranted()
    }


    override fun onMarckerClick(): Observable<MarkerItem> {
        return onMarkerClickObserver
    }

    override fun isActiveFilterButton(isActive: Boolean) {
        activity.runOnUiThread {

        if (isActive) {
            root.iv_filter_btn.setBackgroundResource(R.drawable.background_filter_active)
        } else {
            root.iv_filter_btn.setBackgroundResource(R.drawable.drawable_menu_button_background_filter)


        }}
    }

    override fun isVisibleFiltersButton(isVisible: Boolean) {
        activity.runOnUiThread {
        if (isVisible) {
            root.container_for_filters.visibility = View.VISIBLE
            root.iv_filter_btn.visibility = View.VISIBLE
        } else {
            root.container_for_filters.visibility = View.INVISIBLE
            root.iv_filter_btn.visibility = View.INVISIBLE
        }}
    }

    override fun updateLocaleResources() {
        root.title_loading.text = activity.resources.getText(R.string.title_loading)
    }

    override fun onShowMyLocationAction(): Observable<Any> {
        return RxView.clicks(root.btn_city_location).debounce(250, TimeUnit.MILLISECONDS)
    }

    override fun isEnableProcessingLocation(isVisible: Boolean) {
        try {
            if (isVisible) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap!!.isMyLocationEnabled = true
                    googleMap!!.getUiSettings().isMyLocationButtonEnabled = false;

                }
                root.btn_city_location.visibility = View.VISIBLE
                root.divider.visibility = View.VISIBLE
                isVisibleButtonLocation(true)
            } else {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap!!.isMyLocationEnabled = false
                    //googleMap!!.getUiSettings().isMyLocationButtonEnabled = false;
                }
                root.btn_city_location.visibility = View.GONE
                root.divider.visibility = View.GONE

            }
        } catch (e: Exception) {
            System.err.print(e.localizedMessage)
        }
    }

    override fun onLoadCityWithNotLocationAction(): Observable<Any> {
        return loadCitiesWithNotLocation
    }


    override fun isEnabledReportState(isReportState: Boolean) {
    }

}