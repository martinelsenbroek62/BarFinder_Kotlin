package com.lemust.utils

import android.content.Context
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import com.lemust.LeMustApp
import com.lemust.R
import com.lemust.ui.base.ApiErrorUtils
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.HttpException
import java.io.IOException
import java.io.StringReader
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class ErrorUtils(var err: Throwable, var isShowTitle: Boolean, var context: Context? = null) {
    var titleError = ""
    var bodyError = ""
    var errorList = ArrayList<Error>()
    var titles = HashMap<String, String>()
    private var tempFormattedApiError: StringBuilder? = null
    var isInternetError = false


    fun parse() {
        parseError()
    }

    private fun parseError() {
        when (err) {
            is HttpException -> handleHttpException()
            is UnknownHostException -> handleUnknownHostException()
            is SocketTimeoutException -> handleUnknownHostException()
            else -> handleOtherException()
        }
    }

    private fun handleOtherException() {
        titleError = parseThrowableError(err)
        if (titleError.toUpperCase() == "timeout".toUpperCase()) {
            titleError = LeMustApp.instance.getString(R.string.title_timeout)
            bodyError = LeMustApp.instance!!.getString(R.string.text_timeout)

            context?.let {
                titleError = context!!.getString(R.string.title_timeout)
                bodyError = context!!.getString(R.string.text_timeout)
            }


        }
    }


    private fun handleUnknownHostException() {
        isInternetError = true
        titleError = if (context != null)
            context!!.resources.getString(R.string.no_internet_connection)
        else
            LeMustApp.instance.resources.getString(R.string.no_internet_connection)
        bodyError = ""


    }

    private fun handleHttpException() {
        var httException = (err as HttpException)

        httException?.code()?.let {
            if (it == 500) {
                context?.let {
                    titleError = LeMustApp.instance.getString(R.string.title_server_error)
                    bodyError = LeMustApp.instance!!.getString(R.string.title_try_again)

                    context?.let {
                        titleError = context!!.getString(R.string.title_server_error)
                        bodyError = context!!.getString(R.string.title_try_again)
                    }
                }
                return;
            }
        }


        val errorBody = (err as HttpException).response().errorBody()!!.string()
        val jsonReader = JsonReader(StringReader(errorBody))
        jsonReader.isLenient = true
        val parser = JsonParser()
        try {


            val element = parser.parse(jsonReader)
            val obj = element.asJsonObject
            val entries = obj.entrySet()
            for (entry in entries) {
                var title = entry.key
                if (titles!!.containsKey(entry.key)) {
                    title = titles!![entry.key]!!

                }

                errorList.add(Error(title, entry.value.asString))
            }

            if (errorList.isNotEmpty()) {
                if (isShowTitle) {
                    titleError = errorList.first().title
                    bodyError = errorList.first().mesage
                } else {
                    titleError = errorList.first().mesage
                }
            } else {
                bodyError = parseStringJsonError(errorBody)

            }
        } catch (e: IllegalStateException) {
            titleError = if (e.message != null)
                e.message!!
            else {
                if (context != null)
                    context!!.resources.getString(R.string.no_internet_connection)
                else
                    LeMustApp.instance.resources.getString(R.string.no_internet_connection)

            }
        }
    }

    fun parseStringJsonError(error: String): String {
        tempFormattedApiError = StringBuilder()
        parseStringError(error)
        var result = tempFormattedApiError.toString()
        tempFormattedApiError = null
        return result
    }

    fun parseThrowableError(throwable: Throwable): String {
        var parsedError = ""

        if (throwable is HttpException) {
            var stringJsonError = ""
            try {
                stringJsonError = throwable.response().errorBody()!!.string()
                parsedError = ApiErrorUtils.parseStringJsonError(stringJsonError)

            } catch (ioException: IOException) {
            }
        } else {
            if (throwable.localizedMessage != null) {
                parsedError = throwable.localizedMessage
            }
        }




        return parsedError

    }

    private fun parseStringError(jsonRaw: String, previousDeep: Int = 0) {
        var currentDeep = previousDeep + 1


        var leftMargin = ""
        for (i in 1..currentDeep - 1) {
            leftMargin += "  "
        }

        if (ifJsonIsArray(jsonRaw)) {
            var jsonArray = JSONArray(jsonRaw)
            for (i in 0 until jsonArray.length()) {
                var errorToParse = jsonArray[i].toString()
                if (!errorToParse.equals("")) {
                    if (!ifJsonIsObject(errorToParse) && !ifJsonIsArray(errorToParse)) {
                        tempFormattedApiError!!.append(leftMargin + "- " + errorToParse + "\n")
                    } else {
                        parseStringError(errorToParse, currentDeep)
                    }
                }
            }
        } else if (ifJsonIsObject(jsonRaw)) {
            var baseJsonObject = JSONObject(jsonRaw)
            var baseIterator = baseJsonObject.keys()
            while (baseIterator.hasNext()) {
                var baseKey = baseIterator.next()

                var errorToParse = baseJsonObject.get(baseKey).toString()

                if (!ifJsonIsArray(errorToParse)) {

                    if (!errorToParse.trim().isEmpty() && !ifJsonIsObject(errorToParse)) {
                        tempFormattedApiError!!.append(leftMargin + "${formatErrorName(baseKey)} : ${errorToParse}\n")
                    } else {
                        tempFormattedApiError!!.append(leftMargin + "${formatErrorName(baseKey)} \n")
                    }
                } else {
                    tempFormattedApiError!!.append(leftMargin + formatErrorName(baseKey) + " :\n")
                }

                if (!errorToParse.equals("")) {
                    leftMargin + parseStringError(errorToParse, currentDeep)
                }
            }
        }
    }

    fun ifJsonIsObject(jsonString: String): Boolean {
        if (jsonString.isEmpty()) {
            return false
        }

        var jsonRaw: Any = JSONTokener(jsonString).nextValue()
        if (jsonRaw is JSONObject) {
            return true
        }
        return false
    }

    fun formatErrorName(value: String?): String {
        if (value == null || value.trim().isEmpty()) {
            return ""
        }
        var result = value
        result = result.replace("_", " ")
        result = result.substring(0, 1).toUpperCase() + result.substring(1)
        return result
    }

    fun ifJsonIsArray(jsonString: String): Boolean {
        if (jsonString.isEmpty()) {
            return false
        }

        var jsonRaw: Any = JSONTokener(jsonString).nextValue()
        if (jsonRaw is JSONArray) {
            return true
        }
        return false
    }

    class Error(var title: String, var mesage: String)


    fun addCustomTitle(key: String, title: String) {
        titles[key] = title

    }
}