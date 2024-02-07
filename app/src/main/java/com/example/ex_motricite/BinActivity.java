package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinActivity extends AppCompatActivity {
    // You can choose any number code here
    private ArrayList<Test>tests;

    private final List<Test> selectedTests = new ArrayList<>();
    private LinearLayout layoutListTest;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bin);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


    Button buttonRestore = findViewById(R.id.b_restore);
    Button buttonDelete = findViewById(R.id.b_deletetest);
    Button buttonDeselectAll = findViewById(R.id.b_deselectAll);
    Button buttonSelectAll = findViewById(R.id.b_selectAll);
    Button buttonFilters = findViewById(R.id.b_filters);
    layoutListTest = findViewById(R.id.l_listTest);
    TestDAO testDAO = new TestDAO(this);

    try {
        tests = testDAO.getAllTests();
    } catch (Exception e) {
        handleDataRetrievalError("Erreur lors de la récupération des opérateurs", e);
    }

    // Show all CSV files on startup
    displayAllTests();
    selectedTests.clear();

    buttonSelectAll.setOnClickListener(v -> selectAllTests());

    buttonDeselectAll.setOnClickListener(v -> deselectAllTests());

    buttonFilters.setOnClickListener(v -> {
        Intent intent = new Intent(BinActivity.this, SettingsActivity.class);
        startActivity(intent);
    });

    buttonDelete.setOnClickListener(v -> {
        deleteSelectedTest();
    });

    buttonRestore.setOnClickListener(v -> {
        restoreSelectedTest();
    });
}

    private void deleteSelectedTest(){
        TestDAO testDAO = new TestDAO(this);
        for (Test test : selectedTests){
            File fileToDelete = new File(test.getPath());
            fileToDelete.delete();
            testDAO.delTest(test);
        }
        displayAllTests();
    }
    private void restoreSelectedTest(){
        TestDAO testDAO = new TestDAO(this);
        for (Test test : selectedTests){
            moveFile(new File(test.getPath()) , getFilesDir() );
            testDAO.delTest(test);
        }
        displayAllTests();
    }
    private void displayAllTests () {
        selectedTests.clear();

        if (tests != null) {
            for (Test test : tests) {
                selectedTests.add(test);
            }
        }
        // Show files in layout
        displayFilesInLayout();
    }

    private void displayFilesInLayout() {
    // Clear the previous elements in the LinearLayout
    layoutListTest.removeAllViews();

    // Dynamically add items for each file in the list
    for (Test test : selectedTests) {
        Button testButton = createTestButton(test);
        layoutListTest.addView(testButton);
    }
}

    private Button createTestButton(final Test test){
    Button fileButton = new Button(this);
    fileButton.setText(test.getSuppressionDate());
    fileButton.setBackgroundColor(0xFF615321);

    // Short click to navigate to TestPageActivity

    // Long click for multiple selection
    fileButton.setOnLongClickListener(v -> {
        handleLongClick(fileButton, test);
        return true;  // Consume the long click
    });

    return fileButton;
}


    private void handleLongClick (Button fileButton, Test test){
    // Manage multiple selection here
    if (selectedTests.contains(test)) {
        selectedTests.remove(test);
        fileButton.setBackgroundColor(0xFF615321);
    } else {
        selectedTests.add(test);
        fileButton.setBackgroundColor(0xFFFAD552);
    }
}


    private void selectAllTests () {
        selectedTests.clear();
        if (tests != null) {
            for (Test test : tests) {
                selectedTests.add(test);
            }
        }
        // Update button
        updateButtonsState(true);
}


private void deselectAllTests() {
    // Empty the list of selected files
    selectedTests.clear();
    updateButtonsState(false);
}

    private void updateButtonsState ( boolean isChecked){
    // Go through all the ToggleButtons in the layout and update their state
    for (int i = 0; i < layoutListTest.getChildCount(); i++) {
        View view = layoutListTest.getChildAt(i);
        if (view instanceof Button) {
            Button fileButton = (Button) view;
            if (isChecked) {
                fileButton.setBackgroundColor(0xFFFAD552);
            } else {
                fileButton.setBackgroundColor(0xFF615321);
            }
        }
    }
}private void handleDataRetrievalError(String errorMessage, Exception e) {
        e.printStackTrace();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("QueryPermissionsNeeded")
    private void exportSelectedFilesByMail () {
}
    final File moveFile(File sourceFile , File destDirectory) {

        // Create the destination repository if it doesn't exist
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        // Get the path of the destination file
        File destFile = new File(destDirectory, sourceFile.getName());
        try {

            //Making a copy of the file to the correct directory
            FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();

            //Delete de source file
            if (destFile.exists() && destFile.length() == sourceFile.length()) {
                sourceFile.delete();
            }
            return destFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destFile;
    }
}
