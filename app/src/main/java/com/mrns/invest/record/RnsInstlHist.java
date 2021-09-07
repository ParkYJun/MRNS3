package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

@Table
public class RnsInstlHist extends SugarRecord {
    long InstlIhSn ;
    long StdrspotSn ;
    String BasePointNo;
    String PointsKndCd ;
    String PointsNm;
    String JgrcCd;
    String BrhCd;
    String PointsNo;
    String MlineGradCd ;
    String MtrCd ;
    String InstlSeCd ;
    String BisSeCd ;
    String BisNm;
    String InstlDe ;
    String InstlInsttCd ;
    String InstlPsitn;
    String InstlClsf;
    String InstlNm;
    String DrwNo;
    String MsurpointsLcDrw;
    String MsurpointsLcMap;
    String AdmCd;
    String LocplcLnm;
    String LocplcRdNm;
    String NearbyLocplcLnm;
    String NearbyLocplcRdNm;
    String NotifyNo;
    String NotifyDe ;
    String RsltDocFile;
    String SurvMth ;
    String CalMth ;
    String CalPsitn;
    String CalClsf;
    String CalNm;
    String MlineNm;
    String KspFile;
    String CroPoint;
    String UseBhfPoints;
    String BeginPoints;
    String BeginMachPoints;
    String ArrvPoints;
    String ArrvMachPoints;
    String BeforePoints;
    String AfterPoints;
    String Dstnc;
    String Azimuth;
    String DistX;
    String DistY;
    String TrgnptCd ;
    String PointsX;
    String PointsY;
    String Lat;
    String Lon;
    String H;
    String WdHSeCd ;
    String WdEh;
    String MeridianSucha;
    String InstlRsltAcptncYn;
    String InstlRsltAcptncDe ;
    String InstlRsltAcptncJgrc;
    String InstlRsltAcptncNm;
    String InstlRsltAcptncRtrnResn;

    String WdTrgnptCd;
    String WdPointsX;
    String WdPointsY;
    String WdLat;
    String WdLon;
    String WdH;

    public RnsInstlHist() {
    }

    public RnsInstlHist(
            long instlIhSn,
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
            String dstnc,
            String azimuth,
            String distX,
            String distY,
            String trgnptCd,
            String pointsX,
            String pointsY,
            String lat,
            String lon,
            String h,
            String wdHSeCd,
            String wdEh,
            String meridianSucha,
            String instlRsltAcptncYn,
            String instlRsltAcptncDe,
            String instlRsltAcptncJgrc,
            String instlRsltAcptncNm,
            String instlRsltAcptncRtrnResn,
            String wdTrgnptCd,
            String wdPointsX,
            String wdPointsY,
            String wdLat,
            String wdLon,
            String wdH)
    {
        InstlIhSn = instlIhSn;
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
        Dstnc = dstnc;
        Azimuth = azimuth;
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

        WdTrgnptCd = wdTrgnptCd;
        WdPointsX = wdPointsX;
        WdPointsY = wdPointsY;
        WdLat = wdLat;
        WdLon = wdLon;
        WdH = wdH;
    }

    public String getWdTrgnptCd() {
        return WdTrgnptCd;
    }

    public void setWdTrgnptCd(String wdTrgnptCd) {
        WdTrgnptCd = wdTrgnptCd;
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

    public String getWdLat() {
        return WdLat;
    }

    public void setWdLat(String wdLat) {
        WdLat = wdLat;
    }

    public String getWdLon() {
        return WdLon;
    }

    public void setWdLon(String wdLon) {
        WdLon = wdLon;
    }

    public String getWdH() {
        return WdH;
    }

    public void setWdH(String wdH) {
        WdH = wdH;
    }

    public long getInstlIhSn() {
        return InstlIhSn;
    }

    public void setInstlIhSn(long instlIhSn) {
        InstlIhSn = instlIhSn;
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

    public String getDstnc() {
        return Dstnc;
    }

    public void setDstnc(String dstnc) {
        Dstnc = dstnc;
    }

    public String getAzimuth() {
        return Azimuth;
    }

    public void setAzimuth(String azimuth) {
        Azimuth = azimuth;
    }

    public String getDistX() {
        return DistX;
    }

    public void setDistX(String distX) {
        DistX = distX;
    }

    public String getDistY() {
        return DistY;
    }

    public void setDistY(String distY) {
        DistY = distY;
    }

    public String getTrgnptCd() {
        return TrgnptCd;
    }

    public void setTrgnptCd(String trgnptCd) {
        TrgnptCd = trgnptCd;
    }

    public String getPointsX() {
        return PointsX;
    }

    public void setPointsX(String pointsX) {
        PointsX = pointsX;
    }

    public String getPointsY() {
        return PointsY;
    }

    public void setPointsY(String pointsY) {
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

    public String getH() {
        return H;
    }

    public void setH(String h) {
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

    public static RnsInstlHist FindByInstlIhSn(long instl_ih_sn) {
        List<RnsInstlHist> list = RnsInstlHist.find(RnsInstlHist.class, "INSTL_IH_SN=?", String.valueOf(instl_ih_sn));
        if(list.size() > 0)
            return list.get(0);
        else
            return null;
    }
}
