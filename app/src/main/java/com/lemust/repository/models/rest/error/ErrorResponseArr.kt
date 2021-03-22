package com.lemust.repository.models.rest.error

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ErrorResponseArr(@Expose
                       @SerializedName("code")
                       var code: String? = null,

                       @Expose
                       @SerializedName("success")
                       var success: String? = null,

                       @Expose
                       @SerializedName("status")
                       var status: String? = null,

                       @Expose
                       @SerializedName("errors")
                       var errors: List<ErrorData>? = null){


    fun getErrorMessage(): String{
        return try {
            errors!![0].errors!![0]
        }catch (e: Exception){
            e.printStackTrace()
            "Unknown error..."
        }
    }

    fun getIntCode(): Int{
        return try {
            code!!.toInt()
        }catch (e: Exception){
            e.printStackTrace()
            0
        }
    }
}


data class ErrorResponseObj(@Expose
                            @SerializedName("code")
                            var code: String? = null,

                            @Expose
                            @SerializedName("success")
                            var success: String? = null,

                            @Expose
                            @SerializedName("status")
                            var status: String? = null,

                            @Expose
                            @SerializedName("errors")
                            var errors: ErrorData? = null){

    fun getErrorMessage(): String{
        return try {
            errors!!.errors!![0]
        }catch (e: Exception){
            e.printStackTrace()
            "Unknown error..."
        }
    }

    fun getIntCode(): Int{
        return try {
            code!!.toInt()
        }catch (e: Exception){
            e.printStackTrace()
            0
        }
    }
}
data class ErrorData(
        @Expose
        @SerializedName("field")
        var field: String? = null,
        @Expose
        @SerializedName("type")
        var type: String? = null,
        @Expose
        @SerializedName("errors")
        var errors: List<String>? = null,
        @Expose
        @SerializedName("hint")
        var hint: String? = null
)