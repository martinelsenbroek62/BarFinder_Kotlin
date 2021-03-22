package com.lemust.repository.models.rest.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportFrequentationDTO(@SerializedName("place")
                             @Expose
                             private val place: String? = null,
                             @SerializedName("value")
                             @Expose
                             private val value: Int? = null) {
    @SerializedName("id")
    @Expose
    private val id: String? = null

    override fun toString(): String {
        return "ReportFrequentationDTO(place=$place, value=$value, id=$id)"
    }


}
