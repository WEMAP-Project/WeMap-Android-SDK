package asia.wemap.androidsdktestapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import asia.wemap.androidsdk.OnLocationReadyCallback;
import asia.wemap.androidsdk.WeDirection;
import asia.wemap.androidsdk.WeDirectionResponse;
import asia.wemap.androidsdk.WeMap;
import asia.wemap.androidsdk.WeMapMap;
import asia.wemap.androidsdk.WeMapOptions;
import asia.wemap.androidsdk.WeMapView;
import asia.wemap.androidsdk.geometry.LatLng;

public class DirectionActivity extends AppCompatActivity {

    private WeMapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        WeMap.getInstance(this, getString(R.string.access_token));

        LatLng center =  new LatLng(21.0266469, 105.7615744);
        LatLng originPoint =  new LatLng(21.052403, 105.78362);
        LatLng destinationPoint =  new LatLng(20.982317, 105.863335);

        WeMapOptions options = new WeMapOptions(this, center, 12);

        mapView = new WeMapView(this, options);
        mapView.onCreate(savedInstanceState);

        mapView.getWeMapMapAsync(new WeMapMap.OnWeMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull WeMapMap wemapMap) {
                WeDirection weDirection = new WeDirection();
                WeDirection.WeDirectionOptions weDirectionOptions = new WeDirection.WeDirectionOptions();
                weDirectionOptions.setInstructions(true);
                List<LatLng> points = new ArrayList<LatLng>();
                points.add(originPoint);
                points.add(destinationPoint);
                weDirection.route(points, weDirectionOptions, new WeDirection.WeDirectionCallBack() {
                    @Override
                    public void onReady(WeDirectionResponse weDirectionResponse) {
                        wemapMap.addDirectionLayer(weDirectionResponse);
                    }
                });
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