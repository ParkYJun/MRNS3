package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

@Table
public class RnsSttusInqBefore extends SugarRecord{

    private long SttusBfSn;
    private long SttusInqStdrPointsList;
    private long StdrspotSn;
    private String BasePointNo;
    private String PointsKndCd;
    private String PointsNm;
    private String JgrcCd;
    private String BrhCd;
    private String InqDe;
    private String InqInsttCd;
    private String InqPsitn;
    private String InqClsf;
    private String InqNm;
    private String InqId;
    private String InqMtrCd;
    private String RtkPosblYn;
    private String Ptclmatr;
    private String RogMap;
    private String KImg;
    private String OImg;
    private String MsurpointsLcDrw;
    private String SttusRsltAcptncYn;
    private String SttusRsltAcptncDe;
    private String SttusRsltAcptncJgrc;
    private String SttusRsltAcptncNm;
    private String SttusRsltAcptncRtrnResn;
    private String SttusRsltAcptncSttus;
    private String SttusRsltAcptncCancel;
    private String MtrSttusCd;
    private String InqSttus;
    private String InqMtrEtc;
    private String MtrSttusEtc;
    private String RtkPosblEtc;
    private String LocplcDetail;
    private String Progression;
    private String RlInqPsitn;
    private String RlInqClsf;
    private String RlInqNm;
    private String RlInqId;

    public RnsSttusInqBefore(){}

    public RnsSttusInqBefore(
            long sttus_bf_sn,
            long sttus_inq_stdr_points_list,
            long stdrspot_sn,
            String base_point_no,
            String points_knd_cd,
            String points_nm,
            String jgrc_cd,
            String brh_cd,
            String inq_de,
            String inq_instt_cd,
            String inq_psitn,
            String inq_clsf,
            String inq_nm,
            String inq_id,
            String inq_mtr_cd,
            String rtk_posbl_yn,
            String ptclmatr,
            String rog_map,
            String k_img,
            String o_img,
            String msurpoints_lc_drw,
            String sttus_rslt_acptnc_yn,
            String sttus_rslt_acptnc_de,
            String sttus_rslt_acptnc_jgrc,
            String sttus_rslt_acptnc_nm,
            String sttus_rslt_acptnc_rtrn_resn,
            String sttus_rslt_acptnc_sttus,
            String sttus_rslt_acptnc_cancel,
            String mtr_sttus_cd,
            String inq_sttus,
            String inq_mtr_etc,
            String mtr_sttus_etc,
            String rtk_posbl_etc,
            String locplc_detail,
            String progression,
            String rl_inq_psitn,
            String rl_inq_clsf,
            String rl_inq_nm,
            String rl_inq_id)
    {
        SttusBfSn = sttus_bf_sn;
        SttusInqStdrPointsList = sttus_inq_stdr_points_list;
        StdrspotSn = stdrspot_sn;
        BasePointNo = base_point_no;
        PointsKndCd = points_knd_cd;
        PointsNm = points_nm;
        JgrcCd = jgrc_cd;
        BrhCd = brh_cd;
        InqDe = inq_de;
        InqInsttCd = inq_instt_cd;
        InqPsitn = inq_psitn;
        InqClsf = inq_clsf;
        InqNm = inq_nm;
        InqId = inq_id;
        InqMtrCd = inq_mtr_cd;
        RtkPosblYn = rtk_posbl_yn;
        Ptclmatr = ptclmatr;
        RogMap = rog_map;
        this.KImg = k_img;
        this.OImg = o_img;
        this.MsurpointsLcDrw = msurpoints_lc_drw;
        SttusRsltAcptncYn = sttus_rslt_acptnc_yn;
        SttusRsltAcptncDe = sttus_rslt_acptnc_de;
        SttusRsltAcptncJgrc = sttus_rslt_acptnc_jgrc;
        SttusRsltAcptncNm = sttus_rslt_acptnc_nm;
        SttusRsltAcptncRtrnResn = sttus_rslt_acptnc_rtrn_resn;
        SttusRsltAcptncSttus = sttus_rslt_acptnc_sttus;
        SttusRsltAcptncCancel = sttus_rslt_acptnc_cancel;
        MtrSttusCd = mtr_sttus_cd;
        InqSttus = inq_sttus;
        InqMtrEtc = inq_mtr_etc;
        MtrSttusEtc = mtr_sttus_etc;
        RtkPosblEtc = rtk_posbl_etc;
        LocplcDetail = locplc_detail;
        Progression = progression;
        RlInqPsitn = rl_inq_psitn;
        RlInqClsf = rl_inq_clsf;
        RlInqNm = rl_inq_nm;
        RlInqId = rl_inq_id;
    }

    public long getSttusBfSn() {

        return SttusBfSn;
    }

    public void setSttusBfSn(long sttusBfSn) {
        SttusBfSn = sttusBfSn;
    }

    public long getSttusInqStdrPointsList() {
        return SttusInqStdrPointsList;
    }

    public void setSttusInqStdrPointsList(long sttusInqStdrPointsList) {
        SttusInqStdrPointsList = sttusInqStdrPointsList;
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
        this.RogMap = rogMap;
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

    public String getMsurpointsLcDrw() {
        return MsurpointsLcDrw;
    }

    public void setMsurpointsLcDrw(String MsurpointsLcDrw) {
        this.MsurpointsLcDrw = MsurpointsLcDrw;
    }

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

    public String getSttusRsltAcptncSttus() {
        return SttusRsltAcptncSttus;
    }

    public void setSttusRsltAcptncSttus(String sttusRsltAcptncSttus) {
        SttusRsltAcptncSttus = sttusRsltAcptncSttus;
    }

    public String getSttusRsltAcptncCancel() {
        return SttusRsltAcptncCancel;
    }

    public void setSttusRsltAcptncCancel(String sttusRsltAcptncCancel) {
        SttusRsltAcptncCancel = sttusRsltAcptncCancel;
    }

    public String getMtrSttusCd() {
        return MtrSttusCd;
    }

    public void setMtrSttusCd(String mtrSttusCd) {
        MtrSttusCd = mtrSttusCd;
    }

    public String getInqSttus() {
        return InqSttus;
    }

    public void setInqSttus(String inqSttus) {
        InqSttus = inqSttus;
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

    public String getLocplcDetail() { return LocplcDetail; }

    public void setLocplcDetail(String locplcDetail) { LocplcDetail = locplcDetail; }

    public String getProgression() { return Progression; }

    public void setProgression(String progression) { Progression = progression; }

    public String getRlInqPsitn() {
        return RlInqPsitn;
    }

    public void setRlInqPsitn(String rlInqPsitn) {
        RlInqPsitn = rlInqPsitn;
    }

    public String getRlInqClsf() {
        return RlInqClsf;
    }

    public void setRlInqClsf(String rlInqClsf) {
        RlInqClsf = rlInqClsf;
    }

    public String getRlInqNm() {
        return RlInqNm;
    }

    public void setRlInqNm(String rlInqNm) {
        RlInqNm = rlInqNm;
    }

    public String getRlInqId() {
        return RlInqId;
    }

    public void setRlInqId(String rlInqId) {
        RlInqId = rlInqId;
    }

    public static RnsSttusInqBefore FindByStdrspotSn(Long StdrspotSn) {
        List<RnsSttusInqBefore> list = RnsSttusInqBefore.find(RnsSttusInqBefore.class, "STDRSPOT_SN=?", StdrspotSn.toString());
        if(list.size() > 0)
            return list.get(0);
        else
            return null;
    }
}
