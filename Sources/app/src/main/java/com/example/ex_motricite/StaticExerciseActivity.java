package com.example.ex_motricite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The {@code StaticExerciseActivity} class represents an Android activity for performing static exercises.
 *
 * <p>
 * This activity includes functionalities to display a camera view, handle user input, and navigate to
 * a pop-up window and the home page. It utilizes OpenCV to process camera frames and detect laser coordinates.
 * </p>
 *
 * <p>
 * The class supports static exercises, with dynamic adjustments based on the selected exercise type.
 * It ensures proper input validation and provides a user-friendly interface for performing static exercises.
 * </p>
 *
 * <p>
 * Author: Arricastres, Segot
 * Version: 1.0
 * </p>
 */
public class StaticExerciseActivity extends CameraActivity {

    /**
     * The Tv x.
     */
    TextView tvX;
    /**
     * The Tv y.
     */
    TextView tvY;
    /**
     * The countdown timer display.
     */
    TextView countdownText;
    /**
     * The camera view.
     */
    CameraBridgeViewBase cameraBridgeViewBase;
    /**
     * The previous frame.
     */
    Mat prevGray;
    /**
     * The current frame in RGB.
     */
    Mat rgb;
    /**
     * The current frame in gray.
     */
    Mat currGray;
    /**
     * The difference between the current frame and the previous frame.
     */
    Mat diff;
    /**
     * The result of the difference between the current frame and the previous frame.
     */
    Mat result;
    /**
     * The output of the difference between the current frame and the previous frame.
     */
    Mat output;
    /**
     * The current frame in RGB.
     */
    Mat imageRgb;
    /**
     * The current frame in RGB.
     */
    Mat rgbDisplay;
    /**
     * The state of the initialization.
     */
    boolean isInit;
    /**
     * The list of laser outlines.
     */
    List<MatOfPoint> outlines;
    /**
     * The countdown timer.
     */
    CountDownTimer countDownTimer;
    /**
     * The start button.
     */
    Button bStart;
    /**
     * The state of the countdown timer.
     */
    boolean isRunning;
    /**
     * The time left in milliseconds.
     */
    private long timerLeftInMilliseconds;
    /**
     * The number of frames.
     */
    private int nbFrame;
    /**
     * The distance of the exercise.
     */
    private int distance;
    /**
     * The duration of the exercise.
     */
    private int time;
    /**
     * The name of the patient.
     */
    private String patient;
    /**
     * The name of the operator.
     */
    private String operator;
    /**
     * The list of X coordinates.
     */
    List<Double> listX = new ArrayList<>();
    /**
     * The list of Y coordinates.
     */
    List<Double> listY = new ArrayList<>();
    /**
     * The list of frame numbers.
     */
    List<Integer> listNbFrame = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Get the settings value

        Intent myIntent = getIntent();
        distance = Integer.parseInt(Objects.requireNonNull(myIntent.getStringExtra("Distance")));
        time = Integer.parseInt(Objects.requireNonNull(myIntent.getStringExtra("Time")));
        patient = myIntent.getStringExtra("Patient");
        operator = myIntent.getStringExtra("Operator");

        tvX = findViewById(R.id.tv_x);
        tvY = findViewById(R.id.tv_y);
        countdownText = findViewById (R.id.countdown_text);
        isInit = false;
        nbFrame = 0;

        isRunning = false;
        timerLeftInMilliseconds = time * 1000L;

        updateTimer();
        getPermission();

        cameraBridgeViewBase = findViewById(R.id.java_camera_view);

