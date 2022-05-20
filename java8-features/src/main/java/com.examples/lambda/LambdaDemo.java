package com.examples.lambda;

import com.google.common.collect.Lists;

import java.util.List;

public class LambdaDemo {
    Runnable r;
    @FunctionalInterface
    public static interface IntValues {
        int sum(int a, int b);
    }

    public int calc(int a, int b, IntValues vals){
        return vals.sum(a, b);
    }

    public static void main(String[] args){
        List<Integer> list= Lists.newArrayList(234,55,33466,2233,5);
        list.forEach(x->{//如果有多行，就必须用{}，只有一行可以省略
            System.out.print(x);
            System.out.print("\n");
        });
    }
}