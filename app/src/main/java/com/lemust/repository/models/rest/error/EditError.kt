package com.lemust.repository.models.rest.error

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EditError {

    @SerializedName("first_name")
    @Expose
    var firstName: List<String>? = null
    @SerializedName("last_name")
    @Expose
    var secondName: List<String>? = null

}
