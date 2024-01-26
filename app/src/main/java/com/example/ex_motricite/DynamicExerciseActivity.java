package com.example.ex_motricite;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
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
 * The {@code DynamicExerciseActivity} class represents an Android activity for performing dynamic exercises using the device's camera.
 * It captures and analyzes motion through the camera feed, detecting laser points and recording relevant exercise data.
 *
 * <p>
 * This activity allows users to set parameters such as distance, time, and interval for the exercise.
 * It displays real-time coordinates of laser points and provides feedback through beeping at specified intervals.
 * The exercise can be started, stopped, and canceled, and upon completion, the recorded data is saved in a CSV file.
 * </p>
 *
 * <p>
 * The class extends {@link CameraActivity} from the OpenCV library and leverages camera functionality to process video frames.
 * Laser points are detected through color filtering, and their coordinates are stored for further analysis.
 * The exercise timer is implemented with audible cues at specified intervals.
 * </p>
 *
 * <p>
 * Author: Segot, Arricastres
 * Version: 1.0
 * </p>
 */
public class DynamicExerciseActivity extends CameraActivity {

    TextView tvX;
    TextView tvY;
    TextView countdownText;
    CameraBridgeViewBase cameraBridgeViewBase;
    Mat prevGray;
    Mat rgb;
    Mat currGray;
    Mat diff;
    Mat result;
    Mat output;
    Mat imageRgb;
    Mat rgbDisplay;
    ToneGenerator toneGenerator;
    boolean isInit;
    List<MatOfPoint> outlines; // list of outlines
    CountDownTimer countDownTimer;
    Button bStart;
    boolean isRunning;
    private long timerLeftInMilliseconds;

    private int nbFrame;
    private int distance;
    private int time;
    private int interval;

    private String patient;
    private String operator;

    List<Double> listX = new ArrayList<>();
    List<Double> listY = new ArrayList<>();
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
        interval = Integer.parseInt(Objects.requireNonNull(myIntent.getStringExtra("Interval")));
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

        bStart = findViewById(R.id.b_Start);
        cameraBridgeViewBase = findViewById(R.id.java_camera_view);

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
                // Empty for the moment
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


                    //Convert image in HSV
                    Imgproc.cvtColor(imageRgb, rgb, Imgproc.COLOR_RGB2HSV);



                    // SATURATION MAX
                    // Divide H, S and V
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
                    Scalar lowerRed = new Scalar(160, 100, 20);  // HSV values
                    Scalar upperRed = new Scalar(179, 255, 255);

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

                //Display circles
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
     * Start or stop the exercise timer.
     */
    public void startStop(){
        if (isRunning){
            stopTimer();
        }
        else {
            startTimer(interval);
        }
    }

    /**
     * Starts the exercise timer with interval beeping.
     *
     * @param intervalBeep Interval in seconds for beeping.
     */
    @SuppressLint("SetTextI18n")
    public void startTimer(int intervalBeep){
        toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 8000); // Volume 100
        countDownTimer = new CountDownTimer(timerLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerLeftInMilliseconds = millisUntilFinished;
                int seconds = Math.round((float) millisUntilFinished /1000);
                Log.d("milli", String.valueOf(seconds));
                if(seconds%intervalBeep == 0){
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
                }
                updateTimer();
            }

            @Override
            public void onFinish() {
                isRunning = false;

                startActivity(new Intent(DynamicExerciseActivity.this, PopActivity.class));
                createCSV();
            }
        }.start();

        bStart.setText("CANCEL");
        isRunning= true;
    }

    /**
     * Stops the exercise timer.
     */
    @SuppressLint("SetTextI18n")
    public void stopTimer(){
        countDownTimer.cancel();
        timerLeftInMilliseconds= time * 1000L;
        updateTimer();
        isRunning= false;
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
    @Override
    protected List<?extends CameraBridgeViewBase> getCameraViewList(){
        return Collections.singletonList(cameraBridgeViewBase);
    }

    /**
     * Retrieves camera permissions.
     */
    void getPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 101);
        }
    }

    /**
     * Stores laser coordinates in lists.
     *
     * @param listOfRect List of rectangles representing laser points.
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
     * Stores the frame number in the list.
     *
     * @param nbFrame Frame number.
     */
    void stockTime(int nbFrame){
        listNbFrame.add(nbFrame);
    }

    /**
     * Creates and saves a CSV file with exercise data.
     */
    void createCSV(){
        String exerciseType = "Dynamic";
        //CSVFile creation
        Context context = getApplicationContext();
        CSVFile csvFile = new CSVFile(listX, listY, listNbFrame, exerciseType, time, interval, distance, context, patient, operator);
        csvFile.save();
    }
}