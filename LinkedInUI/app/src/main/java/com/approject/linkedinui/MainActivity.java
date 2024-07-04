package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.*;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.approject.linkedinui.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Controller.RetrofitBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        userData = getSharedPreferences(USER_DATA, MODE_PRIVATE);
        String token = userData.getString("token", "");
        String serverIP = "192.168.1.5";
        RetrofitBuilder clientInterface = new RetrofitBuilder("http://" + serverIP + ":8080");
        //LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
        //Messages loginResp = clientInterface.syncCallLogin(loginCredentials);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}