package com.mrns.invest.record;

import android.content.Context;

import com.mrns.main.R;

import java.util.ArrayList;

//@Table
public class TlSccoSig {
    long Objectid;
    //@Ignore
    String CtprvnCd;
    String SigCd;
    String SigEngNm;
    String SigKorNm;

    public TlSccoSig() {
    }

    public TlSccoSig(String data) {
        if(data == null) {
            return;
        }
        this.setCtprvnCd(data.split("@")[0]);
        this.setSigCd(data.split("@")[1]);
        this.setSigKorNm(data.split("@")[2]);
    }
    public static ArrayList<TlSccoSig> getResArray(Context context, String ctprvnCd) {
        ArrayList<TlSccoSig> mReslist = new ArrayList<TlSccoSig>();
        String[] mArray = context.getResources().getStringArray(R.array.sig_array);
        for (String data : mArray) {
            TlSccoSig sig = new TlSccoSig(data);
            if(sig.getCtprvnCd().equalsIgnoreCase(ctprvnCd))
                mReslist.add(sig);
        }
        return mReslist;
    }
    public static ArrayList<String> ToStringArrayList(Context context, String ctprvnCd, ArrayList<String> mResArray) {
        String[] mArray = context.getResources().getStringArray(R.array.sig_array);
        if(mResArray == null) {
            mResArray = new ArrayList<String>();
        }
        mResArray.clear();
        for (int i = 0; i < mArray.length; i++) {
            if(mArray[i].split("@")[0].equalsIgnoreCase(ctprvnCd)) {
                mResArray.add( mArray[i].split("@")[2]);
            }
        }
        return mResArray;
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

    public String getSigCd() {
        return SigCd;
    }

    public void setSigCd(String sigCd) {
        SigCd = sigCd;
    }

    public String getSigEngNm() {
        return SigEngNm;
    }

    public void setSigEngNm(String sigEngNm) {
        SigEngNm = sigEngNm;
    }

    public String getSigKorNm() {
        return SigKorNm;
    }

    public void setSigKorNm(String sigKorNm) {
        SigKorNm = sigKorNm;
    }
}
