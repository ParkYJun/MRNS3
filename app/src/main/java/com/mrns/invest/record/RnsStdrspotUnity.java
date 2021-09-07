package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

@Table
public class RnsStdrspotUnity extends SugarRecord {
    private long StdrspotSn;
    long id;
    private String BasePointNo;
    private String PointsKndCd;
    private String PointsNm;
    private String JgrcCd;
    private String BrhCd;
    private String PointsNo;
    private String MlineGradCd;
    private String MtrCd;
    private String InstlSeCd;
    private String BisSeCd;
    private String BisNm;
    private String InstlDe;
    private String InstlInsttCd;
    private String InstlPsitn;
    private String InstlClsf;
    private String InstlNm;
    private String DrwNo;
    private String MsurpointsLcDrw;
    private String MsurpointsLcMap;
    private String AdmCd;
    private String LocplcLnm;
    private String LocplcRdNm;
    private String NearbyLocplcLnm;
    private String NearbyLocplcRdNm;
    private String NotifyNo;
    private String NotifyDe;
    private String RsltDocFile;
    private String SurvMth;
    private String CalMth;
    private String CalPsitn;
    private String CalClsf;
    private String CalNm;
    private String MlineNm;
    private String KspFile;
    private String CroPoint;
    private String UseBhfPoints;
    private String BeginPoints;
    private String BeginMachPoints;
    private String ArrvPoints;
    private String ArrvMachPoints;
    private String BeforePoints;
    private String AfterPoints;
    private String Azimuth;
    private Double Dstnc;
    private Double DistX;
    private Double DistY;
    private String TrgnptCd;
    private Double PointsX;
    private Double PointsY;
    private String Lat;
    private String Lon;
    private Double H;
    private String WdHSeCd;
    private String WdEh;
    private String MeridianSucha;
    private String InstlRsltAcptncYn;
    private String InstlRsltAcptncDe;
    private String InstlRsltAcptncJgrc;
    private String InstlRsltAcptncNm;
    private String InstlRsltAcptncRtrnResn;
    private String InqDe;
    private String InqInsttCd;
    private String InqPsitn;
    private String InqClsf;
    private String InqNm;
    private String InqId;
    private String InqMtrCd;
    private String MtrSttusCd;
    private String RtkPosblYn;
    private String Ptclmatr;
    private String RogMap;
    private String KImg;
    private String OImg;
    private String SttusRsltAcptncYn;
    private String SttusRsltAcptncDe;
    private String SttusRsltAcptncJgrc;
    private String SttusRsltAcptncNm;
    private String SttusRsltAcptncRtrnResn;
    private String InqMtrEtc;
    private String MtrSttusEtc;
    private String RtkPosblEtc;

    private String WdTrgnptCd;
    private String WdPointsX;
    private String WdPointsY;
    private String WdLat;
    private String WdLon;
    private String WdH;
    private String LocplcDetail;

    public RnsStdrspotUnity() {
    }

