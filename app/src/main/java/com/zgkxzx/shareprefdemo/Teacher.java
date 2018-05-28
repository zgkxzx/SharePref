package com.zgkxzx.shareprefdemo;

import com.zgkxzx.shareprefprocess.SharePref;

/**
 * @author: zhaoxiang
 * @create: 2018/5/27 23:25
 * @description:
 */
public class Teacher {

    @SharePref("zgkxzx")
    String name;


    @SharePref("China")
    String address;

    @SharePref
    Student student;



    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", student=" + student +
                '}';
    }
}
