package com.lemust.repository.models.rest.user.reset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lemust.repository.models.rest.user.get.UserCityDTO

class CityResetDTO(  @SerializedName("city")
                     @Expose
                     var city: UserCityDTO? = null)
