package com.example.ex_motricite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActorActivity extends AppCompatActivity {

    /**
     * The list of actors to be displayed.
     */
    private ArrayList<Operator> operatorsList;
    private ArrayList<Patient> patientsList;
    private LinearLayout sourceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_actor);
        sourceLayout = findViewById(R.id.l_listActors);
        final String EXERCISE_TYPE = getIntent().getStringExtra("exerciseType");
        final String ACTOR_TYPE = getIntent().getStringExtra("actorType");
        fillList(ACTOR_TYPE);

        fillLayout(ACTOR_TYPE,EXERCISE_TYPE);
    }



    private void fillList(String ACTOR_TYPE) {
        if (ACTOR_TYPE.equals("Patient")){
            // Get the list of patients from the database
            patientsList = new PatientDAO(this).getPatients();
        }
        else if (ACTOR_TYPE.equals("Operator")){
            // Get the list of operators from the database
            OperatorDAO listOps = new OperatorDAO(this);
            operatorsList = listOps.getOperators();
        }
    }

    private void fillLayout(String ACTOR_TYPE, String EXERCISE_TYPE) {
        if (ACTOR_TYPE.equals("Patient")){
            for (Patient patient : patientsList) {
                createNewLayoutWithActor(patient, EXERCISE_TYPE);
            }
        }
        else if (ACTOR_TYPE.equals("Operator")){
            for (Operator operator : operatorsList) {
                createNewLayoutWithActor(operator, EXERCISE_TYPE);
            }
        }
    }

    private void createNewLayoutWithActor(final Actor user, String EXERCISE_TYPE) {

        LinearLayout verticalLayout = new LinearLayout(this);
        verticalLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramsV = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        );
        verticalLayout.setPadding(2,10,2,10);
        verticalLayout.setLayoutParams(paramsV);

        LinearLayout horizontalLayout = new LinearLayout(this);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams paramsH = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        );
        horizontalLayout.setLayoutParams(paramsH);
        horizontalLayout.setWeightSum(2f);

        //TEXTVIEW
        TextView tvName = new TextView(this);
        tvName.setText(user.getName());
        tvName.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        ));
        tvName.setTextColor(0xFFFFFFFF);
        tvName.setTextSize(20);
        horizontalLayout.addView(tvName);


        TextView tvFirstName = new TextView(this);
        tvFirstName.setText(user.getFirstName());
        tvFirstName.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f
        ));
        tvFirstName.setTextColor(0xFFFFFFFF);
        tvFirstName.setTextSize(20);
        horizontalLayout.addView(tvFirstName);

        LinearLayout lineLayout = new LinearLayout(this);
        lineLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams paramsL = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1,1.0f
        );
        lineLayout.setBackgroundColor(0xFF606060);
        lineLayout.setLayoutParams(paramsL);

        verticalLayout.addView(horizontalLayout);
        verticalLayout.addView(lineLayout);

        verticalLayout.setOnClickListener(v->{
            if (EXERCISE_TYPE.equals("Static")){
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("exerciseType", "Static");
                intent.putExtra("page","exerciseStatic");
                intent.putExtra("actorType", getIntent().getStringExtra("actorType"));
                startActivity(intent);
            }
            else if (EXERCISE_TYPE.equals("Dynamic")){

                Intent intent = new Intent(this, HomePageActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("page","exerciseDynamic");
                intent.putExtra("exerciseType", "Dynamic");
                intent.putExtra("actorType", getIntent().getStringExtra("actorType"));
                startActivity(intent);
            }
        });

        sourceLayout.addView(verticalLayout);

    }

}