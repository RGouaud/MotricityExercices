package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class homePageActivity extends AppCompatActivity {

    private LinearLayout layout_static;
    private LinearLayout layout_rythm;
    private LinearLayout layout_patient;
    private ImageButton imageButtonListTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        layout_static = findViewById(R.id.layout_static);
        layout_rythm = findViewById(R.id.layout_rythm);
        layout_patient = findViewById(R.id.layout_patient);
        imageButtonListTest = findViewById(R.id.ib_ListTest);

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
                Intent intent = new Intent(homePageActivity.this, ListUserPageActivity.class);
                startActivity(intent);
            }
        });

        imageButtonListTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePageActivity.this, ListTestActivity.class);
                startActivity(intent);
            }
        });

    }
}