package com.examples;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class OptionalTests {
    @Test
    public void ifPresentOrElse1(){
        Optional<String> op=Optional.of("ifPresentOrElse正常输出");
        op.ifPresentOrElse(System.out::println, ()->System.out.println("这句不会输出"));

        op=Optional.empty();
        op.ifPresentOrElse(System.out::println, ()->System.out.println("没值，所以执行els部分：ifPresentOrElse输出null"));

        op=Optional.ofNullable(null);
        op.ifPresentOrElse(System.out::println, ()->System.out.println("没值，所以执行else部分：ifPresentOrElse输出和上面一样"));
    }
}
