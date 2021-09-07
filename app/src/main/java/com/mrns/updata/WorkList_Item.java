package com.mrns.updata;

import android.graphics.drawable.Drawable;

/**
 * 현황조사 올리기 - 올리기 대상 리스트
 * 데이터를 담고 있을 아이템 정의
 * Created by geopeople08 on 2016-01-11.
 */
public class WorkList_Item {

    /**
     * seq
     */
    int seq;

    /**
     * Icon
     */
    private Drawable pKindIcon;
    private Drawable pLocation;

    /**
     * Data array
     */
    private String[] pData;

    /**
     * True if this item is selectable
     */
    private boolean pSelectable = true;

    /**
     * Initialize with icon and data array
     *
     * @param pIcon
     * @param obj
     */
    public WorkList_Item(Drawable pIcon, String[] obj, Drawable lIcon) {
        pKindIcon = pIcon;
        this.pData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.pData[i] = obj[i];
        }
        pLocation = lIcon;
    }

    /**
     * Initialize with icon and strings
     */
    public WorkList_Item(Drawable icon1, String obj1, String obj2, String obj3, String obj4, int workSeq, Drawable icon2) {
        pKindIcon = icon1;
        pLocation = icon2;
        pData = new String[5];
        pData[0] = obj1;
        pData[1] = obj2;
        pData[2] = obj3;
        pData[3] = obj4;
        seq = workSeq;

    }

    /**
     * True if this item is selectable
     */
    public boolean ispSelectable() { return pSelectable; }

    public String[] getData() {
        String[] ret =null;

        if(this.pData != null){
            ret = new String[this.pData.length];
            for(int i=0; i<this.pData.length; i++){
                ret[i] = this.pData[i];
            }
        }

        return ret;
    }

    public String getData(int index) {
        if(pData == null || index >= pData.length) {
            return null;
        }

        return pData[index];
    }

    public int getSeq() {
        return seq;
    }

    public void setpData(String[] obj) {
        this.pData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.pData[i] = obj[i];
        }
    }

    public void setSeq(int wseq) {
        seq = wseq;
    }

    public void setIcon1(Drawable icon) { pKindIcon = icon; }

    public void setIcon2(Drawable icon) { pLocation = icon; }

    public Drawable getIcon1() { return pKindIcon; }

    public Drawable getIcon2() { return pLocation; }

    public int compareTo(WorkList_Item other) {
        if (pData != null) {
            String[] otherData = other.getData();
            if(otherData == null) {
                return -1;
            }
            if (pData.length == otherData.length) {
                for (int i = 0; i < pData.length; i++) {
                    if (!pData[i].equals(otherData[i])) {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException();
        }

        return 0;
    }



}
