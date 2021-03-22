package com.lemust.repository.models.rest.request

import com.google.gson.annotations.SerializedName

    
class BoolFilter : BaseFilter {
     @SerializedName("data") var data:Boolean?=null

     constructor(id: Int,data: Boolean?) : super(id) {
          this.data = data
     }
}