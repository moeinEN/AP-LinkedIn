package Controller.CallBack;

public interface ProfileIdCallback {
    void onSuccess(int profileId);
    void onError(String errorMessage);
}