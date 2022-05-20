package com.examples.java8feature;

import com.examples.beans.impl.Famale;
import com.examples.beans.impl.Male;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaDemo1 {
    @Data
    @AllArgsConstructor
    private static class TempObj{
        private Integer value;
    }
    @FunctionalInterface
    public static interface Supplier<T>{
        T get();
    }

    public static class Car{
        public static Car create(Supplier<Car> supplier){
            return supplier.get();
        }

        public static void follow(Car car){
            System.out.println("follow card"+car.toString());
        }

        public void repair(){
            System.out.println("repaired"+this.toString());
        }
    }

    public void lambdaTests(){
        List<Integer> l1= Lists.newArrayList(6,293,5,33,9,13,7);
        Collections.sort(l1, (Integer s1, Integer s2)->s1.compareTo(s2));
        System.out.println(l1);
        System.out.println("l1.forEach((t)->System.out.println(t));");
        l1.forEach((t)->System.out.println(t));
        System.out.println("l1.forEach(System.out::println);");
        l1.forEach(System.out::println);
        int x=0;
        new Thread(()->System.out.println("hello")).start();
//        Demo1::add(t->System.out.println(t));
        //()->System.out.println("11");
        List<String> sList1=Lists.newArrayList("ddf", "add", "ab", "ee", "00");
        String[] stringsArray = {"Hello","World"};
        //使用lambda表达式和类型对象的实例方法
        System.out.println("Arrays.sort(stringsArray,(s1,s2)->s1.compareToIgnoreCase(s2));");
        Arrays.sort(stringsArray,(s1,s2)->s1.compareToIgnoreCase(s2));
        Arrays.asList(stringsArray).forEach((s)->System.out.println(s));
        System.out.println(stringsArray);
        //使用方法引用
        //引用的是类型对象的实例方法
        System.out.println("Arrays.sort(stringsArray, String::compareToIgnoreCase);");
        Arrays.sort(stringsArray, String::compareToIgnoreCase);
        Arrays.asList(stringsArray).forEach((s)->System.out.println(s));

        //String t=String::format("hello %s", "jim");
    }

    public void defaultTest(){
        Famale f=new Famale();
        Male m=new Male();
        m.talkTo(f);
        f.talkTo(m);

    }

    public static int add(int val){
        return val+1;
    }

    //方法引用
    public void methodReferenceTest(){
        Car car = Car.create(Car::new);

    }

    public static void main(String[] args){
//        Demo1 demo1=new Demo1();
//        demo1.lambdaTests();
//        demo1.defaultTest();
//        Map<String, String> map=new HashMap<>();
//        Map.Entry<String, String> entry =Maps.immutableEntry("1", "2");
//        map.entrySet().add(entry);
//        System.out.println(map);
//        Date dt=new Date(Long.MAX_VALUE);
//        System.out.println(dt);
        System.out.println(Lists.newArrayList(new TempObj(1), new TempObj(2)).stream().peek(o->o.setValue(o.getValue()*2)).collect(Collectors.toList()));
    }
}
