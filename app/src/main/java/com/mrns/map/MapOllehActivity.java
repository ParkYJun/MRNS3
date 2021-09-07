package com.mrns.map;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.mrns.main.R;

import ktmap.android.map.KMap;

public class MapOllehActivity extends AppCompatActivity {

    KMap kMapView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_olleh);


        kMapView = (KMap) findViewById(R.id.Ollehmap);

        //줌레벨 설정
        kMapView.setZoomLevel(11);
        kMapView.setSatellite(false);
        kMapView.setHybrid(false);


    }



}
