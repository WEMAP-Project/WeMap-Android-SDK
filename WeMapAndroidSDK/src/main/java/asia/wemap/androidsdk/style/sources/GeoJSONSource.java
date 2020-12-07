package asia.wemap.androidsdk.style.sources;

import androidx.annotation.Nullable;

import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.URI;

public class GeoJSONSource {
    private GeoJsonSource geoJsonSource;
    private String sourceID;

    public GeoJSONSource(String id, @Nullable String geoJson){
        this.sourceID = id;
        geoJsonSource = new GeoJsonSource(id, geoJson);
    }

    public GeoJSONSource(String id, URI uri){
        this.sourceID = id;
        geoJsonSource = new GeoJsonSource(id, uri);
    }

    public GeoJsonSource getGeoJSONSource(){
        return this.geoJsonSource;
    }
    public String getGeoJSONSourceId(){
        return this.sourceID;
    }
}
