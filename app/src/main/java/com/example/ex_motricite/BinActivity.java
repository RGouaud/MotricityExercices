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

/**
 * The {@code BinActivity} class represents an Android activity for managing deleted tests.
 *
 * <p>
 * This activity includes functionalities to display a list of deleted tests, handle user input,
 * and navigate to specific exercise activities based on user selections. It utilizes DAOs (Data Access Objects)
 * to retrieve deleted test data from our database.
 * </p>
 *
 * <p>
 * The class supports multiple selection of deleted tests, with options to restore or permanently delete them.
 * It ensures proper input validation and provides a user-friendly interface for managing deleted tests.
 * </p>
 *
 * <p>
 *     Author: Maxime Segot
 *     Version: 1.0
 *     </p>
 */

public class BinActivity extends AppCompatActivity {
    /**
     * The list of deleted tests.
     */
    private ArrayList<Test>tests;
    /**
     * The list of selected tests.
     */
    private final List<Test> selectedTests = new ArrayList<>();
    /**
     * The layout for the list of deleted tests.
     */
    private LinearLayout layoutListTest;

    @Override
    protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bin);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


    Button buttonRestore = findViewById(R.id.b_restore);
    Button buttonDelete = findViewById(R.id.b_deleteTest);
    Button buttonDeselectAll = findViewById(R.id.b_deselectAll);
    Button buttonSelectAll = findViewById(R.id.b_selectAll);
    Button buttonFilters = findViewById(R.id.b_filters);
    layoutListTest = findViewById(R.id.l_listTest);
    TestDAO testDAO = new TestDAO(this);

    try {
        tests = testDAO.getAllTests();
    } catch (Exception e) {
        handleDataRetrievalError(e);
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

    buttonDelete.setOnClickListener(v -> deleteConfirmation());

    buttonRestore.setOnClickListener(v -> restoreSelectedTest());
}
    /**
     * Deletes permanently the selected tests.
     */
    private void deleteSelectedTest(){
        TestDAO testDAO = new TestDAO(this);
        for (Test test : selectedTests){
            File fileToDelete = new File(test.getPath());
            if(!fileToDelete.delete()){
                Toast.makeText(this, "An error has occurred while deleting the file.", Toast.LENGTH_SHORT).show();
            }
            testDAO.delTest(test);
            tests.remove(test);
        }
        displayAllTests();
    }
    /**
     * Restores the selected tests.
     */
    private void restoreSelectedTest(){
        TestDAO testDAO = new TestDAO(this);
        for (Test test : selectedTests){
            moveFile(new File(test.getPath()) , getFilesDir() );
            testDAO.delTest(test);
            tests.remove(test);
        }
        displayAllTests();
    }
    /**
     * Displays all the tests.
     */
    private void displayAllTests () {
        selectedTests.clear();

        if (tests != null) {
            selectedTests.addAll(tests);
        }
        // Show files in layout
        displayFilesInLayout();
    }
    /**
     * Displays the tests in the layout.
     */
    private void displayFilesInLayout() {
    // Clear the previous elements in the LinearLayout
    layoutListTest.removeAllViews();

    // Dynamically add items for each file in the list
    for (Test test : selectedTests) {
        Button testButton = createTestButton(test);
        layoutListTest.addView(testButton);
    }
}
    /**
     * Creates a button for each test.
     *
     * @param test The test to create a button for.
     * @return The button for the test.
     */
    private Button createTestButton(final Test test){
    Button fileButton = new Button(this);
    fileButton.setText(test.getSuppressionDate());
    fileButton.setBackgroundColor(0xFF615321);

    // Short click to navigate to TestPageActivity

    // Long click for multiple selection
    fileButton.setOnClickListener(v -> {
        handleClick(fileButton, test);
    });

    return fileButton;
}

    /**
     * Handles the click on a test button (add the test to the selected tests list.
     *
     * @param fileButton The button representing the test.
     * @param test       The test associated with the button.
     */

    private void handleClick(Button fileButton, Test test){
    // Manage multiple selection here
    if (selectedTests.contains(test)) {
        selectedTests.remove(test);
        fileButton.setBackgroundColor(0xFF615321);
    } else {
        selectedTests.add(test);
        fileButton.setBackgroundColor(0xFFFAD552);
    }
}

    /**
     * Selects all the tests.
     */
    private void selectAllTests () {
        selectedTests.clear();
        if (tests != null) {
            selectedTests.addAll(tests);
        }
        // Update button
        updateButtonsState(true);
    }

    /**
     * Deselects all the tests.
     */
    private void deselectAllTests() {
        // Empty the list of selected files
        selectedTests.clear();
        updateButtonsState(false);
    }

    /**
     * Updates the state of the buttons.
     *
     * @param isChecked The state of the buttons.
     */
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
    }
    /**
     * Handles an error that occurred while retrieving the tests.
     *
     * @param e The exception that occurred.
     */
    private void handleDataRetrievalError(Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "An error has occurred while retrieving the tests", Toast.LENGTH_SHORT).show();
    }

    /**
     * Moves a file to a specific directory.
     *
     * @param sourceFile     The file to move.
     * @param destDirectory The destination directory.
     */
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

    /**
     * Displays a confirmation pop up dialog page that confirm the deletion of the selected tests.
     */
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
