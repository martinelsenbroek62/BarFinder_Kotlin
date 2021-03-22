package com.lemust.repository.models.rest.user.reset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImageResetDTO(
        @SerializedName("image")
        @Expose
        var occupation: String? = null)


