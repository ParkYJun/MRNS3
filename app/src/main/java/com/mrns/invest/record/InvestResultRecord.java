package com.mrns.invest.record;

public class InvestResultRecord {
    private long SttusBfSn;
    private long StdrspotSn;    //SN
    private String PointsKnd;   //종류
    private String pointsNm;    //점의번호
    private String MtrSttusCd;  //상태
    private String InqDe;     //조사일
    private String Inq_sttus;   //조사상태
    private String PointsX;     //위치X
    private String Points_Y;    //위치Y
    private String BeMtrSttusCd;    //조사후 상태코드
    private String BeInqDe;         //조사후 조사일

    public InvestResultRecord(long sttusBfSn, long stdrspotSn, String pointsKnd, String pointsNm, String mtrSttusCd, String instlDe, String inq_sttus, String pointsX, String points_Y, String BeMtrSttusCd, String BeInqDe  ) {
        SttusBfSn = sttusBfSn;
        StdrspotSn = stdrspotSn;
        PointsKnd = pointsKnd;
        this.pointsNm = pointsNm;
        MtrSttusCd = mtrSttusCd;

        if(instlDe == null || instlDe.equalsIgnoreCase("null") || instlDe.equals("")) {
            InqDe = "-";
        }else{
//            InstlDe = instlDe;
            String[] tempDate = instlDe.split("-");
            InqDe = tempDate[0].substring(2,4) + "-" + tempDate[1] + "-" + tempDate[2];
        }
        Inq_sttus = inq_sttus;
        PointsX = pointsX;
        Points_Y = points_Y;
        this.BeMtrSttusCd = BeMtrSttusCd;

        if(BeInqDe == null || BeInqDe.equalsIgnoreCase("null") || BeInqDe.equals("")) {
            this.BeInqDe = "-";
        }else{
//            this.BeInqDe = BeInqDe;
            String[] tempDate = BeInqDe.split("-");
            this.BeInqDe = tempDate[0].substring(2,4) + "-" + tempDate[1] + "-" + tempDate[2];
        }
    }

    public long getSttusBfSn() {
        return SttusBfSn;
    }

    public void setSttusBfSn(long sttusBfSn) {
        SttusBfSn = sttusBfSn;
    }

    public long getStdrspotSn() {
        return StdrspotSn;
    }

    public void setStdrspotSn(long stdrspotSn) {
        StdrspotSn = stdrspotSn;
    }

    public String getPointsKnd() {
        return PointsKnd;
    }

    public void setPointsKnd(String pointsKnd) {
        PointsKnd = pointsKnd;
    }

    public String getPointsNm() {
        return pointsNm;
    }

    public void setPointsNm(String pointsNm) {
        this.pointsNm = pointsNm;
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

    public String getInq_sttus() {
        return Inq_sttus;
    }

    public void setInq_sttus(String inq_sttus) {
        Inq_sttus = inq_sttus;
    }

    public String getPointsX() {
        return PointsX;
    }

    public void setPointsX(String pointsX) {
        PointsX = pointsX;
    }

    public String getPoints_Y() {
        return Points_Y;
    }

    public void setPoints_Y(String points_Y) {
        Points_Y = points_Y;
    }

    public String getBeMtrSttusCd() {
        return BeMtrSttusCd;
    }

    public void setBeMtrSttusCd(String beMtrSttusCd) {
        BeMtrSttusCd = beMtrSttusCd;
    }

    public String getBeInqDe() {
        return BeInqDe;
    }

    public void setBeInqDe(String beInqDe) {
        BeInqDe = beInqDe;
    }
}
