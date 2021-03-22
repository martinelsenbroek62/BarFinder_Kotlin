package com.lemust.repository.models.rest.description

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lemust.repository.models.rest.Features
import com.lemust.repository.models.rest.PlaceType
import com.lemust.repository.models.rest.place_details.PlaceFields


class DescriptionPlaceDTO {

    @SerializedName("id")
    @Expose
    internal var id: Int? = null

    @SerializedName("short_description")
    var shortDescription: PlaceFields? = null

    @SerializedName("features")
    @Expose
    var features: Features? = null

    @SerializedName("name")
    @Expose
     val name: String? = null
    @SerializedName("preview") var preview: String?=null

    @SerializedName("place_types")
    @Expose
     val placeTypes: List<PlaceType>? = null
    @SerializedName("google_place_id")
    @Expose
    val googlePlaceId: String? = null
    @SerializedName("google_rating")
    @Expose
     val googleRating: Double? = null
    @SerializedName("is_gay_friendly")
    @Expose
     val isGayFriendly: Boolean? = null

}
