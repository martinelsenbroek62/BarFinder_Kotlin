package com.lemust.repository.models.deserializer

import android.util.Log
import com.google.gson.*
import com.lemust.repository.models.rest.user.get.OccupationChoicesDTO
import java.lang.reflect.Type
import java.util.*

class OccupationFieldsDeserializer : JsonDeserializer<OccupationChoicesDTO>, JsonSerializer<OccupationChoicesDTO> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): OccupationChoicesDTO {
        val map = HashMap<String, String>()
        val iterator = (json as JsonObject).entrySet().iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            map[entry.key.toString()] = entry.value.asString
        }
        val placeFieldsDeserializer = OccupationChoicesDTO()
        placeFieldsDeserializer.map = map

        return placeFieldsDeserializer
    }

    override fun serialize(src: OccupationChoicesDTO, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        Log.d("", "sd")

     //   val element = context.serialize(src, typeOfSrc)
        val jsonObject = JsonObject();

        src.map!!.forEach {
            jsonObject.add(it.key, JsonPrimitive(it.value))

        }
        return jsonObject
    }
}
