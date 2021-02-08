package asia.wemap.androidsdk.annotaion;

import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;

import java.util.HashMap;
import java.util.Map;

import asia.wemap.androidsdk.geometry.LatLng;

public class WeMapMarker {
    private Symbol symbol;
    private MarkerView markerView;
    private Map<String, String> properties = new HashMap<String, String>();

    public WeMapMarker(){
        this.symbol = symbol;
    }

    public WeMapMarker(Symbol symbol){
        this.symbol = symbol;
    }

    public WeMapMarker(Symbol symbol, Map<String, String> properties){
        this.symbol = symbol;
        this.properties = properties;
    }

    public LatLng getMarkerGeometry(){
        return new LatLng(this.symbol != null ? this.symbol.getGeometry().latitude() : 0, this.symbol != null ? this.symbol.getGeometry().longitude() : 0);
    }

    public void setSymbol(Symbol symbol){
        this.symbol = symbol;
    }

    public Symbol getSymbol(){
        return this.symbol;
    }

    public void setViewMarker(MarkerView markerView){
        this.markerView = markerView;
    }

    public void setWeMapViewMarker(WeMapViewMarker weMapViewMarker){
        this.markerView = weMapViewMarker.getViewMarker();
    }

    public MarkerView getViewMarker(){
        return this.markerView;
    }

    public long getId(){
        return this.symbol != null ? this.symbol.getId() : -1;
    }

    public void set(String key, String value){
        properties.put(key, value);
    }

    public String get(String key){
        return properties.get(key);
    }
}
