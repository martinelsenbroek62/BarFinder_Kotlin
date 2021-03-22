package com.lemust.repository.preferences

import android.graphics.Bitmap
import com.lemust.repository.models.filters.Filter
import com.lemust.repository.models.rest.City
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.place.PlaceTypeDTO
import com.lemust.repository.models.rest.request.BaseFilter
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.ui.screens.filters.base.FiltersPresenter
import com.lemust.ui.screens.filters.base.adapter.type_place.PlaceTypeItem

    
interface PreferenceManager {
    fun saveAccessToken(token: String)
    fun saveAppStarted()
    fun isAppStarted(): Boolean
    fun getToken(): String
    fun saveUserId(id: Int)
    fun getUserId(): Int
    fun saveUserObj(obj: UserDTO)
    fun getUser(): UserDTO?
    fun clearToken()

    fun setLanguageChangedForProfile(id: Boolean)
    fun isLanguageChangedForProfile(): Boolean

    fun setLanguageChangedForMap(id: Boolean)
    fun isLanguageChangedForMap(): Boolean


    fun saveSearchedItem(item: SearchItemDTO)
    fun getSearchedItem(): SearchItemDTO?

    fun saveAvailableCities(cities: List<City>)
    fun getAvailableCities(): List<City>
    fun getCurrentCity(): City
    fun saveCurrentCity(current: City)
    fun isAvailableCities(): Boolean
    fun isAvailableCurrentCity(): Boolean
    fun updateCurrentCity()


    fun saveTypeModels(places: List<PlaceTypeDTO>)
    fun saveCurrentPlaces(places: List<Place>)
    fun saveCurrentSubFilters(places: FiltersPresenter.SubFilters)
    fun getCurrentSubFilters(): FiltersPresenter.SubFilters
    fun saveCurrentSubFiltersPager(subFiltersPuger: java.util.HashMap<Int, FiltersPresenter.SubFilters>)
    fun getCurrentSubFiltersPager(): java.util.HashMap<Int, FiltersPresenter.SubFilters>


    fun markForChangeCity()
    fun isChangedCity(): Boolean


    //Filters


    fun saveFilters(places: java.util.ArrayList<Filter>)
    fun saveCurrentFilterPlacesType(type: PlaceTypeItem)
    fun saveCurrentTypeId(typeId: Int)
    fun saveCurrentFilters(filters: HashMap<Int, ArrayList<BaseFilter>>)


    fun getFilters(): java.util.ArrayList<Filter>
    fun getCurrentFilterPlacesType(): PlaceTypeItem
    fun getCurrentTypeId(): Int
    fun getCurrentFilters(): HashMap<Int, ArrayList<BaseFilter>>


    fun getTypeModels(): List<PlaceTypeDTO>
    fun getCurrentPlaces(): List<Place>

//    fun saveGalleryPhotos(list: ArrayList<Bitmap>,placeId:Int)
//    fun getGalleryPhotos(): ArrayList<Bitmap>?
//    fun deleteGalleryPhotos()
//    fun getGalleryId():Int?



    fun  savePlaceIdForFilters(place:Int?)
    fun  getPlaceIdForFilters():Int


//    fun isRestore(): Boolean
//    fun isRestored(isRestore:Boolean)


}