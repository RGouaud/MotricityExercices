package com.example.ex_motricite;

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

/**
 * The {@code ListUserPageActivity} class represents an Android activity for listing and managing users.
 * It allows users to create, read, update, and delete patients and operators.
 *
 * <p>
 * This activity displays a list of patients and operators stored in the database, providing options
 * for creating, reading, updating, and deleting users. It supports both patient and operator CRUD operations.
 * </p>
 *
 * <p>
 * Additionally, it supports both single-click and long-click interactions for user navigation and
 * multiple user selection, respectively.
 * </p>
 *
 * <p>
 * The class utilizes DAOs (Data Access Objects) for retrieving user data from a database and dynamically
 * updates the layout to reflect the selected users and their visual states.
 * </p>
 *
 * <p>
 *     Author: EduardoXav, Ferreira
 *     Version: 1.0
 *     </p>
 */
public class ListUserPageActivity extends AppCompatActivity {

    private LinearLayout svList;
    private Button buttonAdd;
    private ToggleButton toggleButtonPatient;
    private ToggleButton toggleButtonOperator;
    private ArrayList<Patient> patients;
    private ArrayList<Operator> operators;

    private static final String USER_TYPE_OPERATOR = "operator";
    private static final String USER_TYPE_PATIENT = "patient";
    private static final String USER_ID_EXTRA = "UserId";
    private static final String WHITE_COLOR_HEX = "#FFFFFF";

    /**
     * Create a layout for the given actor and add it to the list.
     *
     * @param actor The user (operator or patient) to be displayed.
     */
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
                intent.putExtra("User", USER_TYPE_OPERATOR);
                intent.putExtra("Crud", "read");
                intent.putExtra(USER_ID_EXTRA, String.valueOf(operator.getId()));
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
        ImageButton boutonModify = new ImageButton(this);
        boutonModify.setImageResource(android.R.drawable.ic_menu_set_as);
        boutonModify.setBackgroundColor(Color.parseColor("#00000000"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
        boutonModify.setLayoutParams(params);

        long actorId = actor.getId();
        boutonModify.setOnClickListener(v -> {
            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
            intent.putExtra("User", getActorTypeString(actor));
            intent.putExtra("Crud", "update");
            intent.putExtra(USER_ID_EXTRA, String.valueOf(actorId));
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
        aLayout.addView(boutonModify);

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
                intent.putExtra("User", USER_TYPE_PATIENT);
                intent.putExtra("Crud", "read");
                intent.putExtra(USER_ID_EXTRA, String.valueOf(patient.getId()));
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

    /**
     * Get the string representation of the user type (patient or operator).
     *
     * @param actor The user for which to determine the type.
     * @return The string representation of the user type.
     */
    private String getActorTypeString(Actor actor) {
        return actor.getClass().equals(Patient.class) ? USER_TYPE_PATIENT : USER_TYPE_OPERATOR;
    }

    /**
     * Display a list of actors (users) in the layout.
     *
     * @param actors The list of actors to display.
     */
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
            handleDataRetrievalError("Erreur lors de la récupération des opérateurs", e);
        }


        try {
            patients = patientDAO.getPatients();
        } catch (Exception e) {
            handleDataRetrievalError("Erreur lors de la récupération des patients", e);
        }


        setButtonClickListeners();
        displayActors(patients);

    }

    /**
     * Initialize the views and components.
     */
    private void initializeViews() {
        svList = findViewById(R.id.sv_list);
        buttonAdd = findViewById(R.id.b_Add);
        toggleButtonPatient = findViewById(R.id.b_TogglePatient);
        toggleButtonOperator = findViewById(R.id.b_ToggleOperator);

    }

    /**
     * Set the screen orientation to portrait mode.
     */
    private void setOrientationLock() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Handle an error that occurred while retrieving data from the database.
     *
     * @param errorMessage The error message to display.
     * @param e            The exception that occurred.
     */
    private void handleDataRetrievalError(String errorMessage, Exception e) {
        e.printStackTrace();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Set the click listeners for the buttons.
     */
    private void setButtonClickListeners() {
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
            String userType = toggleButtonPatient.isChecked() ? USER_TYPE_PATIENT : USER_TYPE_OPERATOR;
            intent.putExtra("User", userType);
            intent.putExtra("Crud", "create");
            intent.putExtra(USER_ID_EXTRA, "");
            startActivity(intent);
        });

        toggleButtonPatient.setOnClickListener(v -> handleToggleButtonClick(toggleButtonPatient, toggleButtonOperator, R.string.add_a_patient, patients));
        toggleButtonOperator.setOnClickListener(v -> handleToggleButtonClick(toggleButtonOperator, toggleButtonPatient, R.string.add_a_operator, operators));
    }

    /**
     * Handle the click on a toggle button.
     *
     * @param clickedButton     The button that was clicked.
     * @param otherButton       The other button.
     * @param buttonTextResource The text resource to display on the button.
     * @param actors            The list of actors to display.
     */
    private void handleToggleButtonClick(ToggleButton clickedButton, ToggleButton otherButton, int buttonTextResource, ArrayList<? extends Actor> actors) {
        if (clickedButton.isChecked()) {
            otherButton.setChecked(false);
            buttonAdd.setText(buttonTextResource);
            svList.removeAllViews();
            displayActors(actors);
            buttonAdd.setOnClickListener(v1 -> {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                String userType = clickedButton == toggleButtonPatient ? USER_TYPE_PATIENT : USER_TYPE_OPERATOR;
                intent.putExtra("User", userType);
                intent.putExtra("Crud", "create");
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