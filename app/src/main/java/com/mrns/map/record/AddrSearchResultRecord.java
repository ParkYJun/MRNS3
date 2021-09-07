package com.mrns.map.record;

public class AddrSearchResultRecord {
    private String objectid;
    private String address;

    public AddrSearchResultRecord() {
    }

    public AddrSearchResultRecord(String objectid, String address) {
        this.objectid = objectid;
        this.address = address;
    }

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
