package com.lemust.repository.models.rest

import com.google.gson.annotations.SerializedName


    

data class City(
        @SerializedName("id") var id: Long,
        @SerializedName("name") var name: String?,
        @SerializedName("country") var country: String?,
        @SerializedName("timezone") var timezone: String?,
        @SerializedName("latitude") var latitude: Double?,
        @SerializedName("longitude") var longitude: Double?,
        @SerializedName("name_variants") var nameVariants: List<String>?,
        @SerializedName("current_city") var currentCity: String
)
