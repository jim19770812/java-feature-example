package com.examples.java8feature;

import java.util.function.Supplier;

/**
 * 供应商方法，用于访问 x.get() 延迟调用方法获取结果
 */
public class SupplierDemo1 {
    public static <T> T orElseGet(T value, Supplier<? extends T> supplier) {
        return value != null ? value : supplier.get();
    }

    /**
     * 供应商方法
     */
    public static void supplierTest1(){
        System.out.println(orElseGet(null, ()->0));
        System.out.println(orElseGet(10, ()->0));
    }

    public static void main(String[] args){
        supplierTest1();
    }
}
