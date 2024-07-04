package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.IP_ADDRESS;
import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.USER_DATA;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import kotlin.jvm.internal.MagicApiIntrinsics;

public class IPGetterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ipgetter);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host_fragment_activity_main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        EditText serverIpEditText = findViewById(R.id.server_ip_edit_text);
        Button submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverIp = serverIpEditText.getText().toString().trim();

                // Validation
                if (serverIp.isEmpty()) {
                    Toast.makeText(IPGetterActivity.this, "Please enter the server IP", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save IP address to SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(IP_ADDRESS, serverIp);
                editor.apply();

                // Handle after saving IP (e.g., close activity, navigate to another screen)
                Intent intent = new Intent(IPGetterActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                 // Close this activity after saving
            }
        });
    }
}