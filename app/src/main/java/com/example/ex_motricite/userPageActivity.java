package com.example.ex_motricite;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class userPageActivity extends AppCompatActivity {

    private LinearLayout layout_static;
    private LinearLayout layout_rythm;
    private LinearLayout layout_patient;
    private ImageView iv_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_page);

        layout_static = findViewById(R.id.layout_static);
        layout_rythm = findViewById(R.id.layout_rythm);
        layout_patient = findViewById(R.id.layout_patient);
        iv_settings =  findViewById(R.id.iv_settings);

        /*layout_static.setOnClickListener(new View.OnClickListener() {
            @Override
            /public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, staticActivity.class);
                startActivity(intent);
            }
        });

        layout_rythm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, rythmActivity.class);
                startActivity(intent);
            }
        });

        layout_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, patientActivity.class);
                startActivity(intent);
            }
        });

        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, settingsActivity.class);
                startActivity(intent);
            }
        });*/

    }
}