package com.examples.java8feature;

import java.util.StringJoiner;

public class StringJoinerDemo1 {
    public static void test1(){
        StringJoiner sj=new StringJoiner(",", "[", "]");
        sj.add("a");
        sj.add("b");
        System.out.println(sj.toString());
    }

    public static void test2(){
        StringJoiner sj=new StringJoiner(",", "[", "]");
        StringJoiner sj1=new StringJoiner("," );
        sj1.add("1");
        sj1.add("2");
        sj1.add("3");
        sj.merge(sj1);
        StringJoiner sj2=new StringJoiner("," );
        sj2.add("4");
        sj2.add("5");
        sj2.add("6");
        sj.merge(sj2);
        System.out.println(sj.toString());
    }

    public static void main(String args[]){
        test1();
        test2();
    }
}
