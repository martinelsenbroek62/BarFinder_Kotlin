package com.lemust.repository.preferences

import android.graphics.Bitmap
import com.google.gson.GsonBuilder
import com.lemust.LeMustApp
import com.lemust.repository.models.filters.Filter
import com.lemust.repository.models.rest.City
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.place.PlaceTypeDTO
import com.lemust.repository.models.rest.request.BaseFilter
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.ui.screens.filters.base.FiltersPresenter
import com.lemust.ui.screens.filters.base.adapter.type_place.PlaceTypeItem
import com.lemust.utils.AppDataHolder
import io.paperdb.Paper


    
class PreferenceManagerImpl : PreferenceManager {
    override fun getPlaceIdForFilters(): Int {
        var flterId = -1

        if (Paper.book().contains(FILTER_LOADED))
            flterId = Paper.book().read(FILTER_LOADED)

        return flterId

    }

    override fun savePlaceIdForFilters(place: Int?) {

        if (place != null)
            Paper.book().write(FILTER_LOADED, place)
        else
            Paper.book().write(FILTER_LOADED, -1)


    }
//    override fun getGalleryId(): Int? {
//        return Paper.book().read(GALLERY_ID_KEY)
//    }
//
//    override fun saveGalleryPhotos(list: ArrayList<Bitmap>, id: Int) {
//        Paper.book().write(GALLERY_KEY, list)
//        Paper.book().write(GALLERY_ID_KEY, id)
//    }
//
//    override fun getGalleryPhotos(): ArrayList<Bitmap>? {
//        return Paper.book().read(GALLERY_KEY)
//    }
//
//    override fun deleteGalleryPhotos() {
//        Paper.book().delete(GALLERY_KEY)
//    }


    private val GALLERY_KEY = "gallery_key"
    private val GALLERY_ID_KEY = "gallery_id"
    private val FILTER_LOADED = "filter_loaded"

    private val FIRST_START_APP_KEY = "firs_start_app_key"
    private val LANGUAGE_CHANGED_APP_KEY = "language_changed"
    private val ACCESS_TOKEN_APP_KEY = "access_token_app_key"
    private val CURRENT_USER_ID_APP_KEY = "user_id_app_key"
    private val CURRENT_USER_OBJ = "usser_obj"


    private val AVAILABLE_CITIES_LIST = "available_cities_list"
    private val CURRENT_AVAILABLE_CITY = "current_city"


    private val SAVED_PLACES = "saved_places"
    private val SAVED_CURRENT_PLACE_ID = "current_place_id"
    private val SAVED_CURRENT_FILTERS = "current_filters"


    private val PLASE_TYPE_MODELS = "place_type_models"

//Filters

    private val SAVE_FILTER_MODELS = "save_filter_model"
    private val SAVE_SUB_FILTER_MODEL = "save_sub_filters_model"
    private val SAVE_SUB_FILTER_PUGER = "save_sub_filter_puger"
    private val SAVE_FILTER_CURRENT_PLACE_TYPE = "save_filter_current_place_type"

    private val MARK_CHANGE_CITY = "mark_change_city"
    private val IS_LANGUAGE_CHANGED_FOR_MAP = "language_chenged_map"

    private val SAVED_SEARCH_ITEM = "saved_search_item"


    override fun saveSearchedItem(item: SearchItemDTO) {
        Paper.book().write(SAVED_SEARCH_ITEM, item)
    }

    override fun getSearchedItem(): SearchItemDTO? {
        return Paper.book().read(SAVED_SEARCH_ITEM)
    }

    override fun setLanguageChangedForMap(isChanged: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(IS_LANGUAGE_CHANGED_FOR_MAP, isChanged)
        editor.apply()
    }

    override fun isLanguageChangedForMap(): Boolean {
        return preferences.getBoolean(IS_LANGUAGE_CHANGED_FOR_MAP, true)
    }


    override fun setLanguageChangedForProfile(isChanged: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(LANGUAGE_CHANGED_APP_KEY, isChanged)
        editor.apply()
    }

    override fun isLanguageChangedForProfile(): Boolean {
        return preferences.getBoolean(LANGUAGE_CHANGED_APP_KEY, true)
    }


    override fun markForChangeCity() {
        Paper.book().write(MARK_CHANGE_CITY, true);

    }

    override fun isChangedCity(): Boolean {
        var isCityChanged = false

        if (Paper.book().contains(MARK_CHANGE_CITY)) {
            isCityChanged = Paper.book().read(MARK_CHANGE_CITY)
            Paper.book().write(MARK_CHANGE_CITY, false);

        }

        return isCityChanged

    }

    val preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(LeMustApp.instance)


    override fun saveCurrentSubFilters(places: FiltersPresenter.SubFilters) {
        Paper.book().write(SAVE_SUB_FILTER_MODEL, places);
    }

    override fun getCurrentSubFilters(): FiltersPresenter.SubFilters {
        return Paper.book().read(SAVE_SUB_FILTER_MODEL);
    }

    override fun saveCurrentSubFiltersPager(subFiltersPuger: java.util.HashMap<Int, FiltersPresenter.SubFilters>) {
        Paper.book().write(SAVE_SUB_FILTER_PUGER, subFiltersPuger);
    }

    override fun getCurrentSubFiltersPager(): java.util.HashMap<Int, FiltersPresenter.SubFilters> {
        return Paper.book().read(SAVE_SUB_FILTER_PUGER);
    }

