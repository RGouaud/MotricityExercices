package com.example.ex_motricite;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.MatOfPoint;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciceActivity extends CameraActivity {

    TextView tv_x,tv_y,countdown_text;
    CameraBridgeViewBase cameraBridgeViewBase;
    Mat prev_gray,rgb,curr_gray, diff, result, output;
    boolean is_init;
    List<MatOfPoint> cnts;
    CountDownTimer countDownTimer;
    Button b_start;
    boolean isRunning;
    private long timerLeftInMilliseconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        tv_x = findViewById(R.id.tv_x);
        tv_y = findViewById(R.id.tv_y);
        countdown_text= findViewById (R.id.countdown_text);
        is_init= false;

        isRunning = false;
        timerLeftInMilliseconds = 10000;
        getPermission();

        cameraBridgeViewBase = findViewById(R.id.java_camera_view);

        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                curr_gray = new Mat();
                rgb = new Mat();
                result = new Mat();
                prev_gray = new Mat();
                diff = new Mat();
                output = new Mat();
                cnts = new ArrayList<>();
            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                if(!is_init){
                    prev_gray = inputFrame.gray();
                    is_init=true;
                    return prev_gray;
                }

                rgb = inputFrame.rgba();


                if (isRunning){


                    curr_gray = inputFrame.gray();




                    //Convertir l'image en espace colorimetrique HSV
                    Mat hsv = new Mat();
                    Imgproc.cvtColor(inputFrame.rgba(), hsv, Imgproc.COLOR_BGR2HSV);




                    // Convertir l'image en espace de couleur HSV
                    /*Imgproc.cvtColor(inputFrame.rgba(), hsv, Imgproc.COLOR_RGB2HSV);

                    // Fractionner les canaux HSV
                    Mat[] channels = new Mat[3];
                    Core.split(hsv, channels);

                    // Maximiser la valeur de saturation (le canal 1)
                    Core.setNumThreads(8); // Optimisation du traitement parallèle
                    Core.add(channels[1], new Scalar(255 - Double.MIN_VALUE), channels[1]);

                    // Fusionner les canaux HSV pour créer l'image modifiée
                    Core.merge(Arrays.asList(channels), hsv);

                    // Convertir l'image de nouveau en espace de couleur RGB
                    Imgproc.cvtColor(hsv, hsv, Imgproc.COLOR_HSV2RGB);*/




                    Core.absdiff(curr_gray,prev_gray,diff);
                    Imgproc.threshold(diff,diff,40,255,Imgproc.THRESH_BINARY);
                    Imgproc.findContours(diff,cnts,new Mat(),Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);

                    Imgproc.drawContours(rgb,cnts,-1,new Scalar(255,0,0), 4);


                    for (MatOfPoint m: cnts){
                        Rect r=Imgproc.boundingRect(m);
                        Imgproc.rectangle(rgb,r,new Scalar(0,255,0),3);
                        // Accédez aux points
                        //Point[] points = m.toArray();

                        // Affichez les coordonnées
                    /*for (Point p : points) {

                    }*/

                    }




                    /*int var = cnts.size();
                    String varStr = String.valueOf(var);

                    tv_x.setText(varStr);
                    cnts.get(var);
                    MatOfPoint dernierMOP = cnts.get(1);
                    Point[] pointsArray = dernierMOP.toArray();
                    Point point = pointsArray[-1];
                    double x = point.x;
                    double y = point.y;
                    String xStr = String.valueOf(x);
                    String yStr = String.valueOf(y);

                    tv_x.setText("x : " + xStr);
                    tv_y.setText("y : " + yStr);*/

                    displayCoordinate(cnts);

                    cnts.clear();


                    prev_gray = curr_gray.clone();
                }


                // Afficher le résultat ou le transmettre à d'autres opérations

                return rgb;
            }
        });

        if (OpenCVLoader.initDebug()) {
            cameraBridgeViewBase.enableView();
        }
        b_start = findViewById(R.id.b_Start);



        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startstop();
            }
        });


    }
    public void startstop(){
        if (isRunning){
            stopTimer();
        }
        else {
            startTimer();
        }
    }
    public void startTimer(){
        countDownTimer = new CountDownTimer(timerLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(ExerciceActivity.this,Pop.class));
            }
        }.start();

        b_start.setText("CANCEL");
        isRunning= true;
    }
    public void stopTimer(){
        countDownTimer.cancel();
        timerLeftInMilliseconds=600000;
        isRunning= false;
        b_start.setText("START");
    }

    private void updateTimer(){
        int minutes = (int) timerLeftInMilliseconds / 60000;
        int seconds = (int) timerLeftInMilliseconds % 60000 / 1000;

        String TimeLeftText;

        TimeLeftText = ""+ minutes;
        TimeLeftText += ":";
        if (seconds <10) TimeLeftText += "0";
        TimeLeftText+=seconds;

        countdown_text.setText(TimeLeftText);
    }
    @Override
    protected List<?extends CameraBridgeViewBase> getCameraViewList(){
        return Collections.singletonList(cameraBridgeViewBase);
    }

    void getPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 101);
        }
    }

    void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantresults){
        super.onRequestPermissionsResult(requestCode,permissions,grantresults);
        if(grantresults.length>0 && grantresults[0]!=PackageManager.PERMISSION_GRANTED){
            getPermission();
        }

    }



    void displayCoordinate(List<MatOfPoint> listeMatOfPoints){
        for (MatOfPoint contour : listeMatOfPoints) {
            // Accéder aux points du contour
            Point[] pointsArray = contour.toArray();

            // Afficher les coordonnées des points dans la console (logcat)
            for (Point point : pointsArray) {
                Log.d("ContourPoint", "X: " + point.x + ", Y: " + point.y);
            }
        }
    }
}