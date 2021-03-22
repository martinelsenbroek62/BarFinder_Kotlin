package com.lemust.repository.models.rest.request

import com.google.gson.annotations.SerializedName

    
open class BaseFilter {
    @SerializedName("id")
    var id: Int = 0

    constructor(id: Int) {
        this.id = id
    }
}