    override fun saveFilters(places: java.util.ArrayList<Filter>) {
        Paper.book().write(SAVE_FILTER_MODELS, places);
    }

    override fun saveCurrentFilterPlacesType(type: PlaceTypeItem) {
        Paper.book().write(SAVE_FILTER_CURRENT_PLACE_TYPE, type);
    }

    override fun getFilters(): java.util.ArrayList<Filter> {
        return Paper.book().read(SAVE_FILTER_MODELS);
    }

    override fun getCurrentFilterPlacesType(): PlaceTypeItem {
        return Paper.book().read(SAVE_FILTER_CURRENT_PLACE_TYPE);
    }

    override fun saveTypeModels(places: List<PlaceTypeDTO>) {
        Paper.book().write(PLASE_TYPE_MODELS, places);
    }

    override fun getTypeModels(): List<PlaceTypeDTO> {
        var filters = mutableListOf<PlaceTypeDTO>()

        if (Paper.book().contains(PLASE_TYPE_MODELS)) {
            filters.addAll(Paper.book().read(PLASE_TYPE_MODELS))
        }
        //va Paper . book ().read(PLASE_TYPE_MODELS);
        return filters
    }


    override fun saveCurrentPlaces(places: List<Place>) {
        Paper.book().write(SAVED_PLACES, places);
    }

    override fun saveCurrentTypeId(typeId: Int) {
        Paper.book().write(SAVED_CURRENT_PLACE_ID, typeId);
    }

    override fun saveCurrentFilters(filters: HashMap<Int, ArrayList<BaseFilter>>) {
        Paper.book().write(SAVED_CURRENT_FILTERS, filters);
    }

    override fun getCurrentPlaces(): List<Place> {
        return Paper.book().read(SAVED_PLACES);
    }

    override fun getCurrentTypeId(): Int {
        return Paper.book().read(SAVED_CURRENT_PLACE_ID);
    }

    override fun getCurrentFilters(): HashMap<Int, ArrayList<BaseFilter>> {
        return Paper.book().read(SAVED_CURRENT_FILTERS);
    }

//    override fun isRestore(): Boolean {
//        var isRestore = false
//        if (Paper.book().contains(IS_SAVE_RESTORE)) {
//            isRestore = Paper.book().read<Boolean>(IS_SAVE_RESTORE);
//        }
//        return isRestore
//    }
//
//    override fun isRestored(isRestore: Boolean) {
//        Paper.book().write(IS_SAVE_RESTORE, isRestore);
//    }


    override fun saveAvailableCities(cities: List<City>) {
        Paper.book().write(AVAILABLE_CITIES_LIST, cities);
    }

    override fun saveCurrentCity(current: City) {
        Paper.book().write(CURRENT_AVAILABLE_CITY, current); }

    override fun isAvailableCities(): Boolean {
        return Paper.book().contains(AVAILABLE_CITIES_LIST)
    }

    override fun isAvailableCurrentCity(): Boolean {
        return Paper.book().contains(CURRENT_AVAILABLE_CITY)
    }

    override fun getAvailableCities(): List<City> {
        return Paper.book().read(AVAILABLE_CITIES_LIST)
    }

    override fun getCurrentCity(): City {
        return Paper.book().read(CURRENT_AVAILABLE_CITY)
    }

    override fun saveAppStarted() {
        val editor = preferences.edit()
        editor.putBoolean(FIRST_START_APP_KEY, false)
        editor.apply()
    }


    override fun isAppStarted(): Boolean {
        return preferences.getBoolean(FIRST_START_APP_KEY, true)
    }


    override fun saveAccessToken(token: String) {
        val editor = preferences.edit()
        editor.putString(ACCESS_TOKEN_APP_KEY, token)
        editor.apply()
    }

    override fun saveUserObj(obj: UserDTO) {
        var gson = GsonBuilder()
                .setLenient().serializeNulls()
                .create()
        val fullJSON: String = gson.toJson(obj)
        val editor = preferences.edit()
        editor.putString(CURRENT_USER_OBJ, fullJSON)
        editor.apply()
    }

    override fun getUser(): UserDTO? {
        var str = preferences.getString(CURRENT_USER_OBJ, "").trim()
        var gson = GsonBuilder()
                .setLenient().serializeNulls()
                .create()
        return gson.fromJson(str, UserDTO::class.java)
    }

    override fun getToken(): String {
        return preferences.getString(ACCESS_TOKEN_APP_KEY, "")
    }

    override fun clearToken() {
        val editor = preferences.edit()
        editor.remove(ACCESS_TOKEN_APP_KEY)
        editor.apply()
    }


    override fun saveUserId(id: Int) {
        val editor = preferences.edit()
        editor.putInt(CURRENT_USER_ID_APP_KEY, id)
        editor.apply()
    }

    override fun getUserId(): Int {
        return preferences.getInt(CURRENT_USER_ID_APP_KEY, -1)
    }


    override fun updateCurrentCity() {
        Paper.book().delete(CURRENT_AVAILABLE_CITY)

        var listCity = getAvailableCities()

        var nativeCity: City? = null
        var closestCity: City? = null


        listCity.forEach {
            if (it.currentCity == "native") nativeCity = it
            if (it.currentCity == "closest") closestCity = it


        }
        if (nativeCity == null) {
            nativeCity = closestCity
        }

        if (nativeCity != null) {
            Paper.book().write(CURRENT_AVAILABLE_CITY, nativeCity)
        }


    }
}