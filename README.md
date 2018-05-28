## SharePref 自动生成sharepefence方法

### 简介

sharePref 是一个能自动生成存储sharePeference 一系列方法的库，方便程序员开发，减少模板型代码浪费大量时间。

### 使用步骤

#### 1.添加arr库到app gradle

	compile files('libs/sharePref-1.0.jar')

#### 2.把需要存储的字段加上注解

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

    }

#### 3.重新构建下工程


#### 4.直接使用生成的工具类
	
	StudentSDO.putAddress(this,"shenZhen");

    String address = StudentSDO.getAddress(this);


    Student student = new Student();
    student.address = "Hubei wuhan";
    student.age = 20;
    student.name = "zhao si";

    TeacherSDO.putName(this,"zhang san");
    TeacherSDO.putAddress(this,"guan dong");
    TeacherSDO.putStudent(this,student);



### 测试数据

	
### 反馈

You have any questions to send email to me.Thanks.

* e-mail : zgkxzx@163.com
* gmail  : my.own.diy@gmail.com

### 我的思想

#### Simplicity is not simple.
	
