package Controller.CallBack;

import Model.Messages;

public interface SignUpCallback {
    void onSuccess(Messages message);
    void onFailure(Messages message);
}