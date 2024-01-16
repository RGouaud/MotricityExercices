package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class GraphicsTestPageActivity extends AppCompatActivity {

    private LineChart lineChart;

    private List<String> xValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_test_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        lineChart = findViewById(R.id.lineChart);
        List<CoordsSample> datas = new ArrayList<>();
        //datas = readCoordsData();

    }
    /*private List<CoordsSample> readCoordsData(){
        List<CoordsSample> coordSamples = new ArrayList<>();

        InputStream is =;
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";

        try {
            //stop over header
            reader.readLine();

            while (((line = reader.readLine()) != null)) {
                Log.d("MyActivity", "Line :" + line);
                //split by ";"
                String[] tokens = line.split(";");
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
    }*/


}