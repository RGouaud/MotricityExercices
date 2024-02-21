package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
/**
 * The {@code FiltersTestActivity} class provides a helper for filtering test files.
 * It includes methods to filter test files based on operator and patient names, test type, and date.
 *
 * <p>
 * The class includes methods to filter test files based on operator and patient names, test type, and date.
 * It also includes methods to display the list of operators and patients, and to confirm the selection of test files.
 * </p>
 *
 * <p>
 *     Author: Maxime Segot
 *     Version: 1.0
 *     </p>
 *
 */
public class FiltersTestActivity extends AppCompatActivity {



    /**
     * The list of patients.
     */
    private ArrayList<Patient> patientList;
    /**
     * The list of patients to be displayed.
     */
    private ArrayList<Patient> currentPatientList;
    /**
     * The list of operators.
     */
    private ArrayList<Operator> operatorList;
    /**
     * The list of operators to be displayed.
     */
    private ArrayList<Operator> currentOperatorList;
    /**
     * The list of CSV files.
     */
    private final ArrayList<File> csvList = new ArrayList<>();
    /**
     * The list of filtered CSV files.
     */
    private final ArrayList<String> filteredCsvList = new ArrayList<>();
    /**
     * The list of selected operator names.
     */
    private final ArrayList<String> selectedOperatorNameList = new ArrayList<>();
    /**
     * The list of selected patient names.
     */
    private final ArrayList<String> selectedPatientNameList = new ArrayList<>();
    /**
     * The layout for the list of operators.
     */
    private LinearLayout layoutListOperator;
    /**
     * The layout for the list of patients.
     */
    private LinearLayout layoutListPatient;
    /**
     * The EditText for the maximum date.
     */
    private EditText etDateMax;
    /**
     * The EditText for the minimum date.
     */
    private EditText etDateMin;
    /**
     * The CheckBox for the static tests.
     */
    private CheckBox cbStatic;
    /**
     * The CheckBox for the dynamic tests.
     */
    private CheckBox cbDynamic;