        bStart = findViewById(R.id.b_Start);
        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                currGray = new Mat();
                rgb = new Mat();
                result = new Mat();
                prevGray = new Mat();
                diff = new Mat();
                output = new Mat();
                rgbDisplay = new Mat();
                imageRgb = new Mat();
                outlines = new ArrayList<>();
            }

            @Override
            public void onCameraViewStopped() {
                // empty for the moment
            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                if(!isInit){
                    prevGray = inputFrame.gray();
                    isInit =true;
                    return prevGray;
                }

                rgbDisplay = inputFrame.rgba();


                if (isRunning){


                    imageRgb = inputFrame.rgba();


                    //Convert image as HSV
                    Imgproc.cvtColor(imageRgb, rgb, Imgproc.COLOR_RGB2HSV);



                    // SATURATION MAX
                    // Divide channels H, S et V
                    List<Mat> hsvChannels = new ArrayList<>();
                    Core.split(rgb, hsvChannels);

                    // increase saturation value
                    Mat s = hsvChannels.get(1);
                    Core.multiply(s, new Scalar(1, 1, 1), s);
                    Core.normalize(s, s, 0, 255, Core.NORM_MINMAX);

                    // merge updated H, S et V channels
                    Core.merge(hsvChannels, rgb);

                    Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_HSV2RGB);
                    Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_RGB2HSV);



                    //RED FILTER
                    // Define Value of Red Filter (HSV)
                    Scalar lowerRed = new Scalar(160, 100, 20);  // HSV Value low
                    Scalar upperRed = new Scalar(179, 255, 255); // HSV Value up

                    // Create red mask
                    Mat mask = new Mat();
                    Core.inRange(rgb, lowerRed, upperRed, mask);

                    // merge red mask with initial image
                    Mat redImage = new Mat();
                    Core.bitwise_and(rgb, rgb, redImage, mask);


                    Imgproc.cvtColor(redImage, currGray, Imgproc.COLOR_BGR2GRAY);



                    Core.absdiff(currGray, prevGray,diff);
                    Imgproc.threshold(diff,diff,40,255,Imgproc.THRESH_BINARY);
                    Imgproc.findContours(diff, outlines,new Mat(),Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);


                    Imgproc.drawContours(rgbDisplay, outlines,-1,new Scalar(0,255,0), 4);


                    List<Rect> listOfRect = new ArrayList<>();

                    // Display outlines
                    for (MatOfPoint m: outlines){
                        Rect r=Imgproc.boundingRect(m);
                        listOfRect.add(r);
                        Imgproc.rectangle(rgbDisplay,r,new Scalar(0,255,0),3);
                    }


                    // Save laser's coordinates
                    if(nbFrame != 0){
                        stockTime(nbFrame);
                        stockCoordinate(listOfRect);
                    }

                    nbFrame += 1;

                    outlines.clear();


                    prevGray = currGray.clone();
                }

                Imgproc.circle(rgbDisplay, new Point(400, 360), 17, new Scalar(0, 0, 0), 10);
                Imgproc.circle(rgbDisplay, new Point(1040, 360), 17, new Scalar(0, 0, 0), 10);


                return rgbDisplay;
            }
        });

        if (OpenCVLoader.initDebug()) {
            cameraBridgeViewBase.enableView();
        }
        bStart = findViewById(R.id.b_Start);



        bStart.setOnClickListener(v -> startStop());


    }

    /**
     * Starts or stops the exercise based on the current state.
     */
    public void startStop(){
        if (isRunning){
            stopTimer();
        }
        else {
            startTimer();
        }
    }

    /**
     * Starts the countdown timer for the exercise.
     */
    @SuppressLint("SetTextI18n")
    public void startTimer(){
        countDownTimer = new CountDownTimer(timerLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                isRunning = false;

                startActivity(new Intent(StaticExerciseActivity.this, PopActivity.class));
                createCSV();
            }
        }.start();

        bStart.setText("CANCEL");
        isRunning= true;
    }

    /**
     * Stops the countdown timer for the exercise.
     */
    @SuppressLint("SetTextI18n")
    public void stopTimer(){
        countDownTimer.cancel();
        timerLeftInMilliseconds= time * 1000L;
        isRunning= false;
        updateTimer();
        bStart.setText("START");
    }

    /**
     * Updates the countdown timer display.
     */
    private void updateTimer(){
        int minutes = (int) timerLeftInMilliseconds / 60000;
        int seconds = (int) timerLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = String.valueOf(minutes);
        timeLeftText += ":";
        if (seconds <10) timeLeftText += "0";
        timeLeftText+=seconds;

        countdownText.setText(timeLeftText);
    }

    /**
     * Returns the list of camera views.
     *
     * @return The list of camera views.
     */
    @Override
    protected List<?extends CameraBridgeViewBase> getCameraViewList(){
        return Collections.singletonList(cameraBridgeViewBase);
    }

    /**
     * Requests camera permission if not granted.
     */
    void getPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 101);
        }
    }

    /**
     * Processes and stores the coordinates of the laser points detected in the current frame.
     *
     * @param listOfRect List of rectangles representing detected laser points.
     */
    void stockCoordinate(List<Rect> listOfRect){
        Rect biggestRect = new Rect();
        double biggestArea = 0;
        double area;

        //Detection of the biggest Rectangle
        for (Rect rect : listOfRect) {
            area = (double)rect.height*rect.width;

            if (area > biggestArea){
                biggestArea = area;
                biggestRect = rect;
            }
        }

        // Middle coordinate of the biggest Rectangle
        double centerX = biggestRect.x + biggestRect.width / 2.0;
        double centerY = biggestRect.y + biggestRect.height / 2.0;

        // add middles coordinates in list
        listX.add(centerX);
        listY.add(centerY);
    }

    /**
     * Stores the current frame number in the list.
     *
     * @param nbFrame The current frame number.
     */
    void stockTime(int nbFrame){
        listNbFrame.add(nbFrame);
    }

    /**
     * Creates a CSV file with the stored data.
     */
    void createCSV(){
        Log.d("debut", "debut");
        String exerciseType = "Static";
        int interval = 0;
        //CSVFile creation
        Context context = getApplicationContext();
        CSVFile csvFile = new CSVFile(listX, listY, listNbFrame, exerciseType, time, interval, distance, context, patient, operator);
        csvFile.save();

    }


}