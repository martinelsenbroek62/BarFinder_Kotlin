package com.lemust.repository.models.rest.place_details

import com.google.gson.annotations.JsonAdapter
import com.lemust.repository.models.deserializer.PlaceFieldsDeserializer
import java.io.Serializable

@JsonAdapter(PlaceFieldsDeserializer::class)
class PlaceFields:Serializable {
    var map: HashMap<String, String>? = null

}
