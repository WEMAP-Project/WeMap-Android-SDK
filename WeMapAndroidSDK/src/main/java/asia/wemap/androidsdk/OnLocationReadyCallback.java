package asia.wemap.androidsdk;

public interface OnLocationReadyCallback<T> {

    void onFailure(Exception exception);

    void onSuccess(T result);
}