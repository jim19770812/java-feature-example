package com.examples.records.beans;

public record User2(String userId, String name, String gender, Integer age) {
    public static String staticValue = "静态值";
}