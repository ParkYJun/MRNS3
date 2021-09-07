package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

@Table
public class RnsNpUnityBf extends SugarRecord {
    private   long NpuBfSn;
    private   long NpuSn;
    private String PointsKndCd;
    private String PointsNm;
    private String PointsSttusCd;
    private String LayDe;
    private String NotifyDe;
    private String NotifyNo;
    private String InstlInsttNm;
    private String RsltMgtNm;
    private String SttusInqNm;
    private String MapNm;
    private String MtrCd;
    private String InqDe;
    private String PointsLegaldongCode;
    private String PointsRegstrSecd;
    private String PointsLocplcMnnm;
    private String PointsLocplcSlno;
    private String RoadNmCode;
    private String RoadNm;
    private String BuildngNm;
    private String PointsPath;
    private String TraverseTablePath;
    private String RogMapFile;
    private String DetailMapFile;
    private String ObserveNm;
    private String ObserveDe;
    private String RegistId;
    private String RegistOfcpsCd;
    private String RegistPsitnCd;
    private String RegistDe;
    private String UpdateId;
    private String UpdateOfcpsCd;
    private String UpdatePsitnCd;
    private String UpdateDe;
    private String AcptncYn;
    private String UseYn;
    private String Rm;
    private String PointsSecd;
    private String FullAddr;
    private String FullRoadAddr;

    public RnsNpUnityBf(){}

    public RnsNpUnityBf(
            long npu_bf_sn,
             long npu_sn,
          String points_knd_cd,
          String points_nm,
          String points_sttus_cd,
          String lay_de,
          String notify_de,
          String notify_no,
          String instl_instt_nm,
          String rslt_mgt_nm,
          String sttus_inq_nm,
          String map_nm,
          String mtr_cd,
          String inq_de,
          String points_legaldong_code,
          String points_regstr_secd,
          String points_locplc_mnnm,
          String points_locplc_slno,
          String road_nm_code,
          String road_nm,
          String buildng_nm,
          String points_path,
          String traverse_table_path,
          String rog_map_file,
          String detail_map_file,
          String observe_nm,
          String observe_de,
          String regist_id,
          String regist_ofcps_cd,
          String regist_psitn_cd,
          String regist_de,
          String update_id,
          String update_ofcps_cd,
          String update_psitn_cd,
          String update_de,
          String acptnc_yn,
          String use_yn,
          String rm,
          String points_secd,
         String full_addr,
         String full_road_addr
    )
    {
        NpuSn                   =	npu_sn;
        NpuBfSn					=	npu_bf_sn;
        PointsKndCd 				=	points_knd_cd;
        PointsNm                =   points_nm;
        PointsSttusCd 			=	points_sttus_cd;
        LayDe 					=	lay_de;
        NotifyDe 				=	notify_de;
        NotifyNo 				=	notify_no;
        InstlInsttNm 			=	instl_instt_nm;
        RsltMgtNm 				=	rslt_mgt_nm;
        SttusInqNm 				=	sttus_inq_nm;
        MapNm 					=	map_nm;
        MtrCd 					=	mtr_cd;
        InqDe					=	inq_de;
        PointsLegaldongCode 	=	points_legaldong_code;
        PointsRegstrSecd 		=	points_regstr_secd;
        PointsLocplcMnnm 		=	points_locplc_mnnm;
        PointsLocplcSlno 		=	points_locplc_slno;
        RoadNmCode 				=	road_nm_code;
        RoadNm 					=	road_nm;
        BuildngNm 				=	buildng_nm;
        PointsPath 				=	points_path;
        TraverseTablePath 		=	traverse_table_path;
        RogMapFile = rog_map_file;
        DetailMapFile = detail_map_file;
        ObserveNm 				=	observe_nm;
        ObserveDe				=	observe_de;
        RegistId				=	regist_id;
        RegistOfcpsCd			=	regist_ofcps_cd;
        RegistPsitnCd			=	regist_psitn_cd;
        RegistDe				=	regist_de;
        UpdateId				=	update_id;
        UpdateOfcpsCd			=	update_ofcps_cd;
        UpdatePsitnCd			=	update_psitn_cd;
        UpdateDe 				=	update_de;
        AcptncYn 				=	acptnc_yn;
        UseYn 					=	use_yn;
        Rm 						=	rm;
        PointsSecd 			    =	points_secd;
        FullAddr                =  full_addr;
        FullRoadAddr            =  full_road_addr;

    }

    public String getRogMapFile() {
        return RogMapFile;
    }

    public void setRogMapFile(String rogMapFile) {
        RogMapFile = rogMapFile;
    }

    public String getDetailMapFile() {
        return DetailMapFile;
    }

    public void setDetailMapFile(String detailMapFile) {
        DetailMapFile = detailMapFile;
    }

    public long getNpuSn() {
        return NpuSn;
    }

    public void setNpuSn(long npuSn) {
        NpuSn = npuSn;
    }

    public long getNpuBfSn() {
        return NpuBfSn;
    }

