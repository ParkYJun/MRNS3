package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by sooinsoft on 2018-01-08.
 */
@Table
public class RnsNpTraverRslt extends SugarRecord {

    private   long NpuSn;
    private   long NptrSn;
    private String GeodeticSurvey;
    private String GPointsNm;
    private String Lat;
    private String Lon;
    private String Eh;
    private String Azimuth;
    private String DirectionAngle;
    private String Dstnc;


    public RnsNpTraverRslt(){}

    public RnsNpTraverRslt(
            long npu_sn,
            long nptr_sn,
            String geodetic_survey,
            String g_points_nm,
            String lat,
            String lon,
            String eh,
            String azimuth,
            String direction_angle,
            String dstnc
    )
    {
        NpuSn				=	npu_sn;
        NptrSn              =	nptr_sn;
        GeodeticSurvey 		=	geodetic_survey;
        GPointsNm           =   g_points_nm;
        Lat 				=	lat;
        Lon 				=	lon;
        Eh 			        =	eh;
        Azimuth             =   azimuth;
        DirectionAngle 		=	direction_angle;
        Dstnc 				=	dstnc;
    }

    public long getNpuSn() {
        return NpuSn;
    }

    public void setNpuSn(long npuSn) {
        NpuSn = npuSn;
    }

    public long getNptrSn() {
        return NptrSn;
    }

    public void setNptrSn(long nptrSn) {
        NptrSn = nptrSn;
    }

    public String getGeodeticSurvey() {
        return GeodeticSurvey;
    }

    public void setGeodeticSurvey(String geodeticSurvey) {
        GeodeticSurvey = geodeticSurvey;
    }

    public String getGPointsNm() {
        return GPointsNm;
    }

    public void setGPointsNm(String GPointsNm) {
        this.GPointsNm = GPointsNm;
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

    public String getEh() {
        return Eh;
    }

    public void setEh(String eh) {
        Eh = eh;
    }

    public String getAzimuth() {
        return Azimuth;
    }

    public void setAzimuth(String azimuth) {
        Azimuth = azimuth;
    }

    public String getDirectionAngle() {
        return DirectionAngle;
    }

    public void setDirectionAngle(String directionAngle) {
        DirectionAngle = directionAngle;
    }

    public String getDstnc() {
        return Dstnc;
    }

    public void setDstnc(String dstnc) {
        Dstnc = dstnc;
    }

    public static RnsNpTraverRslt FindByNpuSn(long npuSn) {
        List<RnsNpTraverRslt> list = RnsNpTraverRslt.find(RnsNpTraverRslt.class, "NPU_SN=?", String.valueOf(npuSn));


        if(list.size() > 0) {
            return list.get(0);
        }else {
            return null;
        }
    }
}