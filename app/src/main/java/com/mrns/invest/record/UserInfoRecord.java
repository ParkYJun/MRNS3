package com.mrns.invest.record;

public class UserInfoRecord {
    private String userId;
    private String userName;
    private String branchCode;
    private String positionCode;
    private String headOfficeCode;

    public UserInfoRecord() {
    }

    public UserInfoRecord(String userId, String userName, String branchCode, String positionCode) {
        this.userId = userId;
        this.userName = userName;
        this.branchCode = branchCode;
        this.positionCode = positionCode;
    }

    public String getHeadOfficeCode() {
        return headOfficeCode;
    }

    public void setHeadOfficeCode(String headOfficeCode) {
        this.headOfficeCode = headOfficeCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }
}
