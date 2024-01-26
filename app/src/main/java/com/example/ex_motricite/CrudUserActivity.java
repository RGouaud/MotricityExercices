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


public class CrudUserActivity extends AppCompatActivity {
    private Patient patient;
    private Operator operator;

    private static final String WHITE = "#FFFFFF";
    private static final String COMPLETE_FIELDS_ERROR = "Complete all fields";

    OperatorDAO operatorDAO;
    PatientDAO patientDAO;
    EditText etRemarks;
    EditText etBirthdate;
    EditText etFirstName;
    EditText etName;
    TextView tvNewUser;
    Button bDelete;
    Button bConfirm;
    LinearLayout llCrud;

    private static boolean isValidDate(String dateStr){
        // date regex "dd/MM/yyyy"
        String regex = "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateStr);

        return matcher.matches();
    }

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

        llCrud = findViewById(R.id.ll_crud);
        bConfirm = findViewById(R.id.b_confirm);
        bDelete = findViewById(R.id.b_delete);
        tvNewUser = findViewById(R.id.tv_newuser);
        etName = findViewById(R.id.et_name);
        etFirstName = findViewById(R.id.et_firstname);
        etBirthdate = findViewById(R.id.et_birthdate);
        etRemarks = findViewById(R.id.et_remarks);

        Intent myIntent = getIntent();
        String user = myIntent.getStringExtra(ConstIntent.USER_TYPE);
        String crud = myIntent.getStringExtra(ConstIntent.CRUD_TYPE);
        String userId = myIntent.getStringExtra(ConstIntent.USER_ID);

        if(crud == null || user == null){
            return;
        }
        switch (user){
            case ConstIntent.USER_TYPE_PATIENT:
                patientDAO = new PatientDAO(this);
                patient(crud, userId, patientDAO);
                break;
            case ConstIntent.USER_TYPE_OPERATOR:
                operatorDAO = new OperatorDAO(this);
                operator(crud, userId, operatorDAO);
                break;
            default:
                break;
        }
    }

    private void patient(String crud, String userId, PatientDAO patientDAO){
        switch (crud){
            case ConstIntent.CRUD_TYPE_CREATE:
                createPatient(patientDAO);
                break;
            case ConstIntent.CRUD_TYPE_UPDATE:
                if(userId != null){
                    updatePatient(userId, patientDAO);
                }
                break;
            case ConstIntent.CRUD_TYPE_READ:
                if(userId != null) {
                    readPatient(userId, patientDAO);
                }
                break;
            default:
                break;
        }
    }

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
            intent.putExtra(ConstIntent.USER_TYPE, ConstIntent.PATIENT);
            intent.putExtra(ConstIntent.CRUD_TYPE, ConstIntent.CRUD_TYPE_UPDATE);
            intent.putExtra(ConstIntent.USER_ID, String.valueOf(patient.getId()));
            startActivity(intent);
        });
    }

    private void operator(String crud, String userId, OperatorDAO operatorDAO){
        switch (crud){
            case ConstIntent.CRUD_TYPE_CREATE:
                createOperator(operatorDAO);
                break;
            case ConstIntent.CRUD_TYPE_UPDATE:
                updateOperator(userId, operatorDAO);
                break;
            case ConstIntent.CRUD_TYPE_READ:
                readOperator(userId, operatorDAO);
                break;
            default:
                break;
        }
    }

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
            intent.putExtra(ConstIntent.USER_TYPE, ConstIntent.OPERATOR);
            intent.putExtra(ConstIntent.CRUD_TYPE, ConstIntent.CRUD_TYPE_UPDATE);
            intent.putExtra(ConstIntent.USER_ID, String.valueOf(operator.getId()));
            startActivity(intent);
        });
    }
}
