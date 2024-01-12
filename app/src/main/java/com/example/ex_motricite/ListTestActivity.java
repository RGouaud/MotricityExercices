package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Environment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_test);

        buttonExport = findViewById(R.id.b_exportSelection);
        buttonDeselectAll = findViewById(R.id.b_deselectAll);
        buttonSelectAll = findViewById(R.id.b_selectAll);
        buttonFilters = findViewById(R.id.b_filters);
        LayoutListTest = findViewById(R.id.l_listTest);

        // Afficher tous les fichiers CSV au démarrage
        displayAllCSVFiles();

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

                // TODO : Exporter les données sélectionnées
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
            Button fileButton = createFileButton(file.getName());
            LayoutListTest.addView(fileButton);
        }
    }

    private Button createFileButton(final String fileName) {
        Button fileButton = new Button(this);
        fileButton.setText(fileName);

        // Définissez une action pour le clic sur le bouton du fichier
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implémentez une action spécifique au clic sur le fichier si nécessaire
                // Vous pouvez ouvrir le fichier, afficher des détails, etc.
                Toast.makeText(ListTestActivity.this, "Clic sur le fichier : " + fileName, Toast.LENGTH_SHORT).show();
            }
        });

        return fileButton;
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
    }

    private void deselectAllFiles() {
        // Videz la liste des fichiers sélectionnés
        selectedFiles.clear();
    }

    private void exportSelectedFiles() {
        if (!selectedFiles.isEmpty()) {
            for (File file : selectedFiles) {
                // Créez une intention pour permettre à l'utilisateur de choisir l'emplacement de téléchargement
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/csv");
                intent.putExtra(Intent.EXTRA_TITLE, file.getName());

                // Lancez l'activité avec cette intention
                startActivityForResult(intent, EXPORT_REQUEST_CODE);
            }
        } else {
            Toast.makeText(this, "Aucun fichier sélectionné", Toast.LENGTH_SHORT).show();
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