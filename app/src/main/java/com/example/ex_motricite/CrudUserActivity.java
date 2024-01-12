package com.example.ex_motricite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CrudUserActivity extends AppCompatActivity {

    private Button b_confirm;
    private Button b_delete;
    private TextView tv_newuser;
    private EditText et_name;
    private EditText et_firstName;
    private EditText et_birthdate;
    private EditText et_remarks;
    private PatientDAO patientDAO;
    private Patient patient;

    private OperatorDAO operatorDAO;
    private Operator operator;

    private static boolean isValidDate(String dateStr){
        // date regex "dd/MM/yyyy"
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateStr);

        return matcher.matches();
    }

    private static void showPopup(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Close popup
                    }
                });
        // Show popup
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_user);

        b_confirm = findViewById(R.id.b_confirm);
        b_delete = findViewById(R.id.b_delete);
        tv_newuser = findViewById(R.id.tv_newuser);
        et_name = findViewById(R.id.et_name);
        et_firstName = findViewById(R.id.et_firstname);
        et_birthdate = findViewById(R.id.et_birthdate);
        et_remarks = findViewById(R.id.et_remarks);

        Intent myIntent = getIntent();
        String user = myIntent.getStringExtra("User");
        String crud = myIntent.getStringExtra("Crud");
        String userId = myIntent.getStringExtra("UserId");

        if (user.equals("patient")){ // User type : patient
            patientDAO = new PatientDAO(this);
            if(crud.equals("create")){ // create a patient
                tv_newuser.setText("Create a new patient");

                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString();
                        String firstName = et_firstName.getText().toString();
                        String birthDate = et_birthdate.getText().toString();
                        String remarks = et_remarks.getText().toString();

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
                            showPopup(CrudUserActivity.this, "Complete all fields");
                        }
                    }
                });

            } else if (crud.equals("update")) {
                // setup display
                tv_newuser.setText("Edit patient");
                b_delete.setVisibility(View.VISIBLE);

                //get previous informations
                Patient patient = patientDAO.getPatient(Integer.parseInt(userId));

                //put previous informations in field
                et_name.setText(patient.getName());
                et_firstName.setText(patient.getFirstName());
                et_birthdate.setText(patient.getBirthDate());
                et_remarks.setText(patient.getRemarks());

                // confirm update
                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString();
                        String firstName = et_firstName.getText().toString();
                        String birthDate = et_birthdate.getText().toString();
                        String remarks = et_remarks.getText().toString();

                        if(!(name.isEmpty()) && !(firstName.isEmpty()) && !(birthDate.isEmpty())){ // check if all obligatory fields are completed
                            if(isValidDate(birthDate)){//check if birthdate seems to be on the waited format DD/MM/YYYY

                                //update of information for current patient object instance
                                patient.setName(name);
                                patient.setFirstName(firstName);
                                patient.setBirthDate(birthDate);
                                patient.setRemarks(remarks);
                                patientDAO.updatePatient(patient);
                                Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                                startActivity(intent);
                            }
                            else{ // error on date
                                showPopup(CrudUserActivity.this, "Type a valid date");
                            }
                        }
                        else{ // not all obligatory fields are completed
                            showPopup(CrudUserActivity.this, "Complete all fields");
                        }
                    }
                });

                // delete button
                b_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check if patient is in an existing test
                        patientDAO.delPatient(patient);
                        Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
        else{ // User type : manipulator
            operatorDAO = new OperatorDAO(this);

            if(crud.equals("create")){
                Log.d("debug", "ouverture de create");
                tv_newuser.setText("Create a new operator");
                et_birthdate.setVisibility(View.INVISIBLE);
                et_remarks.setVisibility(View.INVISIBLE);
                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString();
                        String firstName = et_firstName.getText().toString();
                        if(!(name.isEmpty()) && !(firstName.isEmpty())){
                            operator = new Operator(name, firstName);
                            operatorDAO.addOperator(operator);
                            Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                            startActivity(intent);
                        }
                        else{
                            showPopup(CrudUserActivity.this, "Complete all fields");
                        }
                    }
                });
            }
            else if(crud.equals("update")){
                // setup display
                tv_newuser.setText("Edit manipulator");
                b_delete.setVisibility(View.VISIBLE);

                //get previous informations
                Operator operator = operatorDAO.getOperator(Integer.parseInt(userId));

                //put previous informations in field
                et_name.setText(operator.getName());
                et_firstName.setText(operator.getFirstName());

                // confirm update
                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString();
                        String firstName = et_firstName.getText().toString();

                        if(!(name.isEmpty()) && !(firstName.isEmpty())){ // check if all obligatory fields are completed
                            //update of information for current patient object instance
                            operator.setName(name);
                            operator.setFirstName(firstName);
                            operatorDAO.updateOperator(operator);
                            Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                            startActivity(intent);
                        }

                        else{ // not all obligatory fields are completed
                            showPopup(CrudUserActivity.this, "Complete all fields");
                        }
                    }
                });

                // delete button
                b_delete.setOnClickListener(new View.OnClickListener() {
                    // Check if operator is in an existing test
                    @Override
                    public void onClick(View v) {
                        operatorDAO.delOperator(operator);
                        Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                        startActivity(intent);
                    }
                });

            }


        }
    }
}