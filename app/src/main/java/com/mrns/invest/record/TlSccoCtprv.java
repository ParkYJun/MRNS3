package com.mrns.invest.record;

import android.content.Context;

import com.mrns.main.R;

import java.util.ArrayList;

//@Table
public class TlSccoCtprv {

    long Objectid;
    String CtprvnCd;
    String CtpEngNm;
    String CtpKorNm;

    public TlSccoCtprv() {
    }

    public TlSccoCtprv(String data) {
        if(data == null) {
            return;
        }
        this.setCtprvnCd(data.split("@")[0]);
        this.setCtpKorNm(data.split("@")[1]);
    }
    public static ArrayList<TlSccoCtprv> getResArray(Context context) {
        ArrayList<TlSccoCtprv> mReslist = new ArrayList<TlSccoCtprv>();
        String[] mArray = context.getResources().getStringArray(R.array.ctprvn_array);
        for (String data : mArray) {
            mReslist.add(new TlSccoCtprv(data));
        }
        return mReslist;
    }
    public static ArrayList<String> ToStringArrayList(Context context) {
        String[] mArray = context.getResources().getStringArray(R.array.ctprvn_array);
        ArrayList<String> mResArrayList = new ArrayList<String>();
        for (int i = 0; i < mArray.length; i++) {
            mResArrayList.add(mArray[i].split("@")[1]);
        }
        return mResArrayList;
    }
    public long getObjectid() {
        return Objectid;
    }

    public void setObjectid(long objectid) {
        Objectid = objectid;
    }

    public String getCtprvnCd() {
        return CtprvnCd;
    }

    public void setCtprvnCd(String ctprvnCd) {
        CtprvnCd = ctprvnCd;
    }

    public String getCtpEngNm() {
        return CtpEngNm;
    }

    public void setCtpEngNm(String ctpEngNm) {
        CtpEngNm = ctpEngNm;
    }

    public String getCtpKorNm() {
        return CtpKorNm;
    }

    public void setCtpKorNm(String ctpKorNm) {
        CtpKorNm = ctpKorNm;
    }
}
