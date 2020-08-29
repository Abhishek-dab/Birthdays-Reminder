package com.example.internshipproject;

public class pojo_new {
String name, day, month,phone ,image,bride;


    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public String getBride() {
        return bride;
    }

    public pojo_new(String mname, String mday, String mmonth, String ph, String imageuri, String bridea) {
        name = mname;
        day = mday;
        month = mmonth;
        phone = ph;
        image = imageuri;
        bride = bridea;

    }

    public pojo_new (){

    }
}
