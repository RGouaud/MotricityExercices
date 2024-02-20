package com.example.ex_motricite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
 * A simple {@link Fragment} subclass.
 * Use the {@link TestsFragment} factory method to
 * create an instance of this fragment.
 */
public class TestsFragment extends Fragment {
    Button buttonBin;
    Button buttonFilter;
    Button buttonSelectAll;
    Button buttonUnselectAll;
    Button buttonDelete;
    Button buttonExport;
    LinearLayout layoutTests;
    private final List<File> allFiles = new ArrayList<>();
    // You can choose any number code here
    /**
     * The List of selected files.
     */
    private final List<File> selectedFiles = new ArrayList<>();
    /**
     * The shared preferences object.
     */
    private SharedPreferences sharedPreferences;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonBin = getActivity().findViewById(R.id.b_bin);
        buttonFilter = getActivity().findViewById(R.id.b_filters);
        buttonSelectAll = getActivity().findViewById(R.id.b_selectAll);
        buttonUnselectAll = getActivity().findViewById(R.id.b_unselectAll);
        buttonDelete = getActivity().findViewById(R.id.b_deleteSelection);
        buttonExport = getActivity().findViewById(R.id.b_exportSelection);
        layoutTests = getActivity().findViewById(R.id.l_listTest);

        // Show all CSV files on startup
        displayAllCSVFiles();
        selectedFiles.clear();
        buttonBin.setOnClickListener(v -> startActivity(new Intent(getActivity(), BinActivity.class)));
        buttonFilter.setOnClickListener(v -> startActivity(new Intent(getActivity(), FiltersTestActivity.class)));
        buttonSelectAll.setOnClickListener(v -> selectAllFiles());
        buttonUnselectAll.setOnClickListener(v -> deselectAllFiles());
        buttonDelete.setOnClickListener(v -> deleteConfirmation());
        buttonExport.setOnClickListener(v -> {

            exportSelectedFilesByMail();
            Toast.makeText(getActivity(), "Exported file ", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Display all CSV files in the internal storage directory.
     */
    private void displayAllCSVFiles() {

        Intent myIntent = getActivity().getIntent();
        if (myIntent.hasExtra("csvList")) {
            for (String filePath : myIntent.getStringArrayListExtra("csvList")) {
                File file = new File(filePath);
                if (file.exists()) {
                    allFiles.add(file);
                }
            }
        }
        else {
            // Get directory from internal storage
            File internalStorageDir = getActivity().getFilesDir();

            // List all files in directory
            File[] files = internalStorageDir.listFiles();

            // Add all CSV files to the list of selected files
            allFiles.clear();

            if (files != null) {
                for (File file : files) {
                    // Check if the file is a CSV file
                    if (file.isFile() && file.getName().endsWith(".csv")) {
                        allFiles.add(file);
                    }
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
        layoutTests.removeAllViews();

        // Dynamically add items for each file in the list
        for (File file : allFiles) {
            Button fileButton = createFileButton(file);
            layoutTests.addView(fileButton);
        }
    }

    /**
     * Create a Button for a given file to be displayed in the layout.
     *
     * @param file The CSV file.
     * @return The Button object.
     */
    private Button createFileButton(final File file) {
        Button bFileButton = new Button(getActivity());
        bFileButton.setText(file.getName());
        bFileButton.setBackgroundColor(0xFF615321);

        // Short click to navigate to TestPageActivity
        bFileButton.setOnClickListener(v -> navigateToTestPage(file));

        // Long click for multiple selection
        bFileButton.setOnLongClickListener(v -> {
            handleLongClick(bFileButton, file);
            return true;  // Consume the long click
        });

        return bFileButton;
    }

    /**
     * Navigate to TestPageActivity for a given file.
     *
     * @param file The CSV file.
     */
    private void navigateToTestPage(File file) {
        // Implement navigation logic to TestPageActivity here
        // You can use an Intent for this.
        Intent intent = new Intent(getActivity(), TestPageActivity.class);
        // Add additional data to the intent if necessary
        intent.putExtra("file_path", file.getAbsolutePath());
        startActivity(intent);
    }

    /**
     * Handle long-click for multiple file selection.
     *
     * @param bFileButton The Button associated with the file.
     * @param file       The CSV file.
     */
    private void handleLongClick(Button bFileButton, File file) {
        // Manage multiple selection here
        if (selectedFiles.contains(file)) {
            selectedFiles.remove(file);
            bFileButton.setBackgroundColor(0xFF615321);
        } else {
            selectedFiles.add(file);
            bFileButton.setBackgroundColor(0xFFFAD552);
        }
    }

    /**
     * Select all CSV files in the internal storage directory.
     */
    private void selectAllFiles() {

        // Empty the list of selected files
        selectedFiles.clear();

        if (allFiles != null) {
            for (File file : allFiles) {
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
        for (int i = 0; i < layoutTests.getChildCount(); i++) {
            View view = layoutTests.getChildAt(i);
            if (view instanceof Button) {
                Button fileButton = (Button) view;
                if (isChecked) {
                    fileButton.setBackgroundColor(0xFFFAD552); // Light brown
                } else {
                    fileButton.setBackgroundColor(0xFF615321); // Dark brown
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
                    Uri fileUri = FileProvider.getUriForFile(getActivity(), "com.example.myapp.file-provider", file);
                    fileUris.add(fileUri);
                }

                // Add List Uri to Intent
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);

                // Check if the device has an internet connection and an email app installed
                if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Send email with..."));
                } else {
                    // Handle the case where no messaging app is installed
                    Toast.makeText(getActivity(), "No application installed.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "An error has occurred while deleting the file.", Toast.LENGTH_SHORT).show();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
    private void deleteSelection(){
        DeletedTestDAO deletedTestDAO = new DeletedTestDAO(getActivity());
        File binDirectory = new File(getActivity().getFilesDir(), "bin_directory");
        if (!binDirectory.exists())
        {
            Log.d("Directory", "deleteSelection: bin_directory doesn't exist");
            binDirectory.mkdir();
        }

        for (File file : selectedFiles){
            File destFile = moveFile(file,binDirectory);
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            DeletedTest testBd = new DeletedTest(destFile.getAbsolutePath(),s.format(date));
            deletedTestDAO.addTest(testBd);
            allFiles.remove(file);
        }
        displayAllCSVFiles();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tests, container, false);
    }
}