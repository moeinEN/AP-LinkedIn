package Service;

import Model.LoginCredentials;
import Model.RegisterCredentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/user/register")
    public Call<ResponseBody> signUp(@Body RegisterCredentials registerCredentials);

    @POST("/login")
    Call<ResponseBody> login(@Body LoginCredentials loginCredentials);
}
