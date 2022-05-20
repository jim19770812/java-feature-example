package com.examples;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;

@SpringBootTest
public class DateFormatUtilsTests {
    @Test
    @SneakyThrows
    public void test1(){
        String s= DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date());
        System.out.println(s);
        Date dt=DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse("2021-04-05");
        System.out.println(dt);
        Date dt2=DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse("2021-10-10T10:23:15ZZ");
        System.out.println(dt2);
    }
}
