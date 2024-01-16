package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GraphicsTestPageActivity extends AppCompatActivity {

    private LineChart lineChart;

    private List<String> xValues;

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_test_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent myIntent = getIntent();
        String filePath = myIntent.getStringExtra("file_path");
        File monCsv = new File(filePath);
        fileUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", monCsv);
        lineChart = findViewById(R.id.lineChart);
        List<CoordsSample> datas = new ArrayList<>();
        datas = readCoordsData();

        Description description = new Description();
        Log.d("MyActivity", "onCreate: "+datas.get(3).getTemps());
        description.setText("X(px) and Y(px) over Time(s)");
        description.setPosition(150f,15f);
        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);


        XAxis xAxix = lineChart.getXAxis();
        xAxix.setAxisMinimum(0f);
        xAxix.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxix.setGranularity(10f);
        xAxix.setAxisMaximum(Float.parseFloat(datas.get(datas.size()-1).getTemps())/1000);
        xAxix.setAxisLineWidth(2f);
        xAxix.setAxisLineColor(Color.BLACK);
        xAxix.setLabelCount(10);

        YAxis yAxix = lineChart.getAxisLeft();
        yAxix.setAxisMinimum(70f);
        yAxix.setAxisMaximum(1700f);
        yAxix.setAxisLineWidth(2f);
        yAxix.setAxisLineColor(Color.BLACK);
        yAxix.setLabelCount(10);

        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        for (CoordsSample sample : datas){
            Log.d("MyActivity", "onCreate: "+sample.getTemps());
            entries1.add(new Entry(Float.parseFloat(sample.getTemps())/1000,Float.parseFloat(sample.getC_X())));
            entries2.add(new Entry(Float.parseFloat(sample.getTemps())/1000,Float.parseFloat(sample.getC_Y())));
        }



        LineDataSet dataSet1 = new LineDataSet(entries1, "X[t](pixels)");
        dataSet1.setColor(Color.BLUE);

        LineDataSet dataSet2 = new LineDataSet(entries2, "Y[t](pixels)");
        dataSet1.setColor(Color.RED);

        LineData lineData = new LineData(dataSet1, dataSet2);

        lineChart.setData(lineData);

        lineChart.invalidate();



    }
    private List<CoordsSample> readCoordsData(){
        List<CoordsSample> coordSamples = new ArrayList<>();

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(fileUri);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";

        try {
            //step over header
            for (int i=0; i<10 ;i++) reader.readLine();

            while (((line = reader.readLine()) != null)) {
                Log.d("MyActivity", "Line :" + line);
                //split by ";"
                String[] tokens = line.split(",");
                //read the data

                CoordsSample sample = new CoordsSample();
                sample.setTemps(tokens[0]);
                sample.setC_X(tokens[1]);
                sample.setC_Y(tokens[2]);

                coordSamples.add(sample);

                Log.d("MyActivity", "Just created" + sample);

            }
        } catch (IOException e) {
            Log.wtf("MyActivity", "Error reading data file on line" + line,e);
            e.printStackTrace();
        }

        return coordSamples;
    }


}