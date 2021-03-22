package com.lemust.repository.models.rest.auth.registaration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

 class AuthFacebookDTO (
    @SerializedName("access_token")
    @Expose
    var accessToken: String? = null,
    @SerializedName("code")
    @Expose
    var code: String? = null
)



