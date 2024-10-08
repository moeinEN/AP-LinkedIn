package Controller;


import Controller.CallBack.*;
import Model.*;
import Model.Requests.*;
import Model.Response.*;
import Model.User;
import Service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;

import static Controller.FileController.writeResponseBodyToDisk;

import androidx.annotation.NonNull;

public class RetrofitBuilder {
    private static String BASE_URL;
    private Retrofit retrofit;
    public static RetrofitBuilder clientInterface;
    public RetrofitBuilder(String urlOrIP) {
        BASE_URL = urlOrIP;
        retrofit = retrofitBuilder();
    }

    private Retrofit retrofitBuilder() {
        Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy, hh:mm:ss a").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build())
                .build();
        return retrofit;
    }

    private User syncCallGetUserService(String username) {
        Retrofit retrofit = this.retrofitBuilder();
        UserService service = retrofit.create(UserService.class);
        Call<User> callSync = service.addUser(username, new User());

        try {
            Response<User> response = callSync.execute();
            User user = response.body();
            return user;
        } catch (Exception ex) {
            return null;
        }
    }

    public User syncCallGetUser() {
        UserService service = retrofit.create(UserService.class);
        Call<User> callSync = service.getUser("test");

        try {
            Response<User> response = callSync.execute();
            User user = response.body();
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public JsonObject syncCallSayHello(){
        UserService service = retrofit.create(UserService.class);
        Call<JsonObject> callSync = service.sayHello();

        try {
            Response<JsonObject> response = callSync.execute();
            JsonObject string = response.body();
            return string;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void asyncCallSignUp(RegisterCredentials registerCredentials, SignUpCallback callback) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callAsync = service.signUp(registerCredentials);

        callAsync.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] responseBodyBytes = response.body().bytes();
                        Messages message = Messages.fromByte(responseBodyBytes);
                        callback.onSuccess(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure(Messages.INTERNAL_ERROR);
                    }
                } else {
                    callback.onFailure(Messages.INTERNAL_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(Messages.INTERNAL_ERROR);
            }
        });
    }

    public void asyncCallLogin(LoginCredentials loginCredentials, LoginCallback callback) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLogin = service.login(loginCredentials);

