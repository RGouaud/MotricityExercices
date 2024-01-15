package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Environment;
import android.widget.ToggleButton;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListTestActivity extends AppCompatActivity {

    private static final int EXPORT_REQUEST_CODE = 1001; // Vous pouvez choisir n'importe quel code numérique ici
    private List<File> selectedFiles = new ArrayList<>();
    private Button buttonExport;
    private Button buttonDeselectAll;
    private Button buttonSelectAll;
    private Button buttonFilters;
    private LinearLayout LayoutListTest;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_test);

        buttonExport = findViewById(R.id.b_exportSelection);
        buttonDeselectAll = findViewById(R.id.b_deselectAll);
        buttonSelectAll = findViewById(R.id.b_selectAll);
        buttonFilters = findViewById(R.id.b_filters);
        LayoutListTest = findViewById(R.id.l_listTest);
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);

        createCSVFile("test1");
        createCSVFile("test2");
        createCSVFile("test3");
        createCSVFile("test4");
        createCSVFile("test5");
        createCSVFile("test6");
        createCSVFile("test7");
        createCSVFile("test8");
        createCSVFile("test9");
        createCSVFile("test10");
        createCSVFile("test11");
        createCSVFile("test12");
        createCSVFile("test13");
        createCSVFile("test14");
        createCSVFile("test15");
        // Afficher tous les fichiers CSV au démarrage
        displayAllCSVFiles();
        selectedFiles.clear();

        buttonSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAllFiles();
                Toast.makeText(ListTestActivity.this, "Tous les fichiers sélectionnés", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDeselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAllFiles();
                Toast.makeText(ListTestActivity.this, "Fichiers désélectionnés", Toast.LENGTH_SHORT).show();
            }
        });

        buttonFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListTestActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exportSelectedFiles();
                Toast.makeText(ListTestActivity.this, "Fichiers exportés", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayAllCSVFiles() {
        // Obtenez le répertoire du stockage interne
        File internalStorageDir = getFilesDir();

        // Liste tous les fichiers dans le répertoire
        File[] files = internalStorageDir.listFiles();

        // Ajoutez tous les fichiers CSV à la liste des fichiers sélectionnés
        selectedFiles.clear();

        if (files != null && files.length > 0) {
            for (File file : files) {
                // Vérifiez si le fichier est un fichier CSV
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    selectedFiles.add(file);
                }
            }
        }

        // Affichez les fichiers dans le layout
        displayFilesInLayout();
    }

    private void displayFilesInLayout() {
        // Effacez les éléments précédents dans le LinearLayout
        LayoutListTest.removeAllViews();

        // Ajoutez dynamiquement des éléments pour chaque fichier dans la liste
        for (File file : selectedFiles) {
            ToggleButton fileToggleButton = createFileToggleButtonn(file);
            LayoutListTest.addView(fileToggleButton);
        }
    }

    private ToggleButton createFileToggleButtonn(final File file) {
        ToggleButton fileToggleButton = new ToggleButton(this);
        fileToggleButton.setText(file.getName());
        fileToggleButton.setTextOn(file.getName());
        fileToggleButton.setTextOff(file.getName());
        fileToggleButton.setBackgroundColor(0xFF615321);
        // Définissez une action pour le clic sur le bouton du fichier
        fileToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileToggleButton.isChecked()) {
                    fileToggleButton.setBackgroundColor(0xFFFAD552);
                    selectedFiles.add(file);
                } else {
                    fileToggleButton.setBackgroundColor(0xFF615321);
                    selectedFiles.remove(file);
                }
                // Implémentez une action spécifique au clic sur le fichier si nécessaire
                // Vous pouvez ouvrir le fichier, afficher des détails, etc.
                Toast.makeText(ListTestActivity.this, "Clic sur le fichier : " + file.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return fileToggleButton;
    }

    private void selectAllFiles() {
        // Obtenez le répertoire du stockage interne
        File internalStorageDir = getFilesDir();

        // Liste tous les fichiers dans le répertoire
        File[] files = internalStorageDir.listFiles();

        // Videz la liste des fichiers sélectionnés
        selectedFiles.clear();

        if (files != null && files.length > 0) {
            for (File file : files) {
                // Vérifiez si le fichier est un fichier CSV
                if (file.isFile() && file.getName().endsWith(".csv")) {
                    // Ajoutez le fichier à la liste des fichiers sélectionnés
                    selectedFiles.add(file);
                }
            }
        }
        updateToggleButtonsState(true);
    }

    private void deselectAllFiles() {
        // Videz la liste des fichiers sélectionnés
        selectedFiles.clear();

        updateToggleButtonsState(false);
    }

    private void updateToggleButtonsState(boolean isChecked) {
        // Parcourez tous les ToggleButtons dans le layout et mettez à jour leur état
        for (int i = 0; i < LayoutListTest.getChildCount(); i++) {
            View view = LayoutListTest.getChildAt(i);
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                toggleButton.setChecked(isChecked);
                if (isChecked) {
                    toggleButton.setBackgroundColor(0xFFFAD552);
                } else {
                    toggleButton.setBackgroundColor(0xFF615321);
                }
            }
        }
    }

    private void exportSelectedFiles() {

        String settingsJson = sharedPreferences.getString("settings_json", null);

        if (settingsJson != null) {
            try {
                // Convertir le JSON en objet
                JSONObject jsonObject = new JSONObject(settingsJson);

                // Obtenir l'adresse e-mail du destinataire
                String email = jsonObject.getString("email");

                // Créer une intention pour envoyer plusieurs fichiers CSV
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("text/csv");

                // Ajouter les adresses e-mail, le sujet et le corps du message
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sujet de l'e-mail");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Corps du message de l'e-mail");

                // Créer une liste d'Uri pour les fichiers à envoyer
                ArrayList<Uri> fileUris = new ArrayList<>();
                for (File file : selectedFiles) {
                    // Convertir chaque fichier en Uri et l'ajouter à la liste
                    Uri fileUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", file);
                    fileUris.add(fileUri);
                }

                // Ajouter la liste d'Uri à l'intention
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, fileUris);

                // Vérifier si l'appareil a une connexion à Internet et une application de messagerie installée
                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(emailIntent, "Envoyer l'e-mail via..."));
                } else {
                    // Gérer le cas où aucune application de messagerie n'est installée
                    Toast.makeText(this, "Aucune application de messagerie n'est installée", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void createCSVFile(String fileName) {
        try {
            // Obtenez le répertoire du stockage interne
            File internalStorageDir = getFilesDir();

            // Créez un fichier dans le répertoire interne
            File myFile = new File(internalStorageDir, fileName + ".csv");

            // Vous pouvez maintenant utiliser le fichier pour écrire des données CSV si nécessaire
            // Exemple d'écriture dans le fichier :
            FileWriter writer = new FileWriter(myFile);
            // Écrivez vos données CSV ici
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteCSVFile(String fileName) {
        // Obtenez le répertoire du stockage interne
        File internalStorageDir = getFilesDir();

        // Créez un fichier dans le répertoire interne
        File myFile = new File(internalStorageDir, fileName + ".csv");

        // Supprimez le fichier lorsque nécessaire
        if (myFile.exists()) {
            myFile.delete();
        }
    }

}