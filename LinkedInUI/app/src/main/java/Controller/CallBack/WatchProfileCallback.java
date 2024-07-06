package Controller.CallBack;

import Model.Response.WatchProfileResponse;

public interface WatchProfileCallback {
    void onSuccess(WatchProfileResponse response);
    void onFailure(Throwable t);
}