    public RnsStdrspotUnity(
            long stdrspotSn,
            String basePointNo,
            String pointsKndCd,
            String pointsNm,
            String jgrcCd,
            String brhCd,
            String pointsNo,
            String mlineGradCd,
            String mtrCd,
            String instlSeCd,
            String bisSeCd,
            String bisNm,
            String instlDe,
            String instlInsttCd,
            String instlPsitn,
            String instlClsf,
            String instlNm,
            String drwNo,
            String msurpointsLcDrw,
            String msurpointsLcMap,
            String admCd,
            String locplcLnm,
            String locplcRdNm,
            String nearbyLocplcLnm,
            String nearbyLocplcRdNm,
            String notifyNo,
            String notifyDe,
            String rsltDocFile,
            String survMth,
            String calMth,
            String calPsitn,
            String calClsf,
            String calNm,
            String mlineNm,
            String kspFile,
            String croPoint,
            String useBhfPoints,
            String beginPoints,
            String beginMachPoints,
            String arrvPoints,
            String arrvMachPoints,
            String beforePoints,
            String afterPoints,
            String azimuth,
            Double dstnc,
            Double distX,
            Double distY,
            String trgnptCd,
            Double pointsX,
            Double pointsY,
            String lat,
            String lon,
            Double h,
            String wdHSeCd,
            String wdEh,
            String meridianSucha,
            String instlRsltAcptncYn,
            String instlRsltAcptncDe,
            String instlRsltAcptncJgrc,
            String instlRsltAcptncNm,
            String instlRsltAcptncRtrnResn,
            String inqDe,
            String inqInsttCd,
            String inqPsitn,
            String inqClsf,
            String inqNm,
            String inqId,
            String inqMtrCd,
            String mtrSttusCd,
            String rtkPosblYn,
            String ptclmatr,
            String rogMap,
            String KImg,
            String OImg,
            String sttusRsltAcptncYn,
            String sttusRsltAcptncDe,
            String sttusRsltAcptncJgrc,
            String sttusRsltAcptncNm,
            String sttusRsltAcptncRtrnResn,
            String inqMtrEtc,
            String mtrSttusEtc,
            String rtkPosblEtc,
            String wdTrgnptCd,
            String wdPointsX,
            String wdPointsY,
            String wdLat,
            String wdLon,
            String wdH,
            String locplcDetail)
    {
        StdrspotSn = stdrspotSn;
        BasePointNo = basePointNo;
        PointsKndCd = pointsKndCd;
        PointsNm = pointsNm;
        JgrcCd = jgrcCd;
        BrhCd = brhCd;
        PointsNo = pointsNo;
        MlineGradCd = mlineGradCd;
        MtrCd = mtrCd;
        InstlSeCd = instlSeCd;
        BisSeCd = bisSeCd;
        BisNm = bisNm;
        InstlDe = instlDe;
        InstlInsttCd = instlInsttCd;
        InstlPsitn = instlPsitn;
        InstlClsf = instlClsf;
        InstlNm = instlNm;
        DrwNo = drwNo;
        MsurpointsLcDrw = msurpointsLcDrw;
        MsurpointsLcMap = msurpointsLcMap;
        AdmCd = admCd;
        LocplcLnm = locplcLnm;
        LocplcRdNm = locplcRdNm;
        NearbyLocplcLnm = nearbyLocplcLnm;
        NearbyLocplcRdNm = nearbyLocplcRdNm;
        NotifyNo = notifyNo;
        NotifyDe = notifyDe;
        RsltDocFile = rsltDocFile;
        SurvMth = survMth;
        CalMth = calMth;
        CalPsitn = calPsitn;
        CalClsf = calClsf;
        CalNm = calNm;
        MlineNm = mlineNm;
        KspFile = kspFile;
        CroPoint = croPoint;
        UseBhfPoints = useBhfPoints;
        BeginPoints = beginPoints;
        BeginMachPoints = beginMachPoints;
        ArrvPoints = arrvPoints;
        ArrvMachPoints = arrvMachPoints;
        BeforePoints = beforePoints;
        AfterPoints = afterPoints;
        Azimuth = azimuth;
        Dstnc = dstnc;
        DistX = distX;
        DistY = distY;
        TrgnptCd = trgnptCd;
        PointsX = pointsX;
        PointsY = pointsY;
        Lat = lat;
        Lon = lon;
        H = h;
        WdHSeCd = wdHSeCd;
        WdEh = wdEh;
        MeridianSucha = meridianSucha;
        InstlRsltAcptncYn = instlRsltAcptncYn;
        InstlRsltAcptncDe = instlRsltAcptncDe;
        InstlRsltAcptncJgrc = instlRsltAcptncJgrc;
        InstlRsltAcptncNm = instlRsltAcptncNm;
        InstlRsltAcptncRtrnResn = instlRsltAcptncRtrnResn;
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
        SttusRsltAcptncYn = sttusRsltAcptncYn;
        SttusRsltAcptncDe = sttusRsltAcptncDe;
        SttusRsltAcptncJgrc = sttusRsltAcptncJgrc;
        SttusRsltAcptncNm = sttusRsltAcptncNm;
        SttusRsltAcptncRtrnResn = sttusRsltAcptncRtrnResn;
        InqMtrEtc = inqMtrEtc;
        MtrSttusEtc = mtrSttusEtc;
        RtkPosblEtc = rtkPosblEtc;
        WdTrgnptCd = wdTrgnptCd;
        WdPointsX = wdPointsX;
        WdPointsY = wdPointsY;
        WdLat = wdLat;
        WdLon = wdLon;
        WdH = wdH;
        LocplcDetail = locplcDetail;
    }

