package com.zgkxzx.shareprefdemo.bean;

import com.zgkxzx.shareprefprocess.SharePref;
import com.zgkxzx.shareprefprocess.Table;
//import com.fcbox.lib.wxpay.ApplicationName;

/**
 * @author: zhaoxiang
 * @create: 2018/5/27 23:22
 * @description:
 */
//@ApplicationName("com.zgkxzx.shareprefdemo")
@Table("PersonTable")
public class Person {

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
