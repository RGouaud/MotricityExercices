package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

public class FiltersTestActivity extends AppCompatActivity {

    private ArrayList<File> csvList;

    private ArrayList<File> currentCsvList;
    private LinearLayout layoutListCsvOperator;
    private LinearLayout layoutListCsvPatient;

    private EditText etDateMax;
    private EditText etDateMin;
    private EditText etResearchOperator;
    private EditText etResearchPatient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterstest);

        this.etDateMax = findViewById(R.id.et_dateMax);
        this.etDateMin = findViewById(R.id.et_dateMin);
        this.etResearchOperator = findViewById(R.id.et_researchOperator);
        this.etResearchPatient = findViewById(R.id.et_researchPatient);

        this.layoutListCsvOperator = findViewById(R.id.l_listOperator);
        this.layoutListCsvPatient = findViewById(R.id.l_listPatient);

        getAllCSV();

        currentCsvList = csvList;


    }


    private void displayAllCSVFiles(ArrayList<File> selectedFiles, LinearLayout layoutListTest) {
        // Clear the previous elements in the LinearLayout
        layoutListTest.removeAllViews();

        // Dynamically add items for each file in the list
        for (File file : selectedFiles) {
            Button fileButton = createFileButton(file);
            layoutListTest.addView(fileButton);
        }
    }

    private Button createFileButton(final File file) {
        layoutListCsvOperator.getChildCount(); //pour me souvenir de la fonction pour compter les enfants)

        //3buttons layout
        LinearLayout layout3buttons = new LinearLayout(this);
        layout3buttons.setOrientation(LinearLayout.HORIZONTAL);
        layout3buttons.setPadding(10,5,5,10);
        layout3buttons.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //1button layout
        LinearLayout layoutButton = new LinearLayout(this);
        layoutButton.setOrientation(LinearLayout.VERTICAL);
        layoutButton.setPadding(5,5,5,5);
        layoutButton.setWeightSum(10);


        Button fileButton = new Button(this);
        fileButton.setText(file.getName());
        fileButton.setBackgroundColor(0xFF615321);

        // Short click to navigate to TestPageActivity
        fileButton.setOnClickListener(v -> navigateToTestPage(file));

        // Long click for multiple selection
        fileButton.setOnLongClickListener(v -> {
            handleLongClick(fileButton, file);
            return true;  // Consume the long click
        });

        return fileButton;
    }

    void getCSVByOperatorsName(){

    }
    void getCSVByPatientName(){

    }
    void getCSVByDateMax(){

    }
    void getCSVByDateMin(){

    }
    void getCSVByType(){

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