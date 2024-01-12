package com.example.ex_motricite;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    private LinearLayout ll_crud;
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

        ll_crud = findViewById(R.id.ll_crud);
        b_confirm = findViewById(R.id.b_confirm);
        tv_newuser = findViewById(R.id.tv_newuser);
        et_name = findViewById(R.id.et_name);
        et_firstName = findViewById(R.id.et_firstname);
        et_birthdate = findViewById(R.id.et_birthdate);
        et_remarks = findViewById(R.id.et_remarks);

        Intent myIntent = getIntent();
        String user = myIntent.getStringExtra("User");
        String crud = myIntent.getStringExtra("Crud");
        long idPatient = myIntent.getLongExtra("idPatient", 0);
        long idOperator = myIntent.getLongExtra("idOperator", 0);

        if (user.equals("patient")){
            patientDAO = new PatientDAO(this);
            if (crud.equals("create")) {
                tv_newuser.setText("Create a new patient");

                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString();
                        String firstName = et_firstName.getText().toString();
                        String birthDate = et_birthdate.getText().toString();
                        String remarks = et_remarks.getText().toString();

                        if (!(name.isEmpty()) && !(firstName.isEmpty()) && !(birthDate.isEmpty())) { // check if all obligatory fields are completed
                            if (isValidDate(birthDate)) {//check if birthdate seems to be on the waited format DD/MM/YYYY
                                patient = new Patient(name, firstName, birthDate, remarks);
                                patientDAO.addPatient(patient);
                                Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                                startActivity(intent);
                            } else { // error on date
                                showPopup(CrudUserActivity.this, "Type a valid date");
                            }
                        } else { // not all obligatory fields are completed
                            showPopup(CrudUserActivity.this, "Complete all fields");
                        }
                    }
                });
            }

            if(crud.equals(("read"))){
                Patient patient = patientDAO.getPatient(idPatient);
                tv_newuser.setText("Patient");

                et_name.setText(patient.getName());
                et_name.setTextColor(Color.parseColor("#FFFFFF"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.BLACK);
                gradientDrawable.setStroke(2, Color.parseColor("#FFA500"));
                gradientDrawable.setCornerRadius(8);
                et_name.setBackground(gradientDrawable);
                et_name.setClickable(false);
                et_name.setFocusable(false);
                TextView name = new TextView(this);
                name.setText("Name : ");
                name.setTextColor(Color.parseColor("#FFFFFF"));
                ll_crud.addView(name, 1);

                et_firstName.setText(patient.getFirstName());
                et_firstName.setTextColor(Color.parseColor("#FFFFFF"));
                et_firstName.setBackground(gradientDrawable);
                et_firstName.setClickable(false);
                et_firstName.setFocusable(false);
                TextView firstName = new TextView(this);
                firstName.setText("First Name : ");
                firstName.setTextColor(Color.parseColor("#FFFFFF"));
                ll_crud.addView(firstName, 4);


                et_birthdate.setText(patient.getBirthDate());
                et_birthdate.setTextColor(Color.parseColor("#FFFFFF"));
                et_birthdate.setBackground(gradientDrawable);
                et_birthdate.setClickable(false);
                et_birthdate.setFocusable(false);
                TextView birthDate = new TextView(this);
                birthDate.setText("Birthdate : ");
                birthDate.setTextColor(Color.parseColor("#FFFFFF"));
                ll_crud.addView(birthDate, 7);

                et_remarks.setText(patient.getRemarks());
                et_remarks.setTextColor(Color.parseColor("#FFFFFF"));
                et_remarks.setBackground(gradientDrawable);
                et_remarks.setClickable(false);
                et_remarks.setFocusable(false);
                TextView remarks = new TextView(this);
                remarks.setText("Remarks : ");
                remarks.setTextColor(Color.parseColor("#FFFFFF"));
                ll_crud.addView(remarks, 10);

                b_confirm.setText("Modify");
                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CrudUserActivity.this, CrudUserActivity.class);
                        intent.putExtra("User", "patient");
                        intent.putExtra("Crud", "update");
                        intent.putExtra("idPatient", patient.getId());
                        startActivity(intent);
                    }
                });
            }
        }
        else{
            operatorDAO = new OperatorDAO(this);
            et_birthdate.setVisibility(View.INVISIBLE);
            et_remarks.setVisibility(View.INVISIBLE);

            if (crud.equals("create")) {
                tv_newuser.setText("Create a new operator");
                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_name.getText().toString();
                        String firstName = et_firstName.getText().toString();
                        if (!(name.isEmpty()) && !(firstName.isEmpty())) {
                            operator = new Operator(name, firstName);
                            operatorDAO.addOperator(operator);
                            Intent intent = new Intent(CrudUserActivity.this, ListUserPageActivity.class);
                            startActivity(intent);
                        } else {
                            showPopup(CrudUserActivity.this, "Complete all fields");
                        }
                    }
                });
            }

            if (crud.equals("read")){
                Operator operator = operatorDAO.getOperator(idOperator);
                tv_newuser.setText("Operator");

                et_name.setText(operator.getName());
                et_name.setTextColor(Color.parseColor("#FFFFFF"));
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setColor(Color.BLACK);
                gradientDrawable.setStroke(2, Color.parseColor("#FFA500"));
                gradientDrawable.setCornerRadius(8);
                et_name.setBackground(gradientDrawable);
                et_name.setClickable(false);
                et_name.setFocusable(false);  TextView name = new TextView(this);
                name.setText("Name : ");
                name.setTextColor(Color.parseColor("#FFFFFF"));
                ll_crud.addView(name, 1);


                et_firstName.setText(operator.getFirstName());
                et_firstName.setTextColor(Color.parseColor("#FFFFFF"));
                et_firstName.setBackground(gradientDrawable);
                et_firstName.setClickable(false);
                et_firstName.setFocusable(false);
                TextView firstName = new TextView(this);
                firstName.setText("First Name : ");
                firstName.setTextColor(Color.parseColor("#FFFFFF"));
                ll_crud.addView(firstName, 4);

                b_confirm.setText("Modify");
                b_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CrudUserActivity.this, CrudUserActivity.class);
                        intent.putExtra("User", "operator");
                        intent.putExtra("Crud", "update");
                        intent.putExtra("idPatient", operator.getId());
                        startActivity(intent);
                    }
                });
            }
            }
        }

}
