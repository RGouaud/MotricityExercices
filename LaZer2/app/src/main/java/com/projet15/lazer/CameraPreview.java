package com.projet15.lazer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.security.Policy;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;

import static android.content.ContentValues.TAG;


public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback
{
    //variable caméra
    private SurfaceHolder _holder;
    private Camera _camera;

    //Settings de la caméra
    private Camera.Parameters parameters;

    //éléments d'interface sur la barre verticale de droite:
    private TextView _texte;
    private FrameLayout _displayColor;

    //éléments d'interface par dessus la preview
    private TextView _texteCompteARebours;
    private Button _boutonLancerExercice;
    private TextView _tempsRestant;
    private ImageView _imageView;

    //taille de la préview
    private int _hauteurPreview;
    private int _largeurPreview;

    private int _h;
    private int _l;

    //thread onPreviewFrame
    private ThreadTraitementImage _threadTraitementImage;

    //commencer à enregister le laser?
    private boolean _enregistrerLeLaser = false;

    //paramétres de l'exercice
    private Parameter _parametreDeLexercice;
    private Intent _intent;

    //timers
    private Timer myTimer;
    MyTimerTask myTask = new MyTimerTask();
    private long tempsRestant;

    private Context _context;




    /** constructeurs */



    //CONSTRUCTEUR ACTUELLEMENT UTILISE: AVEC DIMENSIONS, COMPTE A REBOURS, BOUTONlANCEREXERCICE...
    public CameraPreview(Context context, Camera camera, Intent intent, TextView texte , FrameLayout displayColor, Button boutonLancerExercice, TextView texteCompteARebours, TextView tempsRestant, int hauteurPreview, int largeurPreview, ImageView image, int l, int h) //avec le frame layout de droite (si on veux le colorer)
    {
        super(context);
        _camera = camera;
        _context = context;

        //taille de la preview
        this._hauteurPreview = hauteurPreview;
        this._largeurPreview = largeurPreview;
        this._h = h;
        this._l = l;

        //éléments d'interface
        this._texte = texte;
        this._displayColor = displayColor;
        this._boutonLancerExercice = boutonLancerExercice;
        this._texteCompteARebours = texteCompteARebours;
        this._tempsRestant = tempsRestant;
        this._imageView = image;

        //timer
        myTimer = new Timer();

        //paramétres de l'exercice
        this._intent = intent;
        _parametreDeLexercice = (Parameter) _intent.getSerializableExtra("parameters");
        //_tempsRestant.setText(Integer.toString(_parametreDeLexercice.get_time()));

        //onclicklistener du bouton compte à rebours:
        _boutonLancerExercice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                _texteCompteARebours.setText("4");
                _boutonLancerExercice.setEnabled(false);
                CountDownTimer compteARebours = new CountDownTimer(4000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        _texteCompteARebours.setText(Long.toString(millisUntilFinished/1000));
                    }

                    @Override
                    public void onFinish() {
                        _texteCompteARebours.setText("");
                        _enregistrerLeLaser = true;
                        lancerTimer();
                    }
                }.start();
            }
        });

        // Install a SurfaceHolder.Callback so we get notified when the underlying surface is created and destroyed.
        _holder = getHolder();
        _holder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        _holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    /** getters setters */
    Camera get_camera (){
        return this._camera;
    }

    void set_camera(Camera camera){
        this._camera = camera;
    }


    /** comportement caméra */
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            _camera.setPreviewDisplay(holder);

            //paramétrer la taille de la préview
            parameters = _camera.getParameters();
            parameters.setPreviewSize(_largeurPreview,_hauteurPreview);

            _camera.setParameters(parameters);

            parameters = _camera.getParameters();

            _camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        myTimer.cancel();
        myTimer.purge();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        try {
            _camera.stopPreview();

            //paramétrer la taille de la préview
            parameters = _camera.getParameters();
            parameters.setPreviewSize(_largeurPreview,_hauteurPreview);
            _camera.setParameters(parameters);

            _camera.setPreviewDisplay(holder);
            _camera.setPreviewCallback(this);
            _camera.startPreview();

        } catch (Exception ex) {
            return;
        }
    }

    /***********************************************************************
     *      Récupération des coordonnées de la caméra à chaque image       *
     ***********************************************************************/

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (_enregistrerLeLaser) {

            //fonction exécuté à chaque frame capturé
            parameters = camera.getParameters();


            _threadTraitementImage = new ThreadTraitementImage(data,this._largeurPreview,this._hauteurPreview, this._texte, tempsRestant, _imageView, _l, _h);
            _threadTraitementImage.run();

            //_displayColor.setBackgroundColor(rgb(r,g,b));

        }
    }

    public void lancerTimer()
    {
        _tempsRestant.setText(" ");
        if (_parametreDeLexercice.get_bipIntervale() == -1) {
            myTimer.schedule(myTask, _parametreDeLexercice.get_time() * 1000);
        } else {
            myTimer.schedule(myTask, 1000, (int) (_parametreDeLexercice.get_bipIntervale() * 1000));
        }
        new CountDownTimer(_parametreDeLexercice.get_time() * 1000, 1000) {

            public void onTick(long millisUntilFinished)
            {
                _tempsRestant.setText("" + millisUntilFinished / 1000);
                tempsRestant = millisUntilFinished;
                Log.e("temps", Long.toString(tempsRestant));
            }

            public void onFinish()
            {
                _tempsRestant.setText("done!");
                _enregistrerLeLaser = false;
                ArrayList<CoordonneesCmEnFonctionDuTemps> coordCM = new ArrayList<CoordonneesCmEnFonctionDuTemps>();
                int nombreCoordonnee = CoordonneesPxEnFonctionDuTemps.getInstance()._coordoneesPxAvecTemps.size();
               ListIterator<Pair<Coordonnee, Long>> it = CoordonneesPxEnFonctionDuTemps.getInstance()._coordoneesPxAvecTemps.listIterator();
               while(it.hasNext())
               {
                   Pair<Coordonnee, Long> coord = it.next();
                   System.out.print(coord.second);
                   CoordonneesCmEnFonctionDuTemps cm = new CoordonneesCmEnFonctionDuTemps(coord.second, coord.first.getX(), coord.first.getY(), _hauteurPreview, _largeurPreview, _parametreDeLexercice.get_markDistance());
                   coordCM.add(cm);
               }
               CSVFile.sauvegarde(coordCM, _parametreDeLexercice);
                Toast.makeText(_context, "The file has been saved.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(_context, ActivityMenuPrincipal.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _context.startActivity(i);


            }
        }.start();

    }

}