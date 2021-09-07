package com.mrns.invest.record;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by sooinsoft on 2018-01-08.
 */
@Table
public class RnsNpSubPath extends SugarRecord {

    private   long NpuSn;
    private   long NpspSn;
    private String GeodeticSurvey;
    private String SubPointsNm;
    private String SubPointsPath;
    private Double RelativeHeight;
    private String Lat;
    private String Lon;


    public RnsNpSubPath(){}

    public RnsNpSubPath(
            long npu_sn,
            long npsp_sn,
            String geodetic_survey,
            String sub_points_nm,
            String sub_points_path,
            Double relative_height,
            String lat,
            String lon
    )
    {
        NpuSn				=	npu_sn;
        NpspSn              =	npsp_sn;
        GeodeticSurvey 		=	geodetic_survey;
        SubPointsNm         =   sub_points_nm;
        SubPointsPath       =   sub_points_path;
        RelativeHeight      =   relative_height;
        Lat 				=	lat;
        Lon 				=	lon;
    }

    public long getNpuSn() {
        return NpuSn;
    }

    public void setNpuSn(long npuSn) {
        NpuSn = npuSn;
    }

    public long getNpspSn() {
        return NpspSn;
    }

    public void setNpspSn(long npspSn) {
        NpspSn = npspSn;
    }

    public String getGeodeticSurvey() {
        return GeodeticSurvey;
    }

    public void setGeodeticSurvey(String geodeticSurvey) {
        GeodeticSurvey = geodeticSurvey;
    }

    public String getSubPointsNm() {
        return SubPointsNm;
    }

    public void setSubPointsNm(String subPointsNm) {
        SubPointsNm = subPointsNm;
    }

    public String getSubPointsPath() {
        return SubPointsPath;
    }

    public void setSubPointsPath(String subPointsPath) {
        SubPointsPath = subPointsPath;
    }

    public Double getRelativeHeight() {
        return RelativeHeight;
    }

    public void setRelativeHeight(Double relativeHeight) {
        RelativeHeight = relativeHeight;
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

    public static RnsNpSubPath FindByNpuSn(long npuSn) {
        List<RnsNpSubPath> list = RnsNpSubPath.find(RnsNpSubPath.class, "NPU_SN=?", String.valueOf(npuSn));


        if(list.size() > 0) {
            return list.get(0);
        }else {
            return null;
        }
    }
}