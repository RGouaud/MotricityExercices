package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
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

public class ExercisesSettingsActivity extends AppCompatActivity {

    private Spinner s_patient;
    private Spinner s_operator;
    private EditText et_distance;
    private EditText et_interval;
    private EditText et_seconds;
    private TextView tv_settings_tittle;
    private SeekBar sb_interval;
    private Button b_start;
    private PatientDAO patientDAO;
    private Patient patient;
    private ArrayList<Patient> lstPatient;
    private OperatorDAO operatorDAO;
    private Operator operator;
    private ArrayList<Operator> lstOperator;

    public void DisplayActorsOnSpinners(ArrayList<? extends Actor> actors) {
        ArrayAdapter<String> dataSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        if (actors.get(0) instanceof Operator) {
           dataSpinner.add("Choose an operator");
        } else {
           dataSpinner.add("Choose a patient");
        }
        for (int i = 0; i < actors.size(); i++) {
            dataSpinner.add(actors.get(i).getName());
        }
        if (actors.get(0) instanceof Operator) {
            s_operator.setAdapter(dataSpinner);
        } else {
            s_patient.setAdapter(dataSpinner);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_exercises_settings);
        s_operator = findViewById(R.id.s_manipulator);
        s_patient = findViewById(R.id.s_patient);
        et_distance = findViewById(R.id.et_distance);
        et_interval = findViewById(R.id.et_interval);
        et_seconds = findViewById(R.id.et_seconds);
        sb_interval = findViewById(R.id.sb_interval);
        tv_settings_tittle = findViewById(R.id.tv_settings_title);
        sb_interval = findViewById(R.id.sb_interval);
        b_start = findViewById(R.id.b_start);

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

        et_interval.setFilters(new InputFilter[]{new MinMaxFilter(1, 15)});

        sb_interval.setMax(15);
        sb_interval.setProgress(0);


        Intent myIntent = getIntent();
        String exercice = myIntent.getStringExtra("Exercice");

        if (exercice.equals("static")){
            tv_settings_tittle.setText("Static test settings");
            et_interval.setVisibility(View.INVISIBLE);
            sb_interval.setVisibility(View.INVISIBLE);

            b_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et_distance.getText().toString().matches("") || et_seconds.getText().toString().matches(""))
                    {
                        Toast.makeText(ExercisesSettingsActivity.this, "You must complete each fields !", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Intent intent = new Intent(ExercisesSettingsActivity.this, StaticExerciceActivity.class);
                        intent.putExtra("Distance", et_distance.getText().toString());
                        intent.putExtra("Time", et_seconds.getText().toString());
                        intent.putExtra("Patient", s_patient.getSelectedItem().toString());
                        intent.putExtra("Operator", s_operator.getSelectedItem().toString());
                        startActivity(intent);
                    }
                }
            });
            DisplayActorsOnSpinners(lstPatient);
            DisplayActorsOnSpinners(lstOperator);
        }
        else {
            tv_settings_tittle.setText("Rythm test settings");
            sb_interval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


                // When Progress value changed.
                @Override
                public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                    et_interval.setText(String.valueOf(sb_interval.getProgress()));
                }

                // Notification that the user has started a touch gesture.
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                // Notification that the user has finished a touch gesture
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            et_interval.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus){
                        sb_interval.setProgress(Integer.valueOf(et_interval.getText().toString()));
                    }
                }
            });
            b_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et_distance.getText().toString().matches("") || et_seconds.getText().toString().matches("")||et_interval.getText().toString().matches("") || s_patient.getSelectedItem().toString().matches("Choose a patient") || s_operator.getSelectedItem().toString().matches("Choose an operator"))
                    {
                        Toast.makeText(ExercisesSettingsActivity.this, "You must complete each fields !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(ExercisesSettingsActivity.this, DynamicExerciceActivity.class);
                        intent.putExtra("Distance", et_distance.getText().toString());
                        intent.putExtra("Interval", et_interval.getText().toString());
                        intent.putExtra("Time", et_seconds.getText().toString());
                        intent.putExtra("Patient", s_patient.getSelectedItem().toString());
                        intent.putExtra("Operator", s_operator.getSelectedItem().toString());
                        startActivity(intent);
                    }
                }
            });

            DisplayActorsOnSpinners(lstPatient);
            DisplayActorsOnSpinners(lstOperator);
        }

    }



    // logique
    /*
    passage d'un parametre a l'instanciation de la classe pour afficher ou non les parametres relatifs au test dynamiques

    alimentation des spinners avec les données relatives

    SI DYNAMIQUE
    mise en place de la synchro entre les choix d'interval



    BOUTON START : jsp

    BOUTON RECOVER : grisé si pas de CSV trouvé  à l'emplacement
    sinon : lire les méta données du dernier CSV et les écrire dans leurs emplacements
     */
}