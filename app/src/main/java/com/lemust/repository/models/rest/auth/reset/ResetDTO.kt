package com.lemust.repository.models.rest.auth.reset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResetDTO(@SerializedName("detail")
               @Expose
               var detail: String? = null)
