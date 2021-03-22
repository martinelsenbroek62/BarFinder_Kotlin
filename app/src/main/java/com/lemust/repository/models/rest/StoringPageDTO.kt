package com.lemust.repository.models.rest

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StoringPageDTO {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("short_description")
    @Expose
    var shortDescription: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null


}
