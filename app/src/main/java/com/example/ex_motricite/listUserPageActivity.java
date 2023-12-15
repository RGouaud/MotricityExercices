package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
import android.widget.CompoundButton;


public class listUserPageActivity extends AppCompatActivity {

    private View buttonAddPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_page);

        buttonAddPatient = findViewById(R.id.buttonAddPatient);
        ToggleButton toggleButton1 = findViewById(R.id.toggleButton);
        ToggleButton toggleButton2 = findViewById(R.id.toggleButton2);

        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listUserPageActivity.this, userPageActivity.class);
                startActivity(intent);
            }
        });

        toggleButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Si le premier bouton est coché, décochez le deuxième
                    toggleButton2.setChecked(false);
                } else {
                    // Si le premier bouton est décoché, cochez le deuxième
                    toggleButton2.setChecked(true);
                }
            }
        });

        toggleButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Si le deuxième bouton est coché, décochez le premier
                    toggleButton1.setChecked(false);
                } else {
                    // Si le premier bouton est décoché, cochez le deuxième
                    toggleButton1.setChecked(true);
                }
            }
        });

    }
}