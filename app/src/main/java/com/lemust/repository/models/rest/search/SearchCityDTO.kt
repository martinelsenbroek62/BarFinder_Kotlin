package com.lemust.repository.models.rest.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchCityDTO {
    @SerializedName("id")
    @Expose
     val id: Int? = null
    @SerializedName("name")
    @Expose
     val name: String? = null
    @SerializedName("country")
    @Expose
     val country: String? = null
}
