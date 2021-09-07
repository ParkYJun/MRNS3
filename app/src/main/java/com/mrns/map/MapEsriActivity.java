package com.mrns.map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.mrns.main.R;

import java.io.File;

public class MapEsriActivity extends AppCompatActivity {

    MapView mapView = null;
    ArcGISTiledMapServiceLayer tileLayer;
    ArcGISLocalTiledLayer localTplLayer;
    boolean activeNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_esri);

        mapView = (MapView) findViewById(R.id.map);

        // create an ArcGISTiledMapServiceLayer as a background if network available
        activeNetwork = isNetworkAvailable();
        if (activeNetwork) {
            tileLayer = new ArcGISTiledMapServiceLayer(
                    "http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");

            // Add tiled layer to MapView
//            mapView.addLayer(tileLayer);
        }else{
            Toast toast = Toast.makeText(this, R.string.offline_message, Toast.LENGTH_SHORT);
            toast.show();
        }

        // LOCAL FILE Path Set
        String path = Environment.getExternalStorageDirectory().getPath().toString();
        String fname = "/CPMS_BASEMAP.tpk";
        File f = new File(path + fname);
        if(f.exists()) {
            Toast toast = Toast.makeText(MapEsriActivity.this, path+fname, Toast.LENGTH_SHORT);
            toast.show();

            localTplLayer = new ArcGISLocalTiledLayer(path + fname);
            localTplLayer.setOpacity(0.5f);
            mapView.addLayer(localTplLayer);
        }


        // enable map to wrap around
        mapView.enableWrapAround(true);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.unpause();
    }
}
