package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class homePageActivity extends AppCompatActivity {

    private LinearLayout layout_static;
    private LinearLayout layout_rythm;
    private LinearLayout layout_patient;
    private ImageView iv_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        layout_static = findViewById(R.id.layout_static);
        layout_rythm = findViewById(R.id.layout_rythm);
        layout_patient = findViewById(R.id.layout_patient);
        iv_settings =  findViewById(R.id.iv_settings);

        layout_static.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, ExercisesSettingsActivity.class);
                intent.putExtra("Exercice", "static");
                startActivity(intent);
            }
        });

        layout_rythm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, ExercisesSettingsActivity.class);
                intent.putExtra("Exercice", "rythm");
                startActivity(intent);
            }
        });

        layout_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, listUserPageActivity.class);
                startActivity(intent);
            }
        });

        /*
        iv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, settingsActivity.class);
                startActivity(intent);
            }
        });*/

    }
}