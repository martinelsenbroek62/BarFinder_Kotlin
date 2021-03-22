package com.lemust.repository.models.rest.place_details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OpenHours:Serializable{
    @SerializedName("open_now")
    @Expose
    var openNow: Boolean?=null


    @SerializedName("periods")
    @Expose
    var periods: List<PeriodsDTO>? = null


    @SerializedName("weekday_text")
    @Expose
    var weekdayText: List<String>? = null


}
