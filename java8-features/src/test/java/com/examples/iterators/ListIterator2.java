package com.examples.iterators;

import lombok.NonNull;

import java.util.Iterator;
import java.util.List;

public class ListIterator2<E> implements Iterator<E> {
    private List<E> list;
    private int startIndex;
    private int length;
    private int index;

    public ListIterator2(@NonNull List<E> list, @NonNull Integer startIndex, @NonNull Integer length){
        this.list=list;
        this.startIndex=startIndex;
        this.length=length;
        this.index=startIndex;
    }
    @Override
    public boolean hasNext() {
        return this.index<=this.startIndex+length-1;
    }

    @Override
    public E next() {
        E result=this.list.get(this.index);
        this.index++;
        return result;
    }

    @NonNull
    public static <E> Iterator<E> of(@NonNull List<E> list, @NonNull Integer startIndex, @NonNull Integer length){
        return new ListIterator2<E>(list, startIndex, length);
    }
}
