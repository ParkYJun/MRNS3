package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by sooinsoft on 2018-01-08.
 */
@Table
public class RnsNpSttusInqBf extends SugarRecord {

    private   long NpsiBfSn;
    private   long NpuSn;
    private String InqDe;
    private String InqInsttCd;
    private String InqPsitn;
    private String InqOfcps;
    private String InqNm;
    private String InqId;
    private String InqMtrCd;
    private String MtrSttusCd;
    private String RtkPosblYn;
    private String Ptclmatr;
    private String InqMtrEtc;
    private String MtrSttusEtc;
    private String RtkPosblEtc;
    private String RogMapFile;
    private String KImgFile;
    private String OImgFile;
    private String MsurpointsLcDrwFile;
    private String AcptncYn;
    private String UseYn;
    private String SttusRsltAcptncSttus;
    private String InqSttus;
    private String RogMap;
    private String KImg;
    private String OImg;
    private String MsurpointsLcDrw;
    private String Progression;
    private String RlInqPsitn;
    private String RlInqClsf;
    private String RlInqNm;
    private String RlInqId;
    private String PointsPath;


    public RnsNpSttusInqBf(){}

    public RnsNpSttusInqBf(
            long npsi_bf_sn,
            long npu_sn,
            String inq_de,
            String inq_instt_cd,
            String inq_psitn,
            String inq_ofcps,
            String inq_nm,
            String inq_id,
            String inq_mtr_cd,
            String mtr_sttus_cd,
            String rtk_posbl_yn,
            String ptclmatr,
            String inq_mtr_etc,
            String mtr_sttus_etc,
            String rtk_posbl_etc,
            String acptnc_yn,
            String use_yn,
            String sttus_rslt_acptnc_sttus,
            String rog_map_file,
            String k_img_file,
            String o_img_file,
            String msurpoints_lc_drw_file,
            String rog_map,
            String k_img,
            String o_img,
            String msurpoints_lc_drw,
            String progression,
            String inq_sttus,
            String rl_inq_psitn,
            String rl_inq_clsf,
            String rl_inq_nm,
            String rl_inq_id,
            String points_path
    )
    {
        NpuSn=npu_sn;
        NpsiBfSn=npsi_bf_sn;
        InqDe=inq_de;
        InqInsttCd=inq_instt_cd;
        InqPsitn=inq_psitn;
        InqOfcps=inq_ofcps;
        InqNm=inq_nm;
        InqId=inq_id;
        InqMtrCd=inq_mtr_cd;
        MtrSttusCd=mtr_sttus_cd;
        RtkPosblYn=rtk_posbl_yn;
        Ptclmatr=ptclmatr;
        InqMtrEtc=inq_mtr_etc;
        MtrSttusEtc=mtr_sttus_etc;
        RtkPosblEtc=rtk_posbl_etc;
        AcptncYn=acptnc_yn;
        UseYn=use_yn;
        SttusRsltAcptncSttus = sttus_rslt_acptnc_sttus;
        RogMapFile=rog_map_file;
        KImgFile=k_img_file;
        OImgFile=o_img_file;
        MsurpointsLcDrwFile=msurpoints_lc_drw_file;
        RogMap= rog_map;
        KImg= k_img;
        OImg= o_img;
        MsurpointsLcDrw=msurpoints_lc_drw;
        InqSttus=inq_sttus;
        Progression = progression;
        RlInqPsitn = rl_inq_psitn;
        RlInqClsf = rl_inq_clsf;
        RlInqNm = rl_inq_nm;
        RlInqId = rl_inq_id;
        PointsPath=points_path;
    }

    public String getInqSttus() {
        return InqSttus;
    }

    public void setInqSttus(String inqSttus) {
        InqSttus = inqSttus;
    }

    public String getSttusRsltAcptncSttus() {
        return SttusRsltAcptncSttus;
    }

    public void setSttusRsltAcptncSttus(String sttusRsltAcptncSttus) {
        SttusRsltAcptncSttus = sttusRsltAcptncSttus;
    }

    public long getNpuSn() {
        return NpuSn;
    }

    public void setNpuSn(long npuSn) {
        NpuSn = npuSn;
    }

    public long getNpsiBfSn() {
        return NpsiBfSn;
    }

    public void setNpsiBfSn(long npsiBfSn) {
        NpsiBfSn = npsiBfSn;
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

    public String getInqOfcps() {
        return InqOfcps;
    }

    public void setInqOfcps(String inqOfcps) {
        InqOfcps = inqOfcps;
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

    public String getAcptncYn() {
        return AcptncYn;
    }

    public void setAcptncYn(String acptncYn) {
        AcptncYn = acptncYn;
    }

    public String getUseYn() {
        return UseYn;
    }

    public void setUseYn(String useYn) {
        UseYn = useYn;
    }

    public String getRogMapFile() {
        return RogMapFile;
    }

    public void setRogMapFile(String rogMapFile) {
        RogMapFile = rogMapFile;
    }

    public String getKImgFile() {
        return KImgFile;
    }

    public void setKImgFile(String KImgFile) {
        this.KImgFile = KImgFile;
    }

    public String getOImgFile() {
        return OImgFile;
    }

    public void setOImgFile(String OImgFile) {
        this.OImgFile = OImgFile;
    }

    public String getMsurpointsLcDrwFile() {
        return MsurpointsLcDrwFile;
    }

    public void setMsurpointsLcDrwFile(String msurpointsLcDrwFile) {
        MsurpointsLcDrwFile = msurpointsLcDrwFile;
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

    public String getMsurpointsLcDrw() {
        return MsurpointsLcDrw;
    }

    public void setMsurpointsLcDrw(String msurpointsLcDrw) {
        MsurpointsLcDrw = msurpointsLcDrw;
    }

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

    public String getProgression() {
        return Progression;
    }

    public void setProgression(String progression) {
        Progression = progression;
    }

    public String getPointsPath() {
        return PointsPath;
    }

    public void setPointsPath(String pointsPath) {
        PointsPath = pointsPath;
    }

    public static RnsNpSttusInqBf FindByNpuSn(long NpuSn) {
        List<RnsNpSttusInqBf> list = RnsNpSttusInqBf.find(RnsNpSttusInqBf.class, "NPU_SN=?", String.valueOf(NpuSn));

        if(list.size() > 0)
            return list.get(0);
        else
            return null;
    }
}
