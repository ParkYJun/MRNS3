package com.mrns.downdata;

import android.graphics.drawable.Drawable;

/**
 * 현황조사 내려받기 - 지도자료
 * 데이터를 담고 있을 아이템 정의
 * Created by geopeople08 on 2016-01-08.
 */
public class MapList_Item {

    /**
     * seq
     */
    private int mapSeq;

    /**
     * Icon
     */
    private Drawable rSelIcon;

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
     * @param pIcon
     * @param obj
     */
    public MapList_Item(int seq, Drawable pIcon, String[] obj) {
        mapSeq = seq;
        rSelIcon = pIcon;
        pData = obj;
    }

    /**
     * Initialize with icon and strings
     */
    public MapList_Item(int seq, Drawable icon, String obj1, String obj2, String obj3) {
        mapSeq = seq;
        rSelIcon = icon;
        pData = new String[3];
        pData[0] = obj1;
        pData[1] = obj2;
        pData[2] = obj3;
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

    public int getMapSeq() {
        return mapSeq;
    }

    public void setMapSeq(int seq) {
        mapSeq = seq;
    }

    public void setpData(String[] obj) {
        this.pData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.pData[i] = obj[i];
        }
    }

    public void setIcon(Drawable icon) { rSelIcon = icon; }

    public Drawable getIcon() { return rSelIcon; }

    public int compareTo(PointList_Item other) {
        if (pData != null) {
            String[] otherData = other.getData();
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
