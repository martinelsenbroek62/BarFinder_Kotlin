package com.lemust.repository.models.rest.request

import com.google.gson.annotations.SerializedName

    
class MultiChoiceFilter : BaseFilter {
    @SerializedName("options")
    var data = listOf<Int?>()

    constructor(id: Int, data: List<Int?>?) : super(id) {
        this.data = data!!

    }
}