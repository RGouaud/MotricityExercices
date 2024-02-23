package com.example.ex_motricite;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import androidx.annotation.Nullable;

/**
 * The {@code PopActivity} class represents an Android activity for displaying a pop-up window.
 *
 * <p>
 * This activity includes functionalities to display a pop-up window and navigate to the home page.
 * </p>
 *
 * <p>
 * Author: Segot
 * Version: 1.0
 * </p>
 */
public class PopActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Button bHome;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        bHome = findViewById(R.id.b_Home);

        DisplayMetrics dm = new DisplayMetrics();

        //Should work even with deprecated functions getDefaultDisplay().getMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Set a click listener on the Home button to navigate to the home page
        bHome.setOnClickListener(v -> startActivity(new Intent(PopActivity.this,HomePageActivity.class)));

        // Set the layout dimensions of the pop-up window
        getWindow().setLayout((int) (width*.4),(int) (height*.4));
    }
}
