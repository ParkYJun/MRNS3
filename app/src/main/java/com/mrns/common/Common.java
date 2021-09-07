package com.mrns.common;

import android.os.Environment;

import java.io.File;

/**
 * Created by eined on 2016. 1. 21..
 */
public class Common {
    public static int TIMEOUT = 30000;

    //--------------------------------------------------------------------------
    //  APP API URL
    //--------------------------------------------------------------------------
//    public static String SERVER_URL = "http://buffalo.wintermango.com:8080/WINTERMANGO";

    public static String SERVER_URL = "http://qa.wintermango.com:8080";

    // 현황조사 리스트
    public static String URL_INVEST_LIST = SERVER_URL + "/cpms-1.0.0/StdrSpot/List.do";

    // 코드 리스트
    public static String URL_CODE_LIST = SERVER_URL + "/cpms-1.0.0/Code/List.do";
    // 코드 상세
    public static String URL_CODE_DETAIL = SERVER_URL + "cpms-1.0.0/Code/Detail.do";

    //회원 로그인
    public static String URL_LOGIN = SERVER_URL + "/User/Login";

    public static String AUTH_KEY = "d2db7962-9551-443d-9440-f0c3bc960c67";


    public String getSDPath() {
        File extSt = Environment.getExternalStorageDirectory();
        String SDPath = extSt.getAbsolutePath();
        return SDPath;
    }
}
