package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by sooinsoft on 2018-01-08.
 */
@Table
public class RnsNpRslt extends SugarRecord {

    private   long NpuSn;
    private   long NprSn;
    private String TrgnptCd;
    private Double PointsX;
    private Double PointsY;
    private String Lat;
    private String Lon;
    private Double H;
    private String Eh;
    private Double Gravity;
    private String MeridianSucha;
    private String GeodeticSurvey;

    public RnsNpRslt(){}

    public RnsNpRslt(
            long npu_sn,
            long npr_sn,
            String trgnpt_cd,
            Double points_x,
            Double points_y,
            String lat,
            String lon,
            Double h,
            String eh,
            Double gravity,
            String meridian_sucha,
            String geodetic_survey
    )
    {
        NpuSn					=	npu_sn;
        NprSn                   =	npr_sn;
        TrgnptCd 				=	trgnpt_cd;
        PointsX                =   points_x;
        PointsY 			=	points_y;
        Lat 					=	lat;
        Lon 				=	lon;
        H 				=	h;
        Eh 			=	eh;
        Gravity 				=	gravity;
        MeridianSucha 				=	meridian_sucha;
        GeodeticSurvey 					=	geodetic_survey;

    }

    public long getNpuSn() {
        return NpuSn;
    }

    public void setNpuSn(long npuSn) {
        NpuSn = npuSn;
    }

    public long getNprSn() {
        return NprSn;
    }

    public void setNprSn(long nprSn) {
        NprSn = nprSn;
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

    public String getEh() {
        return Eh;
    }

    public void setEh(String eh) {
        Eh = eh;
    }

    public Double getGravity() {
        return Gravity;
    }

    public void setGravity(Double gravity) {
        Gravity = gravity;
    }

    public String getMeridianSucha() {
        return MeridianSucha;
    }

    public void setMeridianSucha(String meridianSucha) {
        MeridianSucha = meridianSucha;
    }

    public String getGeodeticSurvey() {
        return GeodeticSurvey;
    }

    public void setGeodeticSurvey(String geodeticSurvey) {
        GeodeticSurvey = geodeticSurvey;
    }

    public static RnsNpRslt FindByNpuSn(long npuSn) {
        List<RnsNpRslt> list = RnsNpRslt.find(RnsNpRslt.class, "NPU_SN=?", String.valueOf(npuSn));


        if(list.size() > 0) {
            return list.get(0);
        }else {
            return null;
        }
    }
}