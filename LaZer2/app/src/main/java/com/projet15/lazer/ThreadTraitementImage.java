package com.projet15.lazer;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ThreadTraitementImage implements Runnable {

    byte [] _image;
    int _width;
    int _heigh;
    TextView _texte;
    long _tempsRestant;
    ImageView _imageView;
    int _h;
    int _l;

    public ThreadTraitementImage(byte [] image, int width, int heigh, TextView texte, long temps, ImageView imageView, int l, int h)
    {
        this._image = image;
        this._heigh = heigh;
        this._width = width;
        this._texte = texte;
        this._tempsRestant = temps;
        this._imageView = imageView;
        this._h = h;
        this._l = l;
    }

    public void run()
    {
        YuvToRgb imageYUV = new YuvToRgb(_image, _width, _heigh);
        int [] imageDecode = imageYUV.decode();

        //tableau de tout les pixels correspondant au laser trouvés
        List<Coordonnee> laserRouge = new ArrayList<Coordonnee>();

        float a = 1 / 3;

        for (int i = 0; i < imageDecode.length; i++) {

            int r = (imageDecode[i] & 0x00FF0000) >> 16;
            //int g = (tab[i] & 0x0000FF00) >> 8;
            //int b = (tab[i] & 0x000000FF);
            if (r == 255) { // si la composante rouge est égale à 255 c'est que c'est un endroit ou le laser est pointé
                //on ajoute à la liste des coordonées de laser trouvés la coordonnée
                Coordonnee c = new Coordonnee(i, _width, _heigh);
                laserRouge.add(c);
            }

        }

        //on fait la moyenne des coordonées de pixels trouvées pour trouver le centre du laser
        int sommeX = 0;
        int sommeY = 0;
        int nombreDePoints = 0;
        ListIterator<Coordonnee> it = laserRouge.listIterator();
        while (it.hasNext()) {
            Coordonnee c = it.next();
            sommeX += c.getX();
            sommeY += c.getY();
            nombreDePoints++;
        }
        if (nombreDePoints != 0) {
            sommeX = sommeX / nombreDePoints;
            sommeY = sommeY / nombreDePoints;
            Coordonnee c = new Coordonnee(sommeX, sommeY);
            CoordonneesPxEnFonctionDuTemps.getInstance().ajouterCoordEnFonctionDuTemps(c, _tempsRestant); //temps mis à 0 privsoirement mais il faudrait le récupérer dans le timer.
            _texte.setText("x=" + String.valueOf(sommeX) + ",y=" + String.valueOf(sommeY));
        }
        else{
            _texte.setText("inconnus");
        }

    }
}
