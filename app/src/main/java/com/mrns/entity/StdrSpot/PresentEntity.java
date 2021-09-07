package com.mrns.entity.StdrSpot;

/**
 * Created by eined on 2016. 1. 26..
 */
public class PresentEntity {
    String INQ_DE;
    String INQ_INSTT_CD;
    String INQ_PSITN;
    String INQ_CLSF;
    String INQ_NM;

    String INQ_ID;
    String INQ_CN;
    String INQ_MTR_CD;
    String MTR_STTUS_CD;
    String RTK_POSBL_YN;

    String PTCLMATR;
    String ROG_MAP;
    String K_IMG;
    String O_IMG;

    public String getINQ_DE() {
        return INQ_DE;
    }

    public void setINQ_DE(String INQ_DE) {
        if(INQ_DE == null || INQ_DE.equals("") || INQ_DE.equals(null)){
            INQ_DE = "";
        }
        this.INQ_DE = INQ_DE;
    }

    public String getINQ_INSTT_CD() {
        return INQ_INSTT_CD;
    }

    public void setINQ_INSTT_CD(String INQ_INSTT_CD) {
        if(INQ_INSTT_CD == null || INQ_INSTT_CD.equals("") || INQ_INSTT_CD.equals(null)){
            INQ_INSTT_CD = "";
        }
        this.INQ_INSTT_CD = INQ_INSTT_CD;
    }

    public String getINQ_PSITN() {
        return INQ_PSITN;
    }

    public void setINQ_PSITN(String INQ_PSITN) {
        if(INQ_PSITN == null || INQ_PSITN.equals("") || INQ_PSITN.equals(null)){
            INQ_PSITN = "";
        }
        this.INQ_PSITN = INQ_PSITN;
    }

    public String getINQ_CLSF() {
        return INQ_CLSF;
    }

    public void setINQ_CLSF(String INQ_CLSF) {
        if(INQ_CLSF == null || INQ_CLSF.equals("") || INQ_CLSF.equals(null)){
            INQ_CLSF = "";
        }
        this.INQ_CLSF = INQ_CLSF;
    }

    public String getINQ_NM() {
        return INQ_NM;
    }

    public void setINQ_NM(String INQ_NM) {
        if(INQ_NM == null || INQ_NM.equals("") || INQ_NM.equals(null)){
            INQ_NM = "";
        }
        this.INQ_NM = INQ_NM;
    }

    public String getINQ_ID() {
        return INQ_ID;
    }

    public void setINQ_ID(String INQ_ID) {
        if(INQ_ID == null || INQ_ID.equals("") || INQ_ID.equals(null)){
            INQ_ID = "";
        }
        this.INQ_ID = INQ_ID;
    }

    public String getINQ_CN() {
        return INQ_CN;
    }

    public void setINQ_CN(String INQ_CN) {
        if(INQ_CN == null || INQ_CN.equals("") || INQ_CN.equals(null)){
            INQ_CN = "";
        }
        this.INQ_CN = INQ_CN;
    }

    public String getINQ_MTR_CD() {
        return INQ_MTR_CD;
    }

    public void setINQ_MTR_CD(String INQ_MTR_CD) {
        if(INQ_MTR_CD == null || INQ_MTR_CD.equals("") || INQ_MTR_CD.equals(null)){
            INQ_MTR_CD = "";
        }
        this.INQ_MTR_CD = INQ_MTR_CD;
    }

    public String getMTR_STTUS_CD() {
        return MTR_STTUS_CD;
    }

    public void setMTR_STTUS_CD(String MTR_STTUS_CD) {
        if(MTR_STTUS_CD == null || MTR_STTUS_CD.equals("") || MTR_STTUS_CD.equals(null)){
            MTR_STTUS_CD = "";
        }
        this.MTR_STTUS_CD = MTR_STTUS_CD;
    }

    public String getRTK_POSBL_YN() {
        return RTK_POSBL_YN;
    }

    public void setRTK_POSBL_YN(String RTK_POSBL_YN) {
        if(RTK_POSBL_YN == null || RTK_POSBL_YN.equals("") || RTK_POSBL_YN.equals(null)){
            RTK_POSBL_YN = "";
        }
        this.RTK_POSBL_YN = RTK_POSBL_YN;
    }

    public String getPTCLMATR() {
        return PTCLMATR;
    }

    public void setPTCLMATR(String PTCLMATR) {
        if(PTCLMATR == null || PTCLMATR.equals("") || PTCLMATR.equals(null)){
            PTCLMATR = "";
        }
        this.PTCLMATR = PTCLMATR;
    }

    public String getROG_MAP() {
        return ROG_MAP;
    }

    public void setROG_MAP(String ROG_MAP) {
        if(ROG_MAP == null || ROG_MAP.equals("") || ROG_MAP.equals(null)){
            ROG_MAP = "";
        }
        this.ROG_MAP = ROG_MAP;
    }

    public String getK_IMG() {
        return K_IMG;
    }

    public void setK_IMG(String k_IMG) {
        if(k_IMG == null || k_IMG.equals("") || k_IMG.equals(null)){
            k_IMG = "";
        }
        K_IMG = k_IMG;
    }

    public String getO_IMG() {
        return O_IMG;
    }

    public void setO_IMG(String o_IMG) {
        if(o_IMG == null || o_IMG.equals("") || o_IMG.equals(null)){
            o_IMG = "";
        }
        O_IMG = o_IMG;
    }
}
