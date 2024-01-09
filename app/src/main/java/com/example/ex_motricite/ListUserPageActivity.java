package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class ListUserPageActivity extends AppCompatActivity {

   private ScrollView sv_list;
    private Button buttonAdd;
    private ToggleButton toggleButtonPatient;
    private ToggleButton toggleButtonOperator;
    private PatientDAO patientDAO;
    private Patient patient;
    private ArrayList<Patient> patients;

    private OperatorDAO operatorDAO;
    private Operator operator;
    private ArrayList<Operator> operators;

    public void displayOperators(){
            sv_list.removeAllViews();
            for (int i = 0; i < operators.size(); i++) {
                LinearLayout unLayout = new LinearLayout(this);
                TextView name = new TextView(this);
                TextView firstName = new TextView(this);

                unLayout.setOrientation(LinearLayout.HORIZONTAL);
                unLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                name.setText(operators.get(i).getName());
                firstName.setText(operators.get(i).getFirstName());

                unLayout.addView(name);
                unLayout.addView(firstName);
                sv_list.addView(unLayout);
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_page);

        sv_list = findViewById(R.id.sv_list);
        buttonAdd = findViewById(R.id.b_Add);
        toggleButtonPatient = findViewById(R.id.b_TogglePatient);
        toggleButtonOperator = findViewById(R.id.b_ToggleOperator);
        operators = new ArrayList<Operator>();
        patients = new ArrayList<Patient>();
        //operators = operatorDAO.getOperators();
        //patients = patientDAO.getPatients();


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                intent.putExtra("User", "patient");
                startActivity(intent);
            }
        });
        displayOperators();

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
                            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
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
                            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
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