package com.examples.gson.generaltypes.typeadapters;

import com.examples.gson.generaltypes.Animal;
import com.examples.gson.generaltypes.impl.CatImpl;
import com.examples.gson.generaltypes.impl.DogImpl;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class AnimalTypeAdapter extends TypeAdapter<Animal> {
    private final Gson gson;
    public AnimalTypeAdapter(Gson gson){
        this.gson=gson;
    }

    @Override
    public void write(JsonWriter out, Animal value) throws IOException {
        out.beginObject();
        if (value instanceof CatImpl){
            gson.getAdapter(CatImpl.class).write(out, (CatImpl)value);
        }else if (value instanceof DogImpl){
            gson.getAdapter(DogImpl.class).write(out, (DogImpl)value);
        }else{
            throw new RuntimeException();
        }
        out.endObject();
    }

    @Override
    public Animal read(JsonReader in) throws IOException {
        in.beginObject();
        String type=in.nextName();//首个元素一定要是type，不然后面没法区分了，这点切记！
        Animal result=null;
        TypeAdapter<?> typeAdapter=null;

        while(in.hasNext()){
            switch(in.nextName()){
                case "type":{
                    type=in.nextString();
                    if ("cat".equals(type)){
                        typeAdapter= new CatTypeAdapter();
                    }else if ("gog".equals(type)){
                        typeAdapter= new DogTypeAdapter();
                    }else{
                        throw new RuntimeException();
                    }
                }break;
                default:{

                }
            }
        }
        if ("cat".equals(type)){
            CatImpl.CatImplBuilder builder=CatImpl.builder();
            builder.type(in.nextString())
                    .name(in.nextString());
        }
        in.endObject();
        return null;
    }
}
