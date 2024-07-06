package com.approject.linkedinui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import Model.Response.WatchProfileResponse;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    TextView Name, about, country, city,  profession, currentJob, currentInstitute, jobStatus, contactInfo;
    CircleImageView avatar;
    ImageView backgroundImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        Name = findViewById(R.id.fullname);
        about = findViewById(R.id.about);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        profession = findViewById(R.id.profession);
        currentJob = findViewById(R.id.currentJob);
        currentInstitute = findViewById(R.id.institute);
        jobStatus = findViewById(R.id.jobStatus);
        contactInfo = findViewById(R.id.contactInfo);
        avatar = findViewById(R.id.avatar);
        backgroundImage = findViewById(R.id.background);


        String profileJson = getIntent().getStringExtra("profile");
        if (profileJson != null) {
            Gson gson = new Gson();
            WatchProfileResponse profile = gson.fromJson(profileJson, WatchProfileResponse.class);
            // Set the texts based on the profile data
            setProfileData(profile);
        }


        contactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "click received", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setProfileData(WatchProfileResponse profile) {
        // Assuming WatchProfileResponse has the appropriate getters
        Name.setText(profile.getHeader().getFirstName() + " " + profile.getHeader().getLastName());
        about.setText(profile.getHeader().getAbout());
        country.setText(profile.getHeader().getCountry());
        city.setText(profile.getHeader().getCity());
        profession.setText(profile.getHeader().getProfession());
        currentJob.setText(profile.getHeader().getCurrentJob().getTitle());
        currentInstitute.setText(profile.getHeader().getEducationalInfo().getInstituteName());
        jobStatus.setText(profile.getHeader().getJobStatus());
    }

}
