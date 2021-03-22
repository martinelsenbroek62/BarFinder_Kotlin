package com.lemust.repository.models.rest.report_details

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Option(@SerializedName("option")
             @Expose
             var option: String? = null) {

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("is_selected")
    @Expose
    var isSelected: Boolean? = null

}
