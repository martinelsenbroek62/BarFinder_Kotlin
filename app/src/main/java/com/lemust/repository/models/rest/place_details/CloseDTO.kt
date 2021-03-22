package com.lemust.repository.models.rest.place_details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CloseDTO {
    @SerializedName("day")
    @Expose
    var day: Int?=null

    @SerializedName("time")
    @Expose
    var time: String?=null
}