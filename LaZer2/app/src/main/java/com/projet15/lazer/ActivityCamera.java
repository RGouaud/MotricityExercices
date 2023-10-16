package com.projet15.lazer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import     android.hardware.Camera;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;




import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.rgb;


//TODO:dessiner trois carré pour y placé les marqueur adaptatif aux ecrans
//TODO:récupérer les coordonnées du laser
//TODO:tracker le laser sur l'écran
//TODO:Gerer les Threads
//TODO:Gérer les different scénario d'erreurs (voir maquettes et scénario)
//TODO: choisir la couleur du lazer (rouge ou vert)

public class ActivityCamera extends AppCompatActivity {

    /************************
     *      Variables       *
     ************************/
    //éléments de l'interface
    private TextView _texte;
    private FrameLayout _displayColor;
    private TextView _tempsRestant;

    //camera
    private Camera _camera;
    private ThreadCameraPreview threadCameraPreview;

    //dessins par dessus la prevew de la caméra
    private Dessin _dessin;
    private Timer myTimer;
    private CameraPreview _cameraPreview;
    private ImageView _imageView;
    private TextView _texteCompteARebours;
    private Button _boutonLancerExercice;


    //paramètres de l'exercice
    private Parameter _parametreDeLexercice;

    //enregistrement coordonnées
    private ArrayList<CoordonneesPxEnFonctionDuTemps> donneesAenregistrer = new ArrayList<CoordonneesPxEnFonctionDuTemps>();
    private boolean estSave = false;

    private Intent _intent;


    /************************
     *      OnCreate        *
     ************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Activer activity_camera.xml comme contenu
        setContentView(R.layout.activity_camera);
        MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();

        //empeche le telephone de ce mettre en veille

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //Recupération des données du formulaire
        _intent = getIntent();
        _parametreDeLexercice = (Parameter) _intent.getSerializableExtra("parameters");

        //Récupération des éléments graphiques
        _texte = (TextView) findViewById(R.id.CALLBACK_TEXT_VIEW_ID);
        _displayColor = (FrameLayout) findViewById(R.id.FRAME_LAYOUT_COLOR_ID);
        _imageView = (ImageView) findViewById(R.id.iv);
        _texteCompteARebours = (TextView) findViewById(R.id.TEXTE_COMPTE_A_REBOURS_ID);
        _boutonLancerExercice = (Button) findViewById(R.id.BOUTON_LANCER_EXERCICE_ID);
        _tempsRestant = (TextView) findViewById(R.id.TEMPS_RESTANT_TEXT_VIEW_ID);


        //Création d'une instance de la caméra
        _camera = getCameraInstance();

        //Paramétrage de l'autofocus
        Camera.Parameters params = _camera.getParameters(); //paramètres de la caméra

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int largeurTelephone = dm.widthPixels;
        int hauteurTelephone = dm.heightPixels;

        int l = largeurTelephone - 175;
        int h = hauteurTelephone;

        float difference = h/360;
        int hauteurPreview = (int)(h/difference);
        int largeurPreview = (int)(l/difference);

        //recuperation des preview disponibles pour la camera
        List<Camera.Size> listFormatSupporte = new ArrayList<Camera.Size>();
        listFormatSupporte = _camera.getParameters().getSupportedPreviewSizes();
        ListIterator<Camera.Size> it = listFormatSupporte.listIterator();
        Camera.Size c = it.next();
        int differenceHauteur = hauteurPreview - c.height;

        Camera.Size previewSize = c;

        while(it.hasNext())
        {
            Log.e("hauteurDuTelephone",Integer.toString(hauteurTelephone));
            Log.e("hauteur",Integer.toString(c.height));
            Log.e("largeur",Integer.toString(c.width));
            Log.e("différence",Integer.toString(differenceHauteur));
            c = it.next();
            if(Math.abs(hauteurPreview - c.height) < Math.abs(differenceHauteur)) {

                previewSize = c;
                differenceHauteur = hauteurPreview - c.height;

            }

        }
        hauteurPreview = previewSize.height;
        largeurPreview = previewSize.width;

        float ratio = (float)(h)/(float)(hauteurPreview);
        l = (int)(largeurPreview*ratio);
        if (largeurTelephone-l>=175){
            _displayColor.setLayoutParams(new LinearLayout.LayoutParams(largeurTelephone-l,hauteurTelephone));
        }
        else
        {
            l = largeurTelephone-175;
            ratio = (float)(l)/(float)(largeurPreview);
            Log.e("ratio",Float.toString(ratio));
            h = (int)(hauteurPreview*ratio);
            _displayColor.setLayoutParams(new LinearLayout.LayoutParams(175,hauteurTelephone));
            findViewById(R.id.FRAME_LAYOUT_CAMERA_ID).setLayoutParams(new LinearLayout.LayoutParams(l,h));
        }


        /** affecte une hauteur et largeur a camera et on recupere la hauteur et largeur qu'elle a vraiment prise (en fonction des formats qu'elle peut prendre*/
        params = _camera.getParameters();
        params.setPreviewSize(largeurPreview,hauteurPreview);
        _camera.setParameters(params);

        hauteurPreview = _camera.getParameters().getPreviewSize().height;
        largeurPreview = _camera.getParameters().getPreviewSize().width;

        //dessins par dessus la caméra
        _dessin = new Dessin(_imageView, h, l);
        _dessin.dessinerCroix();



        /** Activer l'autofus si il est disponible sur l'appareil */
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            //Le mode autofocus est supporté
            Camera.Parameters ParametreCamera = _camera.getParameters(); //récuperer les paramètres de la caméra
            ParametreCamera.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO); //rajouter le mode autofocus
            _camera.setParameters(ParametreCamera); //affecter les nouveaux paramètres à la caméra
        }




        /** Lancer le thread de capture de la preview */
        //Création de la prévisualisation et paramétrage de cette application comme contenu de du framelayout
        _cameraPreview = new CameraPreview(getApplicationContext(), _camera, _intent,  _texte, _displayColor, _boutonLancerExercice, _texteCompteARebours, _tempsRestant, hauteurPreview, largeurPreview, _imageView, l, h); //par défaut hauteur = 480 et 720 si on ne les spécifie pas (voir constructeurs dans la calsse CameraPreview
        FrameLayout layoutPreview = (FrameLayout) findViewById(R.id.CAMERA_FRAME_LAYOUT_ID);
        //preview.addView(_cameraPreview);
        Thread t = new Thread(new ThreadCameraPreview(_cameraPreview, layoutPreview));
        t.start();


        /*****************************************************************
         *                                                               *
         *                  Test Sauvegarde Fichier csv                  *
         *                                                               *
         *****************************************************************/

        /*
        if(CSVFile.sauvegarde(donneesAenregistrer,_parametreDeLexercice)){
            Toast.makeText(getApplicationContext(), "Save successful", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Save failed", Toast.LENGTH_SHORT).show();
        }
        */
    }




    @Override
    public void onPause() {
        Log.e("onpause","L'activité est en pause");
        super.onPause();

        myTimer.cancel();
        myTimer.purge();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        finish();
    }

    //Gestion Camera
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); //  essaie ouverture de la camera
        }
        catch (Exception e){
            // Camera non disponible
        }
        return c; // returns null if camera is unavailable
    }
    private void releaseCamera() {
        _cameraPreview.set_camera(null);
        if (_camera != null) {
            _camera.stopPreview();
            _camera.release();        // relacher la camera pour que d'autre app puisse y acceder
            _camera = null;

        }
    }




}

