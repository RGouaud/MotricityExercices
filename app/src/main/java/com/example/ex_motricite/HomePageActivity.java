package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.ex_motricite.databinding.ActivityHomePageBinding;

public class HomePageActivity extends AppCompatActivity {
    ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton imageButtonListTest;
        LinearLayout layoutPatient;
        LinearLayout layoutRhythm;
        LinearLayout layoutStatic;
        super.onCreate(savedInstanceState);
        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        replaceFragment(new ExercisesFragment());

        binding.bottonnav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.exercises:
                    replaceFragment(new ExercisesFragment());
                    break;
                case R.id.profiles:
                    replaceFragment(new ProfilesFragment());
                    break;
                case R.id.tests:
                    replaceFragment(new TestsFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;

            }
            return true;
        });
    }
        void replaceFragment(Fragment fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }

        /*layoutStatic = findViewById(R.id.layout_static);
        layoutRhythm = findViewById(R.id.layout_rythm);
        layoutPatient = findViewById(R.id.layout_patient);
        imageButtonListTest = findViewById(R.id.ib_ListTest);

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
        });*/

    }
