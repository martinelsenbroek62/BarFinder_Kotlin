package com.lemust.repository.api

import com.lemust.LeMustApp
import com.lemust.repository.models.filters.Filter
import com.lemust.repository.models.rest.City
import com.lemust.repository.models.rest.Place
import com.lemust.repository.models.rest.StoringPageDTO
import com.lemust.repository.models.rest.auth.EmailDTO
import com.lemust.repository.models.rest.auth.VerificationDTO
import com.lemust.repository.models.rest.auth.login.LoginDTO
import com.lemust.repository.models.rest.auth.registaration.AuthFacebookDTO
import com.lemust.repository.models.rest.auth.registaration.AuthResponseDTO
import com.lemust.repository.models.rest.auth.registaration.RegistrationDTO
import com.lemust.repository.models.rest.auth.reset.ChangePsswordDTO
import com.lemust.repository.models.rest.auth.reset.ResetDTO
import com.lemust.repository.models.rest.description.DescriptionPlaceDTO
import com.lemust.repository.models.rest.place.PlaceTypeDTO
import com.lemust.repository.models.rest.place_details.PlaceDetails
import com.lemust.repository.models.rest.report.ReportDTO
import com.lemust.repository.models.rest.report.ReportFrequentationDTO
import com.lemust.repository.models.rest.report_details.PlaceDetailsReportDTO
import com.lemust.repository.models.rest.request.MarkerFilterRequest
import com.lemust.repository.models.rest.search.SearchCityDTO
import com.lemust.repository.models.rest.search.SearchItemDTO
import com.lemust.repository.models.rest.user.get.UserDTO
import com.lemust.repository.models.rest.user.path.PathUserDTO
import com.lemust.repository.models.rest.user.reset.CityResetDTO
import com.lemust.repository.models.rest.user.reset.ImageResetDTO
import com.lemust.repository.models.rest.user.reset.OccupationResetDTO
import com.lemust.utils.AppHelper
import com.lemust.utils.onBackgroundThread
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.net.ProtocolException
import java.net.SocketTimeoutException


class ApiLeMustImpl(var apiService: ApiService, var tokenIsNotValid: PublishSubject<Any>) : ApiLeMust {
    override fun initInvalidTokenObserver() {
        tokenIsNotValid == PublishSubject.create<Any>()
    }


