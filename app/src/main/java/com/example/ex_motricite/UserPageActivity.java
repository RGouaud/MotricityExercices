package com.example.ex_motricite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserPageActivity extends AppCompatActivity {

    private Button buttonModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        buttonModify = findViewById(R.id.b_modify);

        buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPageActivity.this, ListTestActivity.class);
                startActivity(intent);
            }
        });

    }
}