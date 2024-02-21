package com.example.ex_motricite;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExercisesFragment} factory method to
 * create an instance of this fragment.
 */
public class ExercisesFragment extends Fragment {
    LinearLayout layoutDynamic;

    LinearLayout layoutPatient;
    LinearLayout layoutOperator;
    OperatorDAO operatorDAO;
    PatientDAO patientDAO;


    /**
     * The spinner for patients.
     */
    Spinner spPatientName;
    Spinner spOperatorName;
    /**
     * The list of patients.
     */
    private ArrayList<Patient> lstPatient;
    /**
     * The list of operators.
     */
    private ArrayList<Operator> lstOperator;
    private EditText etInterval;
    /**
     * The SeekBar for the interval.
     */
    private SeekBar sbInterval;
    /**
     * The list of patients.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutDynamic = getActivity().findViewById(R.id.ll_dynamic);
        EditText etSeconds;
        EditText etDistance;
        Button bStart;



        layoutPatient = getActivity().findViewById(R.id.ll_patient_static);
        layoutOperator = getActivity().findViewById(R.id.ll_operator_static);
        patientDAO = new PatientDAO(getActivity());
        operatorDAO = new OperatorDAO(getActivity());

        try {
            lstOperator = operatorDAO.getOperators();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error while getting operators", Toast.LENGTH_SHORT).show();
        }

        try {
            lstPatient = patientDAO.getPatients();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error while getting patients", Toast.LENGTH_SHORT).show();
        }

        layoutDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                ExercisesSettingsDynamicFragment fragment = new ExercisesSettingsDynamicFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });



        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        etDistance = getActivity().findViewById(R.id.et_distance_static);
        etSeconds = getActivity().findViewById(R.id.et_duration_static);
        bStart = getActivity().findViewById(R.id.b_start_exercise_static);
        spOperatorName = getActivity().findViewById(R.id.sp_operator_name_static);
        spPatientName = getActivity().findViewById(R.id.sp_patient_name_static);


        layoutOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                Intent intent = new Intent(getActivity(), ListActorActivity.class);
                intent.putExtra("actorType", "Operator");
                intent.putExtra("exerciseType", "Static");
                startActivity(intent);

            }
        });
        layoutPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListActorActivity.class);
                intent.putExtra("actorType", "Patient");
                intent.putExtra("exerciseType", "Static");
                startActivity(intent);

            }
        });

        bStart.setOnClickListener(v -> {
            if (etDistance.getText().toString().matches("") || etSeconds.getText().toString().matches("")|| spPatientName.getSelectedItem().toString().matches("Choose a patient") || spOperatorName.getSelectedItem().toString().matches("Choose an operator"))
            {
                Toast.makeText(getActivity(), "You must complete each fields !", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(getActivity(), StaticExerciseActivity.class);
                intent.putExtra("Distance", etDistance.getText().toString());
                intent.putExtra("Time", etSeconds.getText().toString());
                intent.putExtra("Patient", spPatientName.getSelectedItem().toString());
                intent.putExtra("Operator", spOperatorName.getSelectedItem().toString());
                startActivity(intent);

            }
        });
        if (lstOperator.isEmpty() || lstPatient.isEmpty()) {
            Toast.makeText(getActivity(), "No actors found", Toast.LENGTH_SHORT).show();
        }
        else{
            displayActorsOnSpinners(lstOperator);
            displayActorsOnSpinners(lstPatient);
        }
    }
    public void displayActorsOnSpinners(List<? extends Actor> actors) {
        ArrayAdapter<String> dataSpinner = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        if (actors.get(0) instanceof Operator) {
            dataSpinner.add("Choose an operator");
        } else {
            dataSpinner.add("Choose a patient");
        }
        for (int i = 0; i < actors.size(); i++) {
            dataSpinner.add(actors.get(i).getName());
        }
        if (actors.get(0) instanceof Operator) {
            spOperatorName.setAdapter(dataSpinner);
        } else {
            spPatientName.setAdapter(dataSpinner);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercices, container, false);
    }
}