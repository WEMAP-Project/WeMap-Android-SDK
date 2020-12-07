package asia.wemap.androidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Style {

    public interface OnStyleLoaded {
        /**
         * Invoked when a style has finished loading.
         *
         * @param style the style that has finished loading
         */
        void onStyleLoaded(@NonNull Style style);
    }

    public static final String WEMAP_BASIC = "https://apis.wemap.asia/vector-tiles/styles/osm-bright/style.json?key=" + WeMap.getAccessToken();
    public static final String WEMAP_TRAFFIC = "https://mt1.google.com/vt/lyrs=transit,traffic&x={x}&y={y}&z={z}";
    public static final String WEMAP_SATELLITE = "https://mt1.google.com/vt/lyrs=y&x={x}&y={y}&z={z}";

}
