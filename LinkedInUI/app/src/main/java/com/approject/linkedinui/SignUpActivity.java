package com.approject.linkedinui;

import static Controller.InputStringValidator.validateEmail;
import static Controller.InputStringValidator.validatePassword;
import static Controller.InputStringValidator.validateUsername;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import Controller.RetrofitBuilder;
import Model.Messages;
import Model.Requests.RegisterCredentials;
import Controller.CallBack.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText emailText, passwordText, usernameText, confirmPasswordText;
    TextView loginButton, signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        emailText = findViewById(R.id.email);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        confirmPasswordText = findViewById(R.id.confirmation_password);
        loginButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString(),
                        username = usernameText.getText().toString(),
                        password = passwordText.getText().toString(),
                        confirmPassword = confirmPasswordText.getText().toString();
                if (!validateEmail(email)) {
                    Toast.makeText(SignUpActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validatePassword(password)) {
                    Toast.makeText(SignUpActivity.this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!validateUsername(username)) {
                    Toast.makeText(SignUpActivity.this, R.string.invalid_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, R.string.signup_error_password_mismatch, Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterCredentials registerCredentials = new RegisterCredentials(email, password, confirmPassword, username);
                // Show progress dialog here if needed
                RetrofitBuilder.clientInterface.asyncCallSignUp(registerCredentials, new SignUpCallback() {
                    @Override
                    public void onSuccess(Messages message) {
                        if (message.equals(Messages.SUCCESS)) {
                            runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.signup_success_message, Toast.LENGTH_SHORT).show());
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            handleSignUpFailure(message);
                        }
                    }

                    @Override
                    public void onFailure(Messages message) {
                        runOnUiThread(() -> Toast.makeText(SignUpActivity.this, R.string.internal_error, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void handleSignUpFailure(Messages message) {
        runOnUiThread(() -> {
            if (message.equals(Messages.INVALID_EMAIL)) {
                Toast.makeText(SignUpActivity.this, R.string.signup_error_email_invalid, Toast.LENGTH_SHORT).show();
            } else if (message.equals(Messages.INVALID_USERNAME)) {
                Toast.makeText(SignUpActivity.this, R.string.invalid_username, Toast.LENGTH_SHORT).show();
            } else if (message.equals(Messages.INVALID_PASSWORD)) {
                Toast.makeText(SignUpActivity.this, R.string.invalid_password, Toast.LENGTH_SHORT).show();
            } else if (message.equals(Messages.CONFIRMATION_PASSWORD)) {
                Toast.makeText(SignUpActivity.this, R.string.signup_error_password_mismatch, Toast.LENGTH_SHORT).show();
            } else if (message.equals(Messages.EMAIL_EXISTS)) {
                Toast.makeText(SignUpActivity.this, R.string.email_exist, Toast.LENGTH_SHORT).show();
            } else if (message.equals(Messages.TAKEN_USERNAME)) {
                Toast.makeText(SignUpActivity.this, R.string.username_exist, Toast.LENGTH_SHORT).show();
            } else if (message.equals(Messages.INTERNAL_ERROR)) {
                Toast.makeText(SignUpActivity.this, R.string.internal_error, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUpActivity.this, R.string.login_error_network, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
