package com.projet15.lazer;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CoordonneesPxEnFonctionDuTemps {
    List<Pair<Coordonnee, Long>> _coordoneesPxAvecTemps; //Coord px et temps en ms depuis le début de l'exercice
    private CoordonneesPxEnFonctionDuTemps(){
        _coordoneesPxAvecTemps = new ArrayList<Pair<Coordonnee, Long>>();
    }
    /** Instance unique non préinitialisée */
    private static CoordonneesPxEnFonctionDuTemps INSTANCE = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized CoordonneesPxEnFonctionDuTemps getInstance()
    {
        if (INSTANCE == null)
        {   INSTANCE = new CoordonneesPxEnFonctionDuTemps();
        }
        return INSTANCE;
    }

     public void viderListe(){
         //vider la liste
         _coordoneesPxAvecTemps.clear();
     }

     public void ajouterCoordEnFonctionDuTemps(Coordonnee coordonnee, Long temps){
        _coordoneesPxAvecTemps.add(new Pair<Coordonnee, Long>(coordonnee, temps));
     }
}

