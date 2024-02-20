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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExercisesFragment} factory method to
 * create an instance of this fragment.
 */
public class ExercisesFragment extends Fragment {
    LinearLayout layoutDynamic;

    TextView tvPatientName;
    TextView tvOperatorName;
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

        layoutDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                ExercisesSettingsDynamicFragment fragment = new ExercisesSettingsDynamicFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        etDistance = getActivity().findViewById(R.id.et_distance);
        etSeconds = getActivity().findViewById(R.id.et_duration);
        bStart = getActivity().findViewById(R.id.b_start_exercise);
        tvOperatorName = getActivity().findViewById(R.id.tv_operator_name);
        tvPatientName = getActivity().findViewById(R.id.tv_patient_name);


        bStart.setOnClickListener(v -> {
            if (etDistance.getText().toString().matches("") || etSeconds.getText().toString().matches("")|| tvPatientName.getText().toString().matches("Choose a patient") || tvOperatorName.getText().toString().matches("Choose an operator"))
            {
                Toast.makeText(getActivity(), "You must complete each fields !", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(getActivity(), DynamicExerciseActivity.class);
                intent.putExtra("Distance", etDistance.getText().toString());
                intent.putExtra("Time", etSeconds.getText().toString());
                intent.putExtra("Patient", tvPatientName.getText().toString());
                intent.putExtra("Operator", tvOperatorName.getText().toString());
                startActivity(intent);

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercices, container, false);
    }
}