package com.lemust.repository.models.rest.place

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlaceTypeDTO {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("slug")
    @Expose
    var slug: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("is_default")
    @Expose
    var isDefault: Boolean? = null

}
