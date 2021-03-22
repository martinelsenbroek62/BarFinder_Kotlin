package com.lemust.repository.models.rest.place_details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PeriodsDTO:Serializable {

    @SerializedName("open")
    @Expose
    var open: OpenDTO?=null

    @SerializedName("close")
    @Expose
    var close: CloseDTO?=null
}