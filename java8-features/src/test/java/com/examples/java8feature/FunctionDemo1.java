package com.examples.java8feature;

import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionDemo1 {
    private static Integer functionTest1_run(String val, Function<String, Integer> function, Consumer<String> successAction, Consumer<String> failAction){
        try{
            Integer result=function.apply(val);
            successAction.accept(val);
            return result;
        }catch (Exception e){
            failAction.accept(val);
            return -1;
        }
    }

    public static void functionTest1(){
        Function<String, Integer> f=(s)->Integer.parseInt(s); /*将字符串转换成数字*/
        Consumer<String> succ=(s)->System.out.println("succ:"+s);
        Consumer<String> fail=(s)->System.out.println("fail:"+s+"不是数字");
        functionTest1_run("1000", f, succ, fail);
        functionTest1_run("", f, succ, fail);
        functionTest1_run("test", f, succ, fail);
    }
    public static void main(String[] args){
        functionTest1();
    }
}
