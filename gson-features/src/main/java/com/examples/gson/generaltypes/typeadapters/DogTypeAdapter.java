package com.examples.gson.generaltypes.typeadapters;

import com.examples.gson.generaltypes.impl.DogImpl;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class DogTypeAdapter extends TypeAdapter<DogImpl> {
    @Override
    public void write(JsonWriter out, DogImpl value) throws IOException {
        out.beginObject();
        out.name("type").value(value.getType())
            .name("name").value(value.getName())
            .name("childrenCount").value(value.getChildrenCount());
        out.endObject();
    }

    @Override
    public DogImpl read(JsonReader in) throws IOException {
        DogImpl.DogImplBuilder builder=DogImpl.builder();
        //in.beginObject(); 不需要执行，因为外部已经执行过了
        while(in.hasNext()){
            switch(in.nextName()){
                case "type": builder.type(in.nextString());break;//这句不会执行，因为外部已经处理过type了
                case "name": builder.name(in.nextString());break;
                case "childrenCount": builder.childrenCount(in.nextInt());break;
                default:break;
            }
        }
        //in.endObject(); 不需要执行，需要在外部执行
        return builder.build();
    }
}
