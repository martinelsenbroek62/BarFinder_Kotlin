package com.lemust.repository.models.serializer;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class NullSerializer extends TypeAdapter<String> {
    private final TypeAdapter<String> defaultAdapter;

    NullSerializer(TypeAdapter<String> defaultAdapter) {
        this.defaultAdapter = defaultAdapter;
    }

    @Override
    public void write(JsonWriter out, String value) throws IOException {
        Log.d("sds", "Sds");
    }

    @Override
    public String read(JsonReader in) throws IOException {
        return defaultAdapter.read(in);
    }
}
