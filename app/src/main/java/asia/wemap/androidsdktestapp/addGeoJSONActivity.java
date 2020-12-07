package asia.wemap.androidsdktestapp;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;
import java.net.URISyntaxException;

import asia.wemap.androidsdk.Style;
import asia.wemap.androidsdk.WeMap;
import asia.wemap.androidsdk.WeMapMap;
import asia.wemap.androidsdk.WeMapOptions;
import asia.wemap.androidsdk.WeMapView;
import asia.wemap.androidsdk.geometry.LatLng;
import asia.wemap.androidsdk.style.sources.GeoJSONSource;

public class addGeoJSONActivity extends AppCompatActivity {

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
//                addGeoJSONPointLayer(wemapMap);
//                addGeoJSONPolylineLayer(wemapMap);
                addGeoJSONPolygonLayer(wemapMap);
            }
        });

        setContentView(mapView);
    }

    public void addGeoJSONPointLayer(WeMapMap wemapMap){
        GeoJSONSource geoJSONSource;
        try {
            geoJSONSource = new GeoJSONSource("point-source", new URI("asset://ne_10m_ports.geojson"));
//                    wemapMap.addGeoJSONPointLayer("point-layer", geoJSONSource);
                    wemapMap.addGeoJSONPointLayer("point-layer", geoJSONSource, 10, "#3bb2d0");
//            wemapMap.addGeoJSONPointLayer("point-layer", geoJSONSource, BitmapFactory.decodeResource(getResources(), R.drawable.marker2));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void addGeoJSONPolylineLayer(WeMapMap wemapMap){
        GeoJSONSource geoJSONSource;
        try {
            geoJSONSource = new GeoJSONSource("polyline-source", new URI("https://wemap-project.github.io/WeMap-Web-SDK-Release/demo/data/data.geojson"));
//            wemapMap.addGeoJSONPolylineLayer("polyline-layer", geoJSONSource);
            wemapMap.addGeoJSONPolylineLayer("polyline-layer", geoJSONSource, 7f, .7f, "#3bb2d0");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public void addGeoJSONPolygonLayer(WeMapMap wemapMap){
        GeoJSONSource geoJSONSource;
        try {
            geoJSONSource = new GeoJSONSource("polygon-source", new URI("https://wemap-project.github.io/WeMap-Web-SDK-Release/demo/data/data.geojson"));
//            wemapMap.addGeoJSONPolygonLayer("polygon-layer", geoJSONSource);
            wemapMap.addGeoJSONPolygonLayer("polygon-layer", geoJSONSource, "#3bb2d0");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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