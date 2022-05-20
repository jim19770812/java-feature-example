package com.examples.java8feature;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ConsumerDemo1 {
    public <T>void nullConsumer(T value, Consumer<? super T> notNullAction, Consumer<T> nullAction){
        if (Objects.nonNull(value)){
            notNullAction.accept(value);
        }else{
            nullAction.accept(value);
        }
    }
    public void test1(){
        Consumer<Integer> nullConsumer=(x)->{
            System.out.println("是空");
        };
        Consumer<Integer> notNullConsumer=(x)->{
            System.out.println("非空");
        };
        nullConsumer(10, notNullConsumer, nullConsumer);
        nullConsumer(100, notNullConsumer, nullConsumer);
        nullConsumer(null, notNullConsumer, nullConsumer);
    }

    public <T>void forEath(List<T> list, Consumer<T> consumer1, Consumer<T> consumer2, Consumer<T> consumer3){
        if (Objects.isNull(list) || list.isEmpty())
            return;
        for(T t : list){
            /**
             * 这段代码和下面的链式代码是等价的
             * Consumer<T> consumer=null;
             * consumer=consumer1.andThen(consumer2);
             * consumer=consumer.andThen(consumer3);
             * consumer.accept(t);
             */
            consumer1.andThen(consumer2).andThen(consumer3).accept(t);
            System.out.println("");
        }
    }

    public <T>void ifPresent(T object, Consumer<T> thenAction, Consumer<T> elseAction){
        if (Objects.nonNull(object)){
            thenAction.accept(object);
        }else{
            elseAction.accept(object);
        }
    }

    public void test2(){
        Consumer<Integer> c1=(x)->{
            System.out.println("Consumer1 "+x);
        };
        Consumer<Integer> c2=(x)->{
            System.out.println("Consumer2 "+x);
        };
        Consumer<Integer> c3=(x)->{
            System.out.println("Consumer3 "+x);
        };
        List<Integer> list= Lists.newArrayList(1,2,3,4);
        forEath(list, c1, c2, c3);
    }

    public void test3(){
        Consumer<Integer> cThen=(x)->{
            System.out.println("传入："+x+"，not null，正常处理");
        };
        Consumer<Integer> cElse=(x)->{
            System.out.println("传入："+x+"，is null，对空值做特殊处理");
        };

        ifPresent(null, cThen, cElse);
        ifPresent(100, cThen, cElse);
    }
    public static void main(String[] args){
        ConsumerDemo1 demo=new ConsumerDemo1();
//        demo.test1();
        demo.test2();
//        demo.test3();;
    }
}
