package com.lemust.repository.models.rest.user.path

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PathFavoriteDTO(@SerializedName("id")
                      @Expose
                      private val id: Int? = null,
                      @SerializedName("options")
                      @Expose
                      private val options: List<Int>? = null)
