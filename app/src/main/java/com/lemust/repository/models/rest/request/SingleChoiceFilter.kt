package com.lemust.repository.models.rest.request

import com.google.gson.annotations.SerializedName

    
class SingleChoiceFilter : BaseFilter {
    @SerializedName("options")
    var data: Int? = null

    constructor(id: Int,  data: Int?) : super(id) {
        this.data = data
    }
}