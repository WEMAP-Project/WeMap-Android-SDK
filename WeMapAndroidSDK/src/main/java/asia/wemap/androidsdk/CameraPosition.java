package asia.wemap.androidsdk;

import asia.wemap.androidsdk.geometry.LatLng;

public final class CameraPosition {
  private com.mapbox.mapboxsdk.camera.CameraPosition mCameraPosition;

  public CameraPosition(){
    this.mCameraPosition = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
            .target(new com.mapbox.mapboxsdk.geometry.LatLng(21.0266469, 105.7615744))
            .zoom(15) // Sets the zoom
            .bearing(180) // Rotate the camera
            .tilt(30) // Set the camera tilt
            .build();
  }


  public CameraPosition(LatLng target, int zoom, int bearing, int tilt){
    this.mCameraPosition = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
            .target(new com.mapbox.mapboxsdk.geometry.LatLng(target.getLatitude(), target.getLongitude()))
            .zoom(zoom) // Sets the zoom
            .bearing(bearing) // Rotate the camera
            .tilt(tilt) // Set the camera tilt
            .build();
  }

  protected com.mapbox.mapboxsdk.camera.CameraPosition getMCameraPosition(){
    return this.mCameraPosition;
  }
}