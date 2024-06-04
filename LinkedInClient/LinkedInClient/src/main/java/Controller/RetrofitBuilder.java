package Controller;

import Model.Cookies;
import Model.LoginCredentials;
import Model.Messages;
import Model.RegisterCredentials;
import Service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static final String BASE_URL = "http://localhost:8080";


    private Retrofit retrofitBuilder(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        return retrofit;
    }
    private Retrofit retrofit = retrofitBuilder();

    public Messages syncCallSignUp(RegisterCredentials registerCredentials){
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callSync = service.signUp(registerCredentials);

        try {
            Response<ResponseBody> response = callSync.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodyBytes = response.body().bytes();
                return Messages.fromByte(responseBodyBytes);
            } else {
                return Messages.INTERNAL_ERROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }
    public Messages syncCallLogin(LoginCredentials loginCredentials) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLogin = service.login(loginCredentials);
        String responseString;
        try {
            Response<ResponseBody> response = callLogin.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodyBytes = response.body().bytes();
                Gson gson = new Gson();
                responseString = gson.fromJson(new String(responseBodyBytes), String.class);
                Cookies.setSessionToken(responseString);
            } else {
                return Messages.INTERNAL_ERROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
        return Messages.USER_LOGGED_IN_SUCCESSFULLY;
    }
}
