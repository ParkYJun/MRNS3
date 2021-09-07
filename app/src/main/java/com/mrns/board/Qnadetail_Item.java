package com.mrns.board;

/**
 * Created by geopeople on 2016-01-20.
 */
public class Qnadetail_Item {

    /**
     * Data array
     */
    private String[] aData;

    /**
     * True if this item is selectable
     */
    private boolean qSelectable = true;

    /**
     * Initialize with icon and data array
     * @param obj
     */
    public Qnadetail_Item(String[] obj) {

        this.aData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.aData[i] = obj[i];
        }

    }

    public Qnadetail_Item(String obj1, String obj2, String obj3) {

        aData = new String[3];
        aData[0] = obj1;
        aData[1] = obj2;
        aData[2] = obj3;

    }

    /**
     * True if this item is selectable
     */
    public boolean ispSelectable() { return qSelectable; }

    public String[] getData() {

        String[] ret =null;

        if(this.aData != null){
            ret = new String[this.aData.length];
            for(int i=0; i<this.aData.length; i++){
                ret[i] = this.aData[i];
            }
        }

        return ret;
    }

    public String getData(int index) {
        if(aData == null || index >= aData.length) {
            return null;
        }

        return aData[index];
    }

    public void setaData(String[] obj) {

        this.aData = new String[obj.length];
        for(int i=0; i<obj.length; i++) {
            this.aData[i] = obj[i];
        }

    }

    public int compareTo(Qnalist_Item other) {
        if (aData != null) {
            String[] otherData = other.getData();
            if (aData.length == otherData.length) {
                for (int i = 0; i < aData.length; i++) {
                    if (!aData[i].equals(otherData[i])) {
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
