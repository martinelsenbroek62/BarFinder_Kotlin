package com.lemust.repository.models.filters

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OptionDTO : Serializable {
    @SerializedName("id")
    @Expose
    val id: Int? = null
    @SerializedName("option")
    @Expose
    val option: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OptionDTO

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id ?: 0
    }


}
