package com.mrns.invest;

import android.graphics.drawable.Drawable;

/**
 * 데이터를 담고 있을 아이템 정의
 *
 * @author Mike
 *
 */
public class ResultList_Item {

    /**
     * Icon
     */
    private Drawable pKindIcon;
    private Drawable pLocaIcon;

    /**
     * Data array
     */
    private String[] mData;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    /**
     * Initialize with icon and data array
     * @param pIcon
     * @param obj
     * @param lIcon
     */
    public ResultList_Item(Drawable pIcon, String[] obj, Drawable lIcon) {
        pKindIcon = pIcon;
        this.mData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.mData[i] = obj[i];
        }
        pLocaIcon = lIcon;
    }

    /**
     * Initialize with icon and strings
     *
     * @param icon
     * @param obj01
     * @param obj02
     * @param obj03
     */
    public ResultList_Item(Drawable icon, String obj01, String obj02, String obj03, Drawable icon1) {
        pKindIcon = icon;
        pLocaIcon = icon1;
        mData = new String[3];
        mData[0] = obj01;
        mData[1] = obj02;
        mData[2] = obj03;
    }


    /**
     * True if this item is selectable
     */
    public boolean isSelectable() {
        return mSelectable;
    }

    /**
     * Set selectable flag
     */
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    /**
     * Get data array
     *
     * @return
     */
    public String[] getData() {

        String[] ret =null;

        if(this.mData != null){
            ret = new String[this.mData.length];
            for(int i=0; i<this.mData.length; i++){
                ret[i] = this.mData[i];
            }
        }

        return ret;
    }

    /**
     * Get data
     */
    public String getData(int index) {
        if (mData == null || index >= mData.length) {
            return null;
        }

        return mData[index];
    }

    /**
     * Set data array
     *
     * @param obj
     */
    public void setData(String[] obj) {
        this.mData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.mData[i] = obj[i];
        }
    }

    /**
     * Set icon
     *
     * @param icon
     */
    public void setIcon1(Drawable icon) {
        pKindIcon = icon;
    }

    public void setIcon2(Drawable icon1) {
        pLocaIcon = icon1;
    }

    /**
     * Get icon
     *
     * @return
     */
    public Drawable getIcon1() {
        return pKindIcon;
    }

    public Drawable getIcon2() {
        return pLocaIcon;
    }

    /**
     * Compare with the input object
     *
     * @param other
     * @return
     */
    public int compareTo(ResultList_Item other) {
        if (mData != null) {
            String[] otherData = other.getData();
            if (mData.length == otherData.length) {
                for (int i = 0; i < mData.length; i++) {
                    if (!mData[i].equals(otherData[i])) {
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
