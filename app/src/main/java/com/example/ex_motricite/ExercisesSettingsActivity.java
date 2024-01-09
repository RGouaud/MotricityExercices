package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ExercisesSettingsActivity extends AppCompatActivity {

    private EditText et_distance;
    private EditText et_interval;
    private TextView tv_settings_tittle;
    private SeekBar sb_interval;
    private Button b_start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_settings);
        et_distance = findViewById(R.id.et_distance);
        et_interval = findViewById(R.id.et_interval);
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
                    Intent intent = new Intent(ExercisesSettingsActivity.this, ExerciceActivity.class);
                    intent.putExtra("User", "patient");
                    startActivity(intent);
                }
            });
        }
        else {
            tv_settings_tittle.setText("Rythm test settings");
        }

        et_distance.setText(exercice);
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