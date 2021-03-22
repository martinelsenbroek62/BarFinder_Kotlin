package com.lemust.ui.screens.details

import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.details.adapters.PlaceDetailsSubCategoryItem
import com.lemust.utils.GpsTracker
import com.steelkiwi.simplehistogram.TimeData
import io.reactivex.Observable
import java.util.*


class PlaceDetailsContract {
    interface View : BaseViewContract {
        fun initViewPager(statics: ArrayList<ArrayList<TimeData>>)
        fun initMarker(iconId: Int, location: LatLng)
        fun initPlaceDetails(title: String, typePlace: String, description: String, rating: Float)
        fun mapIsReady(): Observable<Any>
        fun currentDayListener(): Observable<Int>
        fun onClickGallery(): Observable<Any>
        fun onGraphicContainer(): Observable<Any>
        fun isVisibleStatisticsView(isVisible: Boolean)
        fun isVisibleAboutPlace(isVisible: Boolean)
        //   fun isVisibleReportUserStatistic(isVisible: Boolean)
        fun isVisibleFrequentationContainer(isVisible: Boolean)

        fun initPhotosPreview(items: ArrayList<Bitmap>)
        fun setStaticsDay(day: String)
        fun setAddress(address: String)
        fun setTime(times: String, currentTime: String)
        fun setPhone(phone: String)
        fun setAboutPlace(description: String)
        fun openGallery(images: ArrayList<Bitmap>, placeTitle: String)
        fun isVisibleAddress(isVisible: Boolean)
        fun isVisibleTimeWork(isVisible: Boolean)
        fun isClickableButtonShare(isVisible: Boolean)
        fun isVisiblePhone(isVisible: Boolean)
        fun isAboutPlaceTextView(isVisible: Boolean)
        fun isVisibleButtonRepport(isVisible: Boolean)
        fun isShowGraphic()
        fun showPhoto(list: ArrayList<Bitmap>, position: Int, title: String)
        fun onPhotoPreviewClick(): Observable<Int>
        fun onCallAction(): Observable<Any>
        fun onWebAction(): Observable<Any>
        fun onDirectionsAction(): Observable<Any>
        fun call(number: String)
        fun openWeb(number: String)
        fun openRout(to: LatLng, with: LatLng)
        fun setSubCategory(to: ArrayList<PlaceDetailsSubCategoryItem>)
        fun getPermissionLocation(permission: PermissionListener)
        fun getLocation(): Observable<GpsTracker.LocationResult>

        fun isVisibleMainContent(isVisible: Boolean)

        fun setUserReportStatisticText(text: String)
        fun setFeatures(text: String)

        fun isVisiblePhoneButton(isVisible: Boolean)
        fun isVisibleDirectionButton(isVisible: Boolean)
        fun isVisibleWebButton(isVisible: Boolean)
        fun isVisibleRatingBar(isVisible: Boolean)
        fun isVisibleLoader(isVisible: Boolean)
        fun isVisibleGayFrendly(isVisible: Boolean)


        fun isVisiblePriceLevel(isVisible: Boolean)
        fun setPriceLevel(level: Int)
        fun onShareAction(): Observable<Any>
        fun showBottomSheetDialog()
        fun hideBottomSheetDialog()

        fun shareLink(placeId: Int, cityId: Int, placeTypeId: String)
        fun shareLinkAndMap(placeId: Int, cityId: Int, placeTypeId: String)
//        fun onSharePlaceDetailsAction():Observable<Any>
//        fun onSharePlaceDetailsAndMapAction():Observable<Any>

        fun onReportStatisticAction(): Observable<Any>
        fun onPlaceDetailsReportAction(): Observable<Any>

        fun takeSnapshot(): Bitmap

        fun onDestroy()
        fun isShowProgressLoader(isShow: Boolean)
        fun onBackPressed(): Observable<Any>
        fun showGPSEnabledSettingDialog()


        fun isVisibleButtonShare(isVisibleBoolean: Boolean)
    }

    interface Presenter {
        fun onDestroy()
        fun onBackPressed()
        fun onPause();
        fun onResume();
    }

    interface Remainder {
        fun finish()
        fun getContext(): Context
        fun openReportStatistic(placeId: Int)
        fun openPlaceDetailsReport(placeId: Int, placeTypeId: String)
        fun onShareAction(): Observable<Any>

    }
}
