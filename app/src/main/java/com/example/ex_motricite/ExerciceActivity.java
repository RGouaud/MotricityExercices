package com.example.ex_motricite;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciceActivity extends CameraActivity {

    TextView tv_x,tv_y;
    CameraBridgeViewBase cameraBridgeViewBase;
    Mat prev_gray,rgb,curr_gray, diff, result, output;
    boolean is_init;
    List<MatOfPoint> cnts;
    Chronometer chrono;
    Button b_start;
    boolean isRunning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice);
        tv_x = findViewById(R.id.tv_x);
        tv_y = findViewById(R.id.tv_y);
        is_init= false;

        isRunning = false;
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
                curr_gray = inputFrame.gray();




                //Convertir l'image en espace colorimetrique HSV
                Mat hsv = new Mat();
                Imgproc.cvtColor(inputFrame.rgba(), hsv, Imgproc.COLOR_BGR2HSV);

                // Définir la plage de couleur du laser dans l'espace HSV
                Scalar lowerLaserColor = new Scalar(160, 50 ,50);
                Scalar upperLaserColor = new Scalar(180, 255, 255);

                // Créer un masque pour la couleur du laser
                Mat mask = new Mat();
                Core.inRange(hsv, lowerLaserColor, upperLaserColor, mask);



                // Trouver le contour le plus grand (point lumineux du laser)
                /*double maxArea = 0;
                int maxAreaIdx = -1;
                for (int idx = 0; idx < contours.size(); idx++) {
                    double contourArea = Imgproc.contourArea(contours.get(idx));
                    if (contourArea > maxArea) {
                        maxArea = contourArea;
                        maxAreaIdx = idx;
                    }
                }*/

                // Dessiner un cercle autour du point lumineux du laser
                /*if (maxAreaIdx != -1) {
                    Scalar color = new Scalar(255, 0, 0); // Couleur bleue
                    Imgproc.drawContours(rgb, contours, maxAreaIdx, color, -1);

                    // Obtenez le rectangle englobant du contour
                    MatOfPoint2f approxCurve = new MatOfPoint2f();
                    Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(maxAreaIdx).toArray()), approxCurve, 0.02 * Imgproc.arcLength(new MatOfPoint2f(contours.get(maxAreaIdx).toArray()), true), true);
                    Point[] points = approxCurve.toArray();
                    Point center = new Point();
                    float[] radius = new float[1];
                    Imgproc.minEnclosingCircle(new MatOfPoint2f(points), center, radius);

                    // Dessiner un cercle autour du centre du laser
                    Imgproc.circle(rgb, center, (int) radius[0], color, -1);
                }*/



                Core.absdiff(curr_gray,prev_gray,diff);
                Imgproc.threshold(diff,diff,40,255,Imgproc.THRESH_BINARY);
                Imgproc.findContours(diff,cnts,new Mat(),Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);

                Imgproc.drawContours(rgb,cnts,-1,new Scalar(255,0,0), 4);


                for (MatOfPoint m: cnts){
                    Rect r=Imgproc.boundingRect(m);
                    Imgproc.rectangle(rgb,r,new Scalar(0,255,0),3);
                }


                /*int var = cnts.size();
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

                cnts.clear();


                prev_gray = curr_gray.clone();

                // Afficher le résultat ou le transmettre à d'autres opérations

                return rgb;
            }
        });

        if (OpenCVLoader.initDebug()) {
            cameraBridgeViewBase.enableView();
        }
        b_start = findViewById(R.id.b_Start);
        chrono = findViewById(R.id.idCMmeter);



        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning){
                    b_start.setText("Start Chronometer");
                    isRunning= false;
                    chrono.stop();
                }
                else{
                    b_start.setText("Stop Chronometer");
                    isRunning= true;
                    chrono.start();
                }
            }
        });


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
}