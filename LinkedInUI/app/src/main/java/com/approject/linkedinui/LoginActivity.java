package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.PROFILE_ID;
import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.TOKEN;
import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.USER_DATA;
import static Controller.InputStringValidator.validateEmail;
import static Controller.InputStringValidator.validatePassword;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import Controller.CallBack.LoginCallback;
import Controller.CallBack.ProfileIdCallback;
import Controller.RetrofitBuilder;
import Controller.SignUpController;
import Model.Cookies;
import Model.Messages;
import Model.Requests.LoginCredentials;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    TextView loginButton, signUpButton;
    Dialog progressDialog;
    boolean safeLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignUp();
            }
        });

        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!SignUpController.isValidEmailAddress(emailText.getText().toString()) && !emailText.getText().toString().trim().equals("")) {
                    emailText.setError("Please enter a valid email.");
                }
                else {
                    safeLogin = true;
                }
            }
        });

        passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!SignUpController.isValidPassword(passwordText.getText().toString()) && !passwordText.getText().toString().equals("")) {
                    passwordText.setError("Please enter a valid password.");
                }
                else {
                    safeLogin = true;
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString().trim(), password = passwordText.getText().toString().trim();
                if (safeLogin == false) {
                    Toast.makeText(LoginActivity.this, R.string.login_safeLoginError, Toast.LENGTH_LONG).show();
                    return;
                }

                LoginCredentials loginCredentials = new LoginCredentials(email, password);
                showProgressDialog();
                RetrofitBuilder.clientInterface.asyncCallLogin(loginCredentials, new LoginCallback() {
                    @Override
                    public void onSuccess(Messages message) {
                        runOnUiThread(() -> {
                            hideProgressDialog();
                            Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        });
                        getSharedPreferences(USER_DATA, MODE_PRIVATE).edit().putString(TOKEN, Cookies.getSessionToken()).commit();
                        System.out.println("TOKEN:{" + Cookies.getSessionToken() + "}");
                        RetrofitBuilder.clientInterface.asyncGetUserProfileId(new ProfileIdCallback() {
                            @Override
                            public void onSuccess(int profileId) {
                                getSharedPreferences(USER_DATA, MODE_PRIVATE).edit().putString(PROFILE_ID, Cookies.getProfileId().toString()).commit();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }

                    @Override
                    public void onFailure(Messages message) {
                        runOnUiThread(() -> {
                            hideProgressDialog();
                            if (message.equals(Messages.INVALID_CREDENTIALS)) {
                                Toast.makeText(LoginActivity.this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
                            } else if (message.equals(Messages.INTERNAL_ERROR)) {
                                Toast.makeText(LoginActivity.this, R.string.internal_error, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.login_error_network, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void navigateToSignUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new Dialog(this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.dialog_progress);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
