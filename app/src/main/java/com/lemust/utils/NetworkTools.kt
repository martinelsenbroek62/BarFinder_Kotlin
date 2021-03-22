package com.lemust.utils

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.lemust.LeMustApp
import com.lemust.repository.models.rest.auth.ErrorDTO
import retrofit2.HttpException

object NetworkTools {

    fun parseThrowableErrorByRetrofit(e: Throwable): ErrorDTO? {
        if (e is HttpException) {
            val error = e as HttpException
            val errorBody = error.response().errorBody()!!.string()
            return Gson().fromJson(errorBody, ErrorDTO::class.java)
        } else return null
    }

    public fun isOnline(): Boolean {
        var cm =
                LeMustApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting;
    }
}