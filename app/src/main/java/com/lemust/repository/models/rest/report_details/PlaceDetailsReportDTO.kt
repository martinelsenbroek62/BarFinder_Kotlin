package com.lemust.repository.models.rest.report_details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlaceDetailsReportDTO {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("google_price_level")
    @Expose
    var googlePriceLevel: Int? = null
    @SerializedName("place_filter_field")
    @Expose
    var placeFilterField: List<PlaceFilterField>? = null

}
