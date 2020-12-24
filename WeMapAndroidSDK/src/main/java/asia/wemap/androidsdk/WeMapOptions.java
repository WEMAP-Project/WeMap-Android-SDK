package asia.wemap.androidsdk;

import android.content.Context;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

public class WeMapOptions {
    public MapboxMapOptions options;

    public WeMapOptions(Context ctx){

        options = MapboxMapOptions.createFromAttributes(ctx, null).logoEnabled(false).attributionEnabled(false)
                .camera(new CameraPosition.Builder()
                        .target(new LatLng(21.0266469, 105.7615744))
                        .zoom(12)
                        .build());
    }

    public WeMapOptions(Context ctx, asia.wemap.androidsdk.geometry.LatLng center){

        options = MapboxMapOptions.createFromAttributes(ctx, null).logoEnabled(false).attributionEnabled(false)
                .camera(new CameraPosition.Builder()
                        .target(new LatLng(center.getLatitude(), center.getLongitude()))
                        .zoom(12)
                        .build());
    }

    public WeMapOptions(Context ctx, asia.wemap.androidsdk.geometry.LatLng center, int zoom){

        options = MapboxMapOptions.createFromAttributes(ctx, null).logoEnabled(false).attributionEnabled(false)
                .camera(new CameraPosition.Builder()
                        .target(new LatLng(center.getLatitude(), center.getLongitude()))
                        .zoom(zoom)
                        .build());
    }

    
    /** 
     * @param enabled
     * @return WeMapOptions
     */
    public WeMapOptions rotateGesturesEnabled(boolean enabled) {
        options.rotateGesturesEnabled(enabled);
        return this;
    }

    
    /** 
     * @param enabled
     * @return WeMapOptions
     */
    public WeMapOptions scrollGesturesEnabled(boolean enabled) {
        options.rotateGesturesEnabled(enabled);
        return this;
    }


    
    /** 
     * @param enabled
     * @return WeMapOptions
     */
    public WeMapOptions tiltGesturesEnabled(boolean enabled) {
        options.rotateGesturesEnabled(enabled);
        return this;
    }

    
    /** 
     * @param enabled
     * @return WeMapOptions
     */
    public WeMapOptions zoomGesturesEnabled(boolean enabled) {
        options.rotateGesturesEnabled(enabled);
        return this;
    }

    
    /** 
     * @param enabled
     * @return WeMapOptions
     */
    public WeMapOptions doubleTapGesturesEnabled(boolean enabled) {
        options.rotateGesturesEnabled(enabled);
        return this;
    }

    
    /** 
     * @param enabled
     * @return WeMapOptions
     */
    public WeMapOptions quickZoomGesturesEnabled(boolean enabled) {
        options.rotateGesturesEnabled(enabled);
        return this;
    }
}
