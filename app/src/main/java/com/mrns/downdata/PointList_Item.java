package com.mrns.downdata;

import android.graphics.drawable.Drawable;

/**
 * 현황조사 내려받기 - 현황 조사자료
 * 데이터를 담고 있을 아이템 정의
 *
 * Created by geopeople08 on 2016-01-07.
 */
public class PointList_Item {

    /**
     * seq
     */
    private int invest_seq;
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
    public PointList_Item(int seq, Drawable pIcon, String[] obj) {
        invest_seq = seq;
        rSelIcon = pIcon;
        this.pData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.pData[i] = obj[i];
        }
    }

    /**
     * Initialize with icon and strings
     */
    public PointList_Item(int seq, Drawable icon, String obj1, String obj2) {
        invest_seq = seq;
        rSelIcon = icon;
        pData = new String[2];
        pData[0] = obj1;
        pData[1] = obj2;
    }

    /**
     * True if this item is selectable
     */
    public boolean ispSelectable() { return pSelectable; }

    public int getInvest_seq() { return invest_seq; }

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
