package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

    public void displayOperators() {

        if (operators != null && operators.size() > 0) {
            sv_list.removeAllViews();
            for (int i = 0; i < operators.size(); i++) {
                // Créez un nouveau LinearLayout pour chaque opérateur
                LinearLayout aLayout = new LinearLayout(this);

                // Configurer le LinearLayout
                aLayout.setOrientation(LinearLayout.HORIZONTAL);
                aLayout.setBackgroundResource(R.drawable.rounded_layout);
                aLayout.setPadding(20, 50, 0, 50);
                aLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Créer des TextViews pour le nom et le prénom
                TextView name = new TextView(this);
                TextView firstName = new TextView(this);

                // Configurer les TextViews
                name.setText("Name : " + operators.get(i).getName());
                name.setTextColor(Color.parseColor("#FFFFFF"));

                firstName.setText("First name : " + operators.get(i).getFirstName());
                firstName.setTextColor(Color.parseColor("#FFFFFF"));

                name.setLayoutParams(new LinearLayout.LayoutParams(
                        0, // width
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f)); // weight

                firstName.setLayoutParams(new LinearLayout.LayoutParams(
                        0, // width
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        4f)); // weight

                //Créer et configurer les ImageButton
                ImageButton modify = new ImageButton(this);
                modify.setImageResource(android.R.drawable.ic_menu_set_as);
                modify.setBackgroundColor(Color.parseColor("#00000000"));
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
                modify.setLayoutParams(params);


                //Configurer les espaces entre les layout
                Space space = new Space(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        16));


                // Ajouter les TextViews au LinearLayout
                aLayout.addView(name);
                aLayout.addView(firstName);
                aLayout.addView(modify);

                // Ajouter le LinearLayout à sv_list
                sv_list.addView(aLayout);
                sv_list.addView(space);
            }
        }
    }

    public void displayPatients() {

        if (patients != null && patients.size() > 0) {
            sv_list.removeAllViews();
            for (int i = 0; i < patients.size(); i++) {
                // Créez un nouveau LinearLayout pour chaque opérateur
                LinearLayout parentLayout = new LinearLayout(this);
                LinearLayout childrenLayout = new LinearLayout(this);

                //Configurer le linearLayout parent
                parentLayout.setOrientation(LinearLayout.VERTICAL);
                parentLayout.setBackgroundResource(R.drawable.rounded_layout);
                parentLayout.setPadding(20, 50, 0, 50);
                parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Configurer le LinearLayout enfant
                childrenLayout.setOrientation(LinearLayout.HORIZONTAL);
                childrenLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                // Créer des TextViews pour le nom et le prénom
                TextView name = new TextView(this);
                TextView firstName = new TextView(this);
                TextView birthDate = new TextView(this);


                // Configurer les TextViews
                name.setText("Name : " + patients.get(i).getName());
                name.setTextColor(Color.parseColor("#FFFFFF"));

                firstName.setText("First name : " + patients.get(i).getFirstName());
                firstName.setTextColor(Color.parseColor("#FFFFFF"));

                birthDate.setText("Birth date : " + patients.get(i).getBirthDate());
                birthDate.setTextColor(Color.parseColor("#FFFFFF"));

                name.setLayoutParams(new LinearLayout.LayoutParams(
                        0, // width
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f)); // weight

                firstName.setLayoutParams(new LinearLayout.LayoutParams(
                        0, // width
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        2f)); // weight


                //Créer et configurer les ImageButton
                ImageButton modify = new ImageButton(this);
                modify.setImageResource(android.R.drawable.ic_menu_set_as);
                modify.setBackgroundColor(Color.parseColor("#00000000"));
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(75, 75);
                modify.setLayoutParams(params);


                //Configurer les espaces entre les layout
                Space space = new Space(this);
                space.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        16));


                // Ajouter les TextViews au LinearLayout
                childrenLayout.addView(name);
                childrenLayout.addView(firstName);
                childrenLayout.addView(modify);

                parentLayout.addView(childrenLayout);
                parentLayout.addView(birthDate);

                // Ajouter le LinearLayout à sv_list
                sv_list.addView(parentLayout);
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
        displayPatients();

        toggleButtonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the patient toggle button is clicked
                if (toggleButtonPatient.isChecked()) {
                    // If patient toggle button is checked, uncheck the operator toggle button
                    toggleButtonOperator.setChecked(false);
                    buttonAdd.setText("Add a patient");
                    displayPatients();
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
                        displayPatients();

                    } else {
                        // If operator toggle button is checked, keep both buttons checked
                        toggleButtonOperator.setChecked(true);
                        displayOperators();
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
                    displayOperators();

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
                        displayOperators();
                    } else {
                        // If patient toggle button is checked, keep both buttons checked
                        toggleButtonPatient.setChecked(true);
                        displayPatients();
                    }
                }
            }
        });

    }

}