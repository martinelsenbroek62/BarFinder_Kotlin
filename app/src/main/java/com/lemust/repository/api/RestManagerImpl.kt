package com.lemust.repository.api

import com.google.gson.GsonBuilder
import com.lemust.BuildConfig
import io.reactivex.subjects.PublishSubject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

    
class RestManagerImpl {
     var tokenIsNotValid = PublishSubject.create<Any>()

    private var service: ApiService
    private lateinit var retrofit: Retrofit
    var api: ApiLeMust
    var gson = GsonBuilder()
            .setLenient()
            .create()


    init {
        service = createService()
        api = ApiLeMustImpl(service,tokenIsNotValid)

    }



    private fun createService(): ApiService {
        val httpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.BASIC
        }


        val builder = Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory .create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        httpClientBuilder.addInterceptor(logging)
        builder.client(httpClientBuilder.connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .authenticator(TokenAuthenticator(tokenIsNotValid))
                .writeTimeout(20, TimeUnit.SECONDS).build())
        retrofit = builder.build()
        return retrofit.create(ApiService::class.java)
    }

}