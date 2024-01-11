package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestPageActivity extends AppCompatActivity {

    private Button buttonViewGraphs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);

        buttonViewGraphs = findViewById(R.id.b_viewGraphics);

        buttonViewGraphs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestPageActivity.this, GraphicsTestPageActivity.class);
                startActivity(intent);
            }
        });

    }
}