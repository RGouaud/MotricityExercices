package com.projet15.lazer;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMenuPrincipal extends  AppCompatActivity{

    //Variables
    private Button _staticButton; // représente le bouton "Static Exercice"
    private Button _rythmButton; // représente le bouton "Rythm Exercice"
    private Button _managerButton; // représente le bouton "Data Manager"

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 3;
    //Constructeur
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        autorisationRequest();

        /**transitions
         setupWindowAnimations();
         http://lgvalle.xyz/2015/06/07/material-animations/
         https://developer.android.com/training/transitions/index.html
         https://developer.android.com/training/transitions/start-activity.html#custom-trans**/


        //association des objets Java aux objets graphique
        set_staticButton((Button) findViewById(R.id.STATIC_BUTTON_ID));
        set_rythmButton((Button) findViewById(R.id.RYTHM_BUTTON_ID));
        set_managerButton((Button) findViewById(R.id.DATAMANAGER_BUTTON_ID));

        //mise en attente de click des différents bouton
        //clique sur Static exercice affichage de l'écrans de parametrage de l'exercice
        get_staticButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission(view, ActivityStaticExerciceParameter.class, getString(R.string.TransitionStatic));
            }
        });

        get_rythmButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permission(view, ActivityRythmExerciceParameter.class, getString(R.string.TransitionRythm));
            }
        });


        get_managerButton().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                permission(view,ActivityDataManager.class, getString(R.string.TransitionData));
            }
        });

    }

    /**
     private void setupWindowAnimations() {
     Slide slide = new Slide();
     slide.setDuration(1000);
     getWindow().setExitTransition(slide);
     }
     **/

    //Getteur & setteur
    public void set_staticButton (Button b){
        _staticButton = b;
    }
    public Button get_staticButton(){
        return _staticButton;
    }
    public void  set_rythmButton(Button b){
        _rythmButton = b;
    }
    public Button get_rythmButton(){
        return _rythmButton;
    }
    public void  set_managerButton(Button b){
        _managerButton = b;
    }
    public Button get_managerButton(){
        return _managerButton;
    }

    //Méthodes

    private void autorisationRequest()
    {
        if (ContextCompat.checkSelfPermission(this, //Regarde si l'accès à la camera est autorisé
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, //Regarde si l'accès à la lecture est autorisé
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        else if(ContextCompat.checkSelfPermission(this, //Regarde si l'accès à la camera est autorisé
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);

        }
        else if(ContextCompat.checkSelfPermission(this, //Regarde si l'accès à la lecture est autorisé
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    private void permission(View view, Class classView, String elementTransition)
    {
        if(ContextCompat.checkSelfPermission(this, //Regarde si l'accès à la camera est autorisé
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, //Regarde si l'accès à la lecture est autorisé
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityMenuPrincipal.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_box_layout, null);
            Button _boutonCancel = (Button) mView.findViewById(R.id.CANCEL_BUTTON_ID);
            Button _boutonAuthorize = (Button) mView.findViewById(R.id.CONFIRM_BUTTON_ID);
            _boutonAuthorize.setText(R.string.Bouton_Autoriser);
            TextView _titreText = (TextView) mView.findViewById(R.id.DIALOGTITRE_TEXT_ID);
            TextView _contenuText = (TextView) mView.findViewById(R.id.DIALOGCONTENT_TEXT_ID);

            //Set Text
            _titreText.setText(R.string.Titre_Autorisation_Double);
            _contenuText.setText(R.string.Contenu_Autorisatoin_Double);
            _boutonAuthorize.setText(R.string.Bouton_Autoriser);


            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            _boutonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                    //la dedans faut demander la permission de la caméra

                }
            });

            _boutonAuthorize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.hide();
                    autorisationRequest();
                    Log.e("all","hide");

                }
            });


        }

        else
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                Intent i =new Intent (ActivityMenuPrincipal.this, classView);
                ActivityOptions transitionActivtyOptions = ActivityOptions.makeSceneTransitionAnimation(ActivityMenuPrincipal.this, view, elementTransition);
                startActivity(i, transitionActivtyOptions.toBundle());
            }
            else
            {
                Intent i = new Intent(view.getContext(), classView); //association de l'activité principale et de l'activité exercice statique parameter
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i); //lancement de l'activitéexercice statique parameter
            }

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est garantie
                } else {
                    // La permission est refusée
                }
                return;
            }
        }
    }

}