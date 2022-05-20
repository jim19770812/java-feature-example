package com.examples.records.beans;

public record User(String userId, String name, String gender, Integer age) {
    public User() {
        this("", "", "", 0);
    }
}
