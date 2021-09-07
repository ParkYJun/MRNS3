package com.mrns.manager;

import android.os.IBinder;

import com.raonsecure.touchen_mguard_4_0.MDMAPI;

public class AppManager {
    private static AppManager manager;
    public static AppManager getInstance() {
        if (manager == null) {
            manager = new AppManager();
        }
        return manager;
    }

    private IBinder service;
    private MDMAPI _mdm;

    public MDMAPI get_mdm() {
        return _mdm;
    }

    public void set_mdm(MDMAPI _mdm) {
        this._mdm = _mdm;
    }


    public IBinder getService() {
        return service;
    }

    public void setService(IBinder service) {
        this.service = service;
    }



}
