package com.lemust.repository.models.rest

import com.google.gson.annotations.SerializedName
import java.io.Serializable


    

 class PlaceType:Serializable{
    @SerializedName("id") var id: Long
    @SerializedName("slug") var slug: String?
    @SerializedName("name") var name: String?
    @SerializedName("is_default") var isDefault: Boolean?

    constructor(id: Long, slug: String?, name: String?, isDefault: Boolean?) {
        this.id = id
        this.slug = slug
        this.name = name
        this.isDefault = isDefault
    }
}