package com.lemust.repository.models.rest.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlaceTypeDTO:Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    private val name: String? = null
    @SerializedName("slug")
    @Expose
    private val slug: String? = null
}
