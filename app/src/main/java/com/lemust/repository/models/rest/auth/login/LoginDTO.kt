package com.lemust.repository.models.rest.auth.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginDTO(@SerializedName("email")
               @Expose
               var email: String? = null,
               @SerializedName("password")
               @Expose
               var password: String? = null) {


}
