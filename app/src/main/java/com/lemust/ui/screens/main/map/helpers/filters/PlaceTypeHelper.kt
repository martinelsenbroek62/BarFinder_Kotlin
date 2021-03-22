package com.lemust.ui.screens.main.map.helpers.filters

import android.content.Context
import com.lemust.repository.models.rest.place.PlaceTypeDTO
import com.lemust.utils.Tools

class PlaceTypeHelper {
    private var temporaryPlaceTypeId = 0

    var currentPlaceTypeId = 0
    var currentPlaceTypeIdInFilterScreen = 0
    var placeTypeFilters = ArrayList<TypeFilterModel>()
    var placesTypeDTO = ArrayList<PlaceTypeDTO>()
    var isReady = false


    fun init(t: List<PlaceTypeDTO>, context: Context) {
        generateTypeFilterModelsForUi(t, context)
        isReady = true
    }

    fun clear(){
        isReady=false
        currentPlaceTypeId=0
        placeTypeFilters.clear()
    }

    private fun generateTypeFilterModelsForUi(t: List<PlaceTypeDTO>, context: Context) {
        var isDefaultEnabled = false

        t.forEachIndexed { index, placeTypeDTO ->
            if (placeTypeDTO.isDefault!!) {
                isDefaultEnabled = true
                currentPlaceTypeId = placeTypeDTO.id!!
            }

            placeTypeFilters.add(TypeFilterModel(placeTypeDTO.isDefault!!, placeTypeDTO.id!!, Tools.getPlacePluralTitle(placeTypeDTO.name!!,
                    context), index, t.size))

        }
        if (!isDefaultEnabled) {
            if (placeTypeFilters.isNotEmpty()) {
                currentPlaceTypeId = t[0].id!!
                placeTypeFilters.first().isCurrent = true

            }
        }

    }

    fun changeCurrentPlaceType(typePlace: Int) {
        this.currentPlaceTypeId = typePlace
    }

    fun useTemporaryPlaceType(typePlace: Int) {
        this.temporaryPlaceTypeId = this.currentPlaceTypeId
        this.currentPlaceTypeId = typePlace

    }

    fun resetTemporaryPlaceType() {
        this.currentPlaceTypeId = this.temporaryPlaceTypeId
    }
}
