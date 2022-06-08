package com.examples.java8feature;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 流工具类
 */
public class StreamUtils {
    /**
     * 迭代器转换成串行流
     * @param iterator
     * @param <T>
     * @return
     */
    public static <T> Stream<T> streamOf(Iterator<T> iterator){
        Stream<T> result = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.SORTED), false);
        return result;
    }

    /**
     * 迭代器转换成并行流
     * @param iterator
     * @param <T>
     * @return
     */
    public static <T> Stream<T> parallelStreamOf(Iterator<T> iterator){
        Stream<T> result = StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.SORTED), true);
        return result;
    }
}
