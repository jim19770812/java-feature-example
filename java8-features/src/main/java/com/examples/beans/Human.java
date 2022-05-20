package com.examples.beans;

public interface Human {
    String getName();
    default void talkTo(Human human){
        System.out.println("talk to "+this.getName());
    }
}
