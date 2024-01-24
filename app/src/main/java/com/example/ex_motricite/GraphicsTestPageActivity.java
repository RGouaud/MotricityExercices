package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

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

public class GraphicsTestPageActivity extends AppCompatActivity {

    private enum theme {
        X, Y,Y_X
    }
    private LineChart lineChart;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_test_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent myIntent = getIntent();
        String filePath = myIntent.getStringExtra("file_path");

        //Get the csv from the filepath
        File myCsv = new File(filePath);
        fileUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", myCsv);

        lineChart = findViewById(R.id.lineChart);

        //Setup the onClick
        ImageButton ibXYOverTime = findViewById(R.id.ib_Graph);

        ImageButton ibXOverTime = findViewById(R.id.ib_GraphXOverTime);

        ImageButton ibYOverTime = findViewById(R.id.ib_GraphYOverTime);



        List<CordsSample> data ;
        data = readCordsData();

        List<CordsSample> finalData = data;

        makeGraphic(finalData,theme.Y_X);


        ibXYOverTime.setOnClickListener(v -> makeGraphic(finalData,theme.Y_X));
        ibXOverTime.setOnClickListener(v -> makeGraphic(finalData,theme.X));
        ibYOverTime.setOnClickListener(v -> makeGraphic(finalData,theme.Y));


    }

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
                    entries1.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getC_X())));
                }
                dataSet1 = new LineDataSet(entries1, "X[t](pixels)");
                dataSet1.setColor(Color.RED);

                lineData = new LineData(dataSet1);

                lineChart.setData(lineData);

                lineChart.invalidate();
                break;
            case Y:

                for (CordsSample sample : data){

                    entries1.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getC_Y())));
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

                    entries1.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getC_X())));
                    entries2.add(new Entry(Float.parseFloat(sample.getTime())/1000,Float.parseFloat(sample.getC_Y())));
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

    private List<CordsSample> readCordsData(){
        List<CordsSample> cordSamples = new ArrayList<>();

        InputStream is;
        try {
            is = getContentResolver().openInputStream(fileUri);
        } catch (FileNotFoundException e) {
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
                sample.setC_X(tokens[1]);
                sample.setC_Y(tokens[2]);

                cordSamples.add(sample);


            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line" + line,e);
            e.printStackTrace();
        }

        return cordSamples;
    }


}