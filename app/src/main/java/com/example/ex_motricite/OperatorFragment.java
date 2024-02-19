package com.example.ex_motricite;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OperatorFragment} factory method to
 * create an instance of this fragment.
 */
public class OperatorFragment extends Fragment {
    LinearLayout layoutPatient;
    private LinearLayout svList;
    private Button buttonAdd;
    private ArrayList<Operator> operators;
    private static final String USER_ID_EXTRA = "UserId";
    private static final String WHITE_COLOR_HEX = "#FFFFFF";

    private static final String USER_TYPE_OPERATOR = "operator";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutPatient = getActivity().findViewById(R.id.ll_patient);

        svList = getActivity().findViewById(R.id.l_listOperators);
        buttonAdd = getActivity().findViewById(R.id.b_AddOperator);
        layoutPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                PatientFragment fragment = new PatientFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        initializeViews();
        OperatorDAO operatorDAO = new OperatorDAO(getActivity());
        setOrientationLock();

        try {
            operators = operatorDAO.getOperators();
        } catch (Exception e) {
            handleDataRetrievalError("Erreur lors de la récupération des opérateurs", e);
        }

        setButtonClickListeners();
        displayActors(operators);
    }

    private void createActorLayout(Actor actor) {
        // Create a new LinearLayout for each actor to display
        LinearLayout aLayout = new LinearLayout(getActivity());

        // Setup the layout
        aLayout.setOrientation(LinearLayout.HORIZONTAL);
        aLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        if (actor instanceof Operator) {
            aLayout.setBackgroundResource(R.drawable.rounded_layout);
            aLayout.setPadding(20, 50, 0, 50);

            Operator operator = (Operator) actor;
            aLayout.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CrudUserActivity.class);
                intent.putExtra("User", USER_TYPE_OPERATOR);
                intent.putExtra("Crud", "read");
                intent.putExtra(USER_ID_EXTRA, String.valueOf(operator.getId()));
                startActivity(intent);
            });
        }

        // Create TextView for name and firstname
        TextView name = new TextView(getActivity());
        TextView firstName = new TextView(getActivity());

        // Setup TextViews
        name.setText(getString(R.string.name_format, actor.getName()));
        name.setTextColor(Color.parseColor(WHITE_COLOR_HEX));

        firstName.setText(getString(R.string.first_name_format, actor.getFirstName()));
        firstName.setTextColor(Color.parseColor(WHITE_COLOR_HEX));

        name.setLayoutParams(new LinearLayout.LayoutParams(
                0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f)); // weight

        firstName.setLayoutParams(new LinearLayout.LayoutParams(
                0, // width
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2f)); // weight

        // Create and configure ImageButtons
        ImageButton boutonModify = new ImageButton(getActivity());
        boutonModify.setImageResource(android.R.drawable.ic_menu_set_as);
        boutonModify.setBackgroundColor(Color.parseColor("#00000000"));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(50, 50);
        boutonModify.setLayoutParams(params);

        long actorId = actor.getId();
        boutonModify.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CrudUserActivity.class);
            intent.putExtra("User", USER_TYPE_OPERATOR);
            intent.putExtra("Crud", "update");
            intent.putExtra(USER_ID_EXTRA, String.valueOf(actorId));
            startActivity(intent);
        });

        // Setup spaces between layouts
        Space space = new Space(getActivity());
        space.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                16));

        // Add TextView to LinearLayout
        aLayout.addView(name);
        aLayout.addView(firstName);
        aLayout.addView(boutonModify);


        // Add LinearLayout to sv_list
        if (actor instanceof Operator) {
            svList.addView(aLayout);
        }
        svList.addView(space);
    }

    private void displayActors(ArrayList<? extends Actor> actors) {
        svList.removeAllViews();
        if (actors != null && !actors.isEmpty()) {
            for (Actor actor : actors) {
                createActorLayout(actor);
            }
        }
    }
    private void initializeViews() {
    }

    private void setOrientationLock() {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void handleDataRetrievalError(String errorMessage, Exception e) {
        e.printStackTrace();
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void setButtonClickListeners() {
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CrudUserActivity.class);
            String userType = USER_TYPE_OPERATOR;
            intent.putExtra("User", userType);
            intent.putExtra("Crud", "create");
            intent.putExtra(USER_ID_EXTRA, "");
            startActivity(intent);
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_operator, container, false);
    }
}