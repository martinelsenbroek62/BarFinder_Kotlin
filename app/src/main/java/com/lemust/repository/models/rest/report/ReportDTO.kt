package com.lemust.repository.models.rest.report

import com.google.gson.annotations.SerializedName


    

data class ReportDTO(
        @SerializedName("email") var email: String,
        @SerializedName("message") var message: String?,
        @SerializedName("app_version") var app_version: String?
)
