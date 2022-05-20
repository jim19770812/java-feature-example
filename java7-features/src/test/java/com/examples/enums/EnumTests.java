package com.examples.enums;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EnumTests {
    public String getEnumString(Enum value){
        return value.toString();
    }

    @Test
    public void test1(){
        System.out.println(GenderTypeEnum.FAMLE.desc);
        System.out.println(getEnumString(GenderTypeEnum.FAMLE));
        System.out.println(getEnumString(RGBTypeEnum.BLUE));
    }

    @Test
    public void valueOfTest1(){
        GenderTypeEnum type=GenderTypeEnum.valueOf("FAMLE");
        System.out.println(type + ":" + type.desc);
    }
}
