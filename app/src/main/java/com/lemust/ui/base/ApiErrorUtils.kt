package com.lemust.ui.base

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.HttpException
import java.io.IOException


object ApiErrorUtils {

    private var tempFormattedApiError: StringBuilder? = null

    fun getStatusCode(throwable: Throwable): Int? {
        var statusCode: Int? = null
        if (throwable is HttpException) {
            try {
                statusCode = throwable.response().code()
            } catch (ioException: IOException) {
            }
        }
        return statusCode
    }

    fun parseThrowableError(throwable: Throwable): String {
        var parsedError = ""

        if (throwable is HttpException) {
            var stringJsonError = ""
            try {
                stringJsonError = throwable.response().errorBody()!!.string()
                parsedError = parseStringJsonError(stringJsonError)

            } catch (ioException: IOException) {
            }
        } else {
            if(throwable.localizedMessage!=null){
                parsedError = throwable.localizedMessage
            }
        }

        return parsedError

    }


//    fun parseThrowableErrorInUserText(throwable: Throwable): String {
//        var parsedError = parseThrowableError(throwable)
//
//
//
//
//    }


    fun parseStringJsonError(error: String): String {
        tempFormattedApiError = StringBuilder()
        parseStringError(error)
        var result = tempFormattedApiError.toString()
        tempFormattedApiError = null
        return result
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
}