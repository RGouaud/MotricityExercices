package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

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
        sv_list.removeAllViews();
        if(actors != null && actors.size() > 0){
            // Set user value
            Class<?> actorType = actors.get(0).getClass();
            String actorTypeString;
            if(actorType.equals(Patient.class)){
                actorTypeString = "patient";
            }
            else{
                actorTypeString = "operator";
            }


            for(int i = 0; i < actors.size(); i++) {
                // Create a new LinearLayout for each actor to display
                LinearLayout aLayout = new LinearLayout(this);

                // Setup the layout
                aLayout.setOrientation(LinearLayout.HORIZONTAL);
                aLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                if (actors.get(i) instanceof  Operator) {
                    aLayout.setBackgroundResource(R.drawable.rounded_layout);
                    aLayout.setPadding(20, 50, 0, 50);

                    Operator operator = (Operator) actors.get(i);
                    aLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                            intent.putExtra("User", "operator");
                            intent.putExtra("Crud", "read");
                            intent.putExtra("idOperator", operator.getId());
                            startActivity(intent);
                        }
                    });
                }

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
                        2f)); // weight

                // Create and configure ImageButtons
                ImageButton b_modify = new ImageButton(this);
                b_modify.setImageResource(android.R.drawable.ic_menu_set_as);
                b_modify.setBackgroundColor(Color.parseColor("#00000000"));
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
                b_modify.setLayoutParams(params);

                String.valueOf(actors.get(i).getId());
                int finalI = i;
                b_modify.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                        intent.putExtra("User", actorTypeString);
                        intent.putExtra("Crud", "update");
                        intent.putExtra("UserId", String.valueOf(actors.get(finalI).getId()));
                        startActivity(intent);
                    }
                });



                // Setup spaces between layouts
                Space space = new Space(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        16));

                // Add TextView to LinearLayout
                aLayout.addView(name);
                aLayout.addView(firstName);
                aLayout.addView(b_modify);

                //Creation of birthdate for patient
                if (actors.get(i) instanceof  Patient) {
                    LinearLayout parentLayout = new LinearLayout(this);
                    parentLayout.setOrientation(LinearLayout.VERTICAL);
                    parentLayout.setBackgroundResource(R.drawable.rounded_layout);
                    parentLayout.setPadding(20, 50, 0, 50);
                    parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView birthDate = new TextView(this);
                    Patient patient = (Patient) actors.get(i);
                    birthDate.setText("Birthdate : " + patient.getBirthDate());
                    birthDate.setTextColor(Color.parseColor("#FFFFFF"));
                    birthDate.setPadding(0, 20, 0, 0);

                    parentLayout.addView(aLayout);
                    parentLayout.addView(birthDate);

                    parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                            intent.putExtra("User", "patient");
                            intent.putExtra("Crud", "read");
                            intent.putExtra("idPatient", patient.getId());
                            startActivity(intent);
                        }
                    });

                    sv_list.addView(parentLayout);
                }

                // Add LinearLayout to sv_list
                if (actors.get(i) instanceof  Operator) {
                    sv_list.addView(aLayout);
                }
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
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            operators = operatorDAO.getOperators();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la récupération des opérateurs", Toast.LENGTH_SHORT).show();
        }

        try {
            patients = patientDAO.getPatients();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de la récupération des opérateurs", Toast.LENGTH_SHORT).show();
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                intent.putExtra("User", "patient");
                intent.putExtra("Crud","create");
                intent.putExtra("UserId","");
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
                    sv_list.removeAllViews();
                    displayActors(patients);
                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                            intent.putExtra("User", "patient");
                            intent.putExtra("Crud", "create");
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
                    sv_list.removeAllViews();
                    displayActors(operators);

                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("debug","clic sur add");
                            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                            intent.putExtra("User", "operator");
                            intent.putExtra("Crud", "create");

                            intent.putExtra("UserId", "");
                            Log.d("debug", "passage des parametres");
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