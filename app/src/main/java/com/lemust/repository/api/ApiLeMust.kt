package com.lemust.repository.api

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
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File


interface ApiLeMust {

    fun changeUserInformation(token: String, userId: Int, user: PathUserDTO): Observable<UserDTO>
    fun changeUserOccupation(occupation: OccupationResetDTO?): Observable<UserDTO>
    fun changeUserImage(occupation: ImageResetDTO?): Observable<UserDTO>
    fun resetUserCity(occupation: CityResetDTO?): Observable<UserDTO>
    fun deleteUser(): Observable<Response<Any>>
    fun initInvalidTokenObserver()

    fun reportFrequentation(report: ReportFrequentationDTO): Observable<ReportFrequentationDTO>

    fun getCities(lat: Double, lon: Double): Observable<List<City>>
    fun searchPlaces(id: Long? = null, name: String? = null): Observable<List<SearchItemDTO>>


    fun getPlacesFiltered(id: Long? = null, activeCity: Int): Observable<List<Filter>>

//    fun getPlacesTypes(cityId:Int?=null): Observable<List<PlaceType>>

    fun getPlaceById(id: Int, typePlaceId: String): Observable<PlaceDetails>

    fun getPlacesByCity(id: Int, typeId: Int): Observable<List<Place>>
    fun getPlacesByFilter(filter: MarkerFilterRequest): Observable<List<Place>>

    fun getPlaceTypes(city_id: Int, activeCity: Int): Observable<List<PlaceTypeDTO>>

    fun getPlaces(): Observable<List<Place>>
    fun sendReport(report: ReportDTO): Observable<ReportDTO>
    fun updateCitiesLanguage(lat: Double? = null, lon: Double? = null, languageCode: String): Observable<List<City>>
    fun registration(report: RegistrationDTO): Observable<VerificationDTO>
    fun loginFacebook(report: AuthFacebookDTO): Observable<AuthResponseDTO>
    fun login(report: LoginDTO): Observable<AuthResponseDTO>
    fun getUser(token: String, userId: Int): Observable<UserDTO>
    fun logout(token: String): Observable<ResponseBody>

    fun resetPassword(email: EmailDTO): Observable<ResetDTO>
    fun getStoringPage(slug: String): Observable<StoringPageDTO>
    fun changePassword(token: String, newPassword: ChangePsswordDTO): Observable<ChangePsswordDTO>
    fun searchCity(token: String, city: String? = null): Observable<List<SearchCityDTO>>

    fun uploadUserAvatar(path: File): Observable<UserDTO>
    fun getPlaceDetailsItems(placeId: Int, placeTypeId: String): Observable<PlaceDetailsReportDTO>
    fun savePlaceDetailsItems(placeId: Int, placeTypeId: String, placeDetails: PlaceDetailsReportDTO): Observable<ResponseBody>
    fun getDescriptionPlace(placeId: Int, cityId: Int, placeTypeId: Int): Observable<DescriptionPlaceDTO>

    fun tokenIsNotValid(): PublishSubject<Any>


}