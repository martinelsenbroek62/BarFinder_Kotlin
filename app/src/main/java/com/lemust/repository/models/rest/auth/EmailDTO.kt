package com.lemust.repository.models.rest.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmailDTO(@SerializedName("email")
               @Expose
               var email: String? = null)