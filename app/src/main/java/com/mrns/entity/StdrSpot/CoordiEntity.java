package com.mrns.entity.StdrSpot;

/**
 * Created by eined on 2016. 1. 26..
 */
public class CoordiEntity {
    String LOCAL_Y;
    String WD_TRGNPT_CD;
    String LOCAL_X;
    String LOCAL_H;
    String WD_LON;

    String WD_LAT;
    String WD_X;
    String WD_H;
    String WD_Y;

    public String getLOCAL_Y() {
        return LOCAL_Y;
    }

    public void setLOCAL_Y(String LOCAL_Y) {
        if(LOCAL_Y == null || LOCAL_Y.equals("") || LOCAL_Y.equals(null)){
            LOCAL_Y = "";
        }
        this.LOCAL_Y = LOCAL_Y;
    }

    public String getWD_TRGNPT_CD() {
        return WD_TRGNPT_CD;
    }

    public void setWD_TRGNPT_CD(String WD_TRGNPT_CD) {
        if(WD_TRGNPT_CD == null || WD_TRGNPT_CD.equals("") || WD_TRGNPT_CD.equals(null)){
            WD_TRGNPT_CD = "";
        }
        this.WD_TRGNPT_CD = WD_TRGNPT_CD;
    }

    public String getLOCAL_X() {
        return LOCAL_X;
    }

    public void setLOCAL_X(String LOCAL_X) {
        if(LOCAL_X == null || LOCAL_X.equals("") || LOCAL_X.equals(null)){
            LOCAL_X = "";
        }
        this.LOCAL_X = LOCAL_X;
    }

    public String getLOCAL_H() {
        return LOCAL_H;
    }

    public void setLOCAL_H(String LOCAL_H) {
        if(LOCAL_H == null || LOCAL_H.equals("") || LOCAL_H.equals(null)){
            LOCAL_H = "";
        }
        this.LOCAL_H = LOCAL_H;
    }

    public String getWD_LON() {
        return WD_LON;
    }

    public void setWD_LON(String WD_LON) {
        if(WD_LON == null || WD_LON.equals("") || WD_LON.equals(null)){
            WD_LON = "";
        }
        this.WD_LON = WD_LON;
    }

    public String getWD_LAT() {
        return WD_LAT;
    }

    public void setWD_LAT(String WD_LAT) {
        if(WD_LAT == null || WD_LAT.equals("") || WD_LAT.equals(null)){
            WD_LAT = "";
        }
        this.WD_LAT = WD_LAT;
    }

    public String getWD_X() {
        return WD_X;
    }

    public void setWD_X(String WD_X) {
        if(WD_X == null || WD_X.equals("") || WD_X.equals(null)){
            WD_X = "";
        }
        this.WD_X = WD_X;
    }

    public String getWD_H() {
        return WD_H;
    }

    public void setWD_H(String WD_H) {
        if(WD_H == null || WD_H.equals("") || WD_H.equals(null)){
            WD_H = "";
        }
        this.WD_H = WD_H;
    }

    public String getWD_Y() {
        return WD_Y;
    }

    public void setWD_Y(String WD_Y) {
        if(WD_Y == null || WD_Y.equals("") || WD_Y.equals(null)){
            WD_Y = "";
        }
        this.WD_Y = WD_Y;
    }
}
