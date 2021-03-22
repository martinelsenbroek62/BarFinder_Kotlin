package com.lemust.repository.models.rest.auth.registaration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationDTO(@SerializedName("email")
                      @Expose
                      var email: String? = null,
                      @SerializedName("password1")
                      @Expose
                      var password1: String? = null,
                      @SerializedName("password2")
                      @Expose
                      var password2: String? = null)