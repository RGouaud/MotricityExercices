package com.example.ex_motricite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The {@code SettingsActivity} class represents an Android activity for configuring settings.
 *
 * <p>
 * This activity includes functionalities to display a list of patients and operators, handle user input,
 * and navigate to specific exercise activities based on user selections. It utilizes DAOs (Data Access Objects)
 * to retrieve patient and operator data from a database.
 * </p>
 *
 * <p>
 * The class supports both static and rhythm tests, with dynamic adjustments based on the selected exercise type.
 * It ensures proper input validation and provides a user-friendly interface for configuring exercise settings.
 * </p>
 *
 * <p>
 *     Author: EduardoXav
 *     Version: 1.0
 * </p>
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * The radio button for email settings.
     */
    private RadioButton rbEmail;
    /**
     * The radio button for server settings.
     */
    private RadioButton rbServer;
    /**
     * The EditText for the email address.
     */
    private EditText etEmail;
    /**
     * The EditText for the server URL.
     */
    private EditText etUrlServer;
    /**
     * The EditText for the server ID.
     */
    private EditText etIdServer;
    /**
     * The EditText for the server password.
     */
    private EditText etPassword;
    /**
     * The SharedPreferences object for storing settings.
     */
    private SharedPreferences sharedPreferences;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        rbEmail = findViewById(R.id.rb_email);
        rbServer = findViewById(R.id.rb_server);

        etEmail = findViewById(R.id.et_email);
        etUrlServer = findViewById(R.id.et_urlServer);
        etIdServer = findViewById(R.id.et_idServer);
        etPassword = findViewById(R.id.et_password);

        Button bConfirm = findViewById(R.id.b_confirmSave);
        Button bTestConnection = findViewById(R.id.b_testConnection);

        //get shared preferences
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        //load settings
        loadSettings();

        rbEmail.setOnClickListener(v -> onRadioButtonClicked(rbEmail));

        rbServer.setOnClickListener(v -> onRadioButtonClicked(rbServer));

        bTestConnection.setOnClickListener(view -> {
            // TODO: HTTP request to test the connection
        });

        bConfirm.setOnClickListener(view -> {
            // Logic for the "Confirm" button
            // Get data from EditText and RadioButton fields
            String email = etEmail.getText().toString();
            String urlServer = etUrlServer.getText().toString();
            String idServer = etIdServer.getText().toString();
            String password = etPassword.getText().toString();

            boolean isEmailSelected = rbEmail.isChecked();
            boolean isServerSelected = rbServer.isChecked();

            // Create a JSON object to store the data
            JSONObject jsonData = new JSONObject();

            try {
                jsonData.put("email", email);
                jsonData.put("urlServer", urlServer);
                jsonData.put("idServer", idServer);
                jsonData.put("password", password);
                jsonData.put("isEmailSelected", isEmailSelected);
                jsonData.put("isServerSelected", isServerSelected);

                saveSettings(jsonData);

                Toast.makeText(SettingsActivity.this, "Settings saved", Toast.LENGTH_SHORT).show();

                finish();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SettingsActivity.this, "Error saving settings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handles radio button clicks.
     *
     * @param clickedRadioButton The radio button that was clicked.
     */
    private void onRadioButtonClicked(RadioButton clickedRadioButton) {
        if (clickedRadioButton == rbEmail) {
            rbServer.setChecked(false);
        } else if (clickedRadioButton == rbServer) {
            rbEmail.setChecked(false);
        }
    }

    /**
     * Loads the saved settings from SharedPreferences and updates the UI.
     */
    private void loadSettings() {
        // Load settings from SharedPreferences
        String settingsJson = sharedPreferences.getString("settings_json", null);

        if (settingsJson != null) {
            try {
                // Convert JSON to object and update EditText and RadioButton fields
                JSONObject jsonObject = new JSONObject(settingsJson);
                etEmail.setText(jsonObject.getString("email"));
                etUrlServer.setText(jsonObject.getString("urlServer"));
                etIdServer.setText(jsonObject.getString("idServer"));
                etPassword.setText(jsonObject.getString("password"));
                rbEmail.setChecked(jsonObject.getBoolean("isEmailSelected"));
                rbServer.setChecked(jsonObject.getBoolean("isServerSelected"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the settings to SharedPreferences.
     *
     * @param jsonData The JSON object containing the settings data.
     */
    private void saveSettings(JSONObject jsonData) {
        // Save data in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("settings_json", jsonData.toString());
        editor.apply();
    }

}