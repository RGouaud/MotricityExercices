package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;


public class listUserPageActivity extends AppCompatActivity {

    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_page);

        buttonAdd = findViewById(R.id.buttonAdd);
        ToggleButton toggleButtonPatient = findViewById(R.id.buttonPatient);
        ToggleButton toggleButtonOperator = findViewById(R.id.buttonOperator);


        toggleButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the patient toggle button is clicked
                if (toggleButtonPatient.isChecked()) {
                    // If patient toggle button is checked, uncheck the operator toggle button
                    toggleButtonOperator.setChecked(false);
                    buttonAdd.setText("Add a patient");
                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(listUserPageActivity.this, CrudUserActivity.class);
                            intent.putExtra("User", "patient");
                            startActivity(intent);
                        }
                    });
                } else {
                    // If patient toggle button is unchecked
                    if (!toggleButtonOperator.isChecked()) {
                        // If operator toggle button is also unchecked, check the patient toggle button
                        toggleButtonPatient.setChecked(true);
                        buttonAdd.setText("Add a patient");
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
                    buttonAdd.setText("Add an operator");

                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(listUserPageActivity.this, CrudUserActivity.class);
                            intent.putExtra("User", "operator");
                            startActivity(intent);
                        }
                    });
                } else {
                    // If operator toggle button is unchecked
                    if (!toggleButtonPatient.isChecked()) {
                        // If patient toggle button is also unchecked, check the operator toggle button
                        toggleButtonOperator.setChecked(true);
                        buttonAdd.setText("Add an operator");
                    } else {
                        // If patient toggle button is checked, keep both buttons checked
                        toggleButtonPatient.setChecked(true);
                    }
                }
            }
        });
    }
}