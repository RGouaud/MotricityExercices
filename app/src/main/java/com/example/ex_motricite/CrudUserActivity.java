package com.example.ex_motricite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code CrudUserActivity} class handles the Create, Read, Update, and Delete (CRUD)
 * operations for both patients and operators. It extends {@link AppCompatActivity}.
 *
 * <p>
 * This activity is responsible for displaying forms for creating, updating, and reading
 * information about patients and operators.
 * </p>
 *
 * <p>
 * It uses a {@link PatientDAO} and {@link OperatorDAO} for database operations.
 * </p>
 *
 * @author Rgouaud, Ferreira
 * @version 1.0
 */
public class CrudUserActivity extends AppCompatActivity {
    // Instance variables for patient and operator objects
    private Patient patient;
    private Operator operator;

    // Constants for intent extras and color codes
    private static final String USER_ID_FIELD = "UserId";
    private static final String WHITE = "#FFFFFF";
    private static final String COMPLETE_FIELDS_ERROR = "Complete all fields";
    private static final String UPDATE = "update";

    // DAO instances for database operations
    OperatorDAO operatorDAO;
    PatientDAO patientDAO;

    // UI elements
    EditText etRemarks;
    EditText etBirthdate;
    EditText etFirstName;
    EditText etName;
    TextView tvNewUser;
    /**
     * The {@code bDelete} button is used to delete a patient or operator.
     */
    Button bDelete;
    /**
     * The {@code bConfirm} button is used to confirm CRUD operations.
     */
    Button bConfirm;
    /**
     * The {@code llCrud} linear layout is used to display CRUD forms.
     */
    LinearLayout llCrud;

    /**
     * Regular expression pattern for validating date format "dd/MM/yyyy".
     */
    private static final String DATE_REGEX = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";

    /**
     * Validates if the provided date string matches the expected format.
     *
     * @param dateStr The date string to validate.
     * @return True if the date is in the correct format, false otherwise.
     */
    private static boolean isValidDate(String dateStr) {
        Pattern pattern = Pattern.compile(DATE_REGEX);
        Matcher matcher = pattern.matcher(dateStr);
        return matcher.matches();
    }

