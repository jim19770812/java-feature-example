package com.examples.web.service.impl;

import com.examples.web.service.Demo1;
import org.springframework.stereotype.Component;

@Component
public class Demo1Impl implements Demo1 {
    @Override
    public String sayHello(String name) {
        return "hi, "+ name;
    }

    @Override
    public String getName() {
        return "demo1";
    }
}
