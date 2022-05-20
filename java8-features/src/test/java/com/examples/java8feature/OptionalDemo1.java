package com.examples.java8feature;

import java.util.Objects;
import java.util.Optional;

public class OptionalDemo1 {
    public void ofNullable1(){
        String val="hello";
        Optional.ofNullable(val).ifPresent(System.out::println);
        val=null;
        Optional.ofNullable(val).ifPresent(System.out::println);
    }

    public void ifPresent1(){
        Optional<String> op=Optional.empty();
        op.ifPresent(System.out::println);
        op=Optional.of("这句会正常输出");
        op.ifPresent(System.out::println);
    }

    public void orElse1(){
        Optional<String> op=Optional.empty();
        System.out.println(op.orElse("没有值")); //如果是empty就返回orElse的入参

        System.out.println(op.filter(o->o instanceof String).map(o->o+"abc").orElse("没有值2"));;
    }

    private String orElseGet1Supplier1(){
        return "没有值2";
    }

    public void orElseGet1(){
        Optional<String> op=Optional.empty();
        System.out.println(op.orElseGet(()->"没有值")); //和orElse类似，只是改成了一个lambda方法
        System.out.println(op.orElseGet(this::orElseGet1Supplier1)); //如果值是empty就调用一个供给方法获取
    }

    private Optional<String> orSupplier1(){
        return Optional.of("没有值");
    }

//    public void or1(){
//        Optional<String> op=Optional.empty();
//        System.out.println(op.or(()->Optional.of("hello"))); //如果是empty就返回参数部分 ()->Optionl.of(?) 的结果
//        System.out.println(op.or(this::orSupplier1)); //如果值是empty就返回一个可以返回Optional的方法的结果
//    }

    public void filter1(){
        Optional<Object> o1=Optional.ofNullable(null).filter(o->o instanceof Integer);
        System.out.println("o1 "+ (o1.isPresent()?o1.get():"None"));
        o1 = Optional.ofNullable("hello").filter(Objects::nonNull).map(o->(Object)o).filter(o -> "Integer".equals(o.getClass().getName())).map(o->(Object)o);
        System.out.println("o1 " + (o1.isPresent() ? o1.get() : "None"));
    }

    public static void main(String[] args){
        OptionalDemo1 demo1=new OptionalDemo1();
        // demo1.ofNullable1();
        // demo1.ifPresent1();
        // demo1.ifPresentOrElse1();
        demo1.orElse1();
        // demo1.orElseGet1();
        // demo1.or1();
        // demo1.filter1();
    }
}
