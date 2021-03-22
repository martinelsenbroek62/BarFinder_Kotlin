package com.lemust.repository.models.rest.request

import com.google.gson.annotations.SerializedName

    

class MarkerFilterRequest {
    @SerializedName("city_id")
    var cityId: Int? = null

    @SerializedName("slug")
    var slug: String? = null

    @SerializedName("filter")
    var filter: ArrayList<BaseFilter>? = null

    constructor(cityId: Int, slug: String?, filter: ArrayList<BaseFilter>?) {
        this.cityId = cityId
        this.slug = slug
        this.filter = filter
    }
}
