package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class ListTestActivity extends AppCompatActivity {

    private Button buttonExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_test);

        buttonExport = findViewById(R.id.b_exportSelection);

        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListTestActivity.this, TestPageActivity.class);
                startActivity(intent);
            }
        });
    }
}