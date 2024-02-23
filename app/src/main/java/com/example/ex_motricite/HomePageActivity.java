    package com.example.ex_motricite;

    import static java.lang.Integer.parseInt;

    import android.content.Intent;
    import android.content.pm.ActivityInfo;
    import android.os.Bundle;
    import android.util.Log;
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

    import java.io.File;
    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Date;

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
            ActivityHomePageBinding binding;
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_page);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            String page = getIntent().getStringExtra("page");
            binding = ActivityHomePageBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
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

            switchToFragment(page);
            updateBin();
    }

    private void updateBin() {
        DeletedTestDAO deletedTestDAO = new DeletedTestDAO(this);
        ArrayList<DeletedTest> deletedTests = deletedTestDAO.getAllTests();
        //Same format as the one in the database
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (DeletedTest deletedTest : deletedTests) {
            try {
                Date date = dateFormat.parse(deletedTest.getSuppressionDate());
                Calendar currentDate = Calendar.getInstance();
                currentDate.add(Calendar.DAY_OF_MONTH, -15);
                if (date.before(currentDate.getTime())) {
                    deletedTestDAO.delTest(deletedTest);
                    File fileToDelete = new File(deletedTest.getPath());

                    // check if the file exists
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    } else {
                        Log.d("HomePageActivity", "File not found");
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    private void switchToFragment(String page) {
        Intent intent = getIntent();
        if (page != null) {
            switch (page) {
                case "exerciseDynamic":
                    final ExercisesSettingsDynamicFragment fragmentDynamic = new ExercisesSettingsDynamicFragment();
                    if (intent.getStringExtra("exerciseType") != null && intent.getStringExtra("name") != null && intent.getStringExtra("actorType") != null){
                        final Bundle args = new Bundle();
                        args.putString("name", intent.getStringExtra("name"));
                        args.putString("actorType", intent.getStringExtra("actorType"));
                        fragmentDynamic.setArguments(args);
                    }
                    replaceFragment(fragmentDynamic);
                    break;
                case "exerciseStatic":
                    final ExercisesFragment fragmentStatic = new ExercisesFragment();
                    if (intent.getStringExtra("exerciseType") != null && intent.getStringExtra("name") != null && intent.getStringExtra("actorType") != null){
                        final Bundle args = new Bundle();
                        args.putString("name", intent.getStringExtra("name"));
                        args.putString("actorType", intent.getStringExtra("actorType"));
                        fragmentStatic.setArguments(args);
                    }
                    replaceFragment(new ExercisesFragment());
                    break;
                case "profilesPatient":
                    replaceFragment(new PatientFragment());
                    break;
                case "profilesOperator":
                    replaceFragment(new OperatorFragment());
                    break;
                case "tests":
                    final TestsFragment fragmentTests = new TestsFragment();
                    if (intent.hasExtra("csvList")) {
                        final Bundle args = new Bundle();
                        args.putStringArrayList("actorType", intent.getStringArrayListExtra("actorType"));
                        fragmentTests.setArguments(args);
                    }
                    replaceFragment(new TestsFragment());
                    break;
                case "settings":
                    replaceFragment(new SettingsFragment());
                    break;
            }
        }
    }
        void replaceFragment(Fragment fragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                    fragment).commit();

        }


        private boolean isOperatorAndPatientExistings() {
            OperatorDAO operatorDAO = new OperatorDAO(this);
            PatientDAO patientDAO = new PatientDAO(this);
            return patientDAO.getPatients().isEmpty() || operatorDAO.getOperators().isEmpty();
        }
    }

