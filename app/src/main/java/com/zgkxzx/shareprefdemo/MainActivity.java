package com.zgkxzx.shareprefdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvContent = (TextView)findViewById(R.id.tv_content);


        StudentSDO.putAddress(this,"shenZhen");

        StudentSDO.putAddress(this,"wuhan","123");

        String address = StudentSDO.getAddress(this);


        Student student = new Student();
        student.address = "Hubei wuhan";
        student.age = 20;
        student.name = "zhao si";

        TeacherSDO.putName(this,"zhang san");
        TeacherSDO.putAddress(this,"guan dong");
        TeacherSDO.putStudent(this,student);

        Teacher teacher = TeacherSDO.get(this);

        tvContent.setText(address+" --- "+teacher.toString());


    }
}
