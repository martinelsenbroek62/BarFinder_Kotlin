package com.lemust.ui.screens.main.map.helpers.markers.models

import com.lemust.repository.models.rest.Place


class MarkerItemConfig {
    var listPlaces: List<Place>
    var currentPlaceType: Int
    var hour: Int
    var todaysAccount: Int
    var tomorrowsAccount: Int
    var dayPosition:Int

    constructor(listPlaces: List<Place>, currentPlaceType: Int, hour: Int, todaysAccount: Int, tomorrowsAccount: Int,dayPosition:Int) {
        this.listPlaces = listPlaces
        this.currentPlaceType = currentPlaceType
        this.hour = hour
        this.todaysAccount = todaysAccount
        this.tomorrowsAccount = tomorrowsAccount
        this.dayPosition=dayPosition
    }
}