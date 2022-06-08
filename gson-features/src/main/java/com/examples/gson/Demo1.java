package com.examples.gson;

import com.examples.gson.typeadapter.GsonDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Demo1 {
    @Getter @Setter
    @Accessors(chain = true)
    @ToString
    public static class User1{
        @Expose
        private String name;
        @Expose
        private Integer age;
        @Expose
        private String gender;
    }

    @Getter @Setter
    @Accessors(chain = true)
    @ToString
    public static class User2{
        @Expose
        private String name;
        @Expose
        private Integer age2;
        @Expose
        private String gender;
    }

    @SneakyThrows
    public static void main(String[] args){
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
//        builder.registerTypeAdapter(TUser2.class, new TUser2Deserializer());
//        builder.registerTypeAdapter(TWife2.class, new TWife2Deserializer());
        builder.registerTypeAdapter(Date.class, new GsonDateTypeAdapter());
        Gson gson=builder.create();
        String s="";
        Object o=null;
        s=gson.toJson(new Date());
        System.out.println(s);
        o=gson.fromJson(s, Date.class);
        System.out.println(o);
        s=gson.toJson(new BigDecimal("200.211"));
        System.out.println(s);
        System.out.println(gson.fromJson(s, BigDecimal.class));
        Map<String, Object> map=new HashMap<>(100);
//        {
//            "doorFrameHeight": 1200.0,
//                "doorFrameWidth": 800.0,
//                "doorFrameThickness": 2.0,
//                "drawingNumA": "A1012101010",
//                "drawingNumB": "B1012101011"
//        }
        map.put("doorFrameHeight", 1200.0);
        map.put("doorFrameWidth", 800.0);
        map.put("doorFrameThickness", 2.0);
        map.put("drawingNumA", "A1012101010");
        map.put("drawingNumB", "B1012101011");
        String ss=gson.toJson(map);
        System.out.println(ss);


    }
}
