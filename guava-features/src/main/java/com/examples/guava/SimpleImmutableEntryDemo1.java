package com.examples.guava;

import com.google.common.collect.ImmutableMap;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleImmutableEntryDemo1 {
    public void simpleImmutableEntry1(){
        Map<String, String> map=ImmutableMap.<String, String>builder().put("k1", "v1").put("k2", "v2").put("k3", "v3").build();
        List<Map.Entry> entryList=map.entrySet().parallelStream().map(o-> new AbstractMap.SimpleImmutableEntry<String, String>(o.getKey(), o.getValue())).collect(Collectors.toList());
        System.out.println(entryList);
    }

    public static void main(String[] args){
        SimpleImmutableEntryDemo1 demo1=new SimpleImmutableEntryDemo1();
        demo1.simpleImmutableEntry1();
    }
}
