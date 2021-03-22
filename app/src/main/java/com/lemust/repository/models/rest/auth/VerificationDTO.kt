package com.lemust.repository.models.rest.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerificationDTO {
    @SerializedName("detail")
    @Expose
    var detail: String? = null

}