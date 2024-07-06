package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.PROFILE_ID;
import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.TOKEN;
import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.USER_DATA;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Controller.InputStringValidator;
import Model.Cookies;

public class CreateProfileActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etAdditionalName, etEmail, etCity, etCountry, etProfession;
    private EditText etInstituteName, etMajor, etEducationStartDate, etEducationEndDate, etGPA, etActivitiesDescription, etDescription;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAdditionalName = findViewById(R.id.etAdditionalName);
        etEmail = findViewById(R.id.etEmail);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        etProfession = findViewById(R.id.etProfession);
        etInstituteName = findViewById(R.id.etInstituteName);
        etMajor = findViewById(R.id.etMajor);
        etEducationStartDate = findViewById(R.id.etEducationStartDate);
        etEducationEndDate = findViewById(R.id.etEducationEndDate);
        etGPA = findViewById(R.id.etGPA);
        etActivitiesDescription = findViewById(R.id.etActivitiesDescription);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSubmit);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // for not breaking the app
                Cookies.setProfileId(1);
                getSharedPreferences(USER_DATA, MODE_PRIVATE).edit().putInt(PROFILE_ID, Cookies.getProfileId()).commit();
                finish();


                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String additionalName = etAdditionalName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String city = etCity.getText().toString().trim();
                String country = etCountry.getText().toString().trim();
                String profession = etProfession.getText().toString().trim();
                String instituteName = etInstituteName.getText().toString().trim();
                String major = etMajor.getText().toString().trim();
                String educationStartDate = etEducationStartDate.getText().toString().trim();
                String educationEndDate = etEducationEndDate.getText().toString().trim();
                String gpa = etGPA.getText().toString().trim();
                String activitiesDescription = etActivitiesDescription.getText().toString().trim();
                String description = etDescription.getText().toString().trim();

                if (!InputStringValidator.validateName(firstName)) {
                    showToast(R.string.invalid_name);
                    return;
                }

                if (!InputStringValidator.validateLastName(lastName)) {
                    showToast(R.string.invalid_last_name);
                    return;
                }

                if (!InputStringValidator.validateSize(40, additionalName)) {
                    showToast(R.string.invalid_additional_name);
                    return;
                }

                if (!InputStringValidator.validateEmail(email)) {
                    showToast(R.string.invalid_email);
                    return;
                }

                if (!InputStringValidator.validateSize(40, city)) {
                    showToast(R.string.invalid_city);
                    return;
                }

                if (!InputStringValidator.validateSize(40, country)) {
                    showToast(R.string.invalid_country);
                    return;
                }

                if (!InputStringValidator.validateSize(50, profession)) {
                    showToast(R.string.invalid_profession);
                    return;
                }

                if (!InputStringValidator.validateSize(40, instituteName)) {
                    showToast(R.string.invalid_institute_name);
                    return;
                }

                if (!InputStringValidator.validateSize(40, major)) {
                    showToast(R.string.invalid_major);
                    return;
                }

                if (!InputStringValidator.validateSize(40, educationStartDate)) {
                    showToast(R.string.invalid_education_start_date);
                    return;
                }

                if (!InputStringValidator.validateSize(40, educationEndDate)) {
                    showToast(R.string.invalid_education_end_date);
                    return;
                }

                if (!InputStringValidator.validateSize(40, gpa)) {
                    showToast(R.string.invalid_gpa);
                    return;
                }

                if (!InputStringValidator.validateSize(500, activitiesDescription)) {
                    showToast(R.string.invalid_activities_description);
                    return;
                }

                if (!InputStringValidator.validateSize(1000, description)) {
                    showToast(R.string.invalid_description);
                    return;
                }

                // Save the data
                Toast.makeText(CreateProfileActivity.this, getString(R.string.profile_saved), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(int resId) {
        Toast.makeText(CreateProfileActivity.this, getString(resId), Toast.LENGTH_SHORT).show();
    }
}