    public String getWdTrgnptCd() {
        return WdTrgnptCd;
    }

    public void setWdTrgnptCd(String wdTrgnptCd) {
        WdTrgnptCd = wdTrgnptCd;
    }


    public String getWdLon() {
        return WdLon;
    }

    public void setWdLon(String wdLon) {
        WdLon = wdLon;
    }

    public String getWdLat() {
        return WdLat;
    }

    public void setWdLat(String wdLat) {
        WdLat = wdLat;
    }

    public String getWdPointsX() {
        return WdPointsX;
    }

    public void setWdPointsX(String wdPointsX) {
        WdPointsX = wdPointsX;
    }

    public String getWdPointsY() {
        return WdPointsY;
    }

    public void setWdPointsY(String wdPointsY) {
        WdPointsY = wdPointsY;
    }

    public String getWdH() {
        return WdH;
    }

    public void setWdH(String wdH) {
        WdH = wdH;
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

    public String getPointsNo() {
        return PointsNo;
    }

    public void setPointsNo(String pointsNo) {
        PointsNo = pointsNo;
    }

    public String getMlineGradCd() {
        return MlineGradCd;
    }

    public void setMlineGradCd(String mlineGradCd) {
        MlineGradCd = mlineGradCd;
    }

    public String getMtrCd() {
        return MtrCd;
    }

    public void setMtrCd(String mtrCd) {
        MtrCd = mtrCd;
    }

    public String getInstlSeCd() {
        return InstlSeCd;
    }

    public void setInstlSeCd(String instlSeCd) {
        InstlSeCd = instlSeCd;
    }

    public String getBisSeCd() {
        return BisSeCd;
    }

    public void setBisSeCd(String bisSeCd) {
        BisSeCd = bisSeCd;
    }

    public String getBisNm() {
        return BisNm;
    }

    public void setBisNm(String bisNm) {
        BisNm = bisNm;
    }

    public String getInstlDe() {
        return InstlDe;
    }

    public void setInstlDe(String instlDe) {
        InstlDe = instlDe;
    }

    public String getInstlInsttCd() {
        return InstlInsttCd;
    }

    public void setInstlInsttCd(String instlInsttCd) {
        InstlInsttCd = instlInsttCd;
    }

    public String getInstlPsitn() {
        return InstlPsitn;
    }

    public void setInstlPsitn(String instlPsitn) {
        InstlPsitn = instlPsitn;
    }

    public String getInstlClsf() {
        return InstlClsf;
    }

    public void setInstlClsf(String instlClsf) {
        InstlClsf = instlClsf;
    }

    public String getInstlNm() {
        return InstlNm;
    }

    public void setInstlNm(String instlNm) {
        InstlNm = instlNm;
    }

    public String getDrwNo() {
        return DrwNo;
    }

    public void setDrwNo(String drwNo) {
        DrwNo = drwNo;
    }

    public String getMsurpointsLcDrw() {
        return MsurpointsLcDrw;
    }

    public void setMsurpointsLcDrw(String msurpointsLcDrw) {
        MsurpointsLcDrw = msurpointsLcDrw;
    }

    public String getMsurpointsLcMap() {
        return MsurpointsLcMap;
    }

    public void setMsurpointsLcMap(String msurpointsLcMap) {
        MsurpointsLcMap = msurpointsLcMap;
    }

    public String getAdmCd() {
        return AdmCd;
    }

    public void setAdmCd(String admCd) {
        AdmCd = admCd;
    }

    public String getLocplcLnm() {
        return LocplcLnm;
    }

    public void setLocplcLnm(String locplcLnm) {
        LocplcLnm = locplcLnm;
    }

    public String getLocplcRdNm() {
        return LocplcRdNm;
    }

    public void setLocplcRdNm(String locplcRdNm) {
        LocplcRdNm = locplcRdNm;
    }

    public String getNearbyLocplcLnm() {
        return NearbyLocplcLnm;
    }

    public void setNearbyLocplcLnm(String nearbyLocplcLnm) {
        NearbyLocplcLnm = nearbyLocplcLnm;
    }

    public String getNearbyLocplcRdNm() {
        return NearbyLocplcRdNm;
    }

    public void setNearbyLocplcRdNm(String nearbyLocplcRdNm) {
        NearbyLocplcRdNm = nearbyLocplcRdNm;
    }

    public String getNotifyNo() {
        return NotifyNo;
    }

    public void setNotifyNo(String notifyNo) {
        NotifyNo = notifyNo;
    }

    public String getNotifyDe() {
        return NotifyDe;
    }

    public void setNotifyDe(String notifyDe) {
        NotifyDe = notifyDe;
    }

    public String getRsltDocFile() {
        return RsltDocFile;
    }

    public void setRsltDocFile(String rsltDocFile) {
        RsltDocFile = rsltDocFile;
    }

    public String getSurvMth() {
        return SurvMth;
    }

    public void setSurvMth(String survMth) {
        SurvMth = survMth;
    }

    public String getCalMth() {
        return CalMth;
    }

    public void setCalMth(String calMth) {
        CalMth = calMth;
    }

    public String getCalPsitn() {
        return CalPsitn;
    }

    public void setCalPsitn(String calPsitn) {
        CalPsitn = calPsitn;
    }

    public String getCalClsf() {
        return CalClsf;
    }

    public void setCalClsf(String calClsf) {
        CalClsf = calClsf;
    }

    public String getCalNm() {
        return CalNm;
    }

    public void setCalNm(String calNm) {
        CalNm = calNm;
    }

    public String getMlineNm() {
        return MlineNm;
    }

    public void setMlineNm(String mlineNm) {
        MlineNm = mlineNm;
    }

    public String getKspFile() {
        return KspFile;
    }

    public void setKspFile(String kspFile) {
        KspFile = kspFile;
    }

    public String getCroPoint() {
        return CroPoint;
    }

    public void setCroPoint(String croPoint) {
        CroPoint = croPoint;
    }

    public String getUseBhfPoints() {
        return UseBhfPoints;
    }

    public void setUseBhfPoints(String useBhfPoints) {
        UseBhfPoints = useBhfPoints;
    }

    public String getBeginPoints() {
        return BeginPoints;
    }

    public void setBeginPoints(String beginPoints) {
        BeginPoints = beginPoints;
    }

    public String getBeginMachPoints() {
        return BeginMachPoints;
    }

    public void setBeginMachPoints(String beginMachPoints) {
        BeginMachPoints = beginMachPoints;
    }

    public String getArrvPoints() {
        return ArrvPoints;
    }

    public void setArrvPoints(String arrvPoints) {
        ArrvPoints = arrvPoints;
    }

    public String getArrvMachPoints() {
        return ArrvMachPoints;
    }

    public void setArrvMachPoints(String arrvMachPoints) {
        ArrvMachPoints = arrvMachPoints;
    }

    public String getBeforePoints() {
        return BeforePoints;
    }

    public void setBeforePoints(String beforePoints) {
        BeforePoints = beforePoints;
    }

    public String getAfterPoints() {
        return AfterPoints;
    }

    public void setAfterPoints(String afterPoints) {
        AfterPoints = afterPoints;
    }

    public String getAzimuth() {
        return Azimuth;
    }

    public void setAzimuth(String azimuth) {
        Azimuth = azimuth;
    }

    public Double getDstnc() {
        return Dstnc;
    }

    public void setDstnc(Double dstnc) {
        Dstnc = dstnc;
    }

    public Double getDistX() {
        return DistX;
    }

    public void setDistX(Double distX) {
        DistX = distX;
    }

    public Double getDistY() {
        return DistY;
    }

    public void setDistY(Double distY) {
        DistY = distY;
    }

    public String getTrgnptCd() {
        return TrgnptCd;
    }

    public void setTrgnptCd(String trgnptCd) {
        TrgnptCd = trgnptCd;
    }

    public Double getPointsX() {
        return PointsX;
    }

    public void setPointsX(Double pointsX) {
        PointsX = pointsX;
    }

    public Double getPointsY() {
        return PointsY;
    }

    public void setPointsY(Double pointsY) {
        PointsY = pointsY;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLon() {
        return Lon;
    }

    public void setLon(String lon) {
        Lon = lon;
    }

    public Double getH() {
        return H;
    }

    public void setH(Double h) {
        H = h;
    }

    public String getWdHSeCd() {
        return WdHSeCd;
    }

    public void setWdHSeCd(String wdHSeCd) {
        WdHSeCd = wdHSeCd;
    }

    public String getWdEh() {
        return WdEh;
    }

    public void setWdEh(String wdEh) {
        WdEh = wdEh;
    }

    public String getMeridianSucha() {
        return MeridianSucha;
    }

    public void setMeridianSucha(String meridianSucha) {
        MeridianSucha = meridianSucha;
    }

    public String getInstlRsltAcptncYn() {
        return InstlRsltAcptncYn;
    }

    public void setInstlRsltAcptncYn(String instlRsltAcptncYn) {
        InstlRsltAcptncYn = instlRsltAcptncYn;
    }

    public String getInstlRsltAcptncDe() {
        return InstlRsltAcptncDe;
    }

    public void setInstlRsltAcptncDe(String instlRsltAcptncDe) {
        InstlRsltAcptncDe = instlRsltAcptncDe;
    }

    public String getInstlRsltAcptncJgrc() {
        return InstlRsltAcptncJgrc;
    }

    public void setInstlRsltAcptncJgrc(String instlRsltAcptncJgrc) {
        InstlRsltAcptncJgrc = instlRsltAcptncJgrc;
    }

    public String getInstlRsltAcptncNm() {
        return InstlRsltAcptncNm;
    }

    public void setInstlRsltAcptncNm(String instlRsltAcptncNm) {
        InstlRsltAcptncNm = instlRsltAcptncNm;
    }

    public String getInstlRsltAcptncRtrnResn() {
        return InstlRsltAcptncRtrnResn;
    }

    public void setInstlRsltAcptncRtrnResn(String instlRsltAcptncRtrnResn) {
        InstlRsltAcptncRtrnResn = instlRsltAcptncRtrnResn;
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

    public String getLocplcDetail() {
        return LocplcDetail;
    }

    public void setLocplcDetail(String locplcDetail) {
        LocplcDetail = locplcDetail;
    }

    public static RnsStdrspotUnity FindByStdrspotSn(long StdrspotSnr) {
        List<RnsStdrspotUnity> list = RnsStdrspotUnity.find(RnsStdrspotUnity.class, "STDRSPOT_SN=?", String.valueOf(StdrspotSnr));

        if(list.size() > 0)
            return list.get(0);
        else
            return null;
    }

}
