package com.mrns.utils;

import android.content.Context;
import android.graphics.Color;

import com.mrns.invest.util.StringUtil;
import com.mrns.main.R;

public class CommonUtils {
    private Context mContext;

    public CommonUtils(Context context) {
        mContext = context;
    }

    public String changeNullValue(String strValue, String substitutionValue) {
        return null == strValue || strValue.equals("null") || strValue.equals("NULL") || strValue.equals("Null") ? substitutionValue : strValue;
    }

    public String changeNullValue(String strValue) {
        return null == strValue || strValue.equals("null") || strValue.equals("NULL") || strValue.equals("Null") ? "" : strValue;
    }

    public String getInqSttus(String sttus){
        String returnStr = "";
        Context context;

        switch (sttus) {
            case "0":
                returnStr = "조사전";
                break;
            case "1":
                returnStr = "조사중";
                break;
            case "2":
                returnStr = "조사완료";
                break;
            case "3":
                returnStr = "전송완료";
                break;
        }

        return returnStr;
    }

    public String getPtclma(String sttus){
        String returnStr;

        switch (sttus) {
            case "001":
                returnStr = "양호";
                break;
            case "002":
                returnStr = "보통";
                break;
            case "003":
                returnStr = "불가";
                break;
            case "004":
                returnStr = "기타";
                break;
            default:
                returnStr = "-";
                break;
        }

        return returnStr;
    }

    public String getMtrSttus(String sttus, boolean wide) {
        String returnStr;
        sttus = StringUtil.nullCheck(sttus);

        switch (sttus) {
            case "001":
                returnStr = "완전";
                break;
            case "002":
                returnStr = "망실";
                break;
            case "003":
                if (wide) {
                    returnStr = "조사불가";
                } else {
                    returnStr = "조사\n불가";
                }
                break;
            case "004":
                returnStr = "기타";
                break;
            default:
                returnStr = "-";
                break;
        }

        return returnStr;
    }

    public int getPointKndRes(String pointKindCode){
        int pointKindResId = 0;

        switch (pointKindCode) {
            case "201":
                pointKindResId = R.drawable.icon_point_kind_201;
                break;
            case "301":
                pointKindResId = R.drawable.icon_point_kind_301;
                break;
            case "401":
                pointKindResId = R.drawable.icon_point_kind_401;
                break;
            case "302":
                pointKindResId = R.drawable.icon_point_kind_302;
                break;
            case "402":
                pointKindResId = R.drawable.icon_point_kind_402;
                break;
            case "101":
                pointKindResId = R.drawable.icon_point_kind_101;
                break;
            case "102":
                pointKindResId = R.drawable.icon_point_kind_102;
                break;
            case "103":
                pointKindResId = R.drawable.icon_point_kind_103;
                break;
            case "104":
                pointKindResId = R.drawable.icon_point_kind_104;
                break;
            case "105":
                pointKindResId = R.drawable.icon_point_kind_105;
                break;
            case "106":
                pointKindResId = R.drawable.icon_point_kind_106;
                break;
            case "107":
                pointKindResId = R.drawable.icon_point_kind_107;
                break;
            case "108":
                pointKindResId = R.drawable.icon_point_kind_108;
                break;
            case "109":
                pointKindResId = R.drawable.icon_point_kind_109;
                break;
            case "501":
                pointKindResId = R.drawable.icon_point_kind_501;
                break;
            case "502":
                pointKindResId = R.drawable.icon_point_kind_502;
                break;
            case "503":
                pointKindResId = R.drawable.icon_point_kind_503;
                break;
            case "504":
                pointKindResId = R.drawable.icon_point_kind_504;
                break;
        }

        return pointKindResId;
    }

    public String getPointsKind(String points_knd){
        String pKid = "";

        switch (points_knd) {
            case "201":
                pKid = "지적삼각점";
                break;
            case "301":
                pKid = "지적삼각보조점";
                break;
            case "401":
                pKid = "도근점";
                break;
            case "302":
                pKid = "자체지적삼각보조점";
                break;
            case "402":
                pKid = "자체지적도근점";
                break;
        }

        return pKid;
    }

    public int getColorInqSttus(String sttus){
        int resColor = 0;

        switch (sttus) {
            case "0":
                resColor = Color.GRAY;
                break;
            case "1":
                resColor = Color.RED;
                break;
            case "2":
                resColor = Color.BLUE;
                break;
            case "3":
                resColor = Color.GREEN;
                break;

        }

        return resColor;
    }
}
