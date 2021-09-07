package com.mrns.notice;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class NoticeRecord {
    private int mobileNoticeSn;
    private String userPositionCode;
    private String registEmpUserNo;
    private String registUserName;
    private String noticeTitle;
    private String noticeContents;
    private String registDate;
    private String updateDate;
    private String updateEmpUserNo;
    private String noticeBeginDate;
    private String noticeCompleteDate;
    private int readCount;

    public NoticeRecord() {};

    public NoticeRecord(JSONObject jsonObject) {
        try {
            mobileNoticeSn = jsonObject.getInt("mobile_notice_mgt_sn");
            userPositionCode = jsonObject.getString("user_psitn_cd");
            registEmpUserNo = jsonObject.getString("rgs_empuno");
            registUserName = jsonObject.getString("rgs_user_name");
            noticeTitle = jsonObject.getString("notice_sj");
            noticeContents = jsonObject.getString("notice_cn");
            registDate = jsonObject.getString("rgsdt");
            updateDate = jsonObject.getString("update_dt");
            updateEmpUserNo = jsonObject.getString("update_empuno");
            noticeBeginDate = jsonObject.getString("notice_bgn_dt");
            noticeCompleteDate = jsonObject.getString("notice_compt_dt");
            readCount = jsonObject.getInt("rdcnt");
        } catch (JSONException e) {
            Log.e("JSonException", e.getMessage());
        } finally {
            Log.i("done", "investUnityJsonPaser done");
        }
    }

    public int getMobileNoticeSn() {
        return mobileNoticeSn;
    }

    public void setMobileNoticeSn(int mobileNoticeSn) {
        this.mobileNoticeSn = mobileNoticeSn;
    }

    public String getUserPositionCode() {
        return userPositionCode;
    }

    public void setUserPositionCode(String userPositionCode) {
        this.userPositionCode = userPositionCode;
    }

    public String getRegistEmpUserNo() {
        return registEmpUserNo;
    }

    public void setRegistEmpUserNo(String registEmpUserNo) {
        this.registEmpUserNo = registEmpUserNo;
    }

    public String getRegistUserName() {
        return registUserName;
    }

    public void setRegistUserName(String registUserName) {
        this.registUserName = registUserName;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContents() {
        return noticeContents;
    }

    public void setNoticeContents(String noticeContents) {
        this.noticeContents = noticeContents;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateEmpUserNo() {
        return updateEmpUserNo;
    }

    public void setUpdateEmpUserNo(String updateEmpUserNo) {
        this.updateEmpUserNo = updateEmpUserNo;
    }

    public String getNoticeBeginDate() {
        return noticeBeginDate;
    }

    public void setNoticeBeginDate(String noticeBeginDate) {
        this.noticeBeginDate = noticeBeginDate;
    }

    public String getNoticeCompleteDate() {
        return noticeCompleteDate;
    }

    public void setNoticeCompleteDate(String noticeCompleteDate) {
        this.noticeCompleteDate = noticeCompleteDate;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
