package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The {@code ListTestActivity} class represents an Android activity for listing and managing CSV test files.
 * It allows users to select, deselect, and export CSV files, as well as navigate to detailed test pages.
 *
 * <p>
 * This activity displays a list of CSV files stored in the internal storage directory, providing options
 * for selecting multiple files, applying filters, and exporting the selected files via email.
 * </p>
 *
 * <p>
 * Additionally, it supports both single-click and long-click interactions for file navigation and
 * multiple file selection, respectively.
 * </p>
 *
 * <p>
 * The class utilizes shared preferences for storing application settings and integrates with the Android
 * intent system for email-based file export. It dynamically updates the layout to reflect the selected files
 * and their visual states.
 * </p>
 *
 * <p>
 * Author: EduardoXav, Ferreira, Arricastres
 * Version: 1.0
 * </p>
 */
public class ListTestActivity extends AppCompatActivity {

    // You can choose any number code here
    /**
     * The List of selected files.
     */
    private final List<File> selectedFiles = new ArrayList<>();
    /**
     * The layout for the list of test files.
     */
    private LinearLayout layoutListTest;
    /**
     * The shared preferences object.
     */
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_test);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        Button buttonExport = findViewById(R.id.b_exportSelection);
        Button buttonDeselectAll = findViewById(R.id.b_deselectAll);
        Button buttonSelectAll = findViewById(R.id.b_selectAll);
        Button buttonFilters = findViewById(R.id.b_filters);
        Button buttonDelete = findViewById(R.id.b_deletetests);
        TextView textView = findViewById(R.id.tv_ListOfTest);
        layoutListTest = findViewById(R.id.l_listTest);
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);



        // Show all CSV files on startup
        displayAllCSVFiles();
        selectedFiles.clear();

        textView.setOnClickListener(v -> startActivity(new Intent(ListTestActivity.this, BinActivity.class)));
        buttonSelectAll.setOnClickListener(v -> selectAllFiles());

        buttonDeselectAll.setOnClickListener(v -> deselectAllFiles());

        buttonDelete.setOnClickListener(v -> deleteConfirmation());

        buttonFilters.setOnClickListener(v -> {
            Intent intent = new Intent(ListTestActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        buttonExport.setOnClickListener(v -> {

            exportSelectedFilesByMail();
            Toast.makeText(ListTestActivity.this, "Exported file ", Toast.LENGTH_SHORT).show();
        });

    }

    private void deleteSelection(){
        TestDAO testDAO = new TestDAO(this);
        File binDirectory = new File(getFilesDir(), "bin_directory");
        if (!binDirectory.exists())
        {
            Log.d("Directory", "deleteSelection: bin_directory doesn't exist");
            binDirectory.mkdir();
        }

        for (File file : selectedFiles){
            File destFile = moveFile(file,binDirectory);
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            Test testBd = new Test(destFile.getAbsolutePath(),s.format(date));
            testDAO.addTest(testBd);
        }
        displayAllCSVFiles();
    }
    /**
     * Display all CSV files in the internal storage directory.
     */
    private void displayAllCSVFiles() {
        // Get directory from internal storage
        File internalStorageDir = getFilesDir();

        // List all files in directory
        File[] files = internalStorageDir.listFiles();

        // Add all CSV files to the list of selected files
        selectedFiles.clear();

        if (files != null) {
            for (File file : files) {
                // Check if the file is a CSV file
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    selectedFiles.add(file);
                }
            }
        }

        // Show files in layout
        displayFilesInLayout();
    }

    /**
     * Display files in the layout based on the selectedFiles list.
     */
    private void displayFilesInLayout() {
        // Clear the previous elements in the LinearLayout
        layoutListTest.removeAllViews();

        // Dynamically add items for each file in the list
        for (File file : selectedFiles) {
            Button fileButton = createFileButton(file);
            layoutListTest.addView(fileButton);
        }
    }

    /**
     * Create a Button for a given file to be displayed in the layout.
     *
     * @param file The CSV file.
     * @return The Button object.
     */
    private Button createFileButton(final File file) {
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

    /**
     * Navigate to TestPageActivity for a given file.
     *
     * @param file The CSV file.
     */
    private void navigateToTestPage(File file) {
        // Implement navigation logic to TestPageActivity here
        // You can use an Intent for this.
        Intent intent = new Intent(ListTestActivity.this, TestPageActivity.class);
        // Add additional data to the intent if necessary
        intent.putExtra("file_path", file.getAbsolutePath());
        startActivity(intent);
    }

    /**
     * Handle long-click for multiple file selection.
     *
     * @param fileButton The Button associated with the file.
     * @param file       The CSV file.
     */
    private void handleLongClick(Button fileButton, File file) {
        // Manage multiple selection here
        if (selectedFiles.contains(file)) {
            selectedFiles.remove(file);
            fileButton.setBackgroundColor(0xFF615321);
        } else {
            selectedFiles.add(file);
            fileButton.setBackgroundColor(0xFFFAD552);
        }
    }

    /**
     * Select all CSV files in the internal storage directory.
     */
    private void selectAllFiles() {
        // Get directory from internal storage
        File internalStorageDir = getFilesDir();

        // List all files in directory
        File[] files = internalStorageDir.listFiles();

        // Empty the list of selected files
        selectedFiles.clear();

        if (files != null) {
            for (File file : files) {
                // Check if the file is a CSV file
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    // Add the file to the list of selected files
                    selectedFiles.add(file);
                }
            }
        }
        updateButtonsState(true);
    }

    /**
     * Deselect all selected CSV files.
     */
    private void deselectAllFiles() {
        // Empty the list of selected files
        selectedFiles.clear();

        updateButtonsState(false);
    }

    /**
     * Update the visual state of buttons based on the selection.
     *
     * @param isChecked True if files are selected, false otherwise.
     */
    private void updateButtonsState(boolean isChecked) {
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
     * Export selected CSV files by sending them via email.
     */
    @SuppressLint("QueryPermissionsNeeded")
    private void exportSelectedFilesByMail() {

        String settingsJson = sharedPreferences.getString("settings_json", null);

        if (settingsJson != null) {
            try {
                // Convert JSON to object
                JSONObject jsonObject = new JSONObject(settingsJson);

                // Get the recipient's email address
                String email = jsonObject.getString("email");

                // Create an intent to send multiple CSV files
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("text/csv");

                // Add email addresses, subject and body of the message
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CSV files");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "CSV files attached.");

                // Create a list of Uri for files to send
                ArrayList<Uri> fileUris = new ArrayList<>();
                for (File file : selectedFiles) {
                    // Convert each file to Uri and add it to the list
                    Uri fileUri = FileProvider.getUriForFile(this, "com.example.myapp.file-provider", file);
                    fileUris.add(fileUri);
                }

                // Add List Uri to Intent
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);

                // Check if the device has an internet connection and an email app installed
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Send email with..."));
                } else {
                    // Handle the case where no messaging app is installed
                    Toast.makeText(this, "No application installed.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Moves a file to a specific directory.
     * @param sourceFile
     * @param destDirectory
     * @return The destination file.
     */
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
            if (destFile.exists() && destFile.length() == sourceFile.length() && (!sourceFile.delete())){
                    Toast.makeText(this, "An error has occurred while deleting the file.", Toast.LENGTH_SHORT).show();

            }
            return destFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destFile;
    }
    void deleteConfirmation() {
        // Create a dialog
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure that you want to delete these files ?");
        builder.setCancelable(false);
        builder.setTitle("Confirmation");

        builder.setPositiveButton("SURE",
                (dialog1, id) -> deleteSelection());

        builder.setNegativeButton("CANCEL",
                (dialog12, id) -> dialog12.cancel());

        dialog = builder.create();
        dialog.show();
    }


}