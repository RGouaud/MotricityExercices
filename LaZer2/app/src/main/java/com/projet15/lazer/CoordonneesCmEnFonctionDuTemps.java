package com.projet15.lazer;

import android.util.Log;

public class CoordonneesCmEnFonctionDuTemps {
    private Long _seconde;
    private  float _x;
    private  float _y;

    public CoordonneesCmEnFonctionDuTemps(Long _seconde, int _x, int _y, int hauteur, int largeur, float markDistance) {
        this._seconde = _seconde;

        double a = 3.0/10.0;

        double t = largeur * a ;
        Log.e ("largeur", Integer.toString(largeur));
        Log.e ("t", Double.toString(t));

        //mise a l'echelle
        this._x = _x - largeur/2;
        this._y = _y - hauteur/2;
        Log.e ("x", Float.toString(this._x));
        Log.e ("y", Float.toString(this._y));

        //conversion en cm
        this._x = (float)((this._x * markDistance) / t);
        this._y = (float)((this._y * markDistance) / t);
        Log.e ("x", Float.toString(this._x));
        Log.e ("y", Float.toString(this._y));
        Log.e ("time", Float.toString(this._seconde));
    }

    public Long get_seconde() {
        return _seconde;
    }

    public void set_seconde(Long _seconde) {
        this._seconde = _seconde;
    }

    public float get_x() {
        return _x;
    }

    public void set_x(float _x) {
        this._x = _x;
    }

    public float get_y() {
        return _y;
    }

    public void set_y(float _y) {
        this._y = _y;
    }
}
