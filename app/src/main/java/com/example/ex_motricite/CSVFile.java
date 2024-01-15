package com.example.ex_motricite;


import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CSVFile {

    static String DIRFILENAME = "LaZer";
    static File DIR = getPublicStorageDir(DIRFILENAME);

    Intent myIntent = getIntent();

    List<Integer> listX;
    List<Integer> listY;
    List<Integer> listNbFrame;
    private String exerciceType;
    private Integer exerciceTime;
    private Integer intervalTime;
    private int distance;


    //Path: /sdcard/LaZer
    static public boolean sauvegarde() {

        //Variables
        listX = myIntent.getIntegerArrayListExtra("listX");
        listY = myIntent.getIntegerArrayListExtra("listX");
        exerciceType = myIntent.getIntegerArrayListExtra("exerciceType");
        listNbFrame = myIntent.getIntegerArrayListExtra("listNbFrame");
        exerciceTime = myIntent.getIntegerArrayListExtra("exerciceTime");
        intervalTime = myIntent.getIntegerArrayListExtra("intervalTime");
        distance = myIntent.getIntegerArrayListExtra("distance");

        if (!isExternalStorageWritable()) {
            Environment.getExternalStoragePublicDirectory("").setWritable(true);
        }

        //Reccup de la date au format YYYY_MM_DD_HH_MM
        Calendar rightNow = Calendar.getInstance();
        String date = rightNow.get(Calendar.YEAR) + "_"
                + (rightNow.get(Calendar.MONTH) + 1) + "_"
                + rightNow.get(Calendar.DAY_OF_MONTH) + "_"
                + rightNow.get(Calendar.HOUR) + "_" +
                rightNow.get(Calendar.MINUTE);

        //nom du fichier suvegarder au format csv
        String patientName = "PatientTeste";
        String operatorName = "PatientTeste";
        String savefilename = patientName.replaceAll("\\s", "")
                + operatorName.replaceAll("\\s", "")
                + exerciceType.get_typeExercice()
                + date + ".csv";


        //recupération du dossier de sauvegarde


        //file represente le fichier de sauvegarde
        File file = new File(DIR.getAbsolutePath() + File.separator + savefilename);
        String comment = "TestComment";
        //Recapitulation des parametre de l'exercice réalisé (',' juste pour la mise en forme

        String recapExercice = "Recap of the exercice \n"
                + "Exercice = " + exerciceType + "\n"
                + "Patient Name = " + patientName + "\n"
                + "Operator Name = " + operatorName + "\n"
                + "Mark Distance= " + distance + "\n"
                + "Time(s) = " + exerciceTime + "\n"
                +"Comments : " + comment +"\n";
        recapExercice += (intervalTime > 0) ? "#Bip Interval = ,"
                + intervalTime + "\n\n" : "\n";

        //En-tête du tableau
        String entete = "time(s),x,y\n"; //Entete du fichier

        FileOutputStream outputStream;
        try {
            if (!file.exists()) { //retourne si le fichier existe
                if (!file.createNewFile()) { //cree le fichier si il n'existe pas
                    Log.e("ErrFile", "File was not Created");
                }
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(recapExercice.getBytes()); //ecris le recap de l'exo
            outputStream.write(entete.getBytes()); // affiche l'entete du tableau

            //enregistrement de la liste dans le fichier
            int nbFrame = listNbFrame.size();
            for (int i = 0; i < nbFrame; i++) {
                int frame = listNbFrame.get(i); // Frame number in the list
                time = transformFrameInTime(frame, exerciceTime, nbFrame);
                int coordX = listX.get(i); // Coord X number in the list
                int coordY = listY.get(i); // Coord Y number in the list
                String line = courrant.get_seconde() + "," + courrant.get_x() + "," + courrant.get_y() + "\n";
                //Ecrire Line
                outputStream.write(line.getBytes());//ecris la ligne courrante
            }
            outputStream.close(); //fermture du fichier
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    public static File getPublicStorageDir(String fileName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(""), fileName);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("ErrDir", "Directory not created");
            }
        }
        return file;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    static public File[] liste() {
        //RENVOIS un tableau contenant tout les fichiers du repertoire DIR

        return DIR.listFiles();
    }



    //Supprime les fichier contenue dans la liste
    static public boolean supprimerFichier(List<File> fichiersASupprimer) {
        for (File courrant : fichiersASupprimer) {
            Log.e("deleting", courrant.getName());
            if (!courrant.delete()) {
                return false; //stop si erreur dans la suppression
            }
        }
        return true;
    }

    static public double transformFrameInTime(int numFrame, int nbFrame, int exerciceTime){
        double time;
        double timeByFrame;

        timeByFrame = exerciceTime/nbFrame;

        time = timeByFrame*numFrame;

        return time;
    }

}