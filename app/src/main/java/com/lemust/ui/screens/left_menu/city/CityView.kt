package com.lemust.ui.screens.left_menu.city

import android.app.Activity.RESULT_OK
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.R
import com.lemust.repository.models.rest.City
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseView
import com.lemust.ui.screens.left_menu.city.adapter.CityAdapter
import com.lemust.ui.screens.left_menu.city.adapter.item.BaseItem
import com.lemust.ui.screens.left_menu.city.adapter.item.CityItem
import com.lemust.utils.GpsTracker
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_city.view.*


class CityView(var activity: BaseActivity, var root: View) : CityContract.View, BaseView(activity) {


    override fun isPermissionGranted(): Boolean {
        return activity.isPermissionGranted()
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

    override fun changeCity(city: City) {
        activity.setResult(RESULT_OK, activity.intent);
        activity.finish()

    }


    private var recyclerCity = root.findViewById<RecyclerView>(R.id.rv_city)
    private var cityItems = ArrayList<BaseItem>()
    private var cityAdapter: CityAdapter? = null

    init {
        // initAction()
        initRecycler()
        root.iv_close.setOnClickListener {
            activity.finish()
        }
        //   OverScrollDecoratorHelper.setUpOverScroll(root.city_scroll)
        //  root.city_scroll.fullScroll(ScrollView.FOCUS_UP);
        //root.city_scroll.smoothScrollTo(0, 0);


    }

    override fun onApplyAction(): Observable<Any> {
        return RxView.clicks(root.tv_apply)
    }


    override fun onClickAction(): Observable<City> {
        return cityAdapter!!.clickListener
    }


    override fun setCities(cities: ArrayList<BaseItem>) {
        cityItems.addAll(cities)
        cityAdapter!!.notifyDataSetChanged()
        root.city_scroll.post({ root.city_scroll.init() })

    }

    private fun initRecycler() {
        var managerSubFilters = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerCity.layoutManager = managerSubFilters
        cityAdapter = CityAdapter(cityItems)
        recyclerCity.adapter = cityAdapter
        recyclerCity.isNestedScrollingEnabled = false;

    }

    override fun dismiss() {
        //fragment.dismiss()
        activity.finish()
    }

    override fun setVisibleLoader(isVisible: Boolean) {
        if (isVisible) {
            root.pb_language_map.visibility = View.VISIBLE
        } else {
            root.pb_language_map.visibility = View.GONE

        }
    }

    override fun setVisibleContent(isVisible: Boolean) {
        if (isVisible) {
            root.ll_cities_content.visibility = View.VISIBLE
        } else {
            root.ll_cities_content.visibility = View.GONE

        }

    }
}