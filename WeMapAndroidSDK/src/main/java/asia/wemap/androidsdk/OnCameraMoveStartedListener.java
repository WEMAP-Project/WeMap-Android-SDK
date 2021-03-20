package asia.wemap.androidsdk;

public interface OnCameraMoveStartedListener<T> {
    int REASON_API_GESTURE = 1;
    int REASON_DEVELOPER_ANIMATION = 2;
    int REASON_API_ANIMATION = 3;

    void onCameraMoveStarted(int reason);
}