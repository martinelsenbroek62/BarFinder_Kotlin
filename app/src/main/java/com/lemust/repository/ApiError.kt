package com.lemust.repository

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


    
class ApiError(error: Throwable) {

    companion object {

        const val RESPONSE_CODE_NOT_SPECIFIED = 0

        const val TYPE_UNEXPECTED_EXCEPTION = 0
        const val TYPE_HTTP_EXCEPTION = 1
        const val TYPE_UNKNOWN_HOST_EXCEPTION = 2
        const val TYPE_SSL_HANDSHAKE_EXCEPTION = 3
        const val TYPE_INTERNAL_SERVER_EXCEPTION = 4

        const val KEY_ERROR_TYPE = "error_type"
        const val KEY_ERROR_MESSAGE = "error_message"

        const val KEY_UNAUTHORIZED = "unauthorized"

        const val VALUE_UNEXPECTED_EXCEPTION = "unexpected_exception"
        const val VALUE_HTTP_EXCEPTION = "http_exception"
        const val VALUE_UNKNOWN_HOST_EXCEPTION = "unknown_host_exception"
        const val VALUE_SSL_HANDSHAKE_EXCEPTION = "ssl_handshake_exception"
        const val VALUE_INTERNAL_SERVER_EXCEPTION = "internal_server_error"

        fun noSpecificErrors(error: Map<String, String>): Boolean {
            return error.entries.size == 2 && error.contains(KEY_ERROR_TYPE) && error.contains(KEY_ERROR_MESSAGE)
        }
    }

    private val code: Int
    private var message: String = ""
    private val json: JSONObject?
    private val type: Int

    init {
        when (error) {
            is HttpException -> when (error.code()) {
                500 -> {
                    type = TYPE_INTERNAL_SERVER_EXCEPTION
                    code = error.code()
                    json = error.response()?.errorBody()?.string()?.let { parse(it) }
                }
                else -> {
                    type = TYPE_HTTP_EXCEPTION
                    code = error.code()
                    message = error.localizedMessage
                    json = error.response()?.errorBody()?.string()?.let { parse(it) }
                }
            }
            is UnknownHostException -> {
                type = TYPE_UNKNOWN_HOST_EXCEPTION
                code = RESPONSE_CODE_NOT_SPECIFIED
                json = null
            }
            is SocketTimeoutException -> {
                type = TYPE_SSL_HANDSHAKE_EXCEPTION
                code = RESPONSE_CODE_NOT_SPECIFIED
                json = null
            }
            else -> {
                type = TYPE_UNEXPECTED_EXCEPTION
                code = RESPONSE_CODE_NOT_SPECIFIED
                json = null
            }
        }
    }

    private fun parse(body: String): JSONObject? = try {
        JSONTokener(body).nextValue() as? JSONObject
    } catch (e: Exception) {
        null
    }

    fun map(): Map<String, String> {
        val divider = "\n"
        val result = mutableMapOf<String, String>()

        result[KEY_ERROR_TYPE] = when (type) {
            TYPE_HTTP_EXCEPTION -> VALUE_HTTP_EXCEPTION
            TYPE_UNKNOWN_HOST_EXCEPTION -> VALUE_UNKNOWN_HOST_EXCEPTION
            TYPE_SSL_HANDSHAKE_EXCEPTION -> VALUE_SSL_HANDSHAKE_EXCEPTION
            TYPE_INTERNAL_SERVER_EXCEPTION -> VALUE_INTERNAL_SERVER_EXCEPTION
            else -> VALUE_UNEXPECTED_EXCEPTION
        }
        result.put(KEY_ERROR_MESSAGE, message)

        if (type == TYPE_HTTP_EXCEPTION && code == 401) {
            result.put(KEY_UNAUTHORIZED, "")
        }

        try {
            json?.keys()?.forEach { key ->
                when {
                    json.opt(key) is JSONArray -> {
                        json.optJSONArray(key)?.apply {
                            val count = length()
                            val builder = StringBuffer()
                            for (index: Int in 0 until count) {
                                if (builder.isNotEmpty()) builder.append(divider)
                                builder.append(getString(index))
                            }
                            result.put(key, builder.toString())
                        }
                    }
                    json.opt(key) is String -> {
                        json.optString(key)?.apply {
                            result.put(key, this)
                        }
                    }
                }
            }
        } catch (e: Exception) {
        }
        return result.toMap()
    }

}