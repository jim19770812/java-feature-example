package com.examples.records;

import com.examples.records.beans.User;
import com.examples.records.beans.User2;

public class RecordsDemo1 {
    public static void main(String[] args) {
        User user = new User();
        // user.userId="jim"; //因为属性是final类型的，所以无法赋值
        // user.age=20
        System.out.println(user);
        User2 user2 = new User2("100", "张飞", "男", 20);
        System.out.println(user2);
        System.out.println(User2.staticValue);
    }
}
