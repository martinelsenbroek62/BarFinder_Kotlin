package com.lemust.repository.models.rest.user.get

import com.google.gson.annotations.JsonAdapter
import com.lemust.repository.models.deserializer.OccupationFieldsDeserializer


@JsonAdapter(OccupationFieldsDeserializer::class)
class OccupationChoicesDTO{
   public var map: HashMap<String, String>? = null

}