    public void setNpuBfSn(long npuBfSn) {
        NpuBfSn = npuBfSn;
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

    public String getPointsSecd() {
        return PointsSecd;
    }

    public void setPointsSecd(String pointsSecd) {
        PointsSecd = pointsSecd;
    }

    public String getLayDe() {
        return LayDe;
    }

    public void setLayDe(String layDe) {
        LayDe = layDe;
    }

    public String getNotifyDe() {
        return NotifyDe;
    }

    public void setNotifyDe(String notifyDe) {
        NotifyDe = notifyDe;
    }

    public String getNotifyNo() {
        return NotifyNo;
    }

    public void setNotifyNo(String notifyNo) {
        NotifyNo = notifyNo;
    }

    public String getInstlInsttNm() {
        return InstlInsttNm;
    }

    public void setInstlInsttNm(String instlInsttNm) {
        InstlInsttNm = instlInsttNm;
    }

    public String getRsltMgtNm() {
        return RsltMgtNm;
    }

    public void setRsltMgtNm(String rsltMgtNm) {
        RsltMgtNm = rsltMgtNm;
    }

    public String getSttusInqNm() {
        return SttusInqNm;
    }

    public void setSttusInqNm(String sttusInqNm) {
        SttusInqNm = sttusInqNm;
    }

    public String getMapNm() {
        return MapNm;
    }

    public void setMapNm(String mapNm) {
        MapNm = mapNm;
    }

    public String getMtrCd() {
        return MtrCd;
    }

    public void setMtrCd(String mtrCd) {
        MtrCd = mtrCd;
    }

    public String getInqDe() {
        return InqDe;
    }

    public void setInqDe(String inqDe) {
        InqDe = inqDe;
    }

    public String getPointsLegaldongCode() {
        return PointsLegaldongCode;
    }

    public void setPointsLegaldongCode(String pointsLegaldongCode) {
        PointsLegaldongCode = pointsLegaldongCode;
    }

    public String getPointsRegstrSecd() {
        return PointsRegstrSecd;
    }

    public void setPointsRegstrSecd(String pointsRegstrSecd) {
        PointsRegstrSecd = pointsRegstrSecd;
    }

    public String getPointsLocplcMnnm() {
        return PointsLocplcMnnm;
    }

    public void setPointsLocplcMnnm(String pointsLocplcMnnm) {
        PointsLocplcMnnm = pointsLocplcMnnm;
    }

    public String getPointsLocplcSlno() {
        return PointsLocplcSlno;
    }

    public void setPointsLocplcSlno(String pointsLocplcSlno) {
        PointsLocplcSlno = pointsLocplcSlno;
    }

    public String getRoadNmCode() {
        return RoadNmCode;
    }

    public void setRoadNmCode(String roadNmCode) {
        RoadNmCode = roadNmCode;
    }

    public String getRoadNm() {
        return RoadNm;
    }

    public void setRoadNm(String roadNm) {
        RoadNm = roadNm;
    }

    public String getBuildngNm() {
        return BuildngNm;
    }

    public void setBuildngNm(String buildngNm) {
        BuildngNm = buildngNm;
    }

    public String getPointsPath() {
        return PointsPath;
    }

    public void setPointsPath(String pointsPath) {
        PointsPath = pointsPath;
    }

    public String getTraverseTablePath() {
        return TraverseTablePath;
    }

    public void setTraverseTablePath(String traverseTablePath) {
        TraverseTablePath = traverseTablePath;
    }

    public String getObserveNm() {
        return ObserveNm;
    }

    public void setObserveNm(String observeNm) {
        ObserveNm = observeNm;
    }

    public String getObserveDe() {
        return ObserveDe;
    }

    public void setObserveDe(String observeDe) {
        ObserveDe = observeDe;
    }

    public String getRegistId() {
        return RegistId;
    }

    public void setRegistId(String registId) {
        RegistId = registId;
    }

    public String getRegistOfcpsCd() {
        return RegistOfcpsCd;
    }

    public void setRegistOfcpsCd(String registOfcpsCd) {
        RegistOfcpsCd = registOfcpsCd;
    }

    public String getRegistPsitnCd() {
        return RegistPsitnCd;
    }

    public void setRegistPsitnCd(String registPsitnCd) {
        RegistPsitnCd = registPsitnCd;
    }

    public String getRegistDe() {
        return RegistDe;
    }

    public void setRegistDe(String registDe) {
        RegistDe = registDe;
    }

    public String getUpdateId() {
        return UpdateId;
    }

    public void setUpdateId(String updateId) {
        UpdateId = updateId;
    }

    public String getUpdateOfcpsCd() {
        return UpdateOfcpsCd;
    }

    public void setUpdateOfcpsCd(String updateOfcpsCd) {
        UpdateOfcpsCd = updateOfcpsCd;
    }

    public String getUpdatePsitnCd() {
        return UpdatePsitnCd;
    }

    public void setUpdatePsitnCd(String updatePsitnCd) {
        UpdatePsitnCd = updatePsitnCd;
    }

    public String getUpdateDe() {
        return UpdateDe;
    }

    public void setUpdateDe(String updateDe) {
        UpdateDe = updateDe;
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

    public String getRm() {
        return Rm;
    }

    public void setRm(String rm) {
        Rm = rm;
    }

    public String getPointsSttusCd() {
        return PointsSttusCd;
    }

    public void setPointsSttusCd(String pointsSttusCd) {
        PointsSttusCd = pointsSttusCd;
    }

    public String getFullAddr() {
        return FullAddr;
    }

    public void setFullAddr(String fullAddr) {
        FullAddr = fullAddr;
    }

    public String getFullRoadAddr() {
        return FullRoadAddr;
    }

    public void setFullRoadAddr(String fullRoadAddr) {
        FullRoadAddr = fullRoadAddr;
    }

    public static RnsNpUnityBf FindByNpuSn(long NpuSn) {
        List<RnsNpUnityBf> list = RnsNpUnityBf.find(RnsNpUnityBf.class, "NPU_SN=?", String.valueOf(NpuSn));

       /* List<RnsNpUnity> list = RnsNpUnity.findWithQuery(RnsNpUnity.class,
                " SELECT A.*,  FROM RNS_NP_UNITY A, RNS_NP_STTUS_INQ_BF B " +
                "  WHERE A.NPU_SN = B.NPU_SN "+
                ""
                , String.valueOf(NpuSn));*/


        if(list.size() > 0) {
            return list.get(0);
        }else {
            return null;
        }
    }
}