## SharePref 一种自动生成SharePreference方法工具

### 前言

在Android编程中，轻量级的存储一般用SharePreference 来实现简单数据的存储，比如第一次启动App标记，上次广告的显示时间戳，本地用户名记录等等，一般通过SharePreference来存储，一般通过key-value形式来标记存储每个字段，但是有个问题，随着项目的规模增大，这种轻量级的存储也越来越多，
开发者在项目中还要维持一张很大的Key键值表，需要定义很多这样的Key值和重写大量的这种模版样式的方法，这样导致很大部分开发时间花费在这种无用功上面，得不偿失...


### 简介

    SharePref 是一个能自动生成存储SharePreference 一系列方法的库，方便程序员开发，减少模板型代码编码浪费大量时间。

### 使用步骤

#### 1.添加arr库到app gradle

	compile(name: 'sharepref-release', ext: 'aar')

#### 2.把需要存储类的字段加上注解

	public class Student {

        //默认值是zgkxzx
        @SharePref("zgkxzx")
        String name;

        //默认值是26
        @SharePref("26")
        int age;

        //默认值是0301412
        @SharePref("0301412")
        String no;

        @SharePref
        String address;

        String password;

    }

#### 3.重新构建下工程

<img src="https://github.com/zgkxzx/SharePref/blob/master/screenshot/1.png" width = "505" height = "33" alt="截屏" align=center />

#### 4.生成的工具类

<img src="https://github.com/zgkxzx/SharePref/blob/master/screenshot/2.png" width = "457" height = "263" alt="截屏" align=center />

#### 5.直接使用生成的工具类
	
	StudentSDO.putAddress(this,"shenZhen");
	//加入后缀标签
	StudentSDO.putAddress(this,"wuhan","123");

    String address = StudentSDO.getAddress(this);


    Student student = new Student();
    student.address = "Hubei wuhan";
    student.age = 20;
    student.name = "zhao si";

    TeacherSDO.putName(this,"zhang san");
    TeacherSDO.putAddress(this,"guan dong");
    TeacherSDO.putStudent(this,student);

### 测试数据

<img src="https://github.com/zgkxzx/SharePref/blob/master/screenshot/3.png" width = "549" height = "221" alt="截屏" align=center />


### 实现规则说明

- 在Android编程中，轻量级的存储一般用SharePreference 来实现简单数据的存储，比如第一次启动App标记，上次广告的显示时间戳，本地用户名记录等等，
一般通过SharePreference来存储，一般通过key-value形式来标记存储每个字段，但是有个问题，随着项目的规模增大，这种轻量级的存储也越来越多，
开发者在项目中还要维持一张很大的Key键值表，需要定义很多这样的Key值和重写大量的这种模版样式的方法，这样导致很大部分开发时间花费在这种无用功上面，得不偿失...

- 通过SharePref功能，开发者只需要通过在字段上面添加注解，重新构建后，工程自动帮忙生成完备的SharePreference的方法，而且Key值不需要开发者维护，自动按照约定规则生成

#### 生成规则：

- 生成的工具类类名

        类名+SDO

- 生成的键值名

        _类名_变量名_{后缀标签}


    例如：Student类的name成员变量，默认情况下，在SharePeference的存储键值是_student_name

    如果有多个类似的实例存储，可以通过加后缀标签来进行存储。

    （注：不支持存储表，大量表数据一般用数据库而不是SharePeference）

### 反馈

有什么建议或者问题可直接发邮件给我，谢谢~~

* e-mail : zgkxzx@163.com
* gmail  : my.own.diy@gmail.com

### 思想

- 约定大于配置
- 简单并不简单
