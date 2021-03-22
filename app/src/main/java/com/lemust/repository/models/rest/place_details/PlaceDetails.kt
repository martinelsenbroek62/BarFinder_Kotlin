package com.lemust.repository.models.rest.place_details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lemust.repository.models.rest.Location
import com.lemust.repository.models.rest.PlaceType
import com.lemust.repository.models.rest.PopularTimes
import java.io.Serializable

class PlaceDetails : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("features")
    var features: List<String>?

    @SerializedName("short_description")
    @Expose
    var shortDescription: String? = null
    @SerializedName("about_place")
    @Expose
    var aboutPlace: List<String>? = null
    @SerializedName("google_place_id")
    @Expose
    var googlePlaceId: String? = null
    @SerializedName("is_gay_friendly")
    @Expose
    var isGayFriendly: Boolean? = null
    @SerializedName("place_types")
    @Expose
    var placeTypes: List<PlaceType>? = null
    @SerializedName("google_rating")
    @Expose
    var googleRating: Double? = null
    @SerializedName("google_price_level")
    @Expose
    var googlePriceLevel: Int? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("website")
    @Expose
    var website: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("long_description")
    @Expose
    var longDescription: String? = null

    @SerializedName("place_fields")
    @Expose
    public var placeFields: PlaceFields? = null
    @SerializedName("open_hours")
    @Expose
    var openHours: OpenHours? = null
    @SerializedName("populartimes")
    var popularTimes: ArrayList<PopularTimes>? = null
    @SerializedName("place_images_count")
    @Expose
    var placeImagesCount: Int? = null
    @SerializedName("place_images")


    @Expose
    var placeImages: List<PlaceImage>? = null

    constructor(id: Int?, name: String?, features: List<String>?, shortDescription: String?, aboutPlace: List<String>?, googlePlaceId: String?, isGayFriendly: Boolean?, placeTypes: List<PlaceType>?, googleRating: Double?, googlePriceLevel: Int?, location: Location?, address: String?, website: String?, phone: String?, longDescription: String?, placeFields: PlaceFields?, openHours: OpenHours?, popularTimes: ArrayList<PopularTimes>?, placeImagesCount: Int?, placeImages: List<PlaceImage>?) {
        this.id = id
        this.name = name
        this.features = features
        this.shortDescription = shortDescription
        this.aboutPlace = aboutPlace
        this.googlePlaceId = googlePlaceId
        this.isGayFriendly = isGayFriendly
        this.placeTypes = placeTypes
        this.googleRating = googleRating
        this.googlePriceLevel = googlePriceLevel
        this.location = location
        this.address = address
        this.website = website
        this.phone = phone
        this.longDescription = longDescription
        this.placeFields = placeFields
        this.openHours = openHours
        this.popularTimes = popularTimes
        this.placeImagesCount = placeImagesCount
        this.placeImages = placeImages
    }
}
