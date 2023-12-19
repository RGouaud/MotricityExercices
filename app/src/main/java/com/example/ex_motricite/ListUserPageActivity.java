package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;


public class ListUserPageActivity extends AppCompatActivity {

    private Button buttonAddPatient;
    private ToggleButton toggleButtonPatient;
    private ToggleButton toggleButtonOperator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_page);

        buttonAddPatient = findViewById(R.id.b_Add);
        toggleButtonPatient = findViewById(R.id.b_ToggleOperator);
        toggleButtonOperator = findViewById(R.id.b_TogglePatient);

        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListUserPageActivity.this, UserPageActivity.class);
                startActivity(intent);
            }
        });

        toggleButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the patient toggle button is clicked
                if (toggleButtonPatient.isChecked()) {
                    // If patient toggle button is checked, uncheck the operator toggle button
                    toggleButtonOperator.setChecked(false);
                } else {
                    // If patient toggle button is unchecked
                    if (!toggleButtonOperator.isChecked()) {
                        // If operator toggle button is also unchecked, check the patient toggle button
                        toggleButtonPatient.setChecked(true);
                    } else {
                        // If operator toggle button is checked, keep both buttons checked
                        toggleButtonOperator.setChecked(true);
                    }
                }
            }
        });

        toggleButtonOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the operator toggle button is clicked
                if (toggleButtonOperator.isChecked()) {
                    // If operator toggle button is checked, uncheck the patient toggle button
                    toggleButtonPatient.setChecked(false);
                } else {
                    // If operator toggle button is unchecked
                    if (!toggleButtonPatient.isChecked()) {
                        // If patient toggle button is also unchecked, check the operator toggle button
                        toggleButtonOperator.setChecked(true);
                    } else {
                        // If patient toggle button is checked, keep both buttons checked
                        toggleButtonPatient.setChecked(true);
                    }
                }
            }
        });
    }
}