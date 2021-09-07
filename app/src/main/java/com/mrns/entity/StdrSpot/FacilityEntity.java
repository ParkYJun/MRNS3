package com.mrns.entity.StdrSpot;

/**
 * Created by eined on 2016. 1. 26..
 */
public class FacilityEntity {
    String MLINE_NM;
    String CRO_POINT;

    public String getMlineNm() {
        return MLINE_NM;
    }

    public void setMlineNm(String MLINE_NM) {
        if(MLINE_NM == null || MLINE_NM.equals("") || MLINE_NM.equals(null)){
            MLINE_NM = "";
        }
        this.MLINE_NM = MLINE_NM;
    }

    public String getCroPoint() {
        return CRO_POINT;
    }

    public void setCroPoint(String CRO_POINT) {
        if(CRO_POINT == null || CRO_POINT.equals("") || CRO_POINT.equals(null)){
            CRO_POINT = "";
        }
        this.CRO_POINT = CRO_POINT;
    }
}
