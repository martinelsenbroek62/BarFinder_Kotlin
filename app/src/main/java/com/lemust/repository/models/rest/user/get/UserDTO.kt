package com.lemust.repository.models.rest.user.get

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserDTO:Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("has_password")
    @Expose
    var hasPassword: Boolean? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("occupation")
    @Expose
    var occupation: String? = null
    @SerializedName("occupation_details")
    @Expose
    var occupationDetails: String? = null
    @SerializedName("birthdate")
    @Expose
    var birthdate: String? = null
    @SerializedName("city")
    @Expose
    var city: UserCityDTO? = null
    @SerializedName("go_out_days")
    @Expose
    var goOutDays: List<Int>? = null
    @SerializedName("favorite_place_types")
    @Expose
    var favoritePlaceTypes: ArrayList<UserFavoritePlaceTypeDTO>? = null
    @SerializedName("favorites")
    @Expose
    var favorites: ArrayList<UserFavoriteDTO>? = null

    @SerializedName("occupation_choices")
    @Expose
    var occupationChoices: OccupationChoicesDTO? = null


    override fun toString(): String {
        return "UserDTO(id=$id, firstName=$firstName, lastName=$lastName, occupation=$occupation, occupationDetails=$occupationDetails, birthdate=$birthdate, city=$city, goOutDays=$goOutDays, favoritePlaceTypes=$favoritePlaceTypes, favorites=$favorites)"
    }


}
