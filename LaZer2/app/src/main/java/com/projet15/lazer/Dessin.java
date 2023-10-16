package com.projet15.lazer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.widget.ImageView;


public class Dessin {

    private ImageView _imageView;
    private int _hauteur;
    private int _largeur;

    public Dessin(ImageView im, int hauteur, int largeur)
    {
        this._imageView = im;
        this._hauteur = hauteur;
        this._largeur = largeur;

    }

    public void dessinerCroix() {
        Bitmap bitmap = Bitmap.createBitmap(
                _largeur, // Width
                _hauteur, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.TRANSPARENT);

        // Initialize a new Paint instance to draw the Rectangle
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb( 255, 0, 0));

        int ecart = _largeur * 3/100 ;
        paint.setStrokeWidth (10);

       //croix du milieu
        int milieuX = _largeur * 50/100;
        int milieuY = _hauteur * 50/100;

        canvas.drawLine(milieuX - ecart,milieuY - ecart, milieuX + ecart, milieuY + ecart, paint);
        canvas.drawLine(milieuX + ecart,milieuY - ecart, milieuX - ecart, milieuY + ecart, paint);

        //croix de droite
        int droiteX = milieuX + _largeur * 30/100;
        int droiteY = milieuY;

        canvas.drawLine(droiteX - ecart,droiteY - ecart, droiteX + ecart, droiteY + ecart, paint);
        canvas.drawLine(droiteX + ecart,droiteY - ecart, droiteX - ecart, droiteY + ecart, paint);

        //croix de droite
        int gaucheX = milieuX - _largeur * 30/100;
        int gaucheY = milieuY;

        canvas.drawLine(gaucheX - ecart,gaucheY - ecart, gaucheX + ecart, gaucheY + ecart, paint);
        canvas.drawLine(gaucheX + ecart,gaucheY - ecart, gaucheX - ecart, gaucheY + ecart, paint);

        // Display the newly created bitmap on app interface
        _imageView.setImageBitmap(bitmap);
    }

    void dessinerRond(float x, float y) {
        Bitmap bitmap = Bitmap.createBitmap(
                _largeur, // Width
                _hauteur, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.TRANSPARENT);

        // Initialize a new Paint instance to draw the Rectangle
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(255, 0, 0));
        canvas.drawCircle(x, y, 20, paint);
        // Display the newly created bitmap on app interface
        _imageView.setImageBitmap(bitmap);
    }


}
