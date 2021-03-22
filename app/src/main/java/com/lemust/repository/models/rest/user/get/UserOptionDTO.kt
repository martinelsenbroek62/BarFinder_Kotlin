package com.lemust.repository.models.rest.user.get

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserOptionDTO:Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("option")
    @Expose
    var option: String? = null
    @SerializedName("is_selected")
    @Expose
     var isSelected: Boolean? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserOptionDTO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }


}
