package com.mrns.entity.Code;

import java.util.ArrayList;

/**
 * Created by eined on 2016. 1. 26..
 */
public class CodeListEntity {
    boolean resultCode;

    ArrayList<DataEntity> data;

    public boolean isResultCode() {
        return resultCode;
    }

    public void setResultCode(boolean resultCode) {
        this.resultCode = resultCode;
    }

    public ArrayList<DataEntity> getData() { return this.data; }

    public void setData(ArrayList<DataEntity> data) {
        this.data = data;
    }



    public class DataEntity {
        String CODE_CL_NM;
        String CODE_CLCD;

        public String getCODE_CL_NM() {
            return CODE_CL_NM;
        }

        public void setCODE_CL_NM(String CODE_CL_NM) {
            if(CODE_CL_NM == null || CODE_CL_NM.equals("") || CODE_CL_NM.equals(null)){
                CODE_CL_NM = "";
            }
            this.CODE_CL_NM = CODE_CL_NM;
        }

        public String getCODE_CLCD() {
            return CODE_CLCD;
        }

        public void setCODE_CLCD(String CODE_CLCD) {
            if(CODE_CLCD == null || CODE_CLCD.equals("") || CODE_CLCD.equals(null)){
                CODE_CLCD = "";
            }
            this.CODE_CLCD = CODE_CLCD;
        }
    }
}
