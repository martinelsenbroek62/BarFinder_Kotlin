package com.lemust.ui.screens.main.map


import android.arch.lifecycle.LifecycleObserver
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.City
import com.lemust.repository.models.rest.request.MarkerFilterRequest
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.screens.main.MainActivity


class MainFragment : BaseFragment() {
    var view: MainContract.View? = null
    var presenter: MainContract.Presenter? = null
    var mMapView = MapView(LeMustApp!!.instance)
    var isAvailableLocation = false
    var remainder: MainContract.Remainder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            isAvailableLocation = arguments!!.getBoolean(isAvailableLocationKey)

        }
    }

    companion object {
        val isAvailableLocationKey = "isAvailbleLocationKey"
        var isScreenReady = false
        fun newInstance(booleanArrayExtra: Boolean): MainFragment {
            Log.d("onCreateView_test", "newInstance: ")

            val fragment = MainFragment()
            val args = Bundle()
            args.putBoolean(isAvailableLocationKey, booleanArrayExtra)
            fragment.arguments = args
            return fragment
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var root = inflater.inflate(R.layout.fragment_main, container, false)

        Log.d("activity_test", "fragment start")

        isScreenReady = true
        mMapView = root.findViewById(R.id.mainMap)
        mMapView!!.onCreate(savedInstanceState)
        mMapView!!.onResume();
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        view = MainView(this.activity as MainActivity, root)
        remainder = MainRemainder(activity as MainActivity)
        presenter = MainPresenter(view as MainView, (activity as BaseActivity).activityEventBus, remainder)
        mMapView!!.getMapAsync(this.view as MainView)
        lifecycle.addObserver(presenter as LifecycleObserver);

        return root
    }

    override fun getLayoutId(): Int = R.layout.fragment_main


    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()

        try {
            lifecycle.addObserver(presenter as LifecycleObserver);
            mMapView.onPause()
        } catch (e: Exception) {
            System.out.print(e.localizedMessage)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        isScreenReady = false
        try {
            (activity as BaseActivity).activityEventBus.unregister(presenter)
            lifecycle.removeObserver(presenter as LifecycleObserver);
        } catch (e: Exception) {
            System.out.print(e.localizedMessage)
        }


        mMapView?.let {
            it.onDestroy()

        }


        view?.let {
            it.onDestroy()
        }



        presenter?.onDestroy()
        presenter = null
        view = null

    }

    override fun onLowMemory() {
        super.onLowMemory()
        isScreenReady = false
        mMapView.onLowMemory()
    }


    class ApplyFilters(var placeTypeId: Int, var request: MarkerFilterRequest)


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter?.let {
            it.onSaveState(outState)

        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter?.let {
            it.onRestoreState(savedInstanceState)
        }

    }
}
