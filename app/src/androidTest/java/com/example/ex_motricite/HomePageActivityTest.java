package com.example.ex_motricite;

import static org.junit.jupiter.api.Assertions.*;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Button;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class HomePageActivityTest {
    private BdSQLiteOpenHelper accesBD;
    private Context ct;
    private long idPatient;
    private long idOperator;
    private Patient patient;

    private Operator operator;

    @Rule
    public IntentsTestRule<HomePageActivity> mActivityRule = new IntentsTestRule<>(HomePageActivity.class);

    @Before
    public void setUp(){
        ct = InstrumentationRegistry.getInstrumentation().getTargetContext();
        accesBD = new BdSQLiteOpenHelper(ct, "BDMotricity", null, 1);
        idPatient = 0;
        patient = new Patient("namePatient", "firstNamePatient", "19/03/2001", "remarks");
        operator = new Operator("nameOperator", "firstNameOperator");

    }

    @After
    public void tearDown(){
        accesBD.close();
        ct = null;
        idPatient = 0;
        patient = null;
        operator = null;
    }


    @Test
    public void test_navigation_to_ExercisesSettingsActivity_when_static_exercice() {
        //GIVEN
        ContentValues patientValues = new ContentValues();
        patientValues.put("name",patient.getName() );
        patientValues.put("firstName", patient.getFirstName());
        patientValues.put("birthDate", patient.getBirthDate());
        patientValues.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("patient", null, patientValues);

        ContentValues operatorValues = new ContentValues();
        operatorValues.put("name",operator.getName() );
        operatorValues.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("operator", null, operatorValues);

        //WHEN
        onView(withId(R.id.layout_static)).perform(click());

        //THEN
        intended(allOf(
                hasComponent(ExercisesSettingsActivity.class.getName()),
                toPackage("com.example.ex_motricite")
        ));

        // Vérification de la visibilité de l'élément
        onView(withId(R.id.et_interval)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        // Suppression des données de test
        accesBD.getReadableDatabase().execSQL("delete from patient where idPatient="+idPatient+";");
        accesBD.getReadableDatabase().execSQL("delete from operator where idOperator="+idOperator+";");
    }


    @Test
    public void test_Navigation_To_ExercisesSettingsActivity_When_Rhythm_Exerice() {
        //GIVEN
        ContentValues patientValues = new ContentValues();
        patientValues.put("name",patient.getName() );
        patientValues.put("firstName", patient.getFirstName());
        patientValues.put("birthDate", patient.getBirthDate());
        patientValues.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("patient", null, patientValues);

        ContentValues operatorValues = new ContentValues();
        operatorValues.put("name",operator.getName() );
        operatorValues.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("operator", null, operatorValues);

        //WHEN
        onView(withId(R.id.layout_rythm)).perform(click());

        //THEN
        intended(allOf(
                hasComponent(ExercisesSettingsActivity.class.getName()),
                toPackage("com.example.ex_motricite")


        ));
        accesBD.getReadableDatabase().execSQL("delete from patient where idPatient="+idPatient+";");
        accesBD.getReadableDatabase().execSQL("delete from operator where idOperator="+idOperator+";");
    }

    @Test
    public void test_Navigation_To_ListUserPageActivity_When_Consult_Profiles() {
        //GIVEN
        //WHEN
        //THEN
    }

}