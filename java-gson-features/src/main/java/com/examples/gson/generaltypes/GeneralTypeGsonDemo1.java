package com.examples.gson.generaltypes;

import com.examples.gson.generaltypes.impl.CatImpl;
import com.examples.gson.generaltypes.impl.DogImpl;
import com.examples.gson.generaltypes.typeadapters.AnimalEntityTypeAdapter;
import com.examples.gson.generaltypes.typeadapters.AnimalTypeAdapter;
import com.examples.gson.generaltypes.typeadapters.CatTypeAdapter;
import com.examples.gson.generaltypes.typeadapters.DogTypeAdapter;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

public class GeneralTypeGsonDemo1 {
    public static void test1(){
        AnimalHome home=new AnimalHome();
        home.setName("动物之家");
        AnimalEntity cat=AnimalEntity.builder().price(2000).animal(CatImpl.builder().name("小咪").type("cat").color("yellow").build()).build();
        AnimalEntity dog=AnimalEntity.builder().price(1000).animal(DogImpl.builder().name("小旺").type("dog").childrenCount(0).build()).build();
        home.setEntities(Lists.newArrayList(cat, dog));
        System.out.println(home);
        GsonBuilder gsonBuilder=new GsonBuilder()
                .registerTypeAdapter(CatImpl.class, new CatTypeAdapter())
                .registerTypeAdapter(DogImpl.class, new DogTypeAdapter());

        TypeAdapter ta=gsonBuilder.create().getAdapter(CatTypeAdapter.class);
        System.out.println(ta);
        Gson gson=gsonBuilder
                .setPrettyPrinting()
                .registerTypeAdapter(AnimalEntity.class, new AnimalEntityTypeAdapter(gsonBuilder.create()))
                .registerTypeAdapter(Animal.class, new AnimalTypeAdapter(gsonBuilder.create()))
                .create();
        String t=gson.toJson(home);
        System.out.println(t);
        AnimalHome h2=gson.fromJson(t, AnimalHome.class);
        System.out.println(h2);
    }
    public static void main(String[] args){
        test1();
    }
}
