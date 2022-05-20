package com.examples.enums;

/**
 * 性别
 */
public enum GenderTypeEnum {
    MALE("男"),
    FAMLE("女");
    String desc;
    GenderTypeEnum(String desc){
        this.desc=desc;
    }
}
