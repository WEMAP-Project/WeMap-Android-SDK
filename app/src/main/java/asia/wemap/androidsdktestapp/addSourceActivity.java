package asia.wemap.androidsdktestapp;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import asia.wemap.androidsdk.Style;
import asia.wemap.androidsdk.WeMap;
import asia.wemap.androidsdk.WeMapMap;
import asia.wemap.androidsdk.WeMapOptions;
import asia.wemap.androidsdk.WeMapView;
import asia.wemap.androidsdk.geometry.LatLng;


public class addSourceActivity extends AppCompatActivity {

    private WeMapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        WeMap.getInstance(this, getString(R.string.access_token));
        LatLng center =  new LatLng(21.0266469, 105.7615744);
        WeMapOptions options = new WeMapOptions(this, center);

        mapView = new WeMapView(this, options);
        mapView.onCreate(savedInstanceState);

        mapView.getWeMapMapAsync(new WeMapMap.OnWeMapReadyCallback(){
            @Override
            public void onMapReady(@NonNull WeMapMap wemapMap) {
                wemapMap.addTrafficLayer();
                //wemapMap.addRasterLayer("leaflet-layer", "https://a.tile.openstreetmap.org/{z}/{x}/{y}.png", 19, 0);
            }
        });

        setContentView(mapView);
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}