        callLogin.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] responseBodyBytes = response.body().bytes();
        Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();                        String responseString = gson.fromJson(new String(responseBodyBytes), String.class);
                        Cookies.setSessionToken(responseString);
                        callback.onSuccess(Messages.USER_LOGGED_IN_SUCCESSFULLY);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure(Messages.INTERNAL_ERROR);
                    }
                } else {
                    callback.onFailure(Messages.INVALID_CREDENTIALS);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(Messages.INTERNAL_ERROR);
            }
        });
    }

    public void asyncValidateTokenAsync(final ValidateTokenCallback callback) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callValidate = service.validateToken(Cookies.getSessionToken());

        callValidate.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] responseBodyBytes = response.body().bytes();
                        Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();
                        Messages serverResponse = gson.fromJson(new String(responseBodyBytes), Messages.class);
                        callback.onSuccess(serverResponse);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        callback.onFailure(Messages.INTERNAL_ERROR);
                    }
                } else {
                    byte[] responseBodyBytes = null;
                    try {
                        responseBodyBytes = response.errorBody().bytes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();
                    Messages serverResponse = gson.fromJson(new String(responseBodyBytes), Messages.class);
                    callback.onFailure(serverResponse);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(Messages.INTERNAL_ERROR);
            }
        });
    }

    public void asyncCallProfile(CreateProfileRequest profile, ProfileCallback callback) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callProfile = service.profile(profile, Cookies.getSessionToken());

        callProfile.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] responseBodyBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy, hh:mm:ss a").create();                        Messages serverResponse = gson.fromJson(new String(responseBodyBytes), Messages.class);
                        callback.onSuccess(serverResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure("Internal error: " + e.getMessage());
                    }
                } else {
                    callback.onFailure("Request failed. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure("Request failed: " + t.getMessage());
            }
        });
    }

    public Messages syncCallLike(LikeRequest like) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLike = service.like(like, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callLike.execute();
            byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").create();
            ServerResponse = gson.fromJson(new String(responseBodeBytes), Messages.class);

            return ServerResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public Messages syncCallPost(Post post) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callPost = service.post(post, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callPost.execute();
            byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").create();
            ServerResponse = gson.fromJson(new String(responseBodeBytes), Messages.class);

            return ServerResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public Messages syncCallComment(CommentRequest comment) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLike = service.comment(comment, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callLike.execute();
            byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").create();
            ServerResponse = gson.fromJson(new String(responseBodeBytes), Messages.class);

            return ServerResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public Messages syncCallConnect(ConnectRequest connectRequest) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callConnect = service.connect(connectRequest, Cookies.getSessionToken());
        Messages serverResponse;
        try {
            Response<ResponseBody> response = callConnect.execute();
            byte[] responseBodyBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").create();
            serverResponse = gson.fromJson(new String(responseBodyBytes), Messages.class);

            return serverResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }

    }

    //TODO add size limit to uploaded files
    //TODO filenames
    public String asyncCallUpload(String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        String fileURL = FileController.generateFileName() + "." + FilenameUtils.getExtension(file.getName());
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileURL, requestFile);


        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> call = service.upload(body);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("Upload successful: " + response.body());
                } else {
                    System.out.println("Upload failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return fileURL;
    }

    public void asyncCallDownload(String filename) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> call = service.downloadFile(filename);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean success = writeResponseBodyToDisk(response.body(), filename);
                    if (success) {
                        System.out.println("File downloaded successfully");
                    } else {
                        System.out.println("Failed to save the file");
                    }
                } else {
                    System.out.println("Server returned an error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public Messages acceptConnection(AcceptConnection acceptConnection) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callAcceptConnection = service.acceptConnection(acceptConnection, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callAcceptConnection.execute();
            byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").create();
            ServerResponse = gson.fromJson(new String(responseBodeBytes), Messages.class);

            return ServerResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public void asyncWatchProfileRequest(WatchProfileRequest watchProfileRequest, WatchProfileCallback callback) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callWatchProfile = service.watchProfile(watchProfileRequest, Cookies.getSessionToken());

        callWatchProfile.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] responseBodyBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("MMM d, yyyy, hh:mm:ss a").create();                        WatchProfileResponse serverResponse = gson.fromJson(new String(responseBodyBytes), WatchProfileResponse.class);
                        callback.onSuccess(serverResponse);
                    } catch (IOException e) {
                        callback.onFailure(e);
                    }
                } else {
                    callback.onFailure(new Exception("Response not successful or body is null"));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public WatchPostSearchResults searchPostRequest(SearchPostsRequest searchPostsRequest) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callSearchPost = service.searchPost(searchPostsRequest, Cookies.getSessionToken());
        WatchPostSearchResults ServerResponse;
        try {
            Response<ResponseBody> response = callSearchPost.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();                ServerResponse = gson.fromJson(new String(responseBodeBytes), WatchPostSearchResults.class);

                return ServerResponse;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public WatchProfileSearchResults watchProfileSearchResults(SearchProfileRequest searchProfileRequest) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callWatchProfileSearch = service.searchProfile(searchProfileRequest, Cookies.getSessionToken());
        WatchProfileSearchResults ServerResponse;
        try {
            Response<ResponseBody> response = callWatchProfileSearch.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();                ServerResponse = gson.fromJson(new String(responseBodeBytes), WatchProfileSearchResults.class);

                return ServerResponse;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public WatchConnectionListResponse watchConnectionListResponse() {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callWatchConnectionList = service.watchConnections(Cookies.getSessionToken());
        WatchConnectionListResponse ServerResponse;
        try {
            Response<ResponseBody> response = callWatchConnectionList.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();                ServerResponse = gson.fromJson(new String(responseBodeBytes), WatchConnectionListResponse.class);

                return ServerResponse;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public WatchConnectionPendingLists watchConnectionPendingLists() {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callWatchConnectionPendingLists = service.watchPendingConnections(Cookies.getSessionToken());
        WatchConnectionPendingLists ServerResponse;
        try {
            Response<ResponseBody> response = callWatchConnectionPendingLists.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodeBytes = response.body().bytes();
Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();                ServerResponse = gson.fromJson(new String(responseBodeBytes), WatchConnectionPendingLists.class);

                return ServerResponse;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void asyncGetWatchList(WatchListCallback callback) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callGetWatchList = service.getWatchList(Cookies.getSessionToken());

        callGetWatchList.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        byte[] responseBodyBytes = response.body().bytes();
                        Gson gson = new Gson();
                        WatchPostResponse watchPostResponse = gson.fromJson(new String(responseBodyBytes), WatchPostResponse.class);
                        callback.onSuccess(watchPostResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                        callback.onFailure("Internal error: " + e.getMessage());
                    }
                } else {
                    callback.onFailure("Request failed. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure("Request failed: " + t.getMessage());
            }
        });
    }

    public void asyncGetUserProfileId(ProfileIdCallback callback) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callAsync = service.getUserProfile(Cookies.getSessionToken());

        callAsync.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        Gson gson = new GsonBuilder().setDateFormat("E MMM dd HH:mm:ss z yyyy").create();
                        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                        int profileId = jsonObject.get("profileId").getAsInt();
                        Cookies.setProfileId(profileId);
                        callback.onSuccess(profileId);
                    } catch (IOException e) {
                        callback.onError("Error parsing response: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    callback.onError("Request failed. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError("Request failed: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
