package asia.wemap.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import asia.wemap.androidsdk.exceptions.WeMapConfigurationException;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;

import static android.content.Context.MODE_PRIVATE;

public class WeMapView extends FrameLayout{
    MapView mapview;
    Context context;
    WeMapMap weMapMap;

    FloatingActionButton fab;

    public WeMapView(@NonNull Context context) {
        super(context);
        MapboxMapOptions options = MapboxMapOptions.createFromAttributes(context, null).logoEnabled(false).attributionEnabled(false);
        this.mapview = new MapView(context, options);
        this.mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(asia.wemap.androidsdk.Style.WEMAP_BASIC, new com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        weMapMap = new WeMapMap(context, mapboxMap, mapview, style);
                    }
                });
            }
        });
    }

    
    /** 
     * @return MapView
     */
    protected MapView getMapView(){
        return this.mapview;
    }

    public WeMapView(@NonNull Context context, WeMapOptions options) {
        super(context);
        this.mapview = new MapView(context, options.options);
        this.mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(asia.wemap.androidsdk.Style.WEMAP_BASIC, new com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        weMapMap = new WeMapMap(context, mapboxMap, mapview, style);
                    }
                });
            }
        });
    }

    @UiThread
    public WeMapView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        MapboxMapOptions options = MapboxMapOptions.createFromAttributes(context, attrs).logoEnabled(false).attributionEnabled(false);
        this.mapview = new MapView(context, options);
        this.mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(asia.wemap.androidsdk.Style.WEMAP_BASIC, new com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        weMapMap = new WeMapMap(context, mapboxMap, mapview, style);
                    }
                });
            }
        });
    }

    
    /** 
     * @param callback
     */
    @UiThread
    public void getWeMapMapAsync(WeMapMap.OnWeMapReadyCallback callback) {
        if(weMapMap == null){
            this.mapview.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull MapboxMap mapboxMap) {
                    mapboxMap.setStyle(asia.wemap.androidsdk.Style.WEMAP_BASIC, new com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            weMapMap = new WeMapMap(context, mapboxMap, mapview, style);
                            callback.onMapReady(weMapMap);

                        }
                    });
                }
            });
        } else {
            callback.onMapReady(weMapMap);
        }
    }

    
    /** 
     * @param savedInstanceState
     */
    @UiThread
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.mapview.onCreate(savedInstanceState);
    }

    @UiThread
    public void onResume() {
        this.mapview.onResume();
        this.mapview.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.getStyle(new com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        weMapMap = new WeMapMap(context, mapboxMap, mapview, style);
                    }
                });
            }
        });
    }

    @UiThread
    public void onStart() {
        if (!WeMap.hasInstance()) {
            throw new WeMapConfigurationException();
        }
        addLogo();
        this.mapview.onStart();
        if(this.mapview.getParent() == null)
            this.addView(this.mapview);
    }

    @UiThread
    public void onPause() {
        this.mapview.onPause();
    }


    @UiThread
    public void onLowMemory() {
        this.mapview.onLowMemory();
    }


    @UiThread
    public void onStop() {
        mapview.onStop();
    }

    @UiThread
    public void onDestroy() {
        mapview.onDestroy();
    }

    
    /** 
     * @param outState
     */
    @UiThread
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mapview.onSaveInstanceState(outState);
    }

    protected void addLogo(){
        ImageView logo = new ImageView(this.getContext());
        logo.setImageResource(R.drawable.logo);
        LayoutParams layoutParams = new LayoutParams(300, (int)(200/1.5));
        layoutParams.gravity = Gravity.BOTTOM;
        logo.setLayoutParams(layoutParams);
        this.mapview.addView(logo);
    }

    
    /** 
     * @param icon
     */
    public void addLocationButton(int icon){

        this.fab = new FloatingActionButton(this.getContext());
        LayoutParams FL = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        FL.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        FL.rightMargin = 20;
        FL.bottomMargin = 20;

        this.fab.setLayoutParams(FL);
        this.fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        this.fab.setImageResource(icon);
        this.fab.setVisibility(VISIBLE);
        this.mapview.addView(this.fab);
    }
}
