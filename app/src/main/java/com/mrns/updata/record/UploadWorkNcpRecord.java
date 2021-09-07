package com.mrns.updata.record;

/**
 * Created by geopeople on 2016-05-18.
 */
public class UploadWorkNcpRecord {

    boolean isSelectecd;    //checkbox
    long NpuSn;         //SN
    String PointsKnd;       //종류
    String PointsNm;        //점의명칭
    String MtrSttusCd;      //상태
    String InqDe;           //조사일
    String Inq_sttus;        //조사상태


    public UploadWorkNcpRecord(){}

    public UploadWorkNcpRecord(
        long npuSn,
        String pointsKnd,
        String pointsNm,
        String mtrSttusCd,
        String inqDe,
        String inq_sttus,
        boolean isselected
    ) {
        NpuSn = npuSn;
        PointsKnd = pointsKnd;
        PointsNm = pointsNm;
        MtrSttusCd = mtrSttusCd;
        InqDe = inqDe;
        Inq_sttus = inq_sttus;
        this.isSelectecd = isselected;
    }

    public long getNpuSn() {
        return NpuSn;
    }

    public void setNpuSn(long npuSn) {
        NpuSn = npuSn;
    }

    public boolean isSelectecd() {
        return isSelectecd;
    }

    public void setIsSelectecd(boolean isSelectecd) {
        this.isSelectecd = isSelectecd;
    }

    public String getPointsKnd() {
        return PointsKnd;
    }

    public void setPointsKnd(String pointsKnd) {
        PointsKnd = pointsKnd;
    }

    public String getInq_sttus() {
        return Inq_sttus;
    }

    public void setInq_sttus(String inq_sttus) {
        Inq_sttus = inq_sttus;
    }

    public String getPointsNm() {
        return PointsNm;
    }

    public void setPointsNm(String pointsNm) {
        PointsNm = pointsNm;
    }

    public String getMtrSttusCd() {
        return MtrSttusCd;
    }

    public void setMtrSttusCd(String mtrSttusCd) {
        MtrSttusCd = mtrSttusCd;
    }

    public String getInqDe() {
        return InqDe;
    }

    public void setInqDe(String inqDe) {
        InqDe = inqDe;
    }
}
