package com.examples.iterators;

import com.examples.java8feature.StreamUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class IteratorDemo1 {
    private List<Number> list1= Lists.newArrayList(0,1,2,3,4,5,6,7,8,9);
    public void testIterator1(){
        Iterator<Number> it=new ListIterator2(list1, 2, 5);
        Streams.stream(it).forEach(System.out::println);
    }

//    public void stream_removeDemo1(){
//        List<Number> list=Lists.newArrayList(0,1,2,3,4,5,6,7,8,9);
//        for(java.util.ListIterator<Number> it = list.listIterator(); it.hasNext();){
//            it.remove();
//        }
//        System.out.println(list);
//    }

    public void testIteratorToStream1(){
        Iterator<Integer> it=Lists.newArrayList(1,2,3,4,5).iterator();
        Stream<Integer> stream= StreamUtils.streamOf(it);
        stream.parallel().forEach(System.out::println);

    }

    public static void main(String[] args){
        IteratorDemo1 demo1=new IteratorDemo1();
        demo1.testIterator1();
//        demo1.stream_removeDemo1();
        demo1.testIteratorToStream1();
    }
}
