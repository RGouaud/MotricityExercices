    package com.example.ex_motricite;

    import android.content.Intent;
    import android.content.pm.ActivityInfo;
    import android.os.Bundle;
    import android.widget.ImageButton;
    import android.widget.LinearLayout;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.Fragment;
    import androidx.fragment.app.FragmentTransaction;

    import android.content.pm.ActivityInfo;
    import android.os.Bundle;
    import android.widget.ImageButton;
    import android.widget.LinearLayout;

    import com.example.ex_motricite.databinding.ActivityHomePageBinding;
    import androidx.appcompat.app.AppCompatActivity;

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
            ImageButton ibListTest;
            ActivityHomePageBinding binding;

            LinearLayout llPatient;
            LinearLayout llRhythm;
            LinearLayout llStatic;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            /*llStatic = findViewById(R.id.layout_static);
            llRhythm = findViewById(R.id.layout_rythm);
            llPatient = findViewById(R.id.layout_patient);
            ibListTest = findViewById(R.id.ib_ListTest);*/

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
                    replaceFragment(new PatientFragment());
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


            llRhythm.setOnClickListener(v -> {
                if (isOperatorAndPatientExistings()) {
                    Toast.makeText(this, "First, you need to create a patient and an operator.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(HomePageActivity.this, ExercisesSettingsActivity.class);
                intent.putExtra("exercise", "rhythm");
                startActivity(intent);
            });

            llPatient.setOnClickListener(v -> {
                Intent intent = new Intent(HomePageActivity.this, ListUserPageActivity.class);
                startActivity(intent);
            });

            ibListTest.setOnClickListener(v -> {
                Intent intent = new Intent(HomePageActivity.this, ListTestActivity.class);
                startActivity(intent);
            });

        }*/

        private boolean isOperatorAndPatientExistings() {
            OperatorDAO operatorDAO = new OperatorDAO(this);
            PatientDAO patientDAO = new PatientDAO(this);
            return patientDAO.getPatients().isEmpty() || operatorDAO.getOperators().isEmpty();
        }
    }

