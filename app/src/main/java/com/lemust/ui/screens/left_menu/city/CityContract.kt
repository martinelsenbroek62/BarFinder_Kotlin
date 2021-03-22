package com.lemust.ui.screens.left_menu.city

import android.view.View
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.repository.models.rest.City
import com.lemust.ui.base.BaseViewContract
import com.lemust.ui.screens.left_menu.city.adapter.item.BaseItem
import com.lemust.ui.screens.left_menu.city.adapter.item.CityItem
import com.lemust.utils.GpsTracker
import io.reactivex.Observable


interface CityContract {
    interface View : BaseViewContract {

        fun setCities(hours: ArrayList<BaseItem>)
        fun onClickAction(): Observable<City>
        fun onApplyAction(): Observable<Any>
        fun dismiss()

        fun setVisibleLoader(isVisible: Boolean)
        fun setVisibleContent(isVisible: Boolean)


        fun isAvailableLocation(): Boolean
        fun getPermissionLocation(permission: PermissionListener)
        fun getLocation(): Observable<GpsTracker.LocationResult>
        fun isPermissionGranted(): Boolean
        fun changeCity(city: City)
    }

    interface Presenter {
//        fun changePlaceType(placeId: Int)
//        fun initAction()
    }


}