package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageButtonListTest;
        LinearLayout layoutPatient;
        LinearLayout layoutRhythm;
        LinearLayout layoutStatic;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        layoutStatic = findViewById(R.id.layout_static);
        layoutRhythm = findViewById(R.id.layout_rythm);
        layoutPatient = findViewById(R.id.layout_patient);
        imageButtonListTest = findViewById(R.id.ib_ListTest);
        Space spaceTemp;
        spaceTemp = findViewById(R.id.SpacerTemp);
        spaceTemp.setClickable(true);
        spaceTemp.setOnClickListener(v -> {
            startActivity(new Intent(HomePageActivity.this, BinActivity.class));
        });

        layoutStatic.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ExercisesSettingsActivity.class);
            intent.putExtra("exercise", "static");
            startActivity(intent);
        });

        layoutRhythm.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ExercisesSettingsActivity.class);
            intent.putExtra("exercise", "rhythm");
            startActivity(intent);
        });

        layoutPatient.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ListUserPageActivity.class);
            startActivity(intent);
        });

        imageButtonListTest.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, ListTestActivity.class);
            startActivity(intent);
        });

    }
}