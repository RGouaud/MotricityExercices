package com.example.ex_motricite;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The {@code UserPageActivity} class represents an Android activity for displaying the user page.
 *
 * <p>
 * This activity includes functionalities to display the user page and navigate to the list of tests.
 * </p>
 *
 * <p>
 *     Author: EduardoXav
 *     Version: 1.0
 * </p>
 */
public class UserPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button buttonModify = findViewById(R.id.b_modify);

        buttonModify.setOnClickListener(v -> {
            Intent intent = new Intent(UserPageActivity.this, ListTestActivity.class);
            startActivity(intent);
        });

    }
}