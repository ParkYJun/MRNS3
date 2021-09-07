package com.mrns.invest.innerDetail.install;

/**
 * Created by eined on 2016. 1. 25..
 */
public class InstallHistoryList_Item {
    /**
     * Data array
     */
    private String[] mData;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    public InstallHistoryList_Item( String[] obj) {
        mData = obj;
    }

    public InstallHistoryList_Item(String s, String obj01, String obj02, String obj03) {
        mData = new String[4];
        mData[0] = s;
        mData[1] = obj01;
        mData[2] = obj02;
        mData[3] = obj03;
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
        return mData;
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

        mData = obj;
    }

    public int compareTo(InstallHistoryList_Item other) {
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
