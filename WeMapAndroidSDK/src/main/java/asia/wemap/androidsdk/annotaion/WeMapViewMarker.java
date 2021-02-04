package asia.wemap.androidsdk.annotaion;

import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;

import java.util.HashMap;
import java.util.Map;

import asia.wemap.androidsdk.geometry.LatLng;

public class WeMapViewMarker {
    private MarkerView markerView;
    private Map<String, String> properties = new HashMap<String, String>();

    public WeMapViewMarker(MarkerView markerView){
        this.markerView = markerView;
    }

    public WeMapViewMarker(){
    }


    public WeMapViewMarker(MarkerView markerView, Map<String, String> properties){
        this.markerView = markerView;
        this.properties = properties;
    }

    public void setMarker(MarkerView markerView){
        this.markerView = markerView;
    }

    public MarkerView getViewMarker(){
        return this.markerView;
    }

}
