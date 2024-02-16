package com.example.ex_motricite;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.List;

/**
 * The {@code CSVFile} class represents a CSV file handler for saving exercise data.
 * It facilitates the creation and storage of CSV files containing exercise-related coordinates,
 * timestamps, and other parameters.
 *
 * <p>
 * The class receives various exercise-related data, including lists of X and Y coordinates, frame numbers,
 * exercise type, duration, interval time, distance, and relevant context information. It provides a method,
 * {@link #save()}, to save this exercise data to a CSV file. Additionally, a utility method,
 * {@link #transformFrameInTime(int, int, int)}, transforms frame numbers into corresponding timestamps.
 * </p>
 *
 * <p>
 * The generated CSV file includes a recap of exercise details at the beginning, such as exercise type,
 * patient and operator names, mark distance, exercise time, and comments. The actual data is then appended
 * to the file in a time-coordinate format.
 * </p>
 *
 * <p>
 * Author: Arricastres
 * Version: 1.0
 * </p>
 */
public class CSVFile {

    /**
     * The list of X coordinates.
     */
    private final List<Double> listX;
    /**
     * The list of Y coordinates.
     */
    private final List<Double> listY;
    /**
     * The list of frame numbers.
     */
    private final List<Integer> listNbFrame;
    /**
     * The type of exercise.
     */
    private final String exerciseType;
    /**
     * The total exercise time in seconds.
     */
    private final Integer exerciseTime;
    /**
     * The interval time in seconds.
     */
    private final Integer intervalTime;
    /**
     * The operator's name.
     */
    private final String operatorName;
    /**
     * The patient's name.
     */
    private final String patientName;
    /**
     * The mark distance.
     */
    private final int distance;
    /**
     * The Android application context.
     */
    private final Context context;

    /**
     * Constructor for CSVFile class.
     *
     * @param listX        List of X coordinates.
     * @param listY        List of Y coordinates.
     * @param listNbFrame  List of frame numbers.
     * @param exerciseType Type of exercise.
     * @param exerciseTime Total exercise time in seconds.
     * @param intervalTime Bip interval time in seconds.
     * @param distance     Mark distance.
     * @param context      Android application context.
     * @param patient      Patient's name.
     * @param operator     Operator's name.
     */
    CSVFile(List<Double> listX, List<Double> listY, List<Integer> listNbFrame, String exerciseType, int exerciseTime, int intervalTime, int distance, Context context, String patient, String operator){
        this.listX = listX;
        this.listY = listY;
        this.listNbFrame = listNbFrame;
        this.operatorName = operator;
        this.patientName = patient;
        this.exerciseType = exerciseType;
        this.exerciseTime = exerciseTime;
        this.intervalTime = intervalTime;
        this.distance = distance;
        this.context = context;
    }

    /**
     * Save the exercise data to a CSV file.
     */
//Path: /sdcard/LaZer
     public void save() {
        //retrieving the date as YYYY_MM_DD_HH_MM
        Calendar rightNow = Calendar.getInstance();
        String date = rightNow.get(Calendar.YEAR) + "_"
                + (rightNow.get(Calendar.MONTH) + 1) + "_"
                + rightNow.get(Calendar.DAY_OF_MONTH) + "_"
                + rightNow.get(Calendar.HOUR) + "_"
                + rightNow.get(Calendar.MINUTE) + "_"
                + rightNow.get(Calendar.SECOND);

        // name of the file save as csv
        String saveFileName = patientName.replaceAll("\\s", "")
                + "_"
                + operatorName.replaceAll("\\s", "")
                + "_"
                + this.exerciseType
                + "_"
                + date + ".csv";


        //Comment Test
        String comment = "TestComment";

        //Parameter of the exercise put on the top of the file

        String recapExercise = "Recap of the exercise \n"
                + "Exercise = " + this.exerciseType + "\n"
                + "Patient Name = " + patientName + "\n"
                + "Operator Name = " + operatorName + "\n"
                + "Mark Distance= " + this.distance + "\n"
                + "Time(s) = " + this.exerciseTime + "\n"
                +"Comments : " + comment +"\n";
        recapExercise += (this.intervalTime > 0) ? "#Bip Interval = "
                + this.intervalTime + "\n\n" : "\n";

        //table header
         String header = "time(ms),x,y\n";


         // Obtaining the internal file directory
         File repertoireIntern = context.getFilesDir();

         // Creating the file in the internal file directory
         File file = new File(repertoireIntern, saveFileName);


         try {
             if (!file.exists() && (!file.createNewFile())) { //create the file if it does not exist
                     Log.e("ErrFile", "File was not Created");

             }

             FileWriter writer = new FileWriter(file);
             writer.append(recapExercise);
             writer.append(header);


            //save coordinates and time in csv file
             int nbFrame = listNbFrame.size();
             for (int i = 0; i < nbFrame; i++) {
                 int frame = listNbFrame.get(i); // Frame number in the list
                 double time = transformFrameInTime(frame, exerciseTime, nbFrame);
                 double coordinateX = listX.get(i); // coordinate X number in the list
                 double coordinateY = listY.get(i); // coordinate Y number in the list
                 String line = time + "," + coordinateX + "," + coordinateY + "\n";
                 //Write Line
                 writer.append(line);
             }
             writer.flush();
             writer.close();


         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    /**
     * Transform frame number into corresponding time.
     *
     * @param numFrame     Frame number.
     * @param nbFrame      Total number of frames.
     * @param exerciseTime Total exercise time in seconds.
     * @return Time in seconds corresponding to the given frame.
     */
    public static double transformFrameInTime(int numFrame, int nbFrame, int exerciseTime){
        double time;
        double timeByFrame;

        timeByFrame = (double) exerciseTime /nbFrame;

        time = timeByFrame*numFrame;

        return time;
    }

}