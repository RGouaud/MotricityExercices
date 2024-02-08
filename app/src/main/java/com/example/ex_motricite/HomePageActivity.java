    package com.example.ex_motricite;

    import androidx.appcompat.app.AppCompatActivity;
    import android.content.Intent;
    import android.content.pm.ActivityInfo;
    import android.os.Bundle;
    import android.widget.ImageButton;
    import android.widget.LinearLayout;

    /**
     * The {@code HomePageActivity} class represents an Android activity for the home page,
     * displaying options for different exercises and lists.
     *
     * <p>
     * This activity provides a user interface with clickable elements that lead to specific
     * exercises and lists. It includes options for static exercises, rhythm exercises, patient
     * lists, and a direct link to the list of CSV test files.
     * </p>
     *
     * <p>
     * Each option is implemented with an {@code ImageButton} or {@code LinearLayout} element,
     * and clicking on these elements navigates the user to the corresponding activity.
     * </p>
     *
     * <p>
     * Author: Ferreira, EduardoXav
     * Version: 1.0
     * </p>
     */
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