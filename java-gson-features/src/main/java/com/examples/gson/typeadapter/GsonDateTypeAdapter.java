package com.examples.gson.typeadapter;

import org.jim.utils.DT;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.SneakyThrows;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

public class GsonDateTypeAdapter extends TypeAdapter<Date> {
//    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
//        @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
//        @Override
//        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
//            return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new DateTypeAdapter() : null;
//        }
//    };

    @Override
    @SneakyThrows
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        Optional<DT> dt=DT.of_yyyy_MM_dd_HH_mm_ss(in.nextString());
        Assert.isTrue(dt.isPresent());
        return dt.get().date();
    }

    @Override
    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String dateFormatAsString = DT.ofDate(value).format_yyyy_MM_dd_HH_mm_ss();
        out.value(dateFormatAsString);
    }
}