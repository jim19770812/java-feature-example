package com.examples.functionalInterface;

public class FunctionalInterfaceDemo {
    @FunctionalInterface
    public static interface HelloFunction {
        String hello(String name);
        String toString();//函数接口除了toString，equals外只能有一个方法
    }

    public void test1(){
        HelloFunction t=p->p+="world";//这里相当于写了个匿名类，然后实现了HelloFunction接口
        HelloFunction t1=p->{//多行代码的lambda表达式
            p+="world";
            p+="!";
            return p;
        };
        System.out.println(t.hello("hello, "));//通过t实例调用hello方法，并给name型参赋值
        System.out.println(t1.hello("jim, "));
    }

    public static void main(String[] args){
        FunctionalInterfaceDemo fd=new FunctionalInterfaceDemo();
        fd.test1();
    }
}