package com.lemust.repository.models.rest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

    
 class PopularTimes:Serializable{
    @SerializedName("data") var data: List<Int>
    @SerializedName("name") var name: String

    constructor(data: List<Int>, name: String) {
        this.data = data
        this.name = name
    }
}