package com.lemust.ui.screens.details

import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.lemust.R
import com.lemust.ui.base.BaseActivity
import com.lemust.utils.AppDataHolder


class PlaceDetailsActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details2)


        var location: LatLng? = null
        if (intent.hasExtra(PLACE_LAT_LON_KEY)) {
            location = intent.getParcelableExtra(PLACE_LAT_LON_KEY) as LatLng
        }

        var placeTypeId = intent.getIntExtra(PLACE_TYPE_ID_KEY, -1)
        var placeId = intent.getIntExtra(PLACE_ID_KEY, -1)
        putRootFragment(PlaceDetailsFragment.newInstance(location, placeTypeId, placeId))

    }

    companion object {
        var PLACE_TYPE_ID_KEY = "place_type_key"
        var PLACE_LAT_LON_KEY = "place_lat_lon_key"
        var PLACE_ID_KEY = "place_id_key"
        var PLACE_CITY_TYPE = "place_city_type"

    }

    override fun getDefaultFragmentsContainer(): Int? {
        return R.id.container
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDataHolder.clean()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}
