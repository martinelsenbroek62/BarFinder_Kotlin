package com.lemust.repository.models.rest

import com.google.gson.annotations.SerializedName
import java.io.Serializable


    

class Place(
        @SerializedName("id") var id: Int,
        @SerializedName("name") var name: String,
        @SerializedName("preview") var preview: String,
        @SerializedName("place_images_count") var placeImagesCount: Int?,
        @SerializedName("features") var features: Features?,
        // @SerializedName("short_description") var shortDescription: ShortDescription?,
        @SerializedName("google_place_id") var googlePlaceId: String?,
        @SerializedName("place_types") var placeTypes: ArrayList<PlaceType>?,
        @SerializedName("google_rating") var googleRating: Double?,
//        @SerializedName("google_price_level") var googlePriceLevel: Any?,
        @SerializedName("location") var location: Location?,
        @SerializedName("is_gay_friendly") var isGayFriendly: Boolean?,
        @SerializedName("populartimes") var popularTimes: ArrayList<PopularTimes>?,
        @SerializedName("short_description") var shortDescription: String?


):Serializable