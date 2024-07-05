package com.approject.linkedinui;

import static Controller.InputStringValidator.validateEmail;
import static Controller.InputStringValidator.validatePassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Controller.CallBack.LoginCallback;
import Controller.RetrofitBuilder;
import Model.Messages;
import Model.Requests.LoginCredentials;
import Model.Requests.RegisterCredentials;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    Button loginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString(), password = passwordText.getText().toString();
                if(!validateEmail(email)) {
                    Toast.makeText(LoginActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!validatePassword(password)) {
                    Toast.makeText(LoginActivity.this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginCredentials loginCredentials = new LoginCredentials(email, password);
//                Messages loginResp = RetrofitBuilder.clientInterface.syncCallLogin(loginCredentials);

               RetrofitBuilder.clientInterface.asyncCallLogin(loginCredentials, new LoginCallback() {
                    @Override
                    public void onSuccess(Messages message) {
                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailure(Messages message) {
                        runOnUiThread(() -> {
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

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
    }
}