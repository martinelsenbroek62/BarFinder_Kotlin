package com.lemust.repository.models.filters

import com.google.gson.annotations.SerializedName


    

data class Filter(
        @SerializedName("id") var id: Long,
        @SerializedName("name") var name: String?,
        @SerializedName("slug") var slug: String?,
        @SerializedName("price") var price: List<Int>?,
        @SerializedName("filters") var filters: List<FilterData>?
)