package com.example.ex_motricite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinActivity extends AppCompatActivity {
    // You can choose any number code here
    private ArrayList<DeletedTest>tests;

    private final List<DeletedTest> selectedTests = new ArrayList<>();
    private LinearLayout llListTest;

    @Override
    protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bin);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


    Button bRestore = findViewById(R.id.b_restore);
    Button bDelete = findViewById(R.id.b_deleteTest);
    Button bDeselectAll = findViewById(R.id.b_deselectAll);
    Button bSelectAll = findViewById(R.id.b_selectAll);
    Button bFilters = findViewById(R.id.b_filters);
    llListTest = findViewById(R.id.l_listTest);
    DeletedTestDAO deletedTestDAO = new DeletedTestDAO(this);

    try {
        tests = deletedTestDAO.getAllTests();
    } catch (Exception e) {
        handleDataRetrievalError(e);
    }

    // Show all CSV files on startup
    displayAllTests();
    selectedTests.clear();

    bSelectAll.setOnClickListener(v -> selectAllTests());

    bDeselectAll.setOnClickListener(v -> deselectAllTests());

    bFilters.setOnClickListener(v -> {
        Intent intent = new Intent(BinActivity.this, SettingsActivity.class);
        startActivity(intent);
    });

    bDelete.setOnClickListener(v -> deleteConfirmation());

    bRestore.setOnClickListener(v -> restoreSelectedTest());
}

    private void deleteSelectedTest(){
        DeletedTestDAO deletedTestDAO = new DeletedTestDAO(this);
        for (DeletedTest test : selectedTests){
            File fileToDelete = new File(test.getPath());
            if(!fileToDelete.delete()){
                Toast.makeText(this, "An error has occurred while deleting the file.", Toast.LENGTH_SHORT).show();
            }
            deletedTestDAO.delTest(test);
            tests.remove(test);
        }
        displayAllTests();
    }
    private void restoreSelectedTest(){
        DeletedTestDAO deletedTestDAO = new DeletedTestDAO(this);
        for (DeletedTest test : selectedTests){
            moveFile(new File(test.getPath()) , getFilesDir() );
            deletedTestDAO.delTest(test);
            tests.remove(test);
        }
        displayAllTests();
    }
    private void displayAllTests () {
        selectedTests.clear();

        if (tests != null) {
            selectedTests.addAll(tests);
        }
        // Show files in layout
        displayFilesInLayout();
    }

    private void displayFilesInLayout() {
    // Clear the previous elements in the LinearLayout
    llListTest.removeAllViews();

    // Dynamically add items for each file in the list
    for (DeletedTest test : selectedTests) {
        Button bTest = createTestButton(test);
        llListTest.addView(bTest);
    }
}

    private Button createTestButton(final DeletedTest test){
    Button bFile = new Button(this);
    bFile.setText(test.getSuppressionDate());
    bFile.setBackgroundColor(0xFF615321);

    // Short click to navigate to TestPageActivity

    // Long click for multiple selection
    bFile.setOnLongClickListener(v -> {
        handleLongClick(bFile, test);
        return true;  // Consume the long click
    });

    return bFile;
}


    private void handleLongClick (Button bFile, DeletedTest test){
    // Manage multiple selection here
    if (selectedTests.contains(test)) {
        selectedTests.remove(test);
        bFile.setBackgroundColor(0xFF615321);
    } else {
        selectedTests.add(test);
        bFile.setBackgroundColor(0xFFFAD552);
    }
}


    private void selectAllTests () {
        selectedTests.clear();
        if (tests != null) {
            selectedTests.addAll(tests);
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
    for (int i = 0; i < llListTest.getChildCount(); i++) {
        View view = llListTest.getChildAt(i);
        if (view instanceof Button) {
            Button fileButton = (Button) view;
            if (isChecked) {
                fileButton.setBackgroundColor(0xFFFAD552);
            } else {
                fileButton.setBackgroundColor(0xFF615321);
            }
        }
    }
}private void handleDataRetrievalError(Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "An error has occurred while retrieving the tests", Toast.LENGTH_SHORT).show();
    }


    final void moveFile(File sourceFile , File destDirectory) {

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
            if (destFile.exists() && destFile.length() == sourceFile.length() && (!sourceFile.delete())){
                    Toast.makeText(this, "An error has occurred while deleting the file.", Toast.LENGTH_SHORT).show();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deleteConfirmation() {
        // Create a dialog
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure that you want to delete these files ?");
        builder.setCancelable(false);
        builder.setTitle("Confirmation");

        builder.setPositiveButton("SURE",
                (dialog1, id) -> deleteSelectedTest());

        builder.setNegativeButton("CANCEL",
                (dialog12, id) -> dialog12.cancel());

        dialog = builder.create();
        dialog.show();
    }
}
