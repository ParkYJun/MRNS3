package com.mrns.board;

import android.graphics.drawable.Drawable;

/**
 * Created by geopeople on 2016-01-19.
 */
public class Boardlist_Item {

    /**
     * Icon
     */
    private Drawable bIcon;

    /**
     * Data seq
     */
    private int board_seq;


    /**
     * Data array
     */
    private String[] bData;

    /**
     * True if this item is selectable
     */
    private boolean bSelectable = true;

    /**
     * Initialize with icon and data array
     * @param obj
     */

    public Boardlist_Item(int seq, Drawable icon, String[] obj)
    {
        board_seq = seq;
        bIcon = icon;
        this.bData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.bData[i] = obj[i];
        }
    }

    public Boardlist_Item(int seq, Drawable icon, String obj1, String obj2) {

        board_seq = seq;
        bIcon = icon;
        bData = new String[2];
        bData[0] = obj1;
        bData[1] = obj2;
    }

    /**
     * True if this item is selectable
     */
    public boolean isbSelectable() { return bSelectable; }

    public int getBoard_seq() { return board_seq; }

    public Drawable getIcon() { return bIcon; }

    public String[] getData() { return bData; }

    public String getData(int index) {
        if(bData == null || index >= bData.length) {
            return null;
        }

        return bData[index];
    }



    public void setBoard_seq(int seq) {
        board_seq = seq;
    }
    public void setIcon(Drawable icon) { bIcon = icon; }

    public void setbData(String[] obj) {
        this.bData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.bData[i] = obj[i];
        }

    }

    public int compareTo(Boardlist_Item other) {
        if (bData != null) {
            String[] otherData = other.getData();

            if(otherData == null){
                return -1;
            }

            if (bData.length == otherData.length) {
                for (int i = 0; i < bData.length; i++) {
                    if (!bData[i].equals(otherData[i])) {
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