    private ImageView ivLeave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PatientDAO patientDAO;
        OperatorDAO operatorDAO;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterstest);

        this.cbStatic = findViewById(R.id.cb_static);
        this.cbDynamic = findViewById(R.id.cb_dynamic);

        this.etDateMax = findViewById(R.id.et_dateMax);
        this.etDateMin = findViewById(R.id.et_dateMin);

        EditText etResearchOperator = findViewById(R.id.et_researchOperator);
        EditText etResearchPatient = findViewById(R.id.et_researchPatient);

        this.layoutListOperator = findViewById(R.id.l_listOperator);
        this.layoutListPatient = findViewById(R.id.l_listPatient);

        Button bConfirm = findViewById(R.id.b_confirm);
        ivLeave = findViewById(R.id.iv_leave_filters);
        ivLeave.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("page", "tests");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        });

        bConfirm.setOnClickListener(v -> confirmSelection());

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
                // No need to implement this method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                layoutListOperator.removeAllViews();
                filterOperator(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // No need to implement this method
            }
        });
        etResearchPatient.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to implement this method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                layoutListPatient.removeAllViews();
                filterPatient(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                // No need to implement this method
            }
        });

    }



    /**
     * Creates a new layout with an actor.
     *
     * @param user The actor to be displayed.
     * @param indexElement The index of the element in the list.
     * @param sourceLayout The layout to which the new layout will be added.
     * @param selectedNameList The list of selected names.
     */
    private void createNewLayoutWithActor(final Actor user, int indexElement,LinearLayout sourceLayout, ArrayList<String> selectedNameList) {
        final int NB_COLUMNS = 3;
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
        layoutButton.setBackground(AppCompatResources.getDrawable(this,R.drawable.black_rounded_rectangle));



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
                layoutButton.setBackground(AppCompatResources.getDrawable(this,R.drawable.yellow_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.black));
                tvFirstName.setTextColor(getColor(R.color.black));
                selectedNameList.add(user.getName());
            }
            else {
                layoutButton.setBackground(AppCompatResources.getDrawable(this,R.drawable.black_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.white));
                tvFirstName.setTextColor(getColor(R.color.white));
                selectedNameList.remove(user.getName());
            }

        });
        layoutButton.addView(tvName);
        layoutButton.addView(tvFirstName);



        LinearLayout rowLayout = (LinearLayout) sourceLayout.getChildAt(sourceLayout.getChildCount() - 1);
        rowLayout.addView(layoutButton);

    }/**
     * Filters the list of operators based on the text entered in the search bar.
     *
     * @param text The text entered in the search bar.
     */
    private void filterOperator(String text) {
        currentOperatorList.clear();
        for (Operator user : operatorList) {
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                currentOperatorList.add(user);
            }
        }
        displayAllOperators();
    }
    /**
     * Displays the list of operators.
     */
    private void displayAllOperators() {
        // Clear the previous elements in the LinearLayout
        layoutListOperator.removeAllViews();
        int indexElement = 0;
        // Dynamically add items for each file in the list
        for (Operator user : currentOperatorList) {
            createNewLayoutWithActor(user, indexElement, layoutListOperator, selectedOperatorNameList);
            indexElement++;
        }
    }

    /**
     * Filters the list of patients based on the text entered in the search bar.
     *
     * @param text The text entered in the search bar.
     */
    private void filterPatient(String text) {
        currentPatientList.clear();
        for (Patient user : patientList) {
            if (user.getName().toLowerCase().contains(text.toLowerCase())) {
                currentPatientList.add(user);
            }
        }
        displayAllPatients();
    }
    /**
     * Displays the list of patients.
     */
    private void displayAllPatients() {
        // Clear the previous elements in the LinearLayout
        layoutListPatient.removeAllViews();
        int indexElement = 0;
        // Dynamically add items for each file in the list
        for (Patient user : currentPatientList) {
            createNewLayoutWithActor(user, indexElement, layoutListPatient, selectedPatientNameList);
            indexElement++;
        }
    }
    /**
     * Gets all the CSV files in the internal storage.$
     * Fill the list of CSV files.
     */
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
    /**
     * This method is called when the user clicks on the confirm button.
     * Confirms the selection of test files.
     * It checks if the birthdate seems to be on the waited format DD/MM/YYYY and not empty.
     * If the date is not valid, it displays a message to the user.
     * If the date is valid, it checks if the date is within the range of the minimum and maximum dates, and if the operator and patient names are selected.
     * If the date is within the range of the minimum and maximum dates, and if the operator and patient names are selected, it adds the file to the list of filtered files.
     * It then starts the ListTestActivity with the list of filtered files.
     */
    private void confirmSelection(){
        String dateMin = etDateMin.getText().toString();
        String dateMax = etDateMax.getText().toString();

        boolean isDynamic = cbDynamic.isChecked();
        boolean isStatic = cbStatic.isChecked();

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
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("csvList", filteredCsvList);
            intent.putExtra("page","tests" );
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        }
    }
    /**
     * Checks if the date is within the range of the minimum and maximum dates, and if the operator and patient names are selected.
     *
     * @param dateMin The minimum date.
     * @param dateMax The maximum date.
     * @param fileName The name of the file.
     * @param date The date of the file.
     * @return True if the date is within the range of the minimum and maximum dates, and if the operator and patient names are selected, false otherwise.
     */
    private boolean isExisting(String dateMin, String dateMax, String[] fileName, String date) {
        if ( ( dateMax.isEmpty() && dateMin.isEmpty() ) && (selectedOperatorNameList.isEmpty()||selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() ||selectedPatientNameList.contains(fileName[0]))) {
            return true;
        }
        else if ( ( dateMax.isEmpty() && date.compareTo(dateMin) >= 0 ) && (selectedOperatorNameList.isEmpty()||selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() ||selectedPatientNameList.contains(fileName[0])))
            return true;
        else if (dateMin.isEmpty() && date.compareTo(dateMax) <= 0 && (selectedOperatorNameList.isEmpty()||selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() ||selectedPatientNameList.contains(fileName[0]))) {
            return true;
        }
        else return date.compareTo(dateMin) >= 0 && date.compareTo(dateMax) <= 0 && (selectedOperatorNameList.isEmpty() || selectedOperatorNameList.contains(fileName[1])) && (selectedPatientNameList.isEmpty() || selectedPatientNameList.contains(fileName[0]));
    }
}