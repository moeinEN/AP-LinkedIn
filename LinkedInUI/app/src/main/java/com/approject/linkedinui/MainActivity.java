package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.*;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.approject.linkedinui.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Controller.RetrofitBuilder;
import Model.Cookies;
import Model.Messages;
import Model.Requests.LoginCredentials;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverIP = userData.getString(IP_ADDRESS, "");
        //if(serverIP.isEmpty()) {
        if(serverIP.equals("")){
            Intent intent = new Intent(this, IPGetterActivity.class);
            startActivity(intent);
            finish();
        }
        //Toast.makeText(this, serverIP, Toast.LENGTH_LONG).show();
        RetrofitBuilder clientInterface = new RetrofitBuilder("http://" + serverIP + ":8080");
        RetrofitBuilder.clientInterface = clientInterface;
        String token = userData.getString(TOKEN, "");
        System.out.println("###"+token+"###");
        Cookies.setSessionToken(token);
        if(token.equals("")){// || !clientInterface.validateToken().getMessage().equals(Messages.SUCCESS)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

//        progressDialog.show();
//        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
//        Messages loginResp = clientInterface.syncCallLogin(loginCredentials);
//        progressDialog.dismiss();
//        Toast.makeText(MainActivity.this,  serverIP, Toast.LENGTH_SHORT).show();

    }
}