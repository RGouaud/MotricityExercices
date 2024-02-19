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
 * Author: EduardoXav, Ferreira
 * Version: 1.0
 * </p>
 */
public class ListUserPageActivity extends AppCompatActivity {

    /**
     * The LinearLayout for the list of users.
     */
    private LinearLayout svList;
    /**
     * The Button for adding a user.
     */
    private Button bAdd;
    /**
     * The ToggleButton for selecting patients.
     */
    private ToggleButton tbPatient;
    /**
     * The ToggleButton for selecting operators.
     */
    private ToggleButton tbOperator;
    /**
     * The list of patients.
     */
    private ArrayList<Patient> patients;
    /**
     * The list of operators.
     */
    private ArrayList<Operator> operators;

    /**
     * The string representation of the operator user type.
     */
    private static final String USER_TYPE_OPERATOR = "operator";
    /**
     * The string representation of the patient user type.
     */
    private static final String USER_TYPE_PATIENT = "patient";
    /**
     * The string representation of the user ID extra.
     */
    private static final String USER_ID_EXTRA = "UserId";
    /**
     * The string representation of the white color hex.
     */
    private static final String WHITE_COLOR_HEX = "#FFFFFF";

    /**
     * Create a layout for the given actor and add it to the list.
     *
     * @param actor The user (operator or patient) to be displayed.
     */
    private void createActorLayout(Actor actor) {
        // Create a new LinearLayout for each actor to display
        LinearLayout llLayout = new LinearLayout(this);

        // Setup the layout
        llLayout.setOrientation(LinearLayout.HORIZONTAL);
        llLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (actor instanceof Operator) {
            llLayout.setBackgroundResource(R.drawable.rounded_layout);
            llLayout.setPadding(20, 50, 0, 50);

            Operator operator = (Operator) actor;
            llLayout.setOnClickListener(v -> {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                intent.putExtra("User", USER_TYPE_OPERATOR);
                intent.putExtra("Crud", "read");
                intent.putExtra(USER_ID_EXTRA, String.valueOf(operator.getId()));
                startActivity(intent);
            });
        }

        // Create TextView for name and firstname
        TextView tvName = new TextView(this);
        TextView tvFirstName = new TextView(this);

        // Setup TextViews
        tvName.setText(getString(R.string.name_format, actor.getName()));
        tvName.setTextColor(Color.parseColor(WHITE_COLOR_HEX));

        tvFirstName.setText(getString(R.string.first_name_format, actor.getFirstName()));
        tvFirstName.setTextColor(Color.parseColor(WHITE_COLOR_HEX));

        tvName.setLayoutParams(new LinearLayout.LayoutParams(
                0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f)); // weight

        tvFirstName.setLayoutParams(new LinearLayout.LayoutParams(
                0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f)); // weight

        // Create and configure ImageButtons
        ImageButton ibEdit = new ImageButton(this);
        ibEdit.setImageResource(android.R.drawable.ic_menu_set_as);
        ibEdit.setBackgroundColor(Color.parseColor("#00000000"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
        ibEdit.setLayoutParams(params);

        long actorId = actor.getId();
        ibEdit.setOnClickListener(v -> {
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
        llLayout.addView(tvName);
        llLayout.addView(tvFirstName);
        llLayout.addView(ibEdit);

        //Creation of birthdate for patient
        if (actor instanceof Patient) {
            LinearLayout llParentLayout = new LinearLayout(this);
            llParentLayout.setOrientation(LinearLayout.VERTICAL);
            llParentLayout.setBackgroundResource(R.drawable.rounded_layout);
            llParentLayout.setPadding(20, 50, 0, 50);
            llParentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView birthDate = new TextView(this);
            Patient patient = (Patient) actor;
            birthDate.setText(getString(R.string.birthdate_format, patient.getBirthDate()));
            birthDate.setTextColor(Color.parseColor(WHITE_COLOR_HEX));
            birthDate.setPadding(0, 20, 0, 0);

            llParentLayout.addView(llLayout);
            llParentLayout.addView(birthDate);

            llParentLayout.setOnClickListener(v -> {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                intent.putExtra("User", USER_TYPE_PATIENT);
                intent.putExtra("Crud", "read");
                intent.putExtra(USER_ID_EXTRA, String.valueOf(patient.getId()));
                startActivity(intent);
            });

            svList.addView(llParentLayout);
        }

        // Add LinearLayout to sv_list
        if (actor instanceof Operator) {
            svList.addView(llLayout);
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
        bAdd = findViewById(R.id.b_Add);
        tbPatient = findViewById(R.id.b_TogglePatient);
        tbOperator = findViewById(R.id.b_ToggleOperator);

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
        bAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
            String userType = tbPatient.isChecked() ? USER_TYPE_PATIENT : USER_TYPE_OPERATOR;
            intent.putExtra("User", userType);
            intent.putExtra("Crud", "create");
            intent.putExtra(USER_ID_EXTRA, "");
            startActivity(intent);
        });

        tbPatient.setOnClickListener(v -> handleToggleButtonClick(tbPatient, tbOperator, R.string.add_a_patient, patients));
        tbOperator.setOnClickListener(v -> handleToggleButtonClick(tbOperator, tbPatient, R.string.add_a_operator, operators));
    }

    /**
     * Handle the click on a toggle button.
     *
     * @param clickedButton     The button that was clicked.
     * @param tbOtherButton       The other button.
     * @param buttonTextResource The text resource to display on the button.
     * @param actors            The list of actors to display.
     */
    private void handleToggleButtonClick(ToggleButton clickedButton, ToggleButton tbOtherButton, int buttonTextResource, ArrayList<? extends Actor> actors) {
        if (clickedButton.isChecked()) {
            tbOtherButton.setChecked(false);
            bAdd.setText(buttonTextResource);
            svList.removeAllViews();
            displayActors(actors);
            bAdd.setOnClickListener(v1 -> {
                Intent intent = new Intent(ListUserPageActivity.this, CrudUserActivity.class);
                String userType = clickedButton == tbPatient ? USER_TYPE_PATIENT : USER_TYPE_OPERATOR;
                intent.putExtra("User", userType);
                intent.putExtra("Crud", "create");
                startActivity(intent);
            });
        } else {
            if (!tbOtherButton.isChecked()) {
                clickedButton.setChecked(true);
                bAdd.setText(buttonTextResource);
            } else {
                tbOtherButton.setChecked(true);
            }
        }
    }

}