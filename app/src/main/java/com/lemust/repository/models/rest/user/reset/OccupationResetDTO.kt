package com.lemust.repository.models.rest.user.reset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OccupationResetDTO(
        @SerializedName("occupation")
        @Expose
        var occupation: String? = null,

        @SerializedName("occupation_details")
        @Expose
        var occupationDetails: String? = null)

