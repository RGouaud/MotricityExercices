package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ExercisesSettingsActivity} class represents an Android activity for configuring exercise settings.
 * It allows users to set parameters such as patient, operator, distance, time, and interval for static and rhythm tests.
 *
 * <p>
 * This activity includes functionalities to display a list of patients and operators, handle user input,
 * and navigate to specific exercise activities based on user selections. It utilizes DAOs (Data Access Objects)
 * to retrieve patient and operator data from a database.
 * </p>
 *
 * <p>
 * The class supports both static and rhythm tests, with dynamic adjustments based on the selected exercise type.
 * It ensures proper input validation and provides a user-friendly interface for configuring exercise settings.
 * </p>
 *
 * <p>
 * Author: Segot, Arricastres
 * Version: 1.0
 * </p>
 */
public class ExercisesSettingsActivity extends AppCompatActivity {

    /**
     * The spinner for patients.
     */
    private Spinner sPatient;
    /**
     * The spinner for operators.
     */
    private Spinner sOperator;
    /**
     * The EditText for the interval.
     */
    private EditText etInterval;
    /**
     * The SeekBar for the interval.
     */
    private SeekBar sbInterval;
    /**
     * The list of patients.
     */
    private ArrayList<Patient> lstPatient;
    /**
     * The list of operators.
     */
    private ArrayList<Operator> lstOperator;

    /**
     * Display actors (patients or operators) on spinners.
     *
     * @param actors List of actors to be displayed.
     */
    public void displayActorsOnSpinners(List<? extends Actor> actors) {
        ArrayAdapter<String> dataSpinner = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        if (actors.get(0) instanceof Operator) {
           dataSpinner.add("Choose an operator");
        } else {
           dataSpinner.add("Choose a patient");
        }
        for (int i = 0; i < actors.size(); i++) {
            dataSpinner.add(actors.get(i).getName());
        }
        if (actors.get(0) instanceof Operator) {
            sOperator.setAdapter(dataSpinner);
        } else {
            sPatient.setAdapter(dataSpinner);
        }

    }
    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Todo : use fragments to reduce Cognitive Complexity
        EditText etSeconds;
        EditText etDistance;
        OperatorDAO operatorDAO;
        PatientDAO patientDAO;
        Button bStart;
        TextView tvSettingsTittle;
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_exercises_settings);
        sOperator = findViewById(R.id.s_manipulator);
        sPatient = findViewById(R.id.s_patient);
        etDistance = findViewById(R.id.et_distance);
        etInterval = findViewById(R.id.et_interval);
        etSeconds = findViewById(R.id.et_seconds);
        sbInterval = findViewById(R.id.sb_interval);
        tvSettingsTittle = findViewById(R.id.tv_settings_title);
        sbInterval = findViewById(R.id.sb_interval);
        bStart = findViewById(R.id.b_start);

        patientDAO = new PatientDAO(this);
        operatorDAO = new OperatorDAO(this);

        try {
            lstOperator = operatorDAO.getOperators();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error while getting operators", Toast.LENGTH_SHORT).show();
        }

        try {
            lstPatient = patientDAO.getPatients();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error while getting patients", Toast.LENGTH_SHORT).show();
        }

        etInterval.setFilters(new InputFilter[]{new MinMaxFilter(1, 15)});

        sbInterval.setMax(15);
        sbInterval.setProgress(0);


        Intent myIntent = getIntent();
        String exercise = myIntent.getStringExtra("exercise");

        assert exercise != null;
        if (exercise.equals("static")){
            tvSettingsTittle.setText("Static test settings");
            etInterval.setVisibility(View.INVISIBLE);
            sbInterval.setVisibility(View.INVISIBLE);

            bStart.setOnClickListener(v -> {
                if (etDistance.getText().toString().matches("") || etSeconds.getText().toString().matches(""))
                {
                    Toast.makeText(ExercisesSettingsActivity.this, "You must complete each fields !", Toast.LENGTH_SHORT).show();


                }
                else{
                    Intent intent = new Intent(ExercisesSettingsActivity.this, StaticExerciseActivity.class);
                    intent.putExtra("Distance", etDistance.getText().toString());
                    intent.putExtra("Time", etSeconds.getText().toString());
                    intent.putExtra("Patient", sPatient.getSelectedItem().toString());
                    intent.putExtra("Operator", sOperator.getSelectedItem().toString());
                    startActivity(intent);
                }
            });
        }
        else {
            tvSettingsTittle.setText("Rhythm test settings");
            sbInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


                // When Progress value changed.
                @Override
                public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                    etInterval.setText(String.valueOf(sbInterval.getProgress()));
                }

                // Notification that the user has started a touch gesture.
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // nothing to do when we start touching the bar
                }

                // Notification that the user has finished a touch gesture
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    etInterval.setText(String.valueOf(sbInterval.getProgress()));
                }
            });
            etInterval.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus){
                    sbInterval.setProgress(Integer.parseInt(etInterval.getText().toString()));
                }
            });

            bStart.setOnClickListener(v -> {
                if (etDistance.getText().toString().matches("") || etSeconds.getText().toString().matches("")|| etInterval.getText().toString().matches("") || sPatient.getSelectedItem().toString().matches("Choose a patient") || sOperator.getSelectedItem().toString().matches("Choose an operator"))
                {
                    Toast.makeText(ExercisesSettingsActivity.this, "You must complete each fields !", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(ExercisesSettingsActivity.this, DynamicExerciseActivity.class);
                    intent.putExtra("Distance", etDistance.getText().toString());
                    intent.putExtra("Interval", etInterval.getText().toString());
                    intent.putExtra("Time", etSeconds.getText().toString());
                    intent.putExtra("Patient", sPatient.getSelectedItem().toString());
                    intent.putExtra("Operator", sOperator.getSelectedItem().toString());
                    startActivity(intent);

                }
            });

        }
        displayActorsOnSpinners(lstPatient);
        displayActorsOnSpinners(lstOperator);

    }




/*TODO
    BUTTON START : idk
    RECOVER BUTTON: grayed out if no CSV found at the location
    otherwise: read the meta data from the last CSV and write them to their editText
     */
}