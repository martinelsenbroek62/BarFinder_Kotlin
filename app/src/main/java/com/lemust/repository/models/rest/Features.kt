package com.lemust.repository.models.rest

import com.google.gson.annotations.SerializedName
import java.io.Serializable

    
data class Features(
        @SerializedName("bar") var bar: List<String>?,
        @SerializedName("night_club") var nightClub: List<String>?,
        @SerializedName("restaurant") var restaurant: List<String>?,
        @SerializedName("karaoke") var karaoke: List<String>?

):Serializable