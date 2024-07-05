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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Controller.RetrofitBuilder;
import Model.Messages;
import Model.Requests.LoginCredentials;
import Model.Requests.RegisterCredentials;

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
                TAKEN_USERNAME("This username is already taken!", 0),
                        INVALID_USERNAME("Username is invalid!", 1),
                        INVALID_PASSWORD("Password is invalid!", 2),
                        CONFIRMATION_PASSWORD("Confirmation does not match the password!", 3),
                        INVALID_EMAIL("email is invalid!", 4),
                        EMAIL_EXISTS("email already exists!", 5),
                        INTERNAL_ERROR("Internal error!", 6),
                RegisterCredentials registerCredentials = new RegisterCredentials(email, password, confirmPassword, username);
                Messages signupResp = RetrofitBuilder.clientInterface.syncCallSignUp(registerCredentials);
                if(!signupResp.equals(Messages.SUCCESS)) {
                    if(signupResp.equals(Messages.INVALID_CREDENTIALS)) {
                        Toast.makeText(LoginActivity.this, R.string.login_error_invalid_credentials, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.login_error_network, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}