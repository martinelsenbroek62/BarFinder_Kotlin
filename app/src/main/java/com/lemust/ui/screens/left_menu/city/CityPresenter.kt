package com.lemust.ui.screens.left_menu.city

import android.os.Handler
import android.os.Looper
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.repository.models.rest.City
import com.lemust.ui.base.BaseActivity
import com.lemust.ui.base.dialog.DialogModel
import com.lemust.ui.screens.left_menu.city.adapter.item.BaseItem
import com.lemust.ui.screens.left_menu.city.adapter.item.CityItem
import com.lemust.ui.screens.left_menu.city.adapter.item.CountyItem
import com.lemust.ui.screens.left_menu.city.adapter.item.TypeItem
import com.lemust.utils.AppDataHolder
import com.lemust.utils.AppHelper
import com.lemust.utils.ErrorUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

     
class CityPresenter(var view: CityContract.View, var activity: BaseActivity) : CityContract.Presenter {
    var selectedCity: City? = null

    init {
        loadData()
        initAction()
    }

    private fun initAction() {
        view.onClickAction().subscribe {
            selectedCity = it
            apply()

        }
    }

    private fun apply() {
        var currentCity = AppHelper.preferences.getCurrentCity()


        if (selectedCity != null && currentCity != null) {

            if (currentCity == selectedCity) {
                view.dismiss()
                // eventBus.post(MainFragment.ChangeCity(selectedLanguage!!, true))
                view.dismiss()
            } else {
                selectedCity?.let {
                    AppHelper.preferences.saveCurrentCity(it)
                    AppHelper.preferences.markForChangeCity()
                     view.changeCity(it)
                }
//                AppDataHolder.currentCity = selectedLanguage
                //                    if (NetworkTools.isOnline())
                // eventBus.post(MainFragment.ChangeCity(selectedLanguage!!, false))
                //                    else
                //                        eventBus.post(MainActivity.ShowNoInternetDialog())

            }
        } else {
            //todo show error
        }
      //  view.dismiss()
    }

    fun loadCities(lat: Double? = null, lon: Double? = null) {
        AppHelper.api.updateCitiesLanguage(lat, lon, languageCode = AppHelper.locale.getLanguage(view.getViewContext()))
                .subscribe(object : Observer<List<City>> {
                    override fun onError(e: Throwable) {
                        view.setVisibleLoader(false)
                        Handler(Looper.getMainLooper()).post {
                            var errorMessage = ErrorUtils(e, false, activity)
                            errorMessage.parse()
                            view.showDialog(DialogModel().build(activity, errorMessage.titleError)
                                    .showMessage(errorMessage.bodyError)
                                    .showLastButton(activity.resources.getString(R.string.title_reload))
                                    .showFirstButton(activity.resources.getString(R.string.title_cancel))
                                    .single(true)).subscribe {
                                if (DialogModel.State.FIRST_BUTTON == it.clicked) {
                                    it.dialog.dismiss()
                                    view.dismiss()

                                }
                                if (DialogModel.State.SECOND_BUTTON == it.clicked) {
                                    it.dialog.dismiss()
                                    loadData()


                                }
                            }
                        }
                    }

                    override fun onComplete() =Unit
                    override fun onSubscribe(d: Disposable)=Unit
                    override fun onNext(t: List<City>) {
                        AppDataHolder.citiesLanguageChanged = false
                        AppHelper.preferences.saveAvailableCities(t)

                        handleCities(t)
                        view.setVisibleContent(true)
                        view.setVisibleLoader(false)


                    }

                })

    }


    private fun loadData() {
        if (AppDataHolder.citiesLanguageChanged) {
            view.setVisibleContent(false)
            view.setVisibleLoader(true)
            if (view.isPermissionGranted()) {
                handleLocation()

            } else {
                preparePermission()
            }


        } else {
            handleCities()
        }
    }

    private fun preparePermission() {
        view.getPermissionLocation(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                AppDataHolder.isLocationAvailable = true
                handleLocation()


            }


            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                AppDataHolder.isLocationAvailable = false
                loadCities()


            }

            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                token!!.continuePermissionRequest();
            }
        })
    }

    private fun handleLocation() {
        if (view.isAvailableLocation()) {
            view.getLocation().subscribe {
                if (it.location != null) {
                    loadCities(it.location!!.latitude, it.location!!.longitude)
                } else {
                    loadCities()
                }
            }
        } else {
            loadCities()
        }
    }


    private fun handleCities(cities: List<City>? = null) {
        var citiesList = mutableListOf<City>()
        if (cities == null) {
            if (AppHelper.preferences.isAvailableCities()) {
                citiesList.addAll(AppHelper.preferences.getAvailableCities()!!)
            } else {
                loadData()
            }

        } else {
            citiesList.addAll(cities)

        }

        var mapCountry = HashMap<String, MutableList<City>>()
        var baseItems = mutableListOf<BaseItem>()


        citiesList.forEach {
            if (mapCountry.contains(it.country)) {
                mapCountry.get(it.country)!!.add(it)
            } else {
                mapCountry.put(it.country!!, mutableListOf(it))

            }
        }
        var sorted = mapCountry.toSortedMap()

        sorted.forEach {
            it.value.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.name!! }))
            baseItems.add(CountyItem(TypeItem.COUNRTY, name = it.key))
            addCitiesInList(baseItems, it.value)
        }


        view.setCities(baseItems as ArrayList<BaseItem>)
    }

    private fun addCitiesInList(baseItems: MutableList<BaseItem>, value: MutableList<City>) {
        var currentCityId: Long = 0
        if (AppHelper.preferences.isAvailableCurrentCity()) {
            currentCityId = AppHelper.preferences.getCurrentCity()!!.id
        }

        value.forEach {
            baseItems.add(CityItem(TypeItem.CITY_NAME, isSelected = it.id == currentCityId, city = it))
        }
    }


}