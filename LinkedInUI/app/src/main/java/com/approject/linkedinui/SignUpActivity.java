package com.approject.linkedinui;

import static Controller.InputStringValidator.validateEmail;
import static Controller.InputStringValidator.validatePassword;
import static Controller.InputStringValidator.validateUsername;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Controller.RetrofitBuilder;
import Model.Messages;
import Model.Requests.LoginCredentials;
import Model.Requests.RegisterCredentials;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import Controller.CallBack.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText emailText, passwordText, usernameText, confirmPasswordText;
    Button loginButton, signUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        emailText = findViewById(R.id.email);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.confirmation_password);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString(),
                        username = usernameText.getText().toString(),
                        password = passwordText.getText().toString(),
                        confirmPassword = confirmPasswordText.getText().toString();
                if(!validateEmail(email)) {
                    Toast.makeText(SignUpActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validatePassword(password)) {
                    Toast.makeText(SignUpActivity.this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validateUsername(username)) {
                    Toast.makeText(SignUpActivity.this, R.string.invalid_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, R.string.signup_error_password_mismatch, Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterCredentials registerCredentials = new RegisterCredentials(email, password, confirmPassword, username);
//                Messages signupResp = RetrofitBuilder.clientInterface.syncCallSignUp(registerCredentials);

                RetrofitBuilder.clientInterface.asyncCallSignUp(registerCredentials, new SignUpCallback() {
                    @Override
                    public void onSuccess(Messages message) {
                        System.out.println("######" + message + "#####");
                        if (message.equals(Messages.SUCCESS)){
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.signup_success_message, Toast.LENGTH_SHORT).show());
                        }
                        else if (message.equals(Messages.INVALID_EMAIL)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.signup_error_email_invalid, Toast.LENGTH_SHORT).show());
                        }
                        else if(message.equals(Messages.INVALID_USERNAME)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.invalid_username, Toast.LENGTH_SHORT).show());
                        }
                        else if(message.equals(Messages.INVALID_PASSWORD)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.invalid_password, Toast.LENGTH_SHORT).show());
                        }
                        else if(message.equals(Messages.CONFIRMATION_PASSWORD)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.signup_error_password_mismatch, Toast.LENGTH_SHORT).show());
                        }
                        else if(message.equals(Messages.EMAIL_EXISTS)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.email_exist, Toast.LENGTH_SHORT).show());
                        }
                        else if(message.equals(Messages.TAKEN_USERNAME)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.username_exist, Toast.LENGTH_SHORT).show());
                        }
                        else if(message.equals(Messages.INTERNAL_ERROR)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.internal_error, Toast.LENGTH_SHORT).show());
                        }  else {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.login_error_network, Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onFailure(Messages message) {
                        runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.internal_error, Toast.LENGTH_SHORT).show());
                    }
                });

                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}