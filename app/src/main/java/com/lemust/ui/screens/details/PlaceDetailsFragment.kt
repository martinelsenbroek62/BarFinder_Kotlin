package com.lemust.ui.screens.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.LatLng
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.BaseFragment
import com.lemust.ui.screens.main.MainActivity
import kotlinx.android.synthetic.main.fragment_place_details2.*
import kotlinx.android.synthetic.main.fragment_place_details2.view.*


class PlaceDetailsFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_place_details2; }

    private var location: LatLng? = null
    private var placeTypeId: Int? = null
    private var placeId: Int? = null
    private var placeCityType: String? = null


    var view: PlaceDetailsContract.View? = null
    var presenter: PlaceDetailsContract.Presenter? = null
    var remainder: PlaceDetailsContract.Remainder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            location = it.getParcelable<LatLng>(ARG_PARAM1)
            placeTypeId = it.getInt(ARG_PARAM2)
            placeId = it.getInt(ARG_PARAM3)
            placeCityType = it.getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var root = inflater.inflate(R.layout.fragment_place_details2, container, false)
        remainder = PlaceDetailsRemainder(activity as BaseActivity)
        view = PlaceDetailsView(this, root)
        presenter = PlaceDetailsPresenter(view as PlaceDetailsView, (activity as BaseActivity).activityEventBus, location, placeTypeId, placeId, context, remainder as PlaceDetailsRemainder)
        // (activity as BaseActivity).activityEventBus.post(MainActivity.ShowPreviewDialog())

        root.mainMap.onCreate(savedInstanceState)
        root.mainMap.getMapAsync(view as PlaceDetailsView)

        return root
    }


    companion object {
        private const val ARG_PARAM1 = "place"
        private const val ARG_PARAM2 = "placeType"
        private const val ARG_PARAM3 = "placeId"
        private const val ARG_PARAM4 = "placeCityType"


        fun newInstance(place: LatLng?, placeType: Int, placeId: Int) =
                PlaceDetailsFragment().apply {
                    arguments = Bundle().apply {
                        if (place != null)
                            putParcelable(ARG_PARAM1, place)
                        putInt(ARG_PARAM2, placeType)
                        putInt(ARG_PARAM3, placeId)
                        putString(ARG_PARAM4, placeCityType)
                    }
                }


    }


    override fun onDestroyView() {
        mainMap.onDestroy()
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (view != null)
            view!!.onDestroy()

        if (view != null)
            presenter!!.onDestroy()
        System.gc()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()

    }
}
