package com.mrns.map.record;

public class PointResultRecord {
    private String stdrspotSn;    //SN
    private String basePointNo;
    private String pointKindCode;   //종류
    private String pointSecd;
    private String pointName;
    private String mtrStatusCode;
    private String installDate;
    private String inqDate;
    private String npuSn;
    private String admCode;

    public PointResultRecord(String stdrspotSn, String basePointNo, String pointKindCode, String pointSecd, String pointName, String mtrStatusCode, String installDate, String inqDate, String npuSn, String admCode) {
        this.stdrspotSn = stdrspotSn;
        this.basePointNo = basePointNo;
        this.pointKindCode = pointKindCode;
        this.pointSecd = pointSecd;
        this.pointName = pointName;
        this.mtrStatusCode = mtrStatusCode;
        this.installDate = installDate;
        this.inqDate = inqDate;
        this.npuSn = npuSn;
        this.admCode = admCode;
    }

    public String getStdrspotSn() {
        return stdrspotSn;
    }

    public void setStdrspotSn(String stdrspotSn) {
        this.stdrspotSn = stdrspotSn;
    }

    public String getBasePointNo() {
        return basePointNo;
    }

    public void setBasePointNo(String basePointNo) {
        this.basePointNo = basePointNo;
    }

    public String getPointKindCode() {
        return pointKindCode;
    }

    public void setPointKindCode(String pointKindCode) {
        this.pointKindCode = pointKindCode;
    }

    public String getPointSecd() {
        return pointSecd;
    }

    public void setPointSecd(String pointSecd) {
        this.pointSecd = pointSecd;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getMtrStatusCode() {
        return mtrStatusCode;
    }

    public void setMtrStatusCode(String mtrStatusCode) {
        this.mtrStatusCode = mtrStatusCode;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public String getInqDate() {
        return inqDate;
    }

    public void setInqDate(String inqDate) {
        this.inqDate = inqDate;
    }

    public String getNpuSn() {
        return npuSn;
    }

    public void setNpuSn(String npuSn) {
        this.npuSn = npuSn;
    }
    public String getAdmCode() {
        return admCode;
    }
    public void setAdmCode(String admCode) {
        this.admCode = admCode;
    }
}
