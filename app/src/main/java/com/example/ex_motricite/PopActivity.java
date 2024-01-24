package com.example.ex_motricite;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;

import androidx.annotation.Nullable;

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

        bHome.setOnClickListener(v -> startActivity(new Intent(PopActivity.this,homePageActivity.class)));

        getWindow().setLayout((int) (width*.4),(int) (height*.4));
    }
}
