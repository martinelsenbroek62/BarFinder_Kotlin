package com.lemust.repository.models.rest.place_details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlaceImage :Serializable{

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("created")
    @Expose
    var created: String? = null
    @SerializedName("modified")
    @Expose
    var modified: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("order")
    @Expose
    var order: Int? = null
    @SerializedName("place")
    @Expose
    var place: Int? = null

}
