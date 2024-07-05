package Controller;

import Controller.CallBack.SignUpCallback;
import Model.Messages;
import Model.Requests.LoginCredentials;
import Model.Requests.RegisterCredentials;
import com.google.gson.JsonObject;

public interface NetworkRequest {
    public JsonObject syncCallSayHello();
    public void asyncCallSignUp(RegisterCredentials registerCredentials, SignUpCallback callback);
    public Messages syncCallLogin(LoginCredentials loginCredentials);
}
