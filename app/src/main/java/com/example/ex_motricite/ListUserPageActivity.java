package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class ListUserPageActivity extends AppCompatActivity {

   private LinearLayout sv_list;
    private Button buttonAdd;
    private ToggleButton toggleButtonPatient;
    private ToggleButton toggleButtonOperator;
    private PatientDAO patientDAO;
    private ArrayList<Patient> patients;

    private OperatorDAO operatorDAO;
    private ArrayList<Operator> operators;

    public void displayActors(ArrayList<? extends Actor> actors){
        if(actors != null && actors.size() > 0){
            sv_list.removeAllViews();
            for(int i = 0; i < actors.size(); i++) {
                // Create a new LinearLayout for each actor to display
                LinearLayout aLayout = new LinearLayout(this);

                // Setup the layout
                aLayout.setOrientation(LinearLayout.HORIZONTAL);
                aLayout.setBackgroundResource(R.drawable.rounded_layout);
                aLayout.setPadding(20, 50, 0, 50);
                aLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));


                // Create TextView for name and firstname
                TextView name = new TextView(this);
                TextView firstName = new TextView(this);

                // Setup TextViews
                name.setText("Name : " + actors.get(i).getName());
                name.setTextColor(Color.parseColor("#FFFFFF"));

                firstName.setText("First name : " + actors.get(i).getFirstName());
                firstName.setTextColor(Color.parseColor("#FFFFFF"));

                name.setLayoutParams(new LinearLayout.LayoutParams(
                        0, // width
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f)); // weight

                firstName.setLayoutParams(new LinearLayout.LayoutParams(
                        0, // width
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        4f)); // weight

                // Create and configure ImageButtons
                ImageButton modify = new ImageButton(this);
                modify.setImageResource(android.R.drawable.ic_menu_set_as);
                modify.setBackgroundColor(Color.parseColor("#00000000"));
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
                modify.setLayoutParams(params);

                // Setup spaces between layouts
                Space space = new Space(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        16));

                // Add TextView to LinearLayout
                aLayout.addView(name);
                aLayout.addView(firstName);
                aLayout.addView(modify);

                // Add LinearLayout to sv_list
                sv_list.addView(aLayout);
                sv_list.addView(space);

            }
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
        operatorDAO = new OperatorDAO(this);
        patientDAO = new PatientDAO(this);

        try {
            operators = operatorDAO.getOperators();
            // Le reste du code...
        } catch (Exception e) {
            e.printStackTrace();
            // Gérez l'exception de manière appropriée, par exemple, affichez un message d'erreur
            Toast.makeText(this, "Erreur lors de la récupération des opérateurs", Toast.LENGTH_SHORT).show();
        }

        try {
            patients = patientDAO.getPatients();
            // Le reste du code...
        } catch (Exception e) {
            e.printStackTrace();
            // Gérez l'exception de manière appropriée, par exemple, affichez un message d'erreur
            Toast.makeText(this, "Erreur lors de la récupération des opérateurs", Toast.LENGTH_SHORT).show();
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                intent.putExtra("User", "patient");
                startActivity(intent);
            }
        });
        displayActors(patients);

        toggleButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the patient toggle button is clicked
                if (toggleButtonPatient.isChecked()) {
                    // If patient toggle button is checked, uncheck the operator toggle button
                    toggleButtonOperator.setChecked(false);
                    buttonAdd.setText("Add a patient");
                    displayActors(patients);
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
                        displayActors(patients);

                    } else {
                        // If operator toggle button is checked, keep both buttons checked
                        toggleButtonOperator.setChecked(true);
                        displayActors(operators);
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
                    displayActors(operators);

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
                        displayActors(operators);
                    } else {
                        // If patient toggle button is checked, keep both buttons checked
                        toggleButtonPatient.setChecked(true);
                        displayActors(patients);
                    }
                }
            }
        });

    }

}