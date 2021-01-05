package asia.wemap.androidsdk.annotaion;

import com.mapbox.mapboxsdk.plugins.annotation.Symbol;

import asia.wemap.androidsdk.geometry.LatLng;

public class WeMapMarker {
    private Symbol symbol;
    public WeMapMarker(Symbol symbol){
        this.symbol = symbol;
    }

    public LatLng getMarkerGeometry(){
        return new LatLng(this.symbol.getGeometry().latitude(),this.symbol.getGeometry().longitude());
    }
}
