package com.projet15.lazer;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class ActivityDataManager extends AppCompatActivity {
    private Toolbar _toolbar;

    private RecyclerView _rv;
    private File[] fichierLister;
    private MyFileAdapter _adapter;
    private CheckBox _selectAllCheckbox;
    private Button _deleteButton;
    private Button _sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manager);

        _toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_toolbar);
        _toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back)); //mettre comme icône de naviguation ic_action_back (flêche)
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() { //mettre un listner à l'icone qui reviens au menu principal quand on clique dessus
            @Override
            public void onClick(View view) {
                finish();  //termine l'activité pour retourner au menu principal
            }
        });
        _selectAllCheckbox = (CheckBox) findViewById(R.id.SELECTBOXID);
        _sendButton = (Button) findViewById(R.id.SendButton);
        _deleteButton = (Button) findViewById(R.id.deleteButton);

        _rv = (RecyclerView) findViewById(R.id.RECYCLERVIEWID);
        fichierLister = CSVFile.liste();
        _adapter = new MyFileAdapter(fichierLister, _selectAllCheckbox);
        _rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        _rv.setAdapter(_adapter);


        _selectAllCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (_adapter._modifierEtatCheckBox != false){
                    _adapter.selectAll(b);
                    _adapter._modifierEtatCheckBox = true;
                }

            }
        });

        _sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(_adapter.getFileSelected());
            }
        });

        _deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityDataManager.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_layout, null);
                Button _boutonCancel = (Button) mView.findViewById(R.id.CANCEL_BUTTON_ID);
                Button _boutonContinue = (Button) mView.findViewById(R.id.CONFIRM_BUTTON_ID);
                TextView _titreText = (TextView) mView.findViewById(R.id.DIALOGTITRE_TEXT_ID);
                TextView _contenuText = (TextView) mView.findViewById(R.id.DIALOGCONTENT_TEXT_ID);
                //Set Text
                _titreText.setText(R.string.Titre_Confirmation_Suppresion);
                _contenuText.setText(R.string.Contenu_Confirmation_Suppresion);
                _boutonCancel.setText(R.string.Bouton_Conformation_Suppresion_No);
                _boutonContinue.setText(R.string.Bouton_Conformation_Suppresion_Yes);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                _boutonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                    }
                    });
                _boutonContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.hide();
                        Toast.makeText(ActivityDataManager.this, R.string.fileDeleted, Toast.LENGTH_LONG).show();
                        CSVFile.supprimerFichier(_adapter.getFileSelected());
                        finish();
                    }
                });

            }
        });
    }

    public void send (List<File> fichiersAEnvoyer){
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        List <Uri> listUri = new ArrayList<Uri>();
        for (int i = 0 ; i < fichiersAEnvoyer.size();i++ ) {
            listUri.add(Uri.fromFile(fichiersAEnvoyer.get(i)));
        }

        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, (ArrayList<? extends Parcelable>) listUri);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "LaZer Exercice data");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "send from LaZer");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("end Send", "Finished sending email...");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActivityDataManager.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }



}