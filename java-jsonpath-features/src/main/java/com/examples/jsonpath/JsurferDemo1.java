package com.examples.jsonpath;

import com.example.utils.DT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.jsfr.json.*;
import org.jsfr.json.provider.GsonProvider;

public class JsurferDemo1 {
    public static String json1(){
        JsonObject root=new JsonObject();
        root.addProperty("name", "吕布");
        root.addProperty("gender", "男");
        root.addProperty("age", 20);
        root.addProperty("salary", 20000);
        root.addProperty("salary", 500);
        root.addProperty("joinDate", DT.ofNow().dateTimeStr());
        return root.toString();

    }

    public void test1(@NonNull Gson gson){
        String json=json1();
        System.out.println(json);
        JsonSurfer js=new JsonSurfer(GsonParser.INSTANCE, GsonProvider.INSTANCE);
        Collector col=js.collector(json);
        ValueBox<String> valName=col.collectOne("$.name", String.class);
        col.exec();
        System.out.println(valName.get());
    }

    @SneakyThrows
    public static void main(String[] args){
        Gson gson= new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        JsurferDemo1 demo1=new JsurferDemo1();
        demo1.test1(gson);
    }
}
