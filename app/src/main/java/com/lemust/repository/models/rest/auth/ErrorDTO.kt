package com.lemust.repository.models.rest.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorDTO {

    @SerializedName("non_field_errors")
    @Expose
    var nonFieldErrors: List<String>? = null

    @SerializedName("email")
    @Expose
    var email: List<String>? = null
//
    @SerializedName("old_password")
    @Expose
    var oldPassword: List<String>? = null



}
