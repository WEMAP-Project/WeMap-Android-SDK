package asia.wemap.androidsdk;

import asia.wemap.androidsdk.geometry.LatLng;

public final class CameraPosition {
  public LatLng target;
  public double zoom;
  public double bearing;
  public double tilt;
  private com.mapbox.mapboxsdk.camera.CameraPosition mCameraPosition;
    /** 
     * Sets the zoom
     * Rotate the camera
     * Set the camera tilt
     */
  public CameraPosition(){
    this.target = new LatLng(21.0266469, 105.7615744);
    this.bearing = 180;
    this.zoom = 15;
    this.tilt = 30;
    this.mCameraPosition = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
            .target(new com.mapbox.mapboxsdk.geometry.LatLng(21.0266469, 105.7615744))
            .zoom(15) // Sets the zoom
            .bearing(180) // Rotate the camera
            .tilt(30) // Set the camera tilt
            .build();
  }


    /** 
     * Sets the zoom
     * Rotate the camera
     * Set the camera tilt
     */
  public CameraPosition(LatLng target, double zoom, double bearing, double tilt){
    this.zoom = zoom;
    this.bearing = bearing;
    this.tilt = tilt;
    this.mCameraPosition = new com.mapbox.mapboxsdk.camera.CameraPosition.Builder()
            .target(new com.mapbox.mapboxsdk.geometry.LatLng(target.getLatitude(), target.getLongitude()))
            .zoom(zoom) // Sets the zoom
            .bearing(bearing) // Rotate the camera
            .tilt(tilt) // Set the camera tilt
            .build();
  }

  /**
   * Sets the zoom
   * Rotate the camera
   * Set the camera tilt
   */
  public CameraPosition(com.mapbox.mapboxsdk.camera.CameraPosition CameraPosition){
    this.target = new LatLng(CameraPosition.target.getLatitude(), CameraPosition.target.getLongitude(), CameraPosition.target.getAltitude());
    this.zoom = CameraPosition.zoom;
    this.bearing = CameraPosition.bearing;
    this.tilt = CameraPosition.tilt;
  }
  
  /** 
   * @return CameraPosition
   */
  protected com.mapbox.mapboxsdk.camera.CameraPosition getMCameraPosition(){
    return this.mCameraPosition;
  }
}