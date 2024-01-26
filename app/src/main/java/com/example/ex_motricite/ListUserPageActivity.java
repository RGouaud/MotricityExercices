package com.example.ex_motricite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ListUserPageActivity extends AppCompatActivity {

    private LinearLayout svList;
    private Button buttonAdd;
    private ToggleButton toggleButtonPatient;
    private ToggleButton toggleButtonOperator;
    private ArrayList<Patient> patients;
    private ArrayList<Operator> operators;

    private static final String USER_TYPE_OPERATOR = "operator";
    private static final String USER_TYPE_PATIENT = "patient";
    private static final String WHITE_COLOR_HEX = "#FFFFFF";


    private void createActorLayout(Actor actor) {
        // Create a new LinearLayout for each actor to display
        LinearLayout aLayout = new LinearLayout(this);

        // Setup the layout
        aLayout.setOrientation(LinearLayout.HORIZONTAL);
        aLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (actor instanceof Operator) {
            aLayout.setBackgroundResource(R.drawable.rounded_layout);
            aLayout.setPadding(20, 50, 0, 50);

            Operator operator = (Operator) actor;
            aLayout.setOnClickListener(v -> {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                intent.putExtra(ConstIntent.USER_TYPE, ConstIntent.USER_TYPE_OPERATOR);
                intent.putExtra(ConstIntent.CRUD_TYPE, ConstIntent.CRUD_TYPE_READ);
                intent.putExtra(ConstIntent.USER_ID, String.valueOf(operator.getId()));
                startActivity(intent);
            });
        }

        // Create TextView for name and firstname
        TextView name = new TextView(this);
        TextView firstName = new TextView(this);

        // Setup TextViews
        name.setText(getString(R.string.name_format, actor.getName()));
        name.setTextColor(Color.parseColor(WHITE_COLOR_HEX));

        firstName.setText(getString(R.string.first_name_format, actor.getFirstName()));
        firstName.setTextColor(Color.parseColor(WHITE_COLOR_HEX));

        name.setLayoutParams(new LinearLayout.LayoutParams(
                0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f)); // weight

        firstName.setLayoutParams(new LinearLayout.LayoutParams(
                0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f)); // weight

        // Create and configure ImageButtons
        ImageButton buttonModify = new ImageButton(this);
        buttonModify.setImageResource(android.R.drawable.ic_menu_set_as);
        buttonModify.setBackgroundColor(Color.parseColor("#00000000"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
        buttonModify.setLayoutParams(params);

        long actorId = actor.getId();
        buttonModify.setOnClickListener(v -> {
            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
            intent.putExtra(ConstIntent.USER_TYPE, getActorTypeString(actor));
            intent.putExtra(ConstIntent.CRUD_TYPE, ConstIntent.CRUD_TYPE_UPDATE);
            intent.putExtra(ConstIntent.USER_ID, String.valueOf(actorId));
            startActivity(intent);
        });

        // Setup spaces between layouts
        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                16));

        // Add TextView to LinearLayout
        aLayout.addView(name);
        aLayout.addView(firstName);
        aLayout.addView(buttonModify);

        //Creation of birthdate for patient
        if (actor instanceof Patient) {
            LinearLayout parentLayout = new LinearLayout(this);
            parentLayout.setOrientation(LinearLayout.VERTICAL);
            parentLayout.setBackgroundResource(R.drawable.rounded_layout);
            parentLayout.setPadding(20, 50, 0, 50);
            parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView birthDate = new TextView(this);
            Patient patient = (Patient) actor;
            birthDate.setText(getString(R.string.birthdate_format, patient.getBirthDate()));
            birthDate.setTextColor(Color.parseColor(WHITE_COLOR_HEX));
            birthDate.setPadding(0, 20, 0, 0);

            parentLayout.addView(aLayout);
            parentLayout.addView(birthDate);

            parentLayout.setOnClickListener(v -> {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                intent.putExtra(ConstIntent.USER_TYPE, ConstIntent.USER_TYPE_PATIENT);
                intent.putExtra(ConstIntent.CRUD_TYPE, ConstIntent.CRUD_TYPE_READ);
                intent.putExtra(ConstIntent.USER_ID, String.valueOf(patient.getId())); // TODO : verifier que c'est bon
                startActivity(intent);
            });

            svList.addView(parentLayout);
        }

        // Add LinearLayout to sv_list
        if (actor instanceof Operator) {
            svList.addView(aLayout);
        }
        svList.addView(space);
    }

    private String getActorTypeString(Actor actor) {
        return actor.getClass().equals(Patient.class) ? ConstIntent.USER_TYPE_PATIENT : ConstIntent.USER_TYPE_OPERATOR;
    }

    private void displayActors(ArrayList<? extends Actor> actors) {
        svList.removeAllViews();
        if (actors != null && !actors.isEmpty()) {
            for (Actor actor : actors) {
                createActorLayout(actor);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_page);

        initializeViews();
        OperatorDAO operatorDAO = new OperatorDAO(this);
        PatientDAO patientDAO = new PatientDAO(this);
        setOrientationLock();

        try {
            operators = operatorDAO.getOperators();
        } catch (Exception e) {
            handleDataRetrievalError("Error retrieving operators", e);
        }


        try {
            patients = patientDAO.getPatients();
        } catch (Exception e) {
            handleDataRetrievalError("Error retrieving patients", e);
        }


        setButtonClickListeners();
        displayActors(patients);

    }

    private void initializeViews() {
        svList = findViewById(R.id.sv_list);
        buttonAdd = findViewById(R.id.b_Add);
        toggleButtonPatient = findViewById(R.id.b_TogglePatient);
        toggleButtonOperator = findViewById(R.id.b_ToggleOperator);

    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void setOrientationLock() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void handleDataRetrievalError(String errorMessage, Exception e) {
        e.printStackTrace();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setButtonClickListeners() {
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
            String userType = toggleButtonPatient.isChecked() ? ConstIntent.USER_TYPE_PATIENT : ConstIntent.USER_TYPE_OPERATOR;
            intent.putExtra(ConstIntent.USER_TYPE, userType);
            intent.putExtra(ConstIntent.CRUD_TYPE, ConstIntent.CRUD_TYPE_CREATE);
            intent.putExtra(ConstIntent.USER_ID, "");
            startActivity(intent);
        });

        toggleButtonPatient.setOnClickListener(v -> handleToggleButtonClick(toggleButtonPatient, toggleButtonOperator, R.string.add_a_patient, patients));
        toggleButtonOperator.setOnClickListener(v -> handleToggleButtonClick(toggleButtonOperator, toggleButtonPatient, R.string.add_a_operator, operators));
    }

    private void handleToggleButtonClick(ToggleButton clickedButton, ToggleButton otherButton, int buttonTextResource, ArrayList<? extends Actor> actors) {
        if (clickedButton.isChecked()) {
            otherButton.setChecked(false);
            buttonAdd.setText(buttonTextResource);
            svList.removeAllViews();
            displayActors(actors);
            buttonAdd.setOnClickListener(v1 -> {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                String userType = clickedButton == toggleButtonPatient ? ConstIntent.USER_TYPE_PATIENT : ConstIntent.USER_TYPE_OPERATOR;
                intent.putExtra(ConstIntent.USER_TYPE, userType);
                intent.putExtra(ConstIntent.CRUD_TYPE, ConstIntent.CRUD_TYPE_CREATE);
                startActivity(intent);
            });
        } else {
            if (!otherButton.isChecked()) {
                clickedButton.setChecked(true);
                buttonAdd.setText(buttonTextResource);
            } else {
                otherButton.setChecked(true);
            }
        }
    }

}