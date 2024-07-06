package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.approject.linkedinui.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.TimeZone;

import Controller.CallBack.WatchProfileCallback;
import Controller.RetrofitBuilder;
import Model.Cookies;
import Model.Requests.WatchProfileRequest;
import Model.Response.WatchProfileResponse;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences userData;
    private String serverIP;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        userData = getSharedPreferences(USER_DATA, MODE_PRIVATE);

        serverIP = userData.getString(IP_ADDRESS, "");
        //if(serverIP.isEmpty()) {
        if(serverIP.equals("")){
            Intent intent = new Intent(this, IPGetterActivity.class);
            startActivity(intent);
        }
        //Toast.makeText(this, serverIP, Toast.LENGTH_LONG).show();
        RetrofitBuilder clientInterface = new RetrofitBuilder("http://" + serverIP + ":8080");
        RetrofitBuilder.clientInterface = clientInterface;
        String token = userData.getString(TOKEN, "");
        //System.out.println("###"+token+"###");
        Cookies.setSessionToken(token);
        if(token.equals("")){// || !clientInterface.validateToken().getMessage().equals(Messages.SUCCESS)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        int profileId = userData.getInt(PROFILE_ID, 0);
        Cookies.setProfileId(profileId);
        if(profileId == 0) {
            Intent intent = new Intent(this, CreateProfileActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

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
                });            }
        });



//        progressDialog.show();
//        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
//        Messages loginResp = clientInterface.syncCallLogin(loginCredentials);
//        progressDialog.dismiss();
//        Toast.makeText(MainActivity.this,  serverIP, Toast.LENGTH_SHORT).show();

    }
}