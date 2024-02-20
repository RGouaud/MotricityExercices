package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActorActivity extends AppCompatActivity {

    /**
     * The list of actirs to be displayed.
     */
    private ArrayList<Operator> operatorsList;
    private ArrayList<Patient> patientsList;
    private LinearLayout sourceLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_actor);

        sourceLayout = findViewById(R.id.l_listActors);

        Intent intent = getIntent();
        fillList(intent.getStringExtra("type"));


    }



    private void fillList(String type) {
        if (type.equals("Patient")){
            // Get the list of patients from the database
            patientsList = new PatientDAO(this).getPatients();
        }
        else if (type.equals("Operator")){
            // Get the list of operators from the database
            operatorsList = new OperatorDAO(this).getOperators();
        }
    }

    private void createNewLayoutWithActor(final Actor user) {

        LinearLayout verticalLayout = new LinearLayout(this);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramsV = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        );
        verticalLayout.setLayoutParams(paramsV);

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams paramsH = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        );
        horizontalLayout.setLayoutParams(paramsH);

        LinearLayout lineLayout = new LinearLayout(this);
        lineLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams paramsL = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        );

        lineLayout.setLayoutParams(paramsL);


        // Create a new LinearLayout for the row
            LinearLayout layoutRow = new LinearLayout(this);
            layoutRow.setOrientation(LinearLayout.HORIZONTAL);
            layoutRow.setPadding(10,5,5,10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
            );
            params.setMargins(5,0,5,0);
            layoutRow.setLayoutParams(params);

            sourceLayout.addView(layoutRow);


        //1button layout
        LinearLayout layoutButton = new LinearLayout(this);
        layoutButton.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        );
        params.setMargins(25,0,25,0);
        layoutButton.setLayoutParams(params);
        layoutButton.setPadding(15,15,15,15);
        layoutButton.setBackground(AppCompatResources.getDrawable(this,R.drawable.black_rounded_rectangle));



        //TextView containing the name of the patient and the operator
        TextView tvName = new TextView(this);
        tvName.setText(user.getName());
        tvName.setTextColor(getColor(R.color.white));
        tvName.setGravity(1);
        TextView tvFirstName = new TextView(this);
        tvFirstName.setText(user.getFirstName());
        tvFirstName.setTextColor(getColor(R.color.white));
        tvFirstName.setGravity(1);
        layoutButton.setOnClickListener(v->{
            if (tvName.getCurrentTextColor() == getColor(R.color.white)){
                layoutButton.setBackground(AppCompatResources.getDrawable(this,R.drawable.yellow_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.black));
                tvFirstName.setTextColor(getColor(R.color.black));
                selectedNameList.add(user.getName());
            }
            else {
                layoutButton.setBackground(AppCompatResources.getDrawable(this,R.drawable.black_rounded_rectangle));
                tvName.setTextColor(getColor(R.color.white));
                tvFirstName.setTextColor(getColor(R.color.white));
                selectedNameList.remove(user.getName());
            }

        });
        layoutButton.addView(tvName);
        layoutButton.addView(tvFirstName);



        LinearLayout rowLayout = (LinearLayout) sourceLayout.getChildAt(sourceLayout.getChildCount() - 1);
        rowLayout.addView(layoutButton);

    }

}