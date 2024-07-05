package Controller.CallBack;

import Model.Messages;

public interface LoginCallback {
    void onSuccess(Messages message);
    void onFailure(Messages message);
}
