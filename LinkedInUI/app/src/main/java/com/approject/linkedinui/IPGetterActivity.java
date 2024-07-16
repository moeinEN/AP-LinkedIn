package com.approject.linkedinui;

import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.IP_ADDRESS;
import static com.approject.linkedinui.runtimeconstants.SharedPreferencesNames.USER_DATA;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class IPGetterActivity extends AppCompatActivity {
    EditText serverIpEditText;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ipgetter);

        serverIpEditText = findViewById(R.id.server_ip_edit_text);
        submitButton = findViewById(R.id.submit_button1);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverIp = serverIpEditText.getText().toString().trim();

                // Validation
                if (serverIp.isEmpty() || !isValidIPAddress(serverIp)) {
                    Toast.makeText(IPGetterActivity.this, "Please enter a valid IP address.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(IP_ADDRESS, serverIp);
                editor.apply();

                finish();
            }
        });
    }

    public boolean isValidIPAddress(String ip) {
        String ipPattern =
                "^(([0-9]|[0-9][0-9]|[0-1][0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[0-9][0-9]|[0-1][0-9][0-9]|2[0-4][0-9]|25[0-5])$";
        return ip.matches(ipPattern);
    }
}