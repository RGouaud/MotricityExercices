package com.projet15.lazer;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;

import android.widget.TextView;
import android.widget.Toast;


//TODO:demande de confirmation de temps au dela de 5min = 480s
//TODO:Verification Temps > 0

public class ActivityRythmExerciceParameter extends AppCompatActivity {

    //Variables
    //Objet graphique

    private Toolbar _toolbar;
    private EditText _patientName, _operatorName, _markDistance, _time, _bipInterval,_comments;
    private TextInputLayout _layoutPatientName, _layoutOperatorName, _layoutMarkDistance, _layoutTime, _layoutBipInterval;
    private Button buttonStart;
    //Parameter
    public Parameter parametreDeLexercice;

    //Constructeur
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rythm_exercice_parameter);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        _toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back)); //mettre comme icône de naviguation ic_action_back (flêche)
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() { //mettre un listner à l'icone qui reviens au menu principal quand on clique dessus
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        //textInputLayout
        _layoutPatientName = (TextInputLayout) findViewById(R.id.RYTHM_PATIENT_NAME_LAYOUT_ID);
        _layoutOperatorName = (TextInputLayout) findViewById(R.id.RYTHM_OPERATOR_NAME_LAYOUT_ID);
        _layoutMarkDistance = (TextInputLayout) findViewById(R.id.RYTHM_MARK_DISTANCE_LAYOUT_ID);
        _layoutTime = (TextInputLayout) findViewById(R.id.RYTHM_TIME_LAYOUT_ID);
        _layoutBipInterval = (TextInputLayout) findViewById(R.id.RYTHM_BIP_INTERVAL_LAYOUT_ID);

        //editText
        _patientName = (EditText) findViewById(R.id.RYTHM_PATIENT_NAME_EDITTEXT_ID);
        _operatorName = (EditText) findViewById(R.id.RYTHM_OPERATOR_NAME_EDITTEXT_ID);
        _markDistance = (EditText) findViewById(R.id.RYTHM_MARK_DISTANCE_EDITTEXT_ID);
        _time = (EditText) findViewById(R.id.RYTHM_TIME_EDITTEXT_ID);
        _bipInterval = (EditText) findViewById(R.id.RYTHM_BIP_INTERVAL_EDITTEXT_ID);
        _comments = (EditText) findViewById(R.id.RYTHM_COMENTAIRE_EDITTEXT_ID);
        buttonStart = (Button) findViewById(R.id.RYTHM_START_BUTTON_ID);


        //set les listener
        _patientName.addTextChangedListener(new textChangedListener(_patientName));
        _operatorName.addTextChangedListener(new textChangedListener(_operatorName));
        _markDistance.addTextChangedListener(new textChangedListener(_markDistance));
        _time.addTextChangedListener(new textChangedListener(_time));
        _bipInterval.addTextChangedListener(new textChangedListener(_bipInterval));

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm(view);

            }
        });
    }

    //Getteur Setteur

    //Méthode
    //Validation formulaire
    private void submitForm(View view) {
        if (!validatePatientName()) { //si le nom du patient n'est pas valide
            return; //on valide pas le formulaire
        }

        if(!validateOperatorName()){
            return;
        }

        if (!validateMarkDistance()) {
            return;
        }

        if (!validateTime()) {
            return;
        }

        //sinon on passe à la vue suivante (CODE A RAJOUTER)
        Parameter parametreDeLexercice = new Parameter(2,
                _patientName.getText().toString(),
                _operatorName.getText().toString(),
                Float.parseFloat(_markDistance.getText().toString()),
                Integer.parseInt(_time.getText().toString()),
                Float.parseFloat(_bipInterval.getText().toString()),
                _comments.getText().toString());

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            Intent i = new Intent(view.getContext(),ActivityCamera.class);
            i.putExtra("parameters",parametreDeLexercice);
            startActivity(i);
        } else {
            // no camera on this device
        }

        // Toast.makeText(getApplicationContext(), "la vue suivante n'existe pas encore", Toast.LENGTH_SHORT).show();
    }

    //validation PatientName
    private boolean validatePatientName() {
        //trim() enlève les espaces
        if (_patientName.getText().toString().trim().isEmpty()) { //si le nom du patient est vide
            _layoutPatientName.setError(getString(R.string.Err_Patient_Name)); //on met en message d'erreur Err_Patient_Name
            requestFocus(_patientName);
            return false; //est on return false (nom non-validé)
        } else {
            _layoutPatientName.setErrorEnabled(false); //sinon le patientName est valide donc si le patientName a déjà été validé on enlève le message d'erreur
        }

        return true; //et on retourne true(nom validé)
    }

    private boolean validateOperatorName() {
        if (_operatorName.getText().toString().trim().isEmpty()) {
            _layoutOperatorName.setError(getString(R.string.Err_Operator_Name));
            requestFocus(_operatorName);
            return false;
        } else {
            _layoutOperatorName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMarkDistance() {
        String expression = "([0-9]+(.)?[0-9]+)|([1-9]+)";
        if (_markDistance.getText().toString().trim().isEmpty()) {
            _layoutMarkDistance.setError(getString(R.string.Err_Mark_Distance));
            requestFocus(_markDistance);
            return false;
        }
        else if(!_markDistance.getText().toString().matches(expression)){
            _layoutMarkDistance.setError(getString(R.string.Err_Mark_Distance_Point));
            requestFocus(_markDistance);
            return false;
        }
        else {
            if (_markDistance.getText().toString().contains(".")) {
                _markDistance.getText().toString().replaceAll(".",",");
            }
            _layoutMarkDistance.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateTime() {
        String expression = "[1-9]|[0-9]+[0-9]+";
        if (_time.getText().toString().trim().isEmpty()) {
            _layoutTime.setError(getString(R.string.Err_Time));
            requestFocus(_time);
            return false;
        }
        else if(!_time.getText().toString().matches(expression))
        {
            _layoutTime.setError(getString(R.string.Err_Time_Point));
            requestFocus(_time);
            return false;
        }
        else {
            _layoutTime.setErrorEnabled(false);
        }

        if (Integer.parseInt(_time.getText().toString())>= 180){

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityRythmExerciceParameter.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_box_layout, null);
            Button _boutonCancel = (Button) mView.findViewById(R.id.CANCEL_BUTTON_ID);
            Button _boutonContinue = (Button) mView.findViewById(R.id.CONFIRM_BUTTON_ID);
            TextView _titreText = (TextView) mView.findViewById(R.id.DIALOGTITRE_TEXT_ID);
            TextView _contenuText = (TextView) mView.findViewById(R.id.DIALOGCONTENT_TEXT_ID);
            //Set Text
            _titreText.setText(R.string.Titre_Confirmation_Temps);
            _contenuText.setText(R.string.Contenu_Confirmation_Temps);
            _boutonCancel.setText("OK");
            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            // evenement Boutons
            _boutonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }
        return true;
    }


    private boolean validateBipInterval(){
        String expression = "([0-9]+(.)?[0-9]+)|([1-9]+)";
        if (_bipInterval.getText().toString().trim().isEmpty()) {
            _layoutBipInterval.setError(getString(R.string.Err_Bip_Interval));
            requestFocus(_bipInterval);
            return false;
        }
        else if(!_bipInterval.getText().toString().matches(expression))
        {
            _layoutBipInterval.setError(getString(R.string.Err_Bip_Intervalle_Point));
            requestFocus(_bipInterval);
            return false;
        }
        else {
            if(_bipInterval.getText().toString().contains("."))
            {
                _bipInterval.getText().toString().replaceAll(".",",");
            }
            _layoutBipInterval.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    private class textChangedListener implements TextWatcher {

        private View view;

        private textChangedListener(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

        }

        public void afterTextChanged(Editable editable){ //après qu'on est changé le texte d'un champ, on attend pas la validation pour vérifer que c'est nom vide: pour pouvoir supprimer les messsages d'erreur dès qu'on entre quelque chose
            switch (view.getId()) {
                case R.id.RYTHM_PATIENT_NAME_EDITTEXT_ID:
                    validatePatientName();
                    break;
                case R.id.RYTHM_OPERATOR_NAME_EDITTEXT_ID:
                    validateOperatorName();
                    break;
                case R.id.RYTHM_MARK_DISTANCE_EDITTEXT_ID:
                    validateMarkDistance();
                    break;
                case R.id.RYTHM_TIME_EDITTEXT_ID:
                    validateTime();
                    break;
                case R.id.RYTHM_BIP_INTERVAL_EDITTEXT_ID:
                    validateBipInterval();
                    break;
            }
        }


    }
}


