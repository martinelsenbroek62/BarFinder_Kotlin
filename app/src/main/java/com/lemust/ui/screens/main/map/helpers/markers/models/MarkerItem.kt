package com.lemust.ui.screens.main.map.helpers.markers.models

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.lemust.repository.models.rest.Place


class MarkerItem : ClusterItem {
    var iconBitmap: Bitmap
    var place: Place
    var rating: Double

    constructor(place: Place, icon: Bitmap, rating: Double) {
        this.place = place
        this.iconBitmap = icon
        this.rating = rating
    }


    override fun getPosition(): LatLng {
        return LatLng(place.location!!.lat, place.location!!.lng)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MarkerItem

        if (place.id != other.place.id) return false

        return true
    }

    override fun hashCode(): Int {
        return place.id
    }


}