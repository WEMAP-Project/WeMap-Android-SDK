package asia.wemap.androidsdktestapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import asia.wemap.androidsdk.CameraPosition;
import asia.wemap.androidsdk.OnLocationReadyCallback;
import asia.wemap.androidsdk.WeMap;
import asia.wemap.androidsdk.WeMapMap;
import asia.wemap.androidsdk.WeMapView;
import asia.wemap.androidsdk.WeMapOptions;
import asia.wemap.androidsdk.geometry.LatLng;
import asia.wemap.androidsdk.permissions.PermissionsListener;
import asia.wemap.androidsdk.permissions.PermissionsManager;

public class MainActivity extends AppCompatActivity implements PermissionsListener {

    private WeMapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        WeMap.getInstance(this, getString(R.string.access_token));
        LatLng center =  new LatLng(0, 0);
        WeMapOptions options = new WeMapOptions(this, center, 0);

        mapView = new WeMapView(this, options);
        mapView.onCreate(savedInstanceState);

        mapView.getWeMapMapAsync(new WeMapMap.OnWeMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull WeMapMap wemapMap) {


//                wemapMap.enableLocationPermission(MainActivity.this, MainActivity.this);
                wemapMap.addOnMapClickListener(new WeMapMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(LatLng point) {
                        Toast.makeText(MainActivity.this, "Map click " + point.toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                } );
                wemapMap.addOnMapLongClickListener(new WeMapMap.OnMapLongClickListener() {
                    @Override
                    public boolean onMapLongClick(LatLng point) {
                        Toast.makeText(MainActivity.this, "Map long click " + point.toString(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                CameraPosition position = new CameraPosition(new LatLng(21.0266469, 105.7615744), 12, 0, 0);
                wemapMap.animateCamera(position, 7000);
            }
        });
        setContentView(mapView);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "This app needs location permissions to show its functionality.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapView.getWeMapMapAsync(new WeMapMap.OnWeMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull WeMapMap wemapMap) {
                    wemapMap.enableLocationComponent(MainActivity.this);
                    wemapMap.getLastLocation(MainActivity.this, new OnLocationReadyCallback() {
                        @Override
                        public void onFailure(Exception exception) {
                            Log.d("Error", exception.toString());
                        }

                        @Override
                        public void onSuccess(Object result) {
                            Log.d("Draku", result.toString());
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "You didn\\'t grant location permissions.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
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