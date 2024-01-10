package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {

    private RadioButton rbEmail;
    private RadioButton rbServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rbEmail = findViewById(R.id.rb_email);
        rbServer = findViewById(R.id.rb_server);

        rbEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(rbEmail);
            }
        });

        rbServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(rbServer);
            }
        });
    }

    private void onRadioButtonClicked(RadioButton clickedRadioButton) {
        // Décoche l'autre RadioButton et le désactive
        if (clickedRadioButton == rbEmail) {
            rbServer.setChecked(false);
        } else if (clickedRadioButton == rbServer) {
            rbEmail.setChecked(false);
        }

    }

}