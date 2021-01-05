package asia.wemap.androidsdk;

import asia.wemap.androidsdk.annotaion.WeMapMarker;

public interface OnMarkerClickListener<T> {

    void OnMarkerClick(WeMapMarker marker);

    void OnMarkerLongClick(WeMapMarker marker);
}