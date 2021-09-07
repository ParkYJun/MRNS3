package com.mrns.entity.StdrSpot;

import java.util.ArrayList;

/**
 * Created by eined on 2016. 1. 26..
 */
public class InvestListEntity {

    boolean resultCode;
    ArrayList<DataEntity> data;

    public ArrayList<DataEntity> getData() {
        return this.data;
    }

    public void setData(ArrayList<DataEntity> data) {

        this.data = data;
    }

    public boolean isResultCode() {
        return resultCode;
    }

    public void setResultCode(boolean resultCode) {
        this.resultCode = resultCode;
    }


    public class DataEntity {
        ArrayList<BaseEntity> base;
        ArrayList<FacilityEntity> facilities;
        ArrayList<CoordiEntity> coordinate;
        ArrayList<PresentEntity> present;

        public ArrayList<BaseEntity> getBase() {
            return base;
        }

        public void setBase(ArrayList<BaseEntity> base) {
            //TODO: null check base
            this.base = base;
        }

        public ArrayList<FacilityEntity> facilities() {
            return facilities;
        }

        public void setFacil(ArrayList<FacilityEntity> facilities) {
            //TODO: null check facil
            this.facilities = facilities;
        }

        public ArrayList<CoordiEntity> getCoordi() {
            return coordinate;
        }

        public void setCoordi(ArrayList<CoordiEntity> coordinate) {
            //TODO: null check coordi
            this.coordinate = coordinate;
        }

        public ArrayList<PresentEntity> getPresent() {
            return present;
        }

        public void setPresent(ArrayList<PresentEntity> present) {
            //TODO: null check present
            this.present = present;
        }
    }
}
