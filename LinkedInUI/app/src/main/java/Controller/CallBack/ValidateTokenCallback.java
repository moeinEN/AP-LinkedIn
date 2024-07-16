package Controller.CallBack;

import Model.Messages;

public interface ValidateTokenCallback {
    void onSuccess(Messages message);
    void onFailure(Messages message);
}