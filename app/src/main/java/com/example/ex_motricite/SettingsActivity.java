package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {

    private RadioButton rbEmail;
    private RadioButton rbServer;
    private EditText etEmail;
    private EditText etUrlServer;
    private EditText etIdServer;
    private EditText etPassword;
    private Button bConfirm;
    private Button bTestConnection;
    private SharedPreferences sharedPreferences;

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

        bConfirm = findViewById(R.id.b_confirmSave);
        bTestConnection = findViewById(R.id.b_testConnection);

        //get shared preferences
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        //load settings
        loadSettings();

        rbEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(rbEmail);
            }
        });

        rbServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioButtonClicked(rbServer);
            }
        });

        bTestConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: faire la requête HTTP pour tester la connexion
            }
        });

        bConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logique pour le bouton "Confirm"
                // Récupérez les données des champs EditText et RadioButton
                String email = etEmail.getText().toString();
                String urlServer = etUrlServer.getText().toString();
                String idServer = etIdServer.getText().toString();
                String password = etPassword.getText().toString();

                boolean isEmailSelected = rbEmail.isChecked();
                boolean isServerSelected = rbServer.isChecked();

                // Créez un objet JSON pour stocker les données
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
            }
        });
    }

    private void onRadioButtonClicked(RadioButton clickedRadioButton) {
        if (clickedRadioButton == rbEmail) {
            rbServer.setChecked(false);
        } else if (clickedRadioButton == rbServer) {
            rbEmail.setChecked(false);
        }
    }

    private void loadSettings() {
        // Charger les paramètres depuis SharedPreferences
        String settingsJson = sharedPreferences.getString("settings_json", null);

        if (settingsJson != null) {
            try {
                // Convertir le JSON en objet et mettre à jour les champs EditText et RadioButton
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

    private void saveSettings(JSONObject jsonData) {
        // Enregistrez les données dans SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("settings_json", jsonData.toString());
        editor.apply();
    }

}