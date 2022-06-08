package com.examples.gson.generaltypes.typeadapters;

import com.examples.gson.generaltypes.impl.CatImpl;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CatTypeAdapter extends TypeAdapter<CatImpl> {
    @Override
    public void write(JsonWriter out, CatImpl value) throws IOException {
        if (value==null){
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("type").value(value.getType())
            .name("name").value(value.getName())
            .name("color").value(value.getColor());
        out.endObject();
    }

    @Override
    public CatImpl read(JsonReader in) throws IOException {
        CatImpl.CatImplBuilder builder=CatImpl.builder();
        //in.beginObject(); 不需要执行，因为外部已经执行过了
        while(in.hasNext()){
            String name = in.nextName();
            switch(name){
                case "type": builder.type(in.nextString());break;//这句不会执行，因为外部已经处理过type了
                case "name": builder.name(in.nextString());break;
                case "color": builder.color(in.nextString());break;
                default:break;
            }
        }
        //in.endObject(); 不需要执行，需要在外部执行
        return builder.build();
    }
}
