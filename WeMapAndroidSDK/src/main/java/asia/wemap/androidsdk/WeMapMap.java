package asia.wemap.androidsdk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolLongClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.RasterLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.style.sources.RasterSource;
import com.mapbox.mapboxsdk.style.sources.TileSet;

import java.util.List;

import asia.wemap.androidsdk.annotaion.WeMapMarker;
import asia.wemap.androidsdk.geometry.LatLng;
import asia.wemap.androidsdk.permissions.PermissionsListener;
import asia.wemap.androidsdk.style.sources.GeoJSONSource;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class WeMapMap {

    private static final int REQUEST_LOCATION = 1;

    private static MapboxMap mapboxMap;
    private LocationManager locationManager;
    private MapboxMap.OnMapClickListener onMapClickListener;
    private MapboxMap.OnMapLongClickListener onMapLongClickListener;
    private SymbolManager symbolManager;
    private MarkerViewManager markerViewManager;
    private MarkerView markerView;

    Context context;

    public WeMapMap(Context ctx, MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.context = ctx;
    }

    public WeMapMap(Context ctx, MapboxMap mapboxMap, MapView mapView, Style style) {
        this.mapboxMap = mapboxMap;
        this.context = ctx;
        this.symbolManager = new SymbolManager(mapView, mapboxMap, style);
        this.markerViewManager = new MarkerViewManager(mapView, mapboxMap);

    }

    public void addImage(String ICON_ID, @NonNull Bitmap image){
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addImage(ICON_ID, image);
            }
        });
    }

    public void onMarkerClick(OnMarkerClickListener onMarkerClickListener){
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if(symbolManager != null){
                    symbolManager.addClickListener(new OnSymbolClickListener() {
                        @Override
                        public boolean onAnnotationClick(Symbol symbol) {
                            onMarkerClickListener.OnMarkerClick(new WeMapMarker(symbol));
                            return true;
                        }
                    });
                    symbolManager.addLongClickListener(new OnSymbolLongClickListener() {
                        @Override
                        public boolean onAnnotationLongClick(Symbol symbol) {
                            onMarkerClickListener.OnMarkerLongClick(new WeMapMarker(symbol));
                            return true;
                        }
                    });
                }
            }
        });
    }

    public WeMapSymbol createMarker(LatLng latLng, String ICON_ID){
        WeMapSymbol wemapSymbol = new WeMapSymbol();
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if(symbolManager != null){
                    Symbol symbol = symbolManager.create(new SymbolOptions()
                            .withLatLng(new
                                    com.mapbox.mapboxsdk.geometry.LatLng(latLng.getLatitude(), latLng.getLongitude()))
                            .withIconImage(ICON_ID)
                            .withIconSize(1.0f));
                    wemapSymbol.setSymbol(symbol);
                    symbolManager.addClickListener(new OnSymbolClickListener() {
                        @Override
                        public boolean onAnnotationClick(Symbol symbol) {
                            return true;
                        }
                    });
                }
            }
        });
        return wemapSymbol;
    }

    public void removeMarker(WeMapSymbol wemapSymbol){
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if(symbolManager != null){
                    symbolManager.delete(wemapSymbol.getSymbol());
                }
            }
        });
    }

    public void removeAllMarker(){
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if(symbolManager != null){
                    symbolManager.deleteAll();
                }
            }
        });
    }

    public void createViewMarker(LatLng latLng, View view){
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if(markerView != null)
                    markerViewManager.removeMarker(markerView);
                markerView = new MarkerView(new
                        com.mapbox.mapboxsdk.geometry.LatLng(latLng.getLatitude(), latLng.getLongitude()), view);
                markerViewManager.addMarker(markerView);
            }
        });
    }

    public void addTrafficLayer() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(new RasterSource(
                        "traffic-source",
                        new TileSet("tileset", asia.wemap.androidsdk.Style.WEMAP_TRAFFIC), 256));

                if (style.getLayer("satellite-layer") != null) {
                    style.addLayerBelow(
                            new RasterLayer("traffic-layer", "traffic-source"), "tunnel-street-minor-low");
                } else if (style.getLayer("traffic-layer") == null) {
                    style.addLayer(new RasterLayer("traffic-layer", "traffic-source"));
                }
            }
        });
    }
    
    /** 
     * @param layerID
     */
    public void removeLayer(String layerID) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getLayer(layerID) != null) {
                    style.removeLayer(layerID);
                }
            }
        });
    }

    public void removeTrafficLayer() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getLayer("traffic-layer") != null) {
                    style.removeLayer("traffic-layer");
                }
            }
        });
    }

    public void addSatelliteLayer() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(new RasterSource(
                        "satellite-source",
                        new TileSet("tileset", asia.wemap.androidsdk.Style.WEMAP_SATELLITE), 256));

                if (style.getLayer("satellite-layer") == null) {
                    style.addLayer(new RasterLayer("satellite-layer", "satellite-source"));
                }
            }
        });
    }

    public void removeSatelliteLayer() {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getLayer("satellite-layer") != null) {
                    style.removeLayer("satellite-layer");
                }
            }
        });
    }

    
    /** 
     * @param layerId
     * @param layerURI
     */
    public void addRasterLayer(String layerId, String layerURI) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(new RasterSource(
                        layerId + "-source",
                        new TileSet("tileset", layerURI), 256));

                if (style.getLayer(layerId) == null) {
                    style.addLayer(new RasterLayer(layerId, layerId + "-source"));
                }
            }
        });
    }

    
    /** 
     * @param layerId
     * @param layerURI
     * @param maxZoom
     * @param minZoom
     */
    public void addRasterLayer(String layerId, String layerURI, float maxZoom, float minZoom) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                TileSet tileSet = new TileSet("tileset", layerURI);
                tileSet.setMaxZoom(maxZoom);
                tileSet.setMinZoom(minZoom);
                RasterSource rasterSource = new RasterSource(
                        layerId + "-source", tileSet, 256);
                style.addSource(rasterSource);

                if (style.getLayer(layerId) == null) {
                    style.addLayer(new RasterLayer(layerId, layerId + "-source"));
                }
            }
        });
    }

    
    /** 
     * @param layerId
     */
    public void removeRasterLayer(String layerId) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getLayer(layerId) != null) {
                    style.removeLayer(layerId);
                }
            }
        });
    }

    
    /** 
     * @param latLng
     * @param image
     */
    public void addMarker(LatLng latLng, @NonNull Bitmap image) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            private static final String DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID";
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addImage("dropped-icon-image", image);
                GeoJsonSource source = new GeoJsonSource("dropped-marker-source-id");
                source.setGeoJson(Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude()));
                style.addSource(source);
                style.addLayer(new SymbolLayer(DROPPED_MARKER_LAYER_ID,
                        "dropped-marker-source-id").withProperties(
                        iconImage("dropped-icon-image"),
                        visibility(Property.VISIBLE),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                ));
            }
        });
    }

    
    /** 
     * @param layerID
     * @param geoJSONSource
     */
    public void addGeoJSONPointLayer(String layerID, GeoJSONSource geoJSONSource) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(geoJSONSource.getGeoJSONSource());
                style.addLayer(new CircleLayer("points", geoJSONSource.getGeoJSONSourceId()));
            }
        });
    }

    
    /** 
     * @param layerID
     * @param geoJSONSource
     * @param circleRadius
     * @param circleColor
     */
    public void addGeoJSONPointLayer(String layerID, GeoJSONSource geoJSONSource, float circleRadius, String circleColor) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(geoJSONSource.getGeoJSONSource());
                style.addLayer(new CircleLayer(layerID, geoJSONSource.getGeoJSONSourceId())
                        .withProperties(PropertyFactory.circleRadius(circleRadius), PropertyFactory.circleColor(circleColor)
                        ));
            }
        });
    }
    
    /** 
     * @param layerID
     * @param geoJSONSource
     * @param image
     */
    public void addGeoJSONPointLayer(String layerID, GeoJSONSource geoJSONSource, @NonNull Bitmap image) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addImage(layerID + "-image", image);
                style.addSource(geoJSONSource.getGeoJSONSource());
                style.addLayer(new SymbolLayer(layerID,
                        geoJSONSource.getGeoJSONSourceId()).withProperties(
                        iconImage(layerID + "-image"),
                        visibility(Property.VISIBLE),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                ));
            }
        });
    }
    
    /** 
     * @param layerID
     * @param geoJSONSource
     */
    public void addGeoJSONPolylineLayer(String layerID, GeoJSONSource geoJSONSource) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(geoJSONSource.getGeoJSONSource());
                style.addLayer(new LineLayer(layerID, geoJSONSource.getGeoJSONSourceId()));
            }
        });
    }

    
    /** 
     * @param layerID
     * @param geoJSONSource
     * @param lineWidth
     * @param lineOpacity
     * @param lineColor
     */
    public void addGeoJSONPolylineLayer(String layerID, GeoJSONSource geoJSONSource, float lineWidth, float lineOpacity, String lineColor) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(geoJSONSource.getGeoJSONSource());
                style.addLayer(new LineLayer(layerID, geoJSONSource.getGeoJSONSourceId())
                        .withProperties(PropertyFactory.lineCap(Property.LINE_CAP_SQUARE),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_MITER),
                                PropertyFactory.lineOpacity(lineOpacity),
                                PropertyFactory.lineWidth(lineWidth),
                                PropertyFactory.lineColor(Color.parseColor(lineColor))));
            }
        });
    }

    
    /** 
     * @param layerID
     * @param geoJSONSource
     */
    public void addGeoJSONPolygonLayer(String layerID, GeoJSONSource geoJSONSource) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(geoJSONSource.getGeoJSONSource());
                style.addLayer(new FillLayer(layerID, geoJSONSource.getGeoJSONSourceId()));
            }
        });
    }

    
    /** 
     * @param layerID
     * @param geoJSONSource
     * @param polygonColor
     */
    public void addGeoJSONPolygonLayer(String layerID, GeoJSONSource geoJSONSource, String polygonColor) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addSource(geoJSONSource.getGeoJSONSource());
                style.addLayer(new FillLayer(layerID, geoJSONSource.getGeoJSONSourceId())
                        .withProperties(PropertyFactory.fillColor(polygonColor)));
            }
        });
    }

    
    /** 
     * @param layerID
     */
    public void removeGeoJSONLayer(String layerID) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                if (style.getLayer(layerID) != null) {
                    style.removeLayer(layerID);
                }
            }
        });
    }

    
    /** 
     * @param weDirectionResponse
     */
    public void addDirectionLayer(WeDirectionResponse weDirectionResponse) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                addGeoJSONPolylineLayer("direction-layer", new GeoJSONSource("direction-source", weDirectionResponse.getPointsString()), 5, 1, "#4882c5");
                Feature originPoint = Feature.fromGeometry(Point.fromLngLat(weDirectionResponse.getOriginPoint().getLongitude(), weDirectionResponse.getOriginPoint().getLatitude()));
                Feature destinationPoint = Feature.fromGeometry(Point.fromLngLat(weDirectionResponse.getDestinationPoint().getLongitude(), weDirectionResponse.getDestinationPoint().getLatitude()));

                addGeoJSONPointLayer("origin-point-layer", new GeoJSONSource("origin-point-source", originPoint.toJson()), 5f, "#3bb2d0");
                addGeoJSONPointLayer("destination-point-layer", new GeoJSONSource("destination-point-source", destinationPoint.toJson()), 5f, "#8a8acb");
            }
        });
    }

    public void removeDirectionLayer() {
        removeGeoJSONLayer("direction-layer");
        removeGeoJSONLayer("origin-point-layer");
        removeGeoJSONLayer("destination-point-layer");
    }

    
    /** 
     * @param onMapClickListener
     */
    public void addOnMapClickListener(OnMapClickListener onMapClickListener) {
        this.onMapClickListener = new MapboxMap.OnMapClickListener() {
            @Override
            public boolean onMapClick(@NonNull com.mapbox.mapboxsdk.geometry.LatLng point) {
                onMapClickListener.onMapClick(new LatLng(point.getLatitude(), point.getLongitude()));
                return true;
            }
        };
        mapboxMap.addOnMapClickListener(this.onMapClickListener);
    }

    
    /** 
     * @param onMapClickListener
     */
    public void removeOnMapClickListener(OnMapClickListener onMapClickListener) {
        if(this.onMapClickListener != null)
        mapboxMap.removeOnMapClickListener(this.onMapClickListener);
    }

    
    /** 
     * @param onMapLongClickListener
     */
    public void addOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        this.onMapLongClickListener = new MapboxMap.OnMapLongClickListener() {
            @Override
            public boolean onMapLongClick(@NonNull com.mapbox.mapboxsdk.geometry.LatLng point) {
                onMapLongClickListener.onMapLongClick(new LatLng(point.getLatitude(), point.getLongitude()));
                return true;
            }
        };
        mapboxMap.addOnMapLongClickListener(this.onMapLongClickListener);
    }

    
    /** 
     * @param onMapLongClickListener
     */
    public void removeOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        if(this.onMapLongClickListener != null)
            mapboxMap.removeOnMapLongClickListener(this.onMapLongClickListener);
    }

    
    /** 
     * @param PML
     * @param activity
     */
    @SuppressWarnings({"MissingPermission"})
    public void enableLocationPermission(PermissionsListener PML, Activity activity) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                PermissionsManager permissionsManager;
                // Check if permissions are enabled and if not request
                if (PermissionsManager.areLocationPermissionsGranted(activity.getBaseContext())) {
                    PML.onPermissionResult(true);
                } else {
                    permissionsManager = new PermissionsManager(new com.mapbox.android.core.permissions.PermissionsListener() {
                        @Override
                        public void onExplanationNeeded(List<String> permissionsToExplain) {
                            PML.onExplanationNeeded(permissionsToExplain);
                        }

                        @Override
                        public void onPermissionResult(boolean granted) {
                            PML.onPermissionResult(granted);
                        }
                    });
                    permissionsManager.requestLocationPermissions(activity);
                }
            }
        });

    }

    
    /** 
     * @param ctx
     */
    @SuppressWarnings({"MissingPermission"})
    public void enableLocationComponent(Context ctx) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(ctx)
                        .build();
                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(
                        LocationComponentActivationOptions.builder(ctx, style)
                                .locationComponentOptions(customLocationComponentOptions)
                                .build());
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
                locationComponent.setRenderMode(RenderMode.GPS);
            }
        });
    }

    
    /** 
     * @param ctx
     * @param CameraMode
     * @param RenderMode
     */
    @SuppressWarnings({"MissingPermission"})
    public void enableLocationComponent(Context ctx, int CameraMode, int RenderMode) {
        mapboxMap.getStyle(new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(ctx)
                        .build();
                LocationComponent locationComponent = mapboxMap.getLocationComponent();
                locationComponent.activateLocationComponent(
                        LocationComponentActivationOptions.builder(ctx, style)
                                .locationComponentOptions(customLocationComponentOptions)
                                .build());
                locationComponent.setLocationComponentEnabled(true);
                locationComponent.setCameraMode(CameraMode);
                locationComponent.setRenderMode(RenderMode);
            }
        });
    }

    
    /** 
     * @param ctx
     * @param onLocationReadyCallback
     */
    @SuppressWarnings({"MissingPermission"})
    public void getLastLocation(Context ctx, OnLocationReadyCallback onLocationReadyCallback) {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(ctx);
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                Location lastLocation = result.getLastLocation();
                if (lastLocation != null) {
                    onLocationReadyCallback.onSuccess(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude(), lastLocation.getAltitude()));
                } else {
                    onLocationReadyCallback.onSuccess(null);
                }
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                onLocationReadyCallback.onFailure(exception);
            }
        });
    }

    
    /** 
     * @param cameraPosition
     * @param time
     */
    public void animateCamera(CameraPosition cameraPosition, int time){
        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition.getMCameraPosition()), time);
    }

    public interface OnWeMapReadyCallback {

        void onMapReady(@NonNull WeMapMap wemapMap);
    }

    public interface OnMapClickListener {
        public boolean onMapClick(LatLng point);
    }

    public interface OnMapLongClickListener {
        public boolean onMapLongClick(LatLng point);
    }
}
