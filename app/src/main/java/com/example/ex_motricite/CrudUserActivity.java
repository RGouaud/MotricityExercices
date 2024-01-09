package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class CrudUserActivity extends AppCompatActivity {

    private Button b_confirm;
    private TextView tv_newuser;
    private EditText et_name;
    private EditText et_firstName;
    private EditText et_birthdate;
    private EditText et_remarks;
    private PatientDAO patientDAO;
    private Patient patient;

    private OperatorDAO operatorDAO;
    private Operator operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_user);

        b_confirm = findViewById(R.id.b_confirm);
        tv_newuser = findViewById(R.id.tv_newuser);
        et_birthdate = findViewById(R.id.et_birthdate);
        et_remarks = findViewById(R.id.et_remarks);

        Intent myIntent = getIntent();
        String user = myIntent.getStringExtra("User");

        if (user.equals("patient")){

            tv_newuser.setText("Create a new patient");
            patientDAO = new PatientDAO(this);

            b_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                    String name = et_name.getText().toString();
                    String firstName = et_firstName.getText().toString();
                    String birthDate = et_birthdate.getText().toString();
                    String remarks = et_remarks.getText().toString();

                    patient = new Patient(name, firstName, birthDate, remarks);
                    patientDAO.addPatient(patient);

                    startActivity(intent);
                }
            });
        }
        else{

            tv_newuser.setText("Create a new operator");
            et_birthdate.setVisibility(View.INVISIBLE);
            et_remarks.setVisibility(View.INVISIBLE);

            operatorDAO = new OperatorDAO(this);

            b_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                    String name = et_name.getText().toString();
                    String firstName = et_firstName.getText().toString();

                    operator = new Operator(name, firstName);
                    operatorDAO.addOperator(operator);

                    startActivity(intent);
                }
            });

        }
        b_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(homePageActivity.this, patientActivity.class);
                // startActivity(intent);
            }
        });
    }
}