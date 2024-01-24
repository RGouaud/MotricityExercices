package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TestPageActivity extends AppCompatActivity {

    private TextView tvDuration;
    private TextView tvOperator;
    private TextView tvPatient;
    private TextView tvType;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent myIntent = getIntent();
        String filePath = myIntent.getStringExtra("file_path");

        tvDuration = findViewById(R.id.textViewTestTime);
        tvOperator = findViewById(R.id.textViewOperator);
        tvPatient = findViewById(R.id.textViewPatient);
        tvType = findViewById(R.id.textViewTestType);

        Button buttonViewGraphs = findViewById(R.id.b_viewGraphics);
        fillTextView(filePath);

        buttonViewGraphs.setOnClickListener(v -> {
            Intent intent = new Intent(TestPageActivity.this, GraphicsTestPageActivity.class);
            intent.putExtra("file_path", filePath);
            startActivity(intent);
        });

    }

    private void fillTextView(String filePath){
        File monCsv = new File(filePath);
        Uri fileUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", monCsv);
        ArrayList<String> lineList = new ArrayList<>();

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

        // try closing the BufferedReader
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the information of the CSV

        try {

            //step over the header
            reader.readLine();

            //Type of exercise
            lineList.add(reader.readLine());

            //Patient
            lineList.add(reader.readLine());

            //operator
            lineList.add(reader.readLine());

            //Step over the Distance
            reader.readLine();

            //Duration
            lineList.add(reader.readLine());


        } catch (IOException e) {
            //Todo : Define a dedicated exception
            throw new RuntimeException(e);
        }


        ArrayList<String> newList = new ArrayList<>();
        for (String line:lineList) {
            StringTokenizer tokenizer = new StringTokenizer(line, "=");
            while (tokenizer.hasMoreElements()) {
                //skip the first part
                tokenizer.nextElement();
                newList.add(tokenizer.nextElement().toString());
            }
        }

        //Set the textView
        tvType.setText(newList.get(0));
        tvPatient.setText(newList.get(1));
        tvOperator.setText(newList.get(2));

        String timeInSeconds = newList.get(3)+"(s)";
        tvDuration.setText(timeInSeconds);
    }
}