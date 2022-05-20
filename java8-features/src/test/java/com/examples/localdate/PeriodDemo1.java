package com.examples.localdate;

import java.time.LocalDate;
import java.time.Period;

/**
 * 时间段测试
 */
public class PeriodDemo1 {
    public static void main(String[] args){
        LocalDate ld1=LocalDate.now();
        LocalDate ld2=LocalDate.of(2021, 10, 1);
        Period pr=Period.between(ld1, ld2);
        System.out.printf("距离2021年国庆节还有%d天", pr.getDays());
    }
}
