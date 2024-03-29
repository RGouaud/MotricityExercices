package com.example.ex_motricite;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    /**
     * The radio button for email settings.
     */
    private RadioButton rbEmail;


    /**
     * The EditText for the email address.
     */
    private EditText etEmail;
    /**
     * The SharedPreferences object for storing settings.
     */
    private SharedPreferences sharedPreferences;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        rbEmail = getActivity().findViewById(R.id.rb_email);

        etEmail = getActivity().findViewById(R.id.et_email);

        Button bConfirm = getActivity().findViewById(R.id.b_confirmSave);

        //get shared preferences
        sharedPreferences = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);

        //load settings
        loadSettings();



        bConfirm.setOnClickListener(v -> {
            // Logic for the "Confirm" button
            // Get data from EditText and RadioButton fields
            String email = etEmail.getText().toString();

            boolean isEmailSelected = rbEmail.isChecked();

            // Create a JSON object to store the data
            JSONObject jsonData = new JSONObject();

            try {
                jsonData.put("email", email);
                jsonData.put("isEmailSelected", isEmailSelected);

                saveSettings(jsonData);

                Toast.makeText(getActivity(), "Settings saved", Toast.LENGTH_SHORT).show();


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error saving settings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handles radio button clicks.
     *
     * @param clickedRadioButton The radio button that was clicked.
     */


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
                rbEmail.setChecked(jsonObject.getBoolean("isEmailSelected"));

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}