package com.lemust.repository.models.rest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

    
 class Location:Serializable{
    @SerializedName("lat") var lat: Double
    @SerializedName("lng") var lng: Double

    constructor(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng
    }
}