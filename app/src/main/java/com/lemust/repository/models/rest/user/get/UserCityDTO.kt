package com.lemust.repository.models.rest.user.get

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserCityDTO {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("country")
    @Expose
    private val country: String? = null

}
