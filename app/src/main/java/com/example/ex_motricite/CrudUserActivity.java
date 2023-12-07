package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CrudUserActivity extends AppCompatActivity {

    private Button b_confirm;
    private TextView tv_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_user);

        b_confirm = findViewById(R.id.b_confirm);
        tv_new = findViewById(R.id.tv_new);

        b_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(homePageActivity.this, patientActivity.class);
                // startActivity(intent);
            }
        });
    }
}