    private val maxRepeats = 2
    private val tokenPrefix = "Token "
    var repeatCounters = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)


    override fun tokenIsNotValid(): PublishSubject<Any> {
        return tokenIsNotValid
    }

    override fun getDescriptionPlace(placeId: Int, cityId: Int, placeTypeId: Int): Observable<DescriptionPlaceDTO> {
        var request = apiService.getDescriptionPlace(placeId, AppHelper.locale.getLanguage(LeMustApp.instance), "$tokenPrefix${AppHelper.preferences.getToken()}", cityId, placeTypeId)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[0]
                }

                override fun clearCounter() {
                    repeatCounters[0] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[0]++
                }
            })
        })

    }

    override fun reportFrequentation(report: ReportFrequentationDTO): Observable<ReportFrequentationDTO> {
        var request = apiService.reportFrequentation("$tokenPrefix${AppHelper.preferences.getToken()}", AppHelper.locale.getLanguage(LeMustApp.instance), report)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[1]
                }

                override fun clearCounter() {
                    repeatCounters[1] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[1]++
                }
            })
        })
    }

    override fun uploadUserAvatar(photoFile: File): Observable<UserDTO> {
        var body: MultipartBody.Part? = null
        if (photoFile != null) {
            val requestFile = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    photoFile
            )
            body = MultipartBody.Part.createFormData("image", photoFile.name, requestFile)
        }


        var request = apiService.uploadUserAvatar(body!!, AppHelper.preferences.getUserId(), "$tokenPrefix${AppHelper.preferences.getToken()}")



        return onBackgroundThread(request.doOnNext { AppHelper.preferences.saveUserObj(it) }.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[2]
                }

                override fun clearCounter() {
                    repeatCounters[2] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[2]++
                }
            })
        })
    }

    override fun getPlaceDetailsItems(placeId: Int, placeTypeId: String): Observable<PlaceDetailsReportDTO> {
        var request = apiService.getPlaceDetailsItems("$tokenPrefix${AppHelper.preferences.getToken()}", AppHelper.locale.getLanguage(LeMustApp.instance), placeId, placeTypeId)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[3]
                }

                override fun clearCounter() {
                    repeatCounters[3] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[3]++
                }
            })
        })
    }

    override fun savePlaceDetailsItems(placeId: Int, placeTypeId: String, placeDetails: PlaceDetailsReportDTO): Observable<ResponseBody> {
        var request = apiService.savePlaceDetailsItems("$tokenPrefix${AppHelper.preferences.getToken()}", AppHelper.locale.getLanguage(LeMustApp.instance), placeId, placeDetails)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[4]
                }

                override fun clearCounter() {
                    repeatCounters[4] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[4]++
                }
            })
        })
    }


    override fun changeUserOccupation(occupation: OccupationResetDTO?): Observable<UserDTO> {
        var request = apiService.changeUserOccupation("$tokenPrefix${AppHelper.preferences.getToken()}", AppHelper.locale.getLanguage(LeMustApp.instance), AppHelper.preferences.getUser()!!.id!!, occupation)


        return onBackgroundThread(request.doOnNext { AppHelper.preferences.saveUserObj(it) }.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[5]
                }

                override fun clearCounter() {
                    repeatCounters[5] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[5]++
                }
            })
        })
    }

    override fun changeUserImage(occupation: ImageResetDTO?): Observable<UserDTO> {
        var request = apiService.changeUserImg("$tokenPrefix${AppHelper.preferences.getToken()}", AppHelper.locale.getLanguage(LeMustApp.instance), AppHelper.preferences.getUser()!!.id!!, occupation)
        return onBackgroundThread(request.doOnNext { AppHelper.preferences.saveUserObj(it) }.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[6]
                }

                override fun clearCounter() {
                    repeatCounters[6] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[6]++
                }
            })
        })
    }


    override fun resetUserCity(resetCity: CityResetDTO?): Observable<UserDTO> {
        var request = apiService.resetUserCity("$tokenPrefix${AppHelper.preferences.getToken()}", AppHelper.locale.getLanguage(LeMustApp.instance), AppHelper.preferences.getUser()!!.id!!, resetCity)
        return onBackgroundThread(request.doOnNext { AppHelper.preferences.saveUserObj(it) }.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[7]
                }

                override fun clearCounter() {
                    repeatCounters[7] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[7]++
                }
            })
        })
    }


    override fun searchCity(token: String, city: String?): Observable<List<SearchCityDTO>> {
        var request = apiService.searchCity(city, "$tokenPrefix$token", AppHelper.locale.getLanguage(LeMustApp.instance))

        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[8]
                }

                override fun clearCounter() {
                    repeatCounters[8] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[8]++
                }
            })
        })
    }

    override fun changeUserInformation(token: String, userId: Int, user: PathUserDTO): Observable<UserDTO> {
        var request = apiService.changeUserInformation("$tokenPrefix$token", AppHelper.locale.getLanguage(LeMustApp.instance), userId, user).doOnNext {
            AppHelper.preferences.saveUserObj(it)
        }
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[9]
                }

                override fun clearCounter() {
                    repeatCounters[9] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[9]++
                }
            })
        })
    }

    override fun deleteUser(): Observable<Response<Any>> {
        var request = apiService.deleteUser("$tokenPrefix${AppHelper.preferences.getToken()}", AppHelper.locale.getLanguage(LeMustApp.instance), AppHelper.preferences.getUser()!!.id!!)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[10]
                }

                override fun clearCounter() {
                    repeatCounters[10] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[10]++
                }
            })
        })
    }

    override fun changePassword(token: String, newPassword: ChangePsswordDTO): Observable<ChangePsswordDTO> {
        var request = apiService.changePassword("$tokenPrefix$token", AppHelper.locale.getLanguage(LeMustApp.instance), newPassword)
        return onBackgroundThread(request)
    }

    override fun getStoringPage(slug: String): Observable<StoringPageDTO> {
        var request = apiService.getStoringPage(slug, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[12]
                }

                override fun clearCounter() {
                    repeatCounters[12] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[12]++
                }
            })
        })
    }

    override fun resetPassword(email: EmailDTO): Observable<ResetDTO> {
        var request = apiService.resetPassword(email, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request)
    }

    override fun getUser(token: String, userId: Int): Observable<UserDTO> {
        var request = apiService.getUser("$tokenPrefix$token", userId, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[14]
                }

                override fun clearCounter() {
                    repeatCounters[14] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[14]++
                }
            })
        })
    }

    override fun loginFacebook(auth: AuthFacebookDTO): Observable<AuthResponseDTO> {
        var request = apiService.loginFacebook(auth)
        return onBackgroundThread(request)
    }

    override fun logout(token: String): Observable<ResponseBody> {
        var request = apiService.logout(token, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[16]
                }

                override fun clearCounter() {
                    repeatCounters[16] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[16]++
                }
            })
        })
    }

    override fun registration(registration: RegistrationDTO): Observable<VerificationDTO> {
        var request = apiService.registration(registration, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request)
    }

    override fun login(login: LoginDTO): Observable<AuthResponseDTO> {
        var request = apiService.login(login, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request)
    }


    override fun getPlacesByFilter(filter: MarkerFilterRequest): Observable<List<Place>> {
        var request = apiService.getPlacesByFilters(filter, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[19]
                }

                override fun clearCounter() {
                    repeatCounters[19] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[19]++
                }
            })
        })
    }

    override fun getCities(lat: Double, lon: Double): Observable<List<City>> {
        var request = apiService.getCities(lat, lon)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[20]
                }

                override fun clearCounter() {
                    repeatCounters[20] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[20]++
                }
            })
        })
    }

    override fun getPlacesFiltered(id: Long?, activeCity: Int): Observable<List<Filter>> {
        var request = apiService.getPlacesFiltered(id, AppHelper.locale.getLanguage(LeMustApp.instance), activeCity)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[21]
                }

                override fun clearCounter() {
                    repeatCounters[21] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[21]++
                }
            })
        })
    }


