package Controller.CallBack;

import Model.Messages;

public interface ProfileCallback {
    void onSuccess(Messages serverResponse);
    void onFailure(String errorMessage);
}