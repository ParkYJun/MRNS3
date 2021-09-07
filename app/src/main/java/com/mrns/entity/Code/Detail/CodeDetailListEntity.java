package com.mrns.entity.Code.Detail;

import java.util.ArrayList;

/**
 * Created by eined on 2016. 1. 26..
 */
public class CodeDetailListEntity {
    boolean resultCode;

    ArrayList<DataEntity> data;

    public boolean isResultCode() {
        return resultCode;
    }

    public void setResultCode(boolean resultCode) {
        this.resultCode = resultCode;
    }

    public class DataEntity {
        String OUTPT_ORDR;
        String CODE;
        String USE_YN;
        String CODE_VALUE;
        String LAST_UPDDT;

        public String getOUTPT_ORDR() {
            return OUTPT_ORDR;
        }

        public void setOUTPT_ORDR(String OUTPT_ORDR) {
            if(OUTPT_ORDR == null || OUTPT_ORDR.equals("") || OUTPT_ORDR.equals(null)){
                OUTPT_ORDR = "";
            }
            this.OUTPT_ORDR = OUTPT_ORDR;
        }

        public String getCODE() {
            return CODE;
        }

        public void setCODE(String CODE) {
            if(CODE == null || CODE.equals("") || CODE.equals(null)){
                CODE = "";
            }
            this.CODE = CODE;
        }

        public String getUSE_YN() {
            return USE_YN;
        }

        public void setUSE_YN(String USE_YN) {
            if(USE_YN == null || USE_YN.equals("") || USE_YN.equals(null)){
                USE_YN = "";
            }
            this.USE_YN = USE_YN;
        }

        public String getCODE_VALUE() {
            return CODE_VALUE;
        }

        public void setCODE_VALUE(String CODE_VALUE) {
            if(CODE_VALUE == null || CODE_VALUE.equals("") || CODE_VALUE.equals(null)){
                CODE_VALUE = "";
            }
            this.CODE_VALUE = CODE_VALUE;
        }

        public String getLAST_UPDDT() {
            return LAST_UPDDT;
        }

        public void setLAST_UPDDT(String LAST_UPDDT) {
            if(LAST_UPDDT == null || LAST_UPDDT.equals("") || LAST_UPDDT.equals(null)){
                LAST_UPDDT = "";
            }
            this.LAST_UPDDT = LAST_UPDDT;
        }
    }
}
