package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<String> filteredCsvList = new ArrayList<>();
    private ArrayList<String> selectedOperatorNameList = new ArrayList<>();

    private ArrayList<String> selectedPatientNameList = new ArrayList<>();
    private LinearLayout layoutListOperator;
    private LinearLayout layoutListPatient;

    private EditText etDateMax;
    private EditText etDateMin;
    private EditText etResearchOperator;
    private EditText etResearchPatient;
    private Button bConfirm;

    private CheckBox cbStatic;
    private CheckBox cbDynamic;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterstest);

        this.cbStatic = findViewById(R.id.cb_static);
        this.cbDynamic = findViewById(R.id.cb_dynamic);

        this.etDateMax = findViewById(R.id.et_dateMax);
        this.etDateMin = findViewById(R.id.et_dateMin);

        this.etResearchOperator = findViewById(R.id.et_researchOperator);
        this.etResearchPatient = findViewById(R.id.et_researchPatient);

        this.layoutListOperator = findViewById(R.id.l_listOperator);
        this.layoutListPatient = findViewById(R.id.l_listPatient);

        this.bConfirm = findViewById(R.id.b_confirm);

        bConfirm.setOnClickListener(v -> {

            confirmSelection();
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
        etResearchPatient.addTextChangedListener(new TextWatcher() {
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

    }




    private void createNewLayoutWithActor(final Actor user, int indexElement,LinearLayout sourceLayout, ArrayList<String> selectedNameList) {
        if (indexElement % NB_COLUMNS == 0 )
        {
            // Create a new LinearLayout for the row
            LinearLayout layoutRow = new LinearLayout(this);
            layoutRow.setOrientation(LinearLayout.HORIZONTAL);
            layoutRow.setPadding(10,5,5,10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
            );
            params.setMargins(5,0,5,0);
            layoutRow.setLayoutParams(params);

            sourceLayout.addView(layoutRow);
        }

        //1button layout
        LinearLayout layoutButton = new LinearLayout(this);
        layoutButton.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        );
        params.setMargins(25,0,25,0);
        layoutButton.setLayoutParams(params);
        layoutButton.setPadding(15,15,15,15);
        layoutButton.setBackground(getDrawable(R.drawable.black_rounded_rectangle));



        //TextView containing the name of the patient and the operator
        TextView tvName = new TextView(this);
        tvName.setText(user.getName());
        tvName.setTextColor(getColor(R.color.white));
        tvName.setGravity(1);
        TextView tvFirstName = new TextView(this);
        tvFirstName.setText(user.getFirstName());
        tvFirstName.setTextColor(getColor(R.color.white));
        tvFirstName.setGravity(1);
        layoutButton.setOnClickListener(v->{
            if (tvName.getCurrentTextColor() == getColor(R.color.white)){
                layoutButton.setBackground(getDrawable(R.drawable.yellow_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.black));
                tvFirstName.setTextColor(getColor(R.color.black));
                selectedNameList.add(user.getName());
            }
            else {
                layoutButton.setBackground(getDrawable(R.drawable.black_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.white));
                tvFirstName.setTextColor(getColor(R.color.white));
                selectedNameList.remove(user.getName());
            }

        });
        layoutButton.addView(tvName);
        layoutButton.addView(tvFirstName);



        LinearLayout rowLayout = (LinearLayout) sourceLayout.getChildAt(sourceLayout.getChildCount() - 1);
        rowLayout.addView(layoutButton);

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
            createNewLayoutWithActor(user, indexElement, layoutListOperator, selectedOperatorNameList);
            indexElement++;
        }
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
            createNewLayoutWithActor(user, indexElement, layoutListPatient, selectedPatientNameList);
            indexElement++;
        }
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

    private void confirmSelection(){
        String dateMin = etDateMin.getText().toString();
        String dateMax = etDateMax.getText().toString();

        Boolean isDynamic = cbDynamic.isChecked();
        Boolean isStatic = cbStatic.isChecked();

        //check if birthdate seems to be on the waited format DD/MM/YYYY and not empty
        if( (!(DateValidator.isValid(dateMax)) && !( dateMax.isEmpty() )) || (!(DateValidator.isValid(dateMin)) && !( dateMin.isEmpty() )) ){
            Toast.makeText(FiltersTestActivity.this, "You must type a valid date. Format: DD/MM/YYYY", Toast.LENGTH_SHORT).show();
        }
        else {
            for (File file : csvList) {
                String[] fileName = file.getName().split("_");
                String[] dateTab = fileName[3].split("-");
                String date = dateTab[2] + "/";
                if (dateTab[1].length() == 1) {
                    date += "0" + dateTab[1] + "/";
                } else {
                    date += dateTab[1] + "/";
                }
                if (dateTab[1].length() == 0) {
                    date += "0" + dateTab[0];
                } else {
                    date += dateTab[0];
                }

                if(isExisting(dateMin, dateMax, fileName, date) && (isDynamic && fileName[2].equals("Dynamic"))){
                    filteredCsvList.add(file.getAbsolutePath());
                }
                else if (isExisting(dateMin, dateMax, fileName, date) && (isStatic && fileName[2].equals("Static"))){
                    filteredCsvList.add(file.getAbsolutePath());
                }
                else if (isExisting(dateMin, dateMax, fileName, date) && (isStatic == isDynamic)){
                    filteredCsvList.add(file.getAbsolutePath());
                }
            }
            Intent intent = new Intent(this, ListTestActivity.class);
            intent.putExtra("csvList", filteredCsvList);
            startActivity(intent);
        }
    }

    private Boolean isExisting(String dateMin, String dateMax, String[] fileName, String date) {
        if ( ( dateMax.isEmpty() && dateMin.isEmpty() ) && (selectedOperatorNameList.isEmpty()||selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() ||selectedPatientNameList.contains(fileName[0]))) {
            return true;
        }
        else if ( ( dateMax.isEmpty() && date.compareTo(dateMin) >= 0 ) && (selectedOperatorNameList.isEmpty()||selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() ||selectedPatientNameList.contains(fileName[0])))
            return true;
        else if (dateMin.isEmpty() && date.compareTo(dateMax) <= 0 && (selectedOperatorNameList.isEmpty()||selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() ||selectedPatientNameList.contains(fileName[0]))) {
            return true;
        }
        else if (date.compareTo(dateMin) >= 0 && date.compareTo(dateMax) <= 0 && (selectedOperatorNameList.isEmpty()||selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() ||selectedPatientNameList.contains(fileName[0]))) {
            return true;
        }
        return false;
    }
}