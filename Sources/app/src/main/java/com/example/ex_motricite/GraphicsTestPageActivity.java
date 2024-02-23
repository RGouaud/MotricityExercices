package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GraphicsTestPageActivity} class represents an Android activity for displaying graphics based on CSV data.
 * It utilizes the MPAndroidChart library to create LineChart graphics representing X, Y, or Y_X themes.
 *
 * <p>
 * This activity reads CordsSample data from a CSV file, allowing users to visualize the X and Y coordinates over time.
 * The LineChart graphics support different themes (X, Y, Y_X) and provide interactive navigation options.
 * </p>
 *
 * <p>
 * The class dynamically generates LineChart graphics based on the provided data and theme, offering a visual representation
 * of pixel coordinates over time. Users can switch between X, Y, and combined X-Y themes using interactive buttons.
 * </p>
 *
 * <p>
 * Author: Segot
 * Version: 1.0
 * </p>
 */
public class GraphicsTestPageActivity extends AppCompatActivity {

    /**
     * Enumeration for different themes in the LineChart.
     */
    private enum theme {
        /**
         * X theme.
         */
        X,
        /**
         * Y theme.
         */
        Y,
        /**
         * Y x theme.
         */
        Y_X
    }
    /**
     * The LineChart object.
     */
    private LineChart lineChart;

    /**
     * The URI for the CSV file.
     */
    private Uri fileUri;

    /**
     * The ImageView for leaving the graphics page.
     */
    private ImageView ivLeave;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_test_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent myIntent = getIntent();
        String filePath = myIntent.getStringExtra("file_path");

        //Get the csv from the filepath
        assert filePath != null;
        File myCsv = new File(filePath);
        fileUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", myCsv);

        lineChart = findViewById(R.id.lineChart);

        //Setup the onClick
        ImageButton ibXYOverTime = findViewById(R.id.ib_Graph);

        ImageButton ibXOverTime = findViewById(R.id.ib_GraphXOverTime);

        ImageButton ibYOverTime = findViewById(R.id.ib_GraphYOverTime);

        ivLeave = findViewById(R.id.iv_leave_graphics);
        ivLeave.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("page", "tests");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        });


        List<CordsSample> data ;
        data = readCordsData();

        List<CordsSample> finalData = data;

        makeGraphic(finalData,theme.Y_X);


        ibXYOverTime.setOnClickListener(v -> makeGraphic(finalData,theme.Y_X));
        ibXOverTime.setOnClickListener(v -> makeGraphic(finalData,theme.X));
        ibYOverTime.setOnClickListener(v -> makeGraphic(finalData,theme.Y));


    }

    /**
     * Generate a LineChart graphic based on the provided data and theme.
     *
     * @param data  List of CordsSample data.
     * @param theme Theme for the LineChart (X, Y, Y_X).
     */
    private void makeGraphic(List<CordsSample> data, theme theme){
        Description description = new Description();

        description.setText("X(px) and Y(px) over Time(s)");
        description.setPosition(150f,15f);
        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(10f);
        xAxis.setAxisMaximum(Float.parseFloat(data.get(data.size()-1).getTime())/1000);
        xAxis.setAxisLineWidth(2f);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setLabelCount(10);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(70f);
        yAxis.setAxisMaximum(1700f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        List<Entry> entries1 = new ArrayList<>();
        LineDataSet dataSet1;
        LineData lineData;

        switch (theme){
            case X:
                for (CordsSample sample : data){
                    entries1.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getCX())));
                }
                dataSet1 = new LineDataSet(entries1, "X[t](pixels)");
                dataSet1.setColor(Color.RED);

                lineData = new LineData(dataSet1);

                lineChart.setData(lineData);

                lineChart.invalidate();
                break;
            case Y:

                for (CordsSample sample : data){

                    entries1.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getCY())));
                }
                dataSet1 = new LineDataSet(entries1, "Y[t](pixels)");
                dataSet1.setColor(Color.BLUE);

                lineData = new LineData(dataSet1);

                lineChart.setData(lineData);

                lineChart.invalidate();
                break;
            case Y_X:

                List<Entry> entries2 = new ArrayList<>();
                for (CordsSample sample : data){

                    entries1.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getCX())));
                    entries2.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getCY())));
                }

                dataSet1 = new LineDataSet(entries1, "X[t](pixels)");
                dataSet1.setColor(Color.BLUE);

                LineDataSet dataSet2 = new LineDataSet(entries2, "Y[t](pixels)");
                dataSet1.setColor(Color.RED);

                lineData = new LineData(dataSet1, dataSet2);

                lineChart.setData(lineData);

                lineChart.invalidate();
                break;
        }
    }

    /**
     * Read CordsSample data from a CSV file.
     *
     * @return List of CordsSample objects.
     */
    private List<CordsSample> readCordsData(){
        List<CordsSample> cordSamples = new ArrayList<>();

        InputStream is;
        try {
            is = getContentResolver().openInputStream(fileUri);
        } catch (FileNotFoundException e) {
            //Todo : Define a dedicated exception
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line = "";

        try {
            //step over header
            for (int i=0; i<10 ;i++) reader.readLine();

            while (((line = reader.readLine()) != null)) {
                //split by ";"
                String[] tokens = line.split(",");
                //read the data

                CordsSample sample = new CordsSample();
                sample.setTime(tokens[0]);
                sample.setCX(tokens[1]);
                sample.setCY(tokens[2]);

                cordSamples.add(sample);


            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line" + line,e);
            e.printStackTrace();
        }

        return cordSamples;
    }


}