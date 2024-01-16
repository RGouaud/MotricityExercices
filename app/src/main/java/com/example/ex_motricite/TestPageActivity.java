package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class TestPageActivity extends AppCompatActivity {
    private Uri fileUri;
    private Button buttonViewGraphs;

    private TextView tv_Duration,tv_Operator,tv_Patient,tv_Type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent myIntent = getIntent();
        String filePath = myIntent.getStringExtra("file_path");

        tv_Duration = findViewById(R.id.textViewTestTime);
        tv_Operator = findViewById(R.id.textViewOperator);
        tv_Patient = findViewById(R.id.textViewPatient);
        tv_Type = findViewById(R.id.textViewTestType);

        buttonViewGraphs = findViewById(R.id.b_viewGraphics);
        remplirTextView(filePath);

        buttonViewGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestPageActivity.this, GraphicsTestPageActivity.class);
                intent.putExtra("file_path", filePath);
                startActivity(intent);
            }
        });

    }

    private void remplirTextView(String filePath){
        File monCsv = new File(filePath);
        fileUri = FileProvider.getUriForFile(this, "com.example.myapp.fileprovider", monCsv);
        ArrayList<String> listeLigne = new ArrayList<>();

        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(fileUri);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        //Get the information of the CSV

        try {

            //step over the header
            reader.readLine();

            //Type of exercice
            listeLigne.add(reader.readLine());

            //Patient
            listeLigne.add(reader.readLine());

            //operator
            listeLigne.add(reader.readLine());

            //Step over the Distance
            reader.readLine();

            //Duration
            listeLigne.add(reader.readLine());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //get
        ArrayList<String> newlist = new ArrayList<>();
        for (String ligne:listeLigne) {
            StringTokenizer tokenizer = new StringTokenizer(ligne, "=");
            while (tokenizer.hasMoreElements()) {
                //skip the first part
                tokenizer.nextElement();
                newlist.add(tokenizer.nextElement().toString());
            }
        }

        //Set the textView
        tv_Type.setText(newlist.get(0));
        tv_Patient.setText(newlist.get(1));
        tv_Operator.setText(newlist.get(2));
        tv_Duration.setText(newlist.get(3)+"(s)");
    }
}