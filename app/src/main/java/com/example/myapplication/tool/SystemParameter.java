package com.example.myapplication.tool;

public class SystemParameter{
    public static final String PATHURL = "http://www.skythinking.cn:7777";
    public static final String VERSION = "3";
    public static String TOKEN = "0";
    public static UserData USERINFO = new UserData();

    //用于handler处理信息的常量
    public static final Integer ERROR = 0x1000;
    public static final Integer NO_LOGIN = 0x1001;
    public static final Integer SUCCESS = 0x1002;
}
