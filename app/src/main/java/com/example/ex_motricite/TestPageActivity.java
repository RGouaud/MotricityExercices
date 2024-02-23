package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * The {@code TestPageActivity} class represents an Android activity for displaying test details.
 *
 * <p>
 * This activity includes functionalities to display test details and navigate to the graphics page.
 * </p>
 *
 * <p>
 * Author: EduardoXav
 * Version: 1.0
 * </p>
 */
public class TestPageActivity extends AppCompatActivity {

    /**
     * The TextView for the duration of the test.
     */
    private TextView tvDuration;
    /**
     * The TextView for the operator of the test.
     */
    private TextView tvOperator;
    /**
     * The TextView for the patient of the test.
     */
    private TextView tvPatient;
    /**
     * The TextView for the type of the test.
     */
    private TextView tvType;
    private ImageView ivLeave;
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
        ivLeave = findViewById(R.id.iv_leave_test_page);
        ivLeave.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("page", "tests");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
        });

        Button buttonViewGraphs = findViewById(R.id.b_viewGraphics);
        fillTextView(filePath);

        buttonViewGraphs.setOnClickListener(v -> {
            Intent intent = new Intent(TestPageActivity.this, GraphicsTestPageActivity.class);
            intent.putExtra("file_path", filePath);
            startActivity(intent);
        });

    }

    /**
     * Reads the CSV file, extracts information, and fills the corresponding TextViews.
     * @param filePath The path to the CSV file.
     */
    private void fillTextView(String filePath){
        File monCsv = new File(filePath);
        Uri fileUri = FileProvider.getUriForFile(this, "com.example.ex_motricite.file-provider", monCsv);
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



        //Get the information of the CSV

        try {
            //step over the header
            reader.readLine();

            //Type of exercise
            lineList.add(reader.readLine());

            Log.d("debug", "fillTextView: chef? " +lineList.toString());
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
// try closing the BufferedReader
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
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