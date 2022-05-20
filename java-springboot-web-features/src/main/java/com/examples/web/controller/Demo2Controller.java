package com.examples.web.controller;

import org.springframework.web.bind.annotation.*;

@RestController//u相当于@Controller+@ResponseBody标签结合使用，@Controller需要渲染页面，@ResponseBody定义后会返回json数据不需要渲染页面
@RequestMapping(value = "demo1",method= RequestMethod.GET)
public class Demo2Controller {
    @GetMapping("/v2/echo/{name}")//curl http://localhost:8080/demo1/v2/echo/name=jim
    public String hello(@PathVariable("name") String name){
        return "hello " + name+"\n";
    }

    @GetMapping("v2/hello1")//curl http://localhost:8080/demo1/v2/hello1?name=jim
    public String hello1(@RequestParam(name = "name", required=true) String name){
        return "hello " + name+"\n";
    }

    @GetMapping("/v2/hello")//curl http://localhost:8080/demo1/v2/hello
    public String hello(){
        return "hello, here1\n";
    }

    @PutMapping("/v2/puthello/{name}")//curl -v -X PUT -d "age=20" http://localhost:8080/demo1/v2/puthello/jim
    public String putHello(@PathVariable("name") String name, @RequestParam("age")int age){
//    public String putHello(@PathVariable("name") String name){
        return "hello "+name;
    }

    @PostMapping("/v2/posthello/{name}")//curl -X POST -d "" http://localhost:8080/demo1/v2/posthello/jim
    public String postHello(@PathVariable("name") String name){
        return "hello" + name;
    }
}