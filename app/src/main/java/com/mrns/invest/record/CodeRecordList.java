package com.mrns.invest.record;

import java.util.ArrayList;

public class CodeRecordList {
    private ArrayList<String> codeList;
    private ArrayList<String> codeValueList;
    private ArrayList<String> additionalcodeList;

    public CodeRecordList() {
    }

    public CodeRecordList(ArrayList<String> codeList, ArrayList<String> codeValueList) {
        this.codeList = codeList;
        this.codeValueList = codeValueList;
    }

    public CodeRecordList(ArrayList<String> codeList, ArrayList<String> codeValueList, ArrayList<String> additionalcodeList) {
        this.codeList = codeList;
        this.codeValueList = codeValueList;
        this.additionalcodeList = additionalcodeList;
    }

    public ArrayList<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(ArrayList<String> codeList) {
        this.codeList = codeList;
    }

    public ArrayList<String> getCodeValueList() {
        return codeValueList;
    }

    public void setCodeValueList(ArrayList<String> codeValueList) {
        this.codeValueList = codeValueList;
    }

    public ArrayList<String> getAdditionalcodeList() {
        return additionalcodeList;
    }

    public void setAdditionalcodeList(ArrayList<String> additionalcodeList) {
        this.additionalcodeList = additionalcodeList;
    }
}
