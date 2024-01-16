package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            }
        });

        buttonDeselectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deselectAllFiles();
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

                //exportSelectedFilesByMail();
                exportSelectedFilesByMail();
                Toast.makeText(ListTestActivity.this, "Exported file ", Toast.LENGTH_SHORT).show();
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
            Button fileButton = createFileButton(file);
            LayoutListTest.addView(fileButton);
        }
    }

    private Button createFileButton(final File file) {
        Button fileButton = new Button(this);
        fileButton.setText(file.getName());
        fileButton.setBackgroundColor(0xFF615321);

        // Clic court pour naviguer vers TestPageActivity
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTestPage(file);
            }
        });

        // Clic long pour la sélection multiple
        fileButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                handleLongClick(fileButton, file);
                return true;  // Consume the long click
            }
        });

        return fileButton;
    }

    private void navigateToTestPage(File file) {
        // Implémentez la logique de navigation vers TestPageActivity ici
        // Vous pouvez utiliser une intention (Intent) pour cela.
        Intent intent = new Intent(ListTestActivity.this, TestPageActivity.class);
        // Ajoutez des données supplémentaires à l'intention si nécessaire
        intent.putExtra("file_path", file.getAbsolutePath());
        startActivity(intent);
    }

    private void handleLongClick(Button fileButton, File file) {
        // Gérez la sélection multiple ici
        if (selectedFiles.contains(file)) {
            selectedFiles.remove(file);
            fileButton.setBackgroundColor(0xFF615321);
        } else {
            selectedFiles.add(file);
            fileButton.setBackgroundColor(0xFFFAD552);
        }
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
        updateButtonsState(true);
    }

    private void deselectAllFiles() {
        // Videz la liste des fichiers sélectionnés
        selectedFiles.clear();

        updateButtonsState(false);
    }

    private void updateButtonsState(boolean isChecked) {
        // Parcourez tous les ToggleButtons dans le layout et mettez à jour leur état
        for (int i = 0; i < LayoutListTest.getChildCount(); i++) {
            View view = LayoutListTest.getChildAt(i);
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

    private void exportSelectedFilesByMail() {

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
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CSV files");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "CSV files attached.");

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
                    startActivity(Intent.createChooser(emailIntent, "Send email with..."));
                } else {
                    // Gérer le cas où aucune application de messagerie n'est installée
                    Toast.makeText(this, "No application installed.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

   /* private void exportSelectedFiles() {
        // Vérifier si les fichiers sont sélectionnés
        if (selectedFiles != null && selectedFiles.size() > 0) {
            for (File file : selectedFiles) {
                try {
                    // Créer le répertoire de destination dans le dossier de téléchargement du téléphone
                    File destDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), file.getName());

                    // Copier le fichier dans le répertoire de destination
                    copyFile(file, destDir);

                    // Informer l'utilisateur que les fichiers ont été enregistrés
                    Toast.makeText(this, "Fichiers enregistrés dans le dossier de téléchargement", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Gérer l'erreur de copie de fichier
                }
            }
        }
    }*/

    private void copyFile(File source, File destination) throws IOException {
        // Créer le répertoire de destination s'il n'existe pas
        destination.getParentFile().mkdirs();

        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(destination)) {
            // Copier le contenu du fichier d'origine vers le fichier de destination
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
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