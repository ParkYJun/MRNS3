package com.mrns.invest.util;

import java.util.regex.Pattern;

public class StringUtil {
    public static String nullCheck(String str) {
        if(str == null || str.equalsIgnoreCase("null"))
            return "";
        else
            return str;
    }

    public static String nullCheck(String str, String replaceStr) {
        if(str == null || str.equalsIgnoreCase("null"))
            return replaceStr;
        else
            return str;
    }

    //직급체크
    public static String convertClassification(String classification) {
        if (Pattern.compile("[0-9]{2}").matcher(classification).matches()) {
            return DBUtil.getCmmnCode("150", classification);
        } else {
            return classification;
        }
    }
}
