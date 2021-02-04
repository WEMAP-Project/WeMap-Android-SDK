package asia.wemap.androidsdktestapp;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import asia.wemap.androidsdk.OnMarkerClickListener;
import asia.wemap.androidsdk.WeMap;
import asia.wemap.androidsdk.WeMapMap;
import asia.wemap.androidsdk.WeMapView;
import asia.wemap.androidsdk.WeMapOptions;
import asia.wemap.androidsdk.annotaion.WeMapMarker;
import asia.wemap.androidsdk.geometry.LatLng;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MarkerInfoActivity extends AppCompatActivity {
    private WeMapView mapView;
    private static final String ICON_ID = "ICON_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeMap.getInstance(this, getString(R.string.access_token));
        LatLng center =  new LatLng(21, 105);
        WeMapOptions options = new WeMapOptions(this, center, 4);

        mapView = new WeMapView(this, options);
        mapView.onCreate(savedInstanceState);
        setContentView(mapView);
        mapView.getWeMapMapAsync(new WeMapMap.OnWeMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull WeMapMap wemapMap) {
                wemapMap.addOnMapClickListener(new WeMapMap.OnMapClickListener() {
                    @Override
                    public boolean onMapClick(LatLng point) {
                        WeMapMarker marker = wemapMap.createMarker(new LatLng(point), ICON_ID);
                        marker.set("name", String.valueOf(point.getLatitude()) + ", " + String.valueOf(point.getLongitude()));
                        return true;
                    }
                } );

                wemapMap.onMarkerClick(new OnMarkerClickListener() {
                    @Override
                    public void OnMarkerClick(WeMapMarker marker) {
                        Log.d("test", marker.getMarkerGeometry().toString());

                        //Add markerView
                        View customView = LayoutInflater.from(MarkerInfoActivity.this).inflate(
                                R.layout.marker_view_bubble, null);
                        customView.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

                        TextView titleTextView = customView.findViewById(R.id.marker_window_title);
                        titleTextView.setText(marker.get("name"));

                        TextView snippetTextView = customView.findViewById(R.id.marker_window_snippet);
                        snippetTextView.setText("Hello, World!");
                        wemapMap.createViewMarker(marker.getMarkerGeometry(), customView);
                    }

                    @Override
                    public void OnMarkerLongClick(WeMapMarker marker) {
                        //Xoa marker
                        wemapMap.removeMarker(marker);
                        //Xoa het marker
//                        wemapMap.removeAllMarker();
                        Log.d("test", marker.getMarkerGeometry().toString());
                    }
                });

                //Add marker with image
                //Add Image
                wemapMap.addImage(ICON_ID, BitmapFactory.decodeResource(getResources(), R.drawable.marker2));
                //Add Marker
                WeMapMarker marker = wemapMap.createMarker(new LatLng(21, 105), ICON_ID);
                marker.set("name", "WeMap");

            }
        });
    }

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