package com.mrns.entity;

import java.util.ArrayList;

/**
 * Created by eined on 2016. 1. 21..
 */
public class LoginEntity {
    ArrayList<Data> data;

    boolean resultCode;
    String error;

    public String getError() {

        if(error != null){
            return error;
        }else{
            return "";
        }

    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isResultCode() {
        return resultCode;
    }

    public void setResultCode(boolean resultCode) {
        this.resultCode = resultCode;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(
            ArrayList<Data> data) {
        this.data = data;
    }

    public static class Data{

        String zip;
        String customerno;
        String tel;
        String userid;
        String brsize;
        String idnumber;
        String ptsize;
        String username;
        String addr1;
        String email;
        String addr2;
        String roadzip;
        String roadaddr1;
        String roadaddr2;
        String mobile;
        String bsmsrcv;
        String bmailrcv;

        String totpoint;
        String rank;

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getCustomerno() {
            return customerno;
        }

        public void setCustomerno(String customerno) {
            this.customerno = customerno;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getBrsize() {
            return brsize;
        }

        public void setBrsize(String brsize) {
            this.brsize = brsize;
        }

        public String getIdnumber() {
            return idnumber;
        }

        public void setIdnumber(String idnumber) {
            this.idnumber = idnumber;
        }

        public String getPtsize() {
            return ptsize;
        }

        public void setPtsize(String ptsize) {
            this.ptsize = ptsize;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public String getRoadzip() {
            return roadzip;
        }

        public void setRoadzip(String roadzip) {
            this.roadzip = roadzip;
        }

        public String getRoadaddr1() {
            return roadaddr1;
        }

        public void setRoadaddr1(String roadaddr1) {
            this.roadaddr1 = roadaddr1;
        }

        public String getRoadaddr2() {
            return roadaddr2;
        }

        public void setRoadaddr2(String roadaddr2) {
            this.roadaddr2 = roadaddr2;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBsmsrcv() {
            return bsmsrcv;
        }

        public void setBsmsrcv(String bsmsrcv) {
            this.bsmsrcv = bsmsrcv;
        }

        public String getBmailrcv() {
            return bmailrcv;
        }

        public void setBmailrcv(String bmailrcv) {
            this.bmailrcv = bmailrcv;
        }

        public String getTotpoint() {
            return totpoint;
        }

        public void setTotpoint(String totpoint) {
            this.totpoint = totpoint;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }
}
