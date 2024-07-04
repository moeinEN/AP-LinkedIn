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
import Model.Messages;
import Model.Requests.LoginCredentials;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences userData;
    private String serverIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        userData = getSharedPreferences(USER_DATA, MODE_PRIVATE);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onResume() {
        super.onResume();
        serverIP = userData.getString(IP_ADDRESS, "");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!serverIP.isEmpty()) {

            Intent intent = new Intent(this, IPGetterActivity.class);
            startActivity(intent);

            serverIP = userData.getString(IP_ADDRESS, "");
            //finish();

        }

        Toast.makeText(this, serverIP, Toast.LENGTH_LONG).show();
        String token = userData.getString("token", "");
        RetrofitBuilder clientInterface = new RetrofitBuilder("http://" + serverIP + ":8080");
        //LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
        //Messages loginResp = clientInterface.syncCallLogin(loginCredentials);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent user from dismissing it


        // Execute foo() in background thread using anonymous AsyncTask
        new AsyncTask<Void, Void, Messages>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
                // Optional: Show loading dialog before background work starts
            }

            @Override
            protected Messages doInBackground(Void... voids) {
                // Call your foo() method here which sends the HTTP request
                LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
                Messages loginResp = clientInterface.syncCallLogin(loginCredentials);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                return loginResp;
            }

            @Override
            protected void onPostExecute(Messages result) {
                super.onPostExecute(result);
                // Dismiss loading dialog after background work finishes
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, result.getMessage() + serverIP, Toast.LENGTH_LONG).show();
                // Handle the result from foo() (e.g., update UI)
                // ...
            }
        }.execute();
    }
}