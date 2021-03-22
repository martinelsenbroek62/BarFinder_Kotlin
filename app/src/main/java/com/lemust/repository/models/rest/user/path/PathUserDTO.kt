package com.lemust.repository.models.rest.user.path

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PathUserDTO(@SerializedName("first_name")
                  @Expose
                  var firstName: String? = null,
                  @SerializedName("last_name")
                  @Expose
                  var lastName: String? = null,
                  @SerializedName("occupation")
                  @Expose
                  var occupation: String? = null,
                  @SerializedName("occupation_details")
                  @Expose
                  var occupationDetails: String? = null,
                  @SerializedName("birthdate")
                  @Expose
                  var birthdate: String? = null,

                  @SerializedName("city")
                  @Expose
                  var city: Int? = null,

                  @SerializedName("go_out_days")
                  @Expose
                  var goOutDays: List<Int>? = null,
                  @SerializedName("favorite_place_types")
                  @Expose
                  var favoritePlaceTypes: List<Int?>? = null,

                  @SerializedName("favorites")
                  @Expose
                  private val favorites: List<PathFavoriteDTO>? = null)
