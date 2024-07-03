package Controller;

import Model.Messages;
import Model.Requests.LoginCredentials;
import Model.Requests.RegisterCredentials;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public interface NetworkRequest {
    public JsonObject syncCallSayHello();
    public Messages syncCallSignUp(RegisterCredentials registerCredentials);
    public Messages syncCallLogin(LoginCredentials loginCredentials);
}
