package com.lemust.repository.models.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lemust.repository.models.rest.place_details.PlaceFields;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlaceFieldsDeserializer implements JsonDeserializer<PlaceFields> {
    @Override
    public PlaceFields deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        HashMap<String, String> map = new HashMap<String, String>();
        Iterator<Map.Entry<String, JsonElement>> iterator = ((JsonObject) json).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            String value = entry.getValue().getAsString();
            if (value != null)
                map.put(entry.getKey(), value);
            else
                map.put(entry.getKey(), "");


        }
        PlaceFields placeFieldsDeserializer = new PlaceFields();
        placeFieldsDeserializer.setMap(map);

        return placeFieldsDeserializer;
    }


}
