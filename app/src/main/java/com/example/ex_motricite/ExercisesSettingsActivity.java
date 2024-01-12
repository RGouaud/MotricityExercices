package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExercisesSettingsActivity extends AppCompatActivity {

    private EditText et_distance;
    private EditText et_interval;
    private EditText et_seconds;
    private TextView tv_settings_tittle;
    private SeekBar sb_interval;
    private Button b_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_exercises_settings);
        et_distance = findViewById(R.id.et_distance);
        et_interval = findViewById(R.id.et_interval);
        et_seconds = findViewById(R.id.et_seconds);
        sb_interval = findViewById(R.id.sb_interval);
        tv_settings_tittle = findViewById(R.id.tv_settings_title);
        sb_interval = findViewById(R.id.sb_interval);
        b_start = findViewById(R.id.b_start);

        Intent myIntent = getIntent();
        String exercice = myIntent.getStringExtra("Exercice");

        if (exercice.equals("static")){
            tv_settings_tittle.setText("Static test settings");
            et_interval.setVisibility(View.INVISIBLE);
            sb_interval.setVisibility(View.INVISIBLE);

            b_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et_distance.getText().toString().matches("") || et_seconds.getText().toString().matches(""))
                    {
                        Toast.makeText(ExercisesSettingsActivity.this, "You must complete each fields !", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Intent intent = new Intent(ExercisesSettingsActivity.this, StaticExerciceActivity.class);
                        intent.putExtra("Distance", et_distance.getText().toString());
                        intent.putExtra("Time", et_seconds.getText().toString());
                        startActivity(intent);
                    }
                }
            });
        }
        else {
            tv_settings_tittle.setText("Rythm test settings");
            b_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et_distance.getText().toString().matches("") || et_seconds.getText().toString().matches("")||et_interval.getText().toString().matches(""))
                    {
                        Toast.makeText(ExercisesSettingsActivity.this, "You must complete each fields !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(ExercisesSettingsActivity.this, DynamicExerciceActivity.class);
                        intent.putExtra("Distance", et_distance.getText().toString());
                        intent.putExtra("Interval", et_interval.getText().toString());
                        intent.putExtra("Time", et_seconds.getText().toString());
                        startActivity(intent);
                    }
                }
            });
        }
    }

    // logique
    /*
    passage d'un parametre a l'instanciation de la classe pour afficher ou non les parametres relatifs au test dynamiques

    alimentation des spinners avec les données relatives

    SI DYNAMIQUE
    mise en place de la synchro entre les choix d'interval



    BOUTON START : jsp

    BOUTON RECOVER : grisé si pas de CSV trouvé  à l'emplacement
    sinon : lire les méta données du dernier CSV et les écrire dans leurs emplacements
     */
}