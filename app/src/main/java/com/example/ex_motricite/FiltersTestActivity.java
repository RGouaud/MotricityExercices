package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class FiltersTestActivity extends AppCompatActivity {


    /**
     * The PatientDAO instance for database operations.
     */
    private OperatorDAO operatorDAO;
    /**
     * The OperatorDAO instance for database operations.
     */
    private PatientDAO patientDAO;

    private final int NB_COLUMNS = 3;
    private ArrayList<Patient> patientList;
    private ArrayList<Patient> currentPatientList;
    private ArrayList<Operator> operatorList;
    private ArrayList<Operator> currentOperatorList;
    private ArrayList<File> csvList = new ArrayList<>();
    private ArrayList<File> filteredCsvList = new ArrayList<>();
    private ArrayList<String> selectedOperatorNameList = new ArrayList<>();
    private LinearLayout layoutListOperator;
    private LinearLayout layoutListPatient;

    private EditText etDateMax;
    private EditText etDateMin;
    private EditText etResearchOperator;
    private EditText etResearchPatient;
    private Button bConfirm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterstest);

        this.etDateMax = findViewById(R.id.et_dateMax);
        this.etDateMin = findViewById(R.id.et_dateMin);

        this.etResearchOperator = findViewById(R.id.et_researchOperator);
        this.etResearchPatient = findViewById(R.id.et_researchPatient);

        this.layoutListOperator = findViewById(R.id.l_listOperator);
        this.layoutListPatient = findViewById(R.id.l_listPatient);

        this.bConfirm = findViewById(R.id.b_confirm);

        bConfirm.setOnClickListener(v -> {

            Intent intent = new Intent(this, ListTestActivity.class);
            intent.putExtra("csvList", filteredCsvList);
        });

        operatorDAO = new OperatorDAO(this);
        patientDAO = new PatientDAO(this);

        getAllCSV();

        operatorList = operatorDAO.getOperators();
        patientList = patientDAO.getPatients();

        currentOperatorList = operatorDAO.getOperators();
        currentPatientList = patientDAO.getPatients();
        filterOperator("");
        filterPatient("");

        etResearchOperator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                layoutListOperator.removeAllViews();
                filterOperator(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etResearchOperator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                layoutListPatient.removeAllViews();
                filterPatient(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etDateMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                filteredCsvList.clear();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if(DateValidator.isValid(s.toString())){//check if birthdate seems to be on the waited format DD/MM/YYYY

                }
                else{ // error on date
                    showPopup(CrudUserActivity.this, "Type a valid date");
                }*/
            }
        });
    }


    private void filterOperator(String text) {
        currentOperatorList.clear();
        for (Operator user : operatorList) {
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                currentOperatorList.add(user);
            }
        }
        displayAllOperators();
    }
    private void displayAllOperators() {
        // Clear the previous elements in the LinearLayout
        layoutListOperator.removeAllViews();
        int indexElement = 0;
        int nbOperators = currentOperatorList.size();
        // Dynamically add items for each file in the list
        for (Operator user : currentOperatorList) {
            createOperatorButton(user, indexElement);
            indexElement++;
        }
    }

    private void createOperatorButton(final Operator user, int indexElement) {
        if (indexElement % NB_COLUMNS == 0 )
        {
            // Create a new LinearLayout for the row
            LinearLayout layoutRow = new LinearLayout(this);
            layoutRow.setOrientation(LinearLayout.HORIZONTAL);
            layoutRow.setPadding(10,5,5,10);
            layoutRow.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            layoutRow.setWeightSum(10);

            layoutListOperator.addView(layoutRow);
        }

        //1button layout
        LinearLayout layoutButton = new LinearLayout(this);
        layoutButton.setOrientation(LinearLayout.VERTICAL);
        layoutButton.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        layoutButton.setPadding(5,5,5,5);
        layoutButton.setBackground(getDrawable(R.drawable.black_rounded_rectangle));



        //TextView containing the name of the patient and the operator
        TextView tvName = new TextView(this);
        tvName.setText(user.getName());
        tvName.setTextColor(getColor(R.color.white));
        TextView tvFirstName = new TextView(this);
        tvFirstName.setText(user.getFirstName());
        tvFirstName.setTextColor(getColor(R.color.white));
        layoutButton.setOnClickListener(v->{
            if (tvName.getCurrentTextColor() == getColor(R.color.white)){
                layoutButton.setBackground(getDrawable(R.drawable.yellow_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.black));
                tvFirstName.setTextColor(getColor(R.color.black));
                selectedOperatorNameList.add(user.getName());
            }
            else {
                layoutButton.setBackground(getDrawable(R.drawable.black_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.white));
                tvFirstName.setTextColor(getColor(R.color.white));
                selectedOperatorNameList.remove(user.getName());
            }

        });
        layoutButton.addView(tvName);
        layoutButton.addView(tvFirstName);



        LinearLayout rowLayout = (LinearLayout) layoutListOperator.getChildAt(layoutListOperator.getChildCount() - 1);
        rowLayout.addView(layoutButton);

    }
    private void filterPatient(String text) {
        currentPatientList.clear();
        for (Patient user : patientList) {
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                currentPatientList.add(user);
            }
        }
        displayAllPatients();
    }
    private void displayAllPatients() {
        // Clear the previous elements in the LinearLayout
        layoutListPatient.removeAllViews();
        int indexElement = 0;
        int nbOperators = currentPatientList.size();
        // Dynamically add items for each file in the list
        for (Patient user : currentPatientList) {
            createPatientButton(user, indexElement);
            indexElement++;
        }
    }

    private void createPatientButton(final Patient user, int indexElement) {
        if (indexElement % NB_COLUMNS == 0 )
        {
            // Create a new LinearLayout for the row
            LinearLayout layoutRow = new LinearLayout(this);
            layoutRow.setOrientation(LinearLayout.HORIZONTAL);
            layoutRow.setPadding(10,5,5,10);
            layoutRow.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutListPatient.addView(layoutRow);
        }

        //1button layout
        LinearLayout layoutButton = new LinearLayout(this);
        layoutButton.setOrientation(LinearLayout.VERTICAL);
        layoutButton.setPadding(5,5,5,5);
        layoutButton.setWeightSum(10);



        //TextView containing the name of the patient and the operator
        TextView tvName = new TextView(this);
        tvName.setText(user.getName());
        TextView tvFirstName = new TextView(this);
        tvFirstName.setText(user.getFirstName());

        layoutButton.addView(tvName);
        layoutButton.addView(tvFirstName);

        LinearLayout rowLayout = (LinearLayout) layoutListPatient.getChildAt(layoutListPatient.getChildCount() - 1);
        rowLayout.addView(layoutButton);

    }

    void getAllCSV(){
        // Get directory from internal storage
        File internalStorageDir = getFilesDir();

        // List all files in directory
        File[] files = internalStorageDir.listFiles();

        // Add all CSV files to the list of selected files
        csvList.clear();

        if (files != null) {
            for (File file : files) {
                // Check if the file is a CSV file
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    csvList.add(file);
                }
            }
        }
    }


}