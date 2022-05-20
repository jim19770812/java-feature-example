package com.examples.java8feature;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Function.identity()的例子
 * 是lambda  t->t 的另一种写法
 */
public class FunctionIdentityDemo1 {
    public void identifyTest1(){
        List<Integer> list= Lists.newArrayList(1,2,3,4,5);
        List<Integer> list2=list.stream().map(Function.identity()).collect(Collectors.toList());
        System.out.println(list2);
    }

    public void identifyTest2(){
        List<Integer> list= Lists.newArrayList(1,2,3,4,5);
        Map<Integer, Integer> map1=list.stream().map(Function.identity()) //等价于 t->t
                .map(t->t) //等价于 Function.identity()
                .collect(Collectors.toMap(Function.identity(), v->v*10)); //前面的Function.identify()没法做计算，比如Function.identify()*2是不行的
        System.out.println(map1);
        //{1=10, 2=20, 3=30, 4=40, 5=50}
    }

    public static void main(String[] args){
        FunctionIdentityDemo1 demo1=new FunctionIdentityDemo1();
        //demo1.identifyTest1();
        demo1.identifyTest2();
    }
}
