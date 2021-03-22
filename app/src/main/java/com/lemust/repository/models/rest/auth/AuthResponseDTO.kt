package com.lemust.repository.models.rest.auth.registaration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthResponseDTO {

    @SerializedName("key")
    @Expose
    var key: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    override fun toString(): String {
        return "AuthResponseDTO(key=$key, userId=$userId)"
    }


}