//    override fun getPlacesTypes(cityId: Int?): Observable<List<PlaceType>> = onBackgroundThread(apiService.getPlacesTypes(AppHelper.locale.getLanguage(LeMustApp.instance), cityId,cityId))

    override fun getPlaceById(id: Int, placeTypeId: String): Observable<PlaceDetails> {
        var request = apiService.getPlaceById(id, AppHelper.locale.getLanguage(LeMustApp.instance), placeTypeId)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[22]
                }

                override fun clearCounter() {
                    repeatCounters[22] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[22]++
                }
            })
        })
    }

    override fun getPlaces(): Observable<List<Place>> {
        var request = apiService.getPlaces(AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[23]
                }

                override fun clearCounter() {
                    repeatCounters[23] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[23]++
                }
            })
        })
    }

    override fun getPlacesByCity(id: Int, typeId: Int): Observable<List<Place>> {
        var request = apiService.getPlacesByCity("$tokenPrefix${AppHelper.preferences.getToken()}", id, typeId, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[24]
                }

                override fun clearCounter() {
                    repeatCounters[24] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[24]++
                }
            })
        })
    }


    override fun getPlaceTypes(city_id: Int, activeCity: Int): Observable<List<PlaceTypeDTO>> {
        var request = apiService.getPlaceTypes(city_id, AppHelper.locale.getLanguage(LeMustApp.instance), activeCity)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[25]
                }

                override fun clearCounter() {
                    repeatCounters[25] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[25]++
                }
            })
        })
    }


    override fun sendReport(report: ReportDTO): Observable<ReportDTO> {
        var request = apiService.sendReport(report, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[26]
                }

                override fun clearCounter() {
                    repeatCounters[26] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[26]++
                }
            })
        })
    }

    override fun updateCitiesLanguage(lat: Double?, lon: Double?, languageCode: String): Observable<List<City>> {
        var request = apiService.getCities(lat, lon, languageCode)
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[27]
                }

                override fun clearCounter() {
                    repeatCounters[27] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[27]++
                }
            })
        })
    }

    override fun searchPlaces(id: Long?, name: String?): Observable<List<SearchItemDTO>> {
        var request = apiService.searchPlaces(id, name, AppHelper.locale.getLanguage(LeMustApp.instance))
        return onBackgroundThread(request.retryWhen { errors ->
            handleRepeatRequest(errors, request, object : CounterInterface {
                override fun getCurrentCounter(): Int {
                    return repeatCounters[28]
                }

                override fun clearCounter() {
                    repeatCounters[28] = 0
                }

                override fun incrementCounter() {
                    repeatCounters[28]++
                }
            })
        })
    }


    interface CounterInterface {
        fun getCurrentCounter(): Int
        fun clearCounter()
        fun incrementCounter()
    }

    fun interaptRepeat(it: Throwable): Observable<Observable<Throwable>>? =
            Observable.error(it)

    fun handleRepeatRequest(errors: Observable<Throwable>, ifRepeat: Observable<out Any>, counterInterface: CounterInterface): Observable<Observable<out Any>>? {
        return errors.flatMap {
            if (it is Exception) {
                if (it is SocketTimeoutException || it is ProtocolException) {
                    counterInterface.clearCounter()
                    interaptRepeat(it)
                } else if (counterInterface.getCurrentCounter() > maxRepeats) {
                    counterInterface.clearCounter()
                    interaptRepeat(it)
                } else {
                    counterInterface.incrementCounter()
                    Observable.just(ifRepeat)
                }
            } else {
                counterInterface.clearCounter()
                interaptRepeat(it)

            }

        }
    }
}