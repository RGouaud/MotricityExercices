package com.example.ex_motricite;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicExerciceActivity extends CameraActivity {

    TextView tv_x,tv_y,countdown_text;
    CameraBridgeViewBase cameraBridgeViewBase;
    Mat prev_gray,rgb,curr_gray, diff, result, output, image_rgb, rgb_affich;
    ToneGenerator toneGenerator;
    boolean is_init;
    List<MatOfPoint> cnts;
    CountDownTimer countDownTimer;
    Button b_start;
    boolean isRunning;
    private long timerLeftInMilliseconds;

    private int DISTANCE;
    private int TIME;
    private int INTERVAL;


    List<Double> listX = new ArrayList<Double>();
    List<Double> listY = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Get the settings value

        Intent myIntent = getIntent();
        DISTANCE = Integer.parseInt(myIntent.getStringExtra("Distance"));
        TIME = Integer.parseInt(myIntent.getStringExtra("Time"));
        INTERVAL = Integer.parseInt(myIntent.getStringExtra("Interval"));

        tv_x = findViewById(R.id.tv_x);
        tv_y = findViewById(R.id.tv_y);
        countdown_text= findViewById (R.id.countdown_text);
        is_init= false;

        isRunning = false;
        timerLeftInMilliseconds = TIME *1000;

        updateTimer();
        getPermission();

        b_start = findViewById(R.id.b_Start);
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
                rgb_affich = new Mat();
                image_rgb = new Mat();
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

                //rgb = inputFrame.rgba();
                rgb_affich = inputFrame.rgba();


                if (isRunning){



                    //curr_gray = inputFrame.gray();
                    image_rgb = inputFrame.rgba();



                    //Convertir l'image en espace colorimetrique HSV
                    Imgproc.cvtColor(image_rgb, rgb, Imgproc.COLOR_RGB2HSV);



                    // SATURATION MAX
                    // Diviser les canaux H, S et V
                    List<Mat> hsvChannels = new ArrayList<>();
                    Core.split(rgb, hsvChannels);

                    // Ajuster la composante de saturation
                    Mat s = hsvChannels.get(1);
                    Core.multiply(s, new Scalar(1, 1, 1), s);
                    Core.normalize(s, s, 0, 255, Core.NORM_MINMAX);

                    // Fusionner les canaux H, S et V mis à jour
                    Core.merge(hsvChannels, rgb);

                    Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_HSV2RGB);
                    Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_RGB2HSV);
                    //Imgproc.cvtColor(rgb, rgb, Imgproc.COLOR_HSV2BGR);



                    //RED FILTER
                    // Définir la plage de teintes pour la composante
                    Scalar lowerRed = new Scalar(160, 100, 20);  // Valeurs HSV pour le
                    Scalar upperRed = new Scalar(179, 255, 255);

                    // Créer un masque pour la composante red
                    Mat mask = new Mat();
                    Core.inRange(rgb, lowerRed, upperRed, mask);

                    // Appliquer le masque à l'image d'origine
                    Mat redImage = new Mat();
                    Core.bitwise_and(rgb, rgb, redImage, mask);


                    Imgproc.cvtColor(redImage, curr_gray, Imgproc.COLOR_BGR2GRAY);




                    Core.absdiff(curr_gray,prev_gray,diff);
                    Imgproc.threshold(diff,diff,40,255,Imgproc.THRESH_BINARY);
                    Imgproc.findContours(diff,cnts,new Mat(),Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);

                    //Imgproc.cvtColor(rgb, rgb_affich, Imgproc.COLOR_HSV2BGR);

                    Imgproc.drawContours(rgb_affich,cnts,-1,new Scalar(0,255,0), 4);


                    /*for (MatOfPoint m: cnts){
                        Rect r=Imgproc.boundingRect(m);
                        Imgproc.rectangle(rgb_affich,r,new Scalar(0,255,0),3);
                    }*/

                    displayCoordinate(cnts);

                    cnts.clear();


                    prev_gray = curr_gray.clone();



                }

                int largeur = rgb_affich.cols();
                int hauteur = rgb_affich.rows();
                Log.d("Largeur", String.valueOf(largeur));
                //Log.d("Hauteur", String.valueOf(hauteur));

                Imgproc.circle(rgb_affich, new Point(400, 360), 17, new Scalar(0, 0, 0), 10);
                Imgproc.circle(rgb_affich, new Point(1040, 360), 17, new Scalar(0, 0, 0), 10);







                // Afficher le résultat ou le transmettre à d'autres opérations

                return rgb_affich;
            }
        });

        if (OpenCVLoader.initDebug()) {
            cameraBridgeViewBase.enableView();
        }



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
            startTimer(INTERVAL);
        }
    }
    public void startTimer(int intervalleBeep){
        toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 8000); // Volume 100
        countDownTimer = new CountDownTimer(timerLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerLeftInMilliseconds = millisUntilFinished;
                int secondes = Math.round(millisUntilFinished/1000);
                Log.d("milli", String.valueOf(secondes));
                if(secondes%intervalleBeep == 0){
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
                }
                updateTimer();
            }

            @Override
            public void onFinish() {
                isRunning = false;
                startActivity(new Intent(DynamicExerciceActivity.this,Pop.class));
            }
        }.start();

        b_start.setText("CANCEL");
        isRunning= true;
    }

    public void stopTimer(){
        countDownTimer.cancel();
        timerLeftInMilliseconds=TIME*1000;
        updateTimer();
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
                listX.add(point.x);
                listY.add(point.y);
                Log.d("ContourPoint", "X: " + point.x + ", Y: " + point.y);
            }
        }
    }
}