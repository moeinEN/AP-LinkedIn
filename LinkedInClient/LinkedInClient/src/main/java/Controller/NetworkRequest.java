package Controller;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public interface NetworkRequest {
    public JsonObject syncCallSayHello();
}