    /**
     * Displays a popup dialog with an error message.
     *
     * @param context The context in which the dialog should be displayed.
     * @param message The error message to be shown.
     */
    private static void showPopup(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss(); // Close popup
                });

        // Show popup
        builder.show();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_user);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Initialize UI elements
        llCrud = findViewById(R.id.ll_crud);
        bConfirm = findViewById(R.id.b_confirm);
        bDelete = findViewById(R.id.b_delete);
        tvNewUser = findViewById(R.id.tv_newuser);
        etName = findViewById(R.id.et_name);
        etFirstName = findViewById(R.id.et_firstname);
        etBirthdate = findViewById(R.id.et_birthdate);
        etRemarks = findViewById(R.id.et_remarks);

        // Get intent extras
        Intent myIntent = getIntent();
        String user = myIntent.getStringExtra("User");
        String crud = myIntent.getStringExtra("Crud");
        String userId = myIntent.getStringExtra(USER_ID_FIELD);

        // Check if intent extras are present
        if (crud == null || user == null) {
            return;
        }

        // Process the CRUD operations based on the user type (patient or operator)
        switch (user) {
            case "patient":
                patientDAO = new PatientDAO(this);
                patient(crud, userId, patientDAO);
                break;
            case "operator":
                operatorDAO = new OperatorDAO(this);
                operator(crud, userId, operatorDAO);
                break;
            default:
                break;
        }
    }

    /**
     * Handles CRUD operations for patients based on the specified operation.
     *
     * @param crud       The CRUD operation (create, update, or read).
     * @param userId     The ID of the user (patient).
     * @param patientDAO The PatientDAO instance for database operations.
     */
    private void patient(String crud, String userId, PatientDAO patientDAO){
        switch (crud){
            case "create":
                createPatient(patientDAO);
                break;
            case UPDATE:
                if(userId != null){
                    updatePatient(userId, patientDAO);
                }
                break;
            case "read":
                if(userId != null) {
                    readPatient(userId, patientDAO);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Handles the creation of a new patient.
     *
     * @param patientDAO The PatientDAO instance for database operations.
     */
    private void createPatient(PatientDAO patientDAO){
        tvNewUser.setText(R.string.create_patient);

        bConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String firstName = etFirstName.getText().toString();
            String birthDate = etBirthdate.getText().toString();
            String remarks = etRemarks.getText().toString();

            if(!(name.isEmpty()) && !(firstName.isEmpty()) && !(birthDate.isEmpty())){ // check if all obligatory fields are completed
                if(isValidDate(birthDate)){//check if birthdate seems to be on the waited format DD/MM/YYYY
                    patient = new Patient(name, firstName, birthDate, remarks);
                    patientDAO.addPatient(patient);
                    Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                    startActivity(intent);
                }
                else{ // error on date
                    showPopup(CrudUserActivity.this, "Type a valid date");
                }
            }
            else{ // not all obligatory fields are completed
                showPopup(CrudUserActivity.this, COMPLETE_FIELDS_ERROR);
            }
        });
    }

    /**
     * Handles the update of an existing patient.
     *
     * @param userId     The ID of the user (patient).
     * @param patientDAO The PatientDAO instance for database operations.
     */
    private void updatePatient(String userId, PatientDAO patientDAO){
        // setup display
        tvNewUser.setText(R.string.edit_patient);
        bDelete.setVisibility(View.VISIBLE);

        //get previous information
        patient = patientDAO.getPatient(Long.parseLong(userId));

        //put previous information in field
        etName.setText(patient.getName());
        etFirstName.setText(patient.getFirstName());
        etBirthdate.setText(patient.getBirthDate());
        etRemarks.setText(patient.getRemarks());

        // confirm update
        bConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String firstName = etFirstName.getText().toString();
            String birthDate = etBirthdate.getText().toString();
            String remarks = etRemarks.getText().toString();

            if (!(name.isEmpty()) && !(firstName.isEmpty()) && !(birthDate.isEmpty())) { // check if all obligatory fields are completed
                if (isValidDate(birthDate)) {//check if birthdate seems to be on the waited format DD/MM/YYYY

                    //update of information for current patient object instance
                    patient.setName(name);
                    patient.setFirstName(firstName);
                    patient.setBirthDate(birthDate);
                    patient.setRemarks(remarks);
                    patientDAO.updatePatient(patient);
                    Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                    startActivity(intent);
                } else { // error on date
                    showPopup(CrudUserActivity.this, "Type a valid date");
                }
            } else { // not all obligatory fields are completed
                showPopup(CrudUserActivity.this, COMPLETE_FIELDS_ERROR);
            }
        });

        // delete button
        bDelete.setOnClickListener(v -> {
            // Check if patient is in an existing test
            patientDAO.delPatient(patient);
            Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Handles the display of patient information for reading purposes.
     *
     * @param userId     The ID of the user (patient).
     * @param patientDAO The PatientDAO instance for database operations.
     */
    private void readPatient(String userId, PatientDAO patientDAO){
        patient = patientDAO.getPatient(Long.parseLong(userId));
        tvNewUser.setText(R.string.patient);

        etName.setText(patient.getName());
        etName.setTextColor(Color.parseColor(WHITE));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.BLACK);
        gradientDrawable.setStroke(2, Color.parseColor("#FFA500"));
        gradientDrawable.setCornerRadius(8);
        etName.setBackground(gradientDrawable);
        etName.setClickable(false);
        etName.setFocusable(false);
        TextView name = new TextView(this);
        name.setText(R.string.set_name);
        name.setTextColor(Color.parseColor(WHITE));
        llCrud.addView(name, 1);

        etFirstName.setText(patient.getFirstName());
        etFirstName.setTextColor(Color.parseColor(WHITE));
        etFirstName.setBackground(gradientDrawable);
        etFirstName.setClickable(false);
        etFirstName.setFocusable(false);
        TextView firstName = new TextView(this);
        firstName.setText(R.string.set_first_name);
        firstName.setTextColor(Color.parseColor(WHITE));
        llCrud.addView(firstName, 4);


        etBirthdate.setText(patient.getBirthDate());
        etBirthdate.setTextColor(Color.parseColor(WHITE));
        etBirthdate.setBackground(gradientDrawable);
        etBirthdate.setClickable(false);
        etBirthdate.setFocusable(false);
        TextView birthDate = new TextView(this);
        birthDate.setText(R.string.set_birth_date);
        birthDate.setTextColor(Color.parseColor(WHITE));
        llCrud.addView(birthDate, 7);

        etRemarks.setText(patient.getRemarks());
        etRemarks.setTextColor(Color.parseColor(WHITE));
        etRemarks.setBackground(gradientDrawable);
        etRemarks.setClickable(false);
        etRemarks.setFocusable(false);
        TextView remarks = new TextView(this);
        remarks.setText(R.string.set_remarks);
        remarks.setTextColor(Color.parseColor(WHITE));
        llCrud.addView(remarks, 10);

        bConfirm.setText(R.string.button_edit);
        bConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(CrudUserActivity.this, CrudUserActivity.class);
            intent.putExtra("User", "patient");
            intent.putExtra("Crud", UPDATE);
            intent.putExtra(USER_ID_FIELD, String.valueOf(patient.getId()));
            startActivity(intent);
        });
    }

    /**
     * Handles CRUD operations for operators based on the specified operation.
     *
     * @param crud         The CRUD operation (create, update, or read).
     * @param userId       The ID of the user (operator).
     * @param operatorDAO  The OperatorDAO instance for database operations.
     */
    private void operator(String crud, String userId, OperatorDAO operatorDAO){
        switch (crud){
            case "create":
                createOperator(operatorDAO);
                break;
            case UPDATE:
                updateOperator(userId, operatorDAO);
                break;
            case "read":
                readOperator(userId, operatorDAO);
                break;
            default:
                break;
        }
    }

    /**
     * Handles the creation of a new operator.
     *
     * @param operatorDAO The OperatorDAO instance for database operations.
     */
    private void createOperator(OperatorDAO operatorDAO){
        tvNewUser.setText(R.string.create_operator);
        bConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String firstName = etFirstName.getText().toString();

            if(!(name.isEmpty()) && !(firstName.isEmpty())){
                operator = new Operator(name, firstName);
                operatorDAO.addOperator(operator);
                Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                startActivity(intent);
            }
            else{
                showPopup(CrudUserActivity.this, COMPLETE_FIELDS_ERROR);
            }
        });
    }

    /**
     * Handles the update of an existing operator.
     *
     * @param userId       The ID of the user (operator).
     * @param operatorDAO  The OperatorDAO instance for database operations.
     */
    private void updateOperator(String userId, OperatorDAO operatorDAO){
        // setup display
        tvNewUser.setText(R.string.edit_operator);
        bDelete.setVisibility(View.VISIBLE);

        //get previous information
        operator = operatorDAO.getOperator(Long.parseLong(userId));

        //put previous information in field
        etName.setText(operator.getName());
        etFirstName.setText(operator.getFirstName());

        // confirm update
        bConfirm.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String firstName = etFirstName.getText().toString();

            if(!(name.isEmpty()) && !(firstName.isEmpty())){ // check if all obligatory fields are completed
                //update of information for current patient object instance
                operator.setName(name);
                operator.setFirstName(firstName);
                operatorDAO.updateOperator(operator);
                Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                startActivity(intent);
            }

            else{ // not all obligatory fields are completed
                showPopup(CrudUserActivity.this, COMPLETE_FIELDS_ERROR);
            }
        });

        // delete button
        // Check if operator is in an existing test
        bDelete.setOnClickListener(v -> {
            operatorDAO.delOperator(operator);
            Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Handles the display of operator information for reading purposes.
     *
     * @param userId       The ID of the user (operator).
     * @param operatorDAO  The OperatorDAO instance for database operations.
     */
    private void readOperator(String userId, OperatorDAO operatorDAO){
        operator = operatorDAO.getOperator(Long.parseLong(userId));
        tvNewUser.setText(R.string.operator);

        etName.setText(operator.getName());
        etName.setTextColor(Color.parseColor(WHITE));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.BLACK);
        gradientDrawable.setStroke(2, Color.parseColor("#FFA500"));
        gradientDrawable.setCornerRadius(8);
        etName.setBackground(gradientDrawable);
        etName.setClickable(false);
        etName.setFocusable(false);  TextView name = new TextView(this);
        name.setText(R.string.set_name);
        name.setTextColor(Color.parseColor(WHITE));
        llCrud.addView(name, 1);


        etFirstName.setText(operator.getFirstName());
        etFirstName.setTextColor(Color.parseColor(WHITE));
        etFirstName.setBackground(gradientDrawable);
        etFirstName.setClickable(false);
        etFirstName.setFocusable(false);
        TextView firstName = new TextView(this);
        firstName.setText(R.string.set_first_name);
        firstName.setTextColor(Color.parseColor(WHITE));
        llCrud.addView(firstName, 4);

        bConfirm.setText(R.string.button_edit);
        bConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(CrudUserActivity.this, CrudUserActivity.class);
            intent.putExtra("User", "operator");
            intent.putExtra("Crud", UPDATE);
            intent.putExtra(USER_ID_FIELD, String.valueOf(operator.getId()));
            startActivity(intent);
        });
    }
}
