package Controller.CallBack;

import Model.Response.WatchPostResponse;

public interface WatchListCallback {
    void onSuccess(WatchPostResponse watchPostResponse);
    void onFailure(String errorMessage);
}