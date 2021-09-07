package com.mrns.entity.StdrSpot;

/**
 * Created by eined on 2016. 1. 26..
 */
public class BaseEntity {
    String DRW_NO;
    String INSTL_SE_CD;
    String JGRC_CD;
    String LOCPLC_LNM;
    String BRH_CD;

    String MLINE_GRAD_CD;
    String LOCPLC_RD_NM;
    String BASE_POINT_NO;
    String POINTS_NO;
    String INSTL_DE;

    String ADM_CD;
    String POINTS_NM;
    String MTR_CD;
    String STDRSPOT_SN;

    public String getDrwNo() {
        return DRW_NO;
    }

    public void setDrwNo(String DRW_NO) {
        if(DRW_NO == null || DRW_NO.equals("") || DRW_NO.equals(null)){
            DRW_NO = "";
        }
        this.DRW_NO = DRW_NO;
    }

    public String getInstlSeCd() {
        return INSTL_SE_CD;
    }

    public void setInstlSeCd(String INSTL_SE_CD) {
        if(INSTL_SE_CD == null || INSTL_SE_CD.equals("") || INSTL_SE_CD.equals(null)){
            INSTL_SE_CD = "";
        }
        this.INSTL_SE_CD = INSTL_SE_CD;
    }

    public String getJgrcCd() {
        return JGRC_CD;
    }

    public void setJgrcCd(String JGRC_CD) {
        if(JGRC_CD == null || JGRC_CD.equals("") || JGRC_CD.equals(null)){
            JGRC_CD = "";
        }
        this.JGRC_CD = JGRC_CD;
    }

    public String getLocplcLnm() {
        return LOCPLC_LNM;
    }

    public void setLocplcLnm(String LOCPLC_LNM) {
        if(LOCPLC_LNM == null || LOCPLC_LNM.equals("") || LOCPLC_LNM.equals(null)){
            LOCPLC_LNM = "";
        }
        this.LOCPLC_LNM = LOCPLC_LNM;
    }

    public String getBrhCd() {
        return BRH_CD;
    }

    public void setBrhCd(String BRH_CD) {
        if(BRH_CD == null || BRH_CD.equals("") || BRH_CD.equals(null)){
            BRH_CD = "";
        }
        this.BRH_CD = BRH_CD;
    }

    public String getMlineGradCd() {
        return MLINE_GRAD_CD;
    }

    public void setMlineGradCd(String MLINE_GRAD_CD) {
        if(MLINE_GRAD_CD == null || MLINE_GRAD_CD.equals("") || MLINE_GRAD_CD.equals(null)){
            MLINE_GRAD_CD = "";
        }
        this.MLINE_GRAD_CD = MLINE_GRAD_CD;
    }

    public String getLocplcRdNm() {
        return LOCPLC_RD_NM;
    }

    public void setLocplcRdNm(String LOCPLC_RD_NM) {
        if(LOCPLC_RD_NM == null || LOCPLC_RD_NM.equals("") || LOCPLC_RD_NM.equals(null)){
            LOCPLC_RD_NM = "";
        }
        this.LOCPLC_RD_NM = LOCPLC_RD_NM;
    }

    public String getBasePointNo() {
        return BASE_POINT_NO;
    }

    public void setBasePointNo(String BASE_POINT_NO) {
        if(BASE_POINT_NO == null || BASE_POINT_NO.equals("") || BASE_POINT_NO.equals(null)){
            BASE_POINT_NO = "";
        }
        this.BASE_POINT_NO = BASE_POINT_NO;
    }

    public String getPointsNo() {
        return POINTS_NO;
    }

    public void setPointsNo(String POINTS_NO) {
        if(POINTS_NO == null || POINTS_NO.equals("") || POINTS_NO.equals(null)){
            POINTS_NO = "";
        }
        this.POINTS_NO = POINTS_NO;
    }

    public String getInstlDe() {
        return INSTL_DE;
    }

    public void setInstlDe(String INSTL_DE) {
        if(INSTL_DE == null || INSTL_DE.equals("") || INSTL_DE.equals(null)){
            INSTL_DE = "";
        }
        this.INSTL_DE = INSTL_DE;
    }

    public String getAdmCd() {
        return ADM_CD;
    }

    public void setAdmCd(String ADM_CD) {
        if(ADM_CD == null || ADM_CD.equals("") || ADM_CD.equals(null)){
            ADM_CD = "";
        }
        this.ADM_CD = ADM_CD;
    }

    public String getPointsNm() {
        return POINTS_NM;
    }

    public void setPointsNm(String POINTS_NM) {
        if(POINTS_NM == null || POINTS_NM.equals("") || POINTS_NM.equals(null)){
            POINTS_NM = "";
        }
        this.POINTS_NM = POINTS_NM;
    }

    public String getMtrCd() {
        return MTR_CD;
    }

    public void setMtrCd(String MTR_CD) {
        if(MTR_CD == null || MTR_CD.equals("") || MTR_CD.equals(null)){
            MTR_CD = "";
        }
        this.MTR_CD = MTR_CD;
    }

    public String getStdrspotSn() {
        return STDRSPOT_SN;
    }

    public void setStdrspotSn(String STDRSPOT_SN) {
        if(STDRSPOT_SN == null || STDRSPOT_SN.equals("") || STDRSPOT_SN.equals(null)){
            STDRSPOT_SN = "";
        }
        this.STDRSPOT_SN = STDRSPOT_SN;
    }
}
