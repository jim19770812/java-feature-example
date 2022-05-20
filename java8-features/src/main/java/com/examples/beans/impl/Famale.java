package com.examples.beans.impl;

import com.examples.beans.Human;

public class Famale implements Human {
    @Override
    public String getName() {
        return "famale";
    }

    @Override
    public void talkTo(Human human) {
        System.out.println("与"+human.getName()+"对话（覆盖掉了default方法）");
    }

    public static void smoke(){
        System.out.println("smoke");
    }
}
