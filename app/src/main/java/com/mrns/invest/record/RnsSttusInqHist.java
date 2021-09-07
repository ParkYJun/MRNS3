package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class RnsSttusInqHist extends SugarRecord {
    private long SttusSn;
    private long StdrspotSn;
    private String BasePointNo ;
    private String PointsKndCd ;
    private String PointsNm ;
    private String JgrcCd ;
    private String BrhCd ;
    private String InqDe ;
    private String InqInsttCd ;
    private String InqPsitn;
    private String InqClsf ;
    private String InqNm;
    private String InqId ;
    private String InqMtrCd ;
    private String MtrSttusCd ;
    private String RtkPosblYn ;
    private String Ptclmatr ;
    private String RogMap ;
    private String KImg ;
    private String OImg ;
    private String MsurpointsLcDrw;
    private String SttusRsltAcptncYn  ;
    private String SttusRsltAcptncDe ;
    private String SttusRsltAcptncJgrc ;
    private String SttusRsltAcptncNm;
    private String SttusRsltAcptncRtrnResn ;
    private String InqMtrEtc ;
    private String MtrSttusEtc ;
    private String RtkPosblEtc ;

    public RnsSttusInqHist(long sttusSn, long stdrspotSn, String basePointNo, String pointsKndCd, String pointsNm, String jgrcCd, String brhCd, String inqDe, String inqInsttCd, String inqPsitn, String inqClsf, String inqNm, String inqId, String inqMtrCd, String mtrSttusCd, String rtkPosblYn, String ptclmatr, String rogMap, String KImg, String OImg, String MsurpointsLcDrw, String sttusRsltAcptncYn, String sttusRsltAcptncDe, String sttusRsltAcptncJgrc, String sttusRsltAcptncNm, String sttusRsltAcptncRtrnResn, String inqMtrEtc, String mtrSttusEtc, String rtkPosblEtc) {
        SttusSn = sttusSn;
        StdrspotSn = stdrspotSn;
        BasePointNo = basePointNo;
        PointsKndCd = pointsKndCd;
        PointsNm = pointsNm;
        JgrcCd = jgrcCd;
        BrhCd = brhCd;
        InqDe = inqDe;
        InqInsttCd = inqInsttCd;
        InqPsitn = inqPsitn;
        InqClsf = inqClsf;
        InqNm = inqNm;
        InqId = inqId;
        InqMtrCd = inqMtrCd;
        MtrSttusCd = mtrSttusCd;
        RtkPosblYn = rtkPosblYn;
        Ptclmatr = ptclmatr;
        RogMap = rogMap;
        this.KImg = KImg;
        this.OImg = OImg;
        this.MsurpointsLcDrw = MsurpointsLcDrw;
        SttusRsltAcptncYn = sttusRsltAcptncYn;
        SttusRsltAcptncDe = sttusRsltAcptncDe;
        SttusRsltAcptncJgrc = sttusRsltAcptncJgrc;
        SttusRsltAcptncNm = sttusRsltAcptncNm;
        SttusRsltAcptncRtrnResn = sttusRsltAcptncRtrnResn;
        InqMtrEtc = inqMtrEtc;
        MtrSttusEtc = mtrSttusEtc;
        RtkPosblEtc = rtkPosblEtc;
    }

    public RnsSttusInqHist() {
    }

    public long getSttusSn() {
        return SttusSn;
    }

    public void setSttusSn(long sttusSn) {
        SttusSn = sttusSn;
    }

    public long getStdrspotSn() {
        return StdrspotSn;
    }

    public void setStdrspotSn(long stdrspotSn) {
        StdrspotSn = stdrspotSn;
    }

    public String getBasePointNo() {
        return BasePointNo;
    }

    public void setBasePointNo(String basePointNo) {
        BasePointNo = basePointNo;
    }

    public String getPointsKndCd() {
        return PointsKndCd;
    }

    public void setPointsKndCd(String pointsKndCd) {
        PointsKndCd = pointsKndCd;
    }

    public String getPointsNm() {
        return PointsNm;
    }

    public void setPointsNm(String pointsNm) {
        PointsNm = pointsNm;
    }

    public String getJgrcCd() {
        return JgrcCd;
    }

    public void setJgrcCd(String jgrcCd) {
        JgrcCd = jgrcCd;
    }

    public String getBrhCd() {
        return BrhCd;
    }

    public void setBrhCd(String brhCd) {
        BrhCd = brhCd;
    }

    public String getInqDe() {
        return InqDe;
    }

    public void setInqDe(String inqDe) {
        InqDe = inqDe;
    }

    public String getInqInsttCd() {
        return InqInsttCd;
    }

    public void setInqInsttCd(String inqInsttCd) {
        InqInsttCd = inqInsttCd;
    }

    public String getInqPsitn() {
        return InqPsitn;
    }

    public void setInqPsitn(String inqPsitn) {
        InqPsitn = inqPsitn;
    }

    public String getInqClsf() {
        return InqClsf;
    }

    public void setInqClsf(String inqClsf) {
        InqClsf = inqClsf;
    }

    public String getInqNm() {
        return InqNm;
    }

    public void setInqNm(String inqNm) {
        InqNm = inqNm;
    }

    public String getInqId() {
        return InqId;
    }

    public void setInqId(String inqId) {
        InqId = inqId;
    }

    public String getInqMtrCd() {
        return InqMtrCd;
    }

    public void setInqMtrCd(String inqMtrCd) {
        InqMtrCd = inqMtrCd;
    }

    public String getMtrSttusCd() {
        return MtrSttusCd;
    }

    public void setMtrSttusCd(String mtrSttusCd) {
        MtrSttusCd = mtrSttusCd;
    }

    public String getRtkPosblYn() {
        return RtkPosblYn;
    }

    public void setRtkPosblYn(String rtkPosblYn) {
        RtkPosblYn = rtkPosblYn;
    }

    public String getPtclmatr() {
        return Ptclmatr;
    }

    public void setPtclmatr(String ptclmatr) {
        Ptclmatr = ptclmatr;
    }

    public String getRogMap() {
        return RogMap;
    }

    public void setRogMap(String rogMap) {
        RogMap = rogMap;
    }

    public String getKImg() {
        return KImg;
    }

    public void setKImg(String KImg) {
        this.KImg = KImg;
    }

    public String getOImg() {
        return OImg;
    }

    public void setOImg(String OImg) {
        this.OImg = OImg;
    }

    public String getMsurpointsLcDrw() { return MsurpointsLcDrw; }

    public void setMsurpointsLcDrw(String MsurpointsLcDrw) { this.MsurpointsLcDrw = MsurpointsLcDrw; }

    public String getSttusRsltAcptncYn() {
        return SttusRsltAcptncYn;
    }

    public void setSttusRsltAcptncYn(String sttusRsltAcptncYn) {
        SttusRsltAcptncYn = sttusRsltAcptncYn;
    }

    public String getSttusRsltAcptncDe() {
        return SttusRsltAcptncDe;
    }

    public void setSttusRsltAcptncDe(String sttusRsltAcptncDe) {
        SttusRsltAcptncDe = sttusRsltAcptncDe;
    }

    public String getSttusRsltAcptncJgrc() {
        return SttusRsltAcptncJgrc;
    }

    public void setSttusRsltAcptncJgrc(String sttusRsltAcptncJgrc) {
        SttusRsltAcptncJgrc = sttusRsltAcptncJgrc;
    }

    public String getSttusRsltAcptncNm() {
        return SttusRsltAcptncNm;
    }

    public void setSttusRsltAcptncNm(String sttusRsltAcptncNm) {
        SttusRsltAcptncNm = sttusRsltAcptncNm;
    }

    public String getSttusRsltAcptncRtrnResn() {
        return SttusRsltAcptncRtrnResn;
    }

    public void setSttusRsltAcptncRtrnResn(String sttusRsltAcptncRtrnResn) {
        SttusRsltAcptncRtrnResn = sttusRsltAcptncRtrnResn;
    }

    public String getInqMtrEtc() {
        return InqMtrEtc;
    }

    public void setInqMtrEtc(String inqMtrEtc) {
        InqMtrEtc = inqMtrEtc;
    }

    public String getMtrSttusEtc() {
        return MtrSttusEtc;
    }

    public void setMtrSttusEtc(String mtrSttusEtc) {
        MtrSttusEtc = mtrSttusEtc;
    }

    public String getRtkPosblEtc() {
        return RtkPosblEtc;
    }

    public void setRtkPosblEtc(String rtkPosblEtc) {
        RtkPosblEtc = rtkPosblEtc;
    }
}
