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
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


    
interface ApiService {

    @GET("places/cities/")
    fun getCities(@Query("lat") lat: Double, @Query("lng") lng: Double): Observable<List<City>>

    @GET("places/cities/")
    fun getCities(@Query("lat") lat: Double?, @Query("lng") lng: Double?, @Header("Accept-Language") language: String): Observable<List<City>>


    @GET("places/filter/")
    fun getPlacesFiltered(@Query("city_id") id: Long? = null, @Header("Accept-Language") language: String, @Query("active_in_cities") activeCity: Int): Observable<List<Filter>>

    @GET("places/search/")
    fun searchPlaces(@Query("city_id") id: Long? = null, @Query("name") name: String? = null, @Header("Accept-Language") language: String): Observable<List<SearchItemDTO>>

//    @GET("places/types/")
//    fun getPlacesTypes(@Header("Accept-Language") language: String, @Query("city_id") cityId: Int? = null, @Query("active_in_cities") activeCity: Int? = null): Observable<List<PlaceType>>

    @GET("places/{id}/")
    fun getPlaceById(@Path("id") id: Int, @Header("Accept-Language") language: String,@Query("place_type") placeType: String): Observable<PlaceDetails>

    @GET("places/")
    fun getPlaces(@Header("Accept-Language") language: String): Observable<List<Place>>

    @GET("places/")
    fun getPlacesByCity(@Header("Authorization") token: String,@Query("city_id") city: Int, @Query("type_id") id: Int, @Header("Accept-Language") language: String): Observable<List<Place>>

    @GET("places/types/")
    fun getPlaceTypes(@Query("city_id") cityID: Int, @Header("Accept-Language") language: String, @Query("active_in_cities") activeCity: Int): Observable<List<PlaceTypeDTO>>

    @POST("places/filter/")
    fun getPlacesByFilters(@Body filter: MarkerFilterRequest, @Header("Accept-Language") language: String): Observable<List<Place>>

    @POST("report/")
    fun sendReport(@Body report: ReportDTO, @Header("Accept-Language") language: String): Observable<ReportDTO>

    @POST("rest-auth/registration/")
    fun registration(@Body report: RegistrationDTO, @Header("Accept-Language") language: String): Observable<VerificationDTO>
//    fun registration(@Body report: RegistrationDTO, @Header("Accept-Language") language: String): Observable<AuthResponseDTO>


    @POST("rest-auth/login/")
    fun login(@Body report: LoginDTO, @Header("Accept-Language") language: String): Observable<AuthResponseDTO>

    @GET("/api/users/{id}/ ")
    fun getUser(@Header("Authorization") token: String, @Path("id") report: Int, @Header("Accept-Language") language: String): Observable<UserDTO>


    @POST("users/facebook/")
    fun loginFacebook(@Body report: AuthFacebookDTO): Observable<AuthResponseDTO>

    @POST("rest-auth/password/reset/")
    fun resetPassword(@Body email: EmailDTO, @Header("Accept-Language") language: String): Observable<ResetDTO>


    @POST("rest-auth/logout/")
    fun logout(@Header("X-CSRFToken") token: String, @Header("Accept-Language") language: String): Observable<ResponseBody>


    @GET("/api/pages/{slug}/")
    fun getStoringPage(@Path("slug") report: String, @Header("Accept-Language") language: String): Observable<StoringPageDTO>


    @POST("rest-auth/password/change/")
    fun changePassword(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Body newPassword: ChangePsswordDTO): Observable<ChangePsswordDTO>

    @Headers("Content-Type: application/json")
    @PATCH("users/{id}/ ")
    fun changeUserInformation(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Path("id") useId: Int, @Body user: PathUserDTO): Observable<UserDTO>

    @Headers("Content-Type: application/json")
    @PATCH("users/{id}/ ")
    fun changeUserOccupation(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Path("id") useId: Int, @Body id: OccupationResetDTO?): Observable<UserDTO>

    @Headers("Content-Type: application/json")
    @PATCH("users/{id}/ ")
    fun changeUserImg(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Path("id") useId: Int, @Body id: ImageResetDTO?): Observable<UserDTO>




    @Headers("Content-Type: application/json")
    @PATCH("users/{id}/ ")
    fun resetUserCity(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Path("id") useId: Int, @Body id: CityResetDTO?): Observable<UserDTO>



    @DELETE("users/{id}/ ")
    fun deleteUser(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Path("id") useId: Int): Observable<Response<Any>>


    @GET("users/search_city/")
    fun searchCity(@Query("search") city: String? = null, @Header("Authorization") token: String, @Header("Accept-Language") language: String): Observable<List<SearchCityDTO>>


    @Multipart
    @PATCH("users/{id}/")
    fun uploadUserAvatar(@Part image: MultipartBody.Part, @Path("id") useId: Int, @Header("Authorization") token: String): Observable<UserDTO>


    @POST("places/update_frequentation/")
    fun reportFrequentation(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Body report: ReportFrequentationDTO): Observable<ReportFrequentationDTO>

    @GET("places/report_place/{place_id}")
    fun getPlaceDetailsItems(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Path("place_id") placeId: Int, @Query("place_type") placeTypeId: String): Observable<PlaceDetailsReportDTO>


    @Headers("Content-Type: application/json")
    @POST("places/report_place/{place_id}/")
    fun savePlaceDetailsItems(@Header("Authorization") token: String, @Header("Accept-Language") language: String, @Path("place_id") placeId: Int,@Body placeDetails:PlaceDetailsReportDTO): Observable<ResponseBody>


    @GET("places/{id}/short_description")
    fun getDescriptionPlace(@Path("id") id: Int, @Header("Accept-Language") language: String, @Header("Authorization") token: String,@Query("city") cityId: Int,@Query("type_id") placeTypeId: Int): Observable<DescriptionPlaceDTO>

}