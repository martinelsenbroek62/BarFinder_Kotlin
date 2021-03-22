package com.lemust.ui.screens.main.map

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.description.DescriptionPlaceDTO
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.base.navigation.NavigationController
import com.lemust.ui.screens.main.map.adapter.HourItem
import com.lemust.ui.screens.main.map.helpers.filters.TypeFilterModel
import com.lemust.ui.screens.main.map.helpers.markers.models.MarkerItem
import com.lemust.utils.GpsTracker
import io.reactivex.Observable

    
interface MainContract {
    interface View : BaseViewContract {
        fun initMap()
        fun focusMapOnCity(lat: Double, lon: Double, zoomLevel: Int = 14)
        fun isEnabledReportType(isEnbled: Boolean)
        fun isClickableMenu(isEnbled: Boolean)
        fun onMapReady(): Observable<Any>
        fun closeReportState()
        fun onCloseReportStateAction(): Observable<Any>
        fun onTakeScreenshotAction(): Observable<Any>
        fun getMap(): GoogleMap
        fun takeSnapshot(map: Bitmap): Bitmap


        fun onLeftMenuAction(): Observable<Any>
        //        fun setMarkers(list: List<MarkerItem>):Observable<Any>

        // fun updateMarkers(list: List<MarkerItem>)
        fun setHours(hours: ArrayList<HourItem>, currentPosition: Int)

        fun onTimeChangedAction(): Observable<HourItem>
        fun getCurrentTime(): HourItem
        fun getAvailableMarkers(): HashMap<Int, Marker>
        fun onFiltersAction(): Observable<Any>
        fun onReloadAction(): Observable<Any>
        fun clearMarkers()
        fun setVisibleLoader(isVisible: Boolean)
        fun setVisibleContent(isVisible: Boolean)
        fun setVisibleButtonReload(isVisible: Boolean)
        fun onZoomAction(): Observable<Float>
        fun isVisibleFiltersButton(isVisible: Boolean)
        fun onMarckerClick(): Observable<MarkerItem>
        fun getCurrentCenterPosition(): LatLng
        fun getCurrentZoomLevel(): Int
        fun hideMarkers(list: List<Place>)
        fun showMarkers(list: List<Place>)
        //        fun setMarkers(list: List<MarkerItem>, listShow: List<Place>)
        fun setMarkers(list: List<MarkerItem>, icons: List<BitmapDescriptor>, visibleStates: List<Boolean>)

        fun updateMarkers(list: List<Marker>, icons: List<BitmapDescriptor>)

        fun isActiveFilterButton(isActive: Boolean)
        fun setDisplayTimePickerDate(date: String)

        fun isEnabledReportState(isReportState: Boolean)
        //        fun addItemFilterInContainer(isSelected: Boolean, placeId: Int, name: String, currentItemPosition: Int, sizeFilters: Int)
        fun addItemsFilterInContainer(types: List<TypeFilterModel>)

        fun onClickFilterAction(): Observable<Int>
        fun selectItem(placeId: Int)
        fun removeFilters()
        fun updateLocaleResources()
        //        fun updateHours(lis: ArrayList<HourItem>, currentPosition1: Int)
        fun getCurrentTimePosition(): Int?

        fun onShowMyLocationAction(): Observable<Any>
        fun onSearchButtonAction(): Observable<Any>
        fun showSearchScreen(cityId: Int)
        fun isEnableProcessingLocation(isVisible: Boolean)
        fun isVisibleButtonLocation(isVisible: Boolean)
        fun isSearchState(isSearchState: Boolean)
        fun getCurrentPlaceItemMarkers(): List<MarkerItem>
        fun showGPSEnabledSettingDialog(isLoadDefaultCity:Boolean=true)
        fun openPlaceDetails(placeId: Int, lat: Double?, lon: Double?, placeType: Int)
        fun isActiveSearchAndFilters(isActive: Boolean)
        //        fun showGpsSettingDialog()
        fun onLoadCityWithNotLocationAction(): Observable<Any>

        fun hideMenuWithoutAnimation()
        //        fun showPlacePreviewDialog(markerItem: MarkerItem, placeTypeId: Int, typeCity: String, description: DescriptionPlaceDTO)
        fun closePreviewDialog()

        fun isEnabledTakeSnapshotButton(isEnabled: Boolean)
        fun updateTimeWheelPosition(position: Int)
        fun onDestroy()
        fun focusMyLocation(location:LatLng)


        fun isActiveSearchButton(isActive: Boolean)
        fun isActiveTimeWheel(isActive: Boolean)

        fun showLeftMenu()
        fun showRightMenu()
        fun hideMenu()
        fun isVisibleLeftMenu(): Boolean
        fun isAvailableLocation(): Boolean
        fun getPermissionLocation(permission: PermissionListener)
        fun getLocation(): Observable<GpsTracker.LocationResult>
        fun showPreview(placeNamePreview: String,
                        shortDescription: String,
                        typeString: String,
                        placeType: String,
                        preview: String,
                        currentIcon: Int,
                        rating: Double,
                        isGayFriendly: Boolean,
                        googleId: String
        )

        fun closePreview()
        fun onOpenPlaceDetailsActivity(): Observable<Any>
        fun isPermissionGranted(): Boolean

        fun showRequestError(e: Throwable)
        fun getCtx(): Context
        fun showChangedFilterServerDialog(): Observable<DialogModel.OnDialogResult>
        fun isSelectedCurrentTime(): Boolean
        fun onFilterException(): Observable<Throwable>
        fun isTimeWheelSelected(): Boolean


    }

    interface Presenter {
        fun changePlaceType(placeId: Int)
        fun initAction()
        fun onDestroy()

        fun onSaveState(savedInstanceState: Bundle)
        fun onRestoreState(savedInstanceState: Bundle?)
    }

    interface Remainder {
        fun onDestroy()
        fun getContext(): Context
        fun onLocationAction(): Observable<Any>
        fun onInternetAction(): Observable<Any>
        fun onCityChanged(): Observable<Any>
        fun onLanguageChanged(): Observable<Any>
        fun onSharingResetAction(): Observable<Any>
        fun onSearchAction(): Observable<SearchItemDTO>
        fun openScreenshotPreviewActivity()
        fun onTakeScreenAction(): Observable<Any>


    }

}