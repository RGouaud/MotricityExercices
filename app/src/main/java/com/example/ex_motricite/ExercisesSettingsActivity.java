package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ExercisesSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_settings);
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