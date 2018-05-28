package com.zgkxzx.shareprefdemo;

import com.zgkxzx.shareprefprocess.SharePref;

/**
 * @author: zhaoxiang
 * @create: 2018/5/27 23:22
 * @description:
 */
public class Student {

    @SharePref("zgkxzx")
    String name;

    @SharePref("26")
    int age;

    @SharePref("0301412")
    String no;

    @SharePref
    String address;


    String password;


    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", no='" + no + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
