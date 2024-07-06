package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.approject.linkedinui.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;

import Controller.*;
import Controller.CallBack.WatchListCallback;
import Controller.CallBack.WatchProfileCallback;
import Model.*;
import Model.Requests.WatchProfileRequest;
import Model.Response.WatchPostResponse;
import Model.Response.WatchProfileResponse;


public class MainActivity extends AppCompatActivity implements OnItemClickListener{

    private ActivityMainBinding binding;
    private SharedPreferences userData;
    private String serverIP;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private ArrayList<Post> FeedModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);



    }

    @Override
    protected void onResume() {
        super.onResume();
        userData = getSharedPreferences(USER_DATA, MODE_PRIVATE);
        serverIP = userData.getString(IP_ADDRESS, "");
        //if(serverIP.isEmpty()) {
        if(serverIP.equals("")){
            Intent intent = new Intent(this, IPGetterActivity.class);
            startActivity(intent);
        }
        Toast.makeText(this, serverIP, Toast.LENGTH_LONG).show();
        RetrofitBuilder.clientInterface = new RetrofitBuilder("http://" + serverIP + ":8080");
        String token = userData.getString(TOKEN, "");
        System.out.println("###"+token+"###");
        Cookies.setSessionToken(token);
        if(token.equals("")){// || !clientInterface.validateToken().getMessage().equals(Messages.SUCCESS)){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        int profileId = userData.getInt(PROFILE_ID, 0);
        Cookies.setProfileId(profileId);
        if(profileId == 0) {
            Intent intent = new Intent(this, CreateProfileActivity.class);
            startActivity(intent);
            finish();
        }
        RetrofitBuilder.clientInterface.asyncWatchProfileRequest(new WatchProfileRequest(userData.getInt(PROFILE_ID, 0)), new WatchProfileCallback() {
            @Override
            public void onSuccess(WatchProfileResponse response) {
                Gson gson = new Gson();
                String profileJson = gson.toJson(response);

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("profile", profileJson);
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {
                runOnUiThread(() -> {
                    if (!isFinishing()) {
                        Toast.makeText(MainActivity.this, "Failed to fetch profile: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });}
        });


        progressBar = findViewById(R.id.idLoadingPB);

        // calling method to load data in recycler view.
        getFeeds();



//        progressDialog.show();
//        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
//        Messages loginResp = clientInterface.syncCallLogin(loginCredentials);
//        progressDialog.dismiss();
//        Toast.makeText(MainActivity.this,  serverIP, Toast.LENGTH_SHORT).show();

    }

    private void getFeeds() {
        progressBar.setVisibility(View.GONE);
        FeedModalArrayList = new ArrayList<>();

        RetrofitBuilder.clientInterface.asyncCallDownload("kir.jpg");

        RetrofitBuilder.clientInterface.asyncGetWatchList(new WatchListCallback() {
            @Override
            public void onSuccess(WatchPostResponse watchPostResponse) {
                if (watchPostResponse.getPosts().size() != 0) {
                    FeedModalArrayList.addAll(watchPostResponse.getPosts());
                    System.out.println(watchPostResponse.getPosts().toString());

                    // Set up the RecyclerView with the posts
                    FeedRVAdapter adapter = new FeedRVAdapter(FeedModalArrayList, MainActivity.this, MainActivity.this);
                    RecyclerView instRV = findViewById(R.id.idRVInstaFeeds);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                    instRV.setLayoutManager(linearLayoutManager);
                    instRV.setAdapter(adapter);
                } else {
                    FeedEmptyAdapter adapter = new FeedEmptyAdapter(MainActivity.this);
                    RecyclerView instRV = findViewById(R.id.idRVInstaFeeds);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);
                    instRV.setLayoutManager(linearLayoutManager);
                    instRV.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                System.out.println(errorMessage);
                Toast.makeText(MainActivity.this, "Fail to get data with error " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthorImageClick(int position) {
        Post clickedPost = FeedModalArrayList.get(position);
        Toast.makeText(this, "Author image clicked: " + clickedPost.getMiniProfile().getFirstName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostImageClick(int position) {
        Post clickedPost = FeedModalArrayList.get(position);
        Toast.makeText(this, "Post image clicked", Toast.LENGTH_SHORT).show();
        // You can start a new activity or perform other actions here
    }

    @Override
    public void onAuthorNameClick(int position) {
        Post clickedPost = FeedModalArrayList.get(position);
        Toast.makeText(this, "Author name clicked: " + clickedPost.getMiniProfile().getFirstName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLikeImageClick(int position) {
        Post clickedPost = FeedModalArrayList.get(position);
        Toast.makeText(this, "Liked post: " + clickedPost.getText(), Toast.LENGTH_SHORT).show();
        // Handle the like action here, such as updating the server or UI
    }
}