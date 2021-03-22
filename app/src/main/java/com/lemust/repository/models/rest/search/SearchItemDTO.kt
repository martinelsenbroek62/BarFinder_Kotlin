package com.lemust.repository.models.rest.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lemust.repository.models.rest.place_details.PlaceDetails
import java.io.Serializable

class SearchItemDTO:Serializable {
    @SerializedName("id")
    @Expose
    private val id: Int? = null
    @SerializedName("place")
    @Expose
    val place: PlaceDetails? = null
    @SerializedName("place_type")
    @Expose
    val placeType: PlaceTypeDTO? = null
}
