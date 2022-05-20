package com.examples.jsurfer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.jim.utils.DT;
import org.jim.utils.ST;
import org.jsfr.json.*;
import org.jsfr.json.provider.GsonProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JsurferTests {
    private Gson gson;

    @BeforeEach
    public void initilaize(){
        this.gson=new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

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

    @Test
    public void test1(){
        String json=json1();
        System.out.println(json);
        JsonSurfer js=new JsonSurfer(GsonParser.INSTANCE, GsonProvider.INSTANCE);
        Collector col=js.collector(json);
        ValueBox<String> valName=col.collectOne("$.name", String.class);
        col.exec();
        System.out.println(valName.get());
        Assertions.assertEquals("吕布", valName.get());
    }
}
