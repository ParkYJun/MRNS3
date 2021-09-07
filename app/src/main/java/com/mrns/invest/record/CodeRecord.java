package com.mrns.invest.record;

import java.util.ArrayList;

public class CodeRecord {
    String code;
    String value;
    String headCode;

    public CodeRecord(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getHeadCode(String code) {
        return headCode;
    }
    public void setHeadCode(String headCode) {this.headCode = headCode;}


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public static int getCodePostion(ArrayList<CodeRecord> codeRecords, String code) {
        for (int i = 0; i < codeRecords.size(); i++) {
            if(codeRecords.get(i).getCode().equalsIgnoreCase(code)) {
                return i;
            }
        }
        return 0;
    }

}
