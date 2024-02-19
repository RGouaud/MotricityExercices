package com.example.ex_motricite;


import android.content.ContentValues;
import android.content.Context;

import androidx.test.espresso.intent.rule.IntentsTestRule;

import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isFocusable;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The {@code ListUserPageActivityTest} class represents the instrumentation test for the {@link ListUserPageActivity} class.
 *
 * <p>
 * This class tests the functionality of the {@link ListUserPageActivity} activity by simulating user interactions
 * </p>
 *
 * <p>
 * Author: Alexandre Ferreira
 * Version: 1.0
 * </p>
 */
@RunWith(AndroidJUnit4.class)
public class ListUserPageActivityTest {

    /**
     * The instance of the database
     */
    private BdSQLiteOpenHelper accesBD;

    /**
     * The context of the application
     */
    private Context ct;

    /**
     * The id of the patient retrieved from the database
     */
    private long idPatient;

    /**
     * The id of the operator retrieved from the database
     */
    private long idOperator;

    /**
     * The patient
     */
    private Patient patient;

    /**
     * The operator
     */
    private Operator operator;

    /**
     * The rule for the activity
     */
    @Rule
    public IntentsTestRule<ListUserPageActivity> mActivityRule = new IntentsTestRule<>(ListUserPageActivity.class);


    /**
     * Set up all the variables needed for the tests
     */
    @Before
    public void setUp(){
        ct = InstrumentationRegistry.getInstrumentation().getTargetContext();
        accesBD = new BdSQLiteOpenHelper(ct, "BDMotricity", null, 1);
        idPatient = 0;
        patient = new Patient("namePatient", "firstNamePatient", "01/09/1111", "remarks");
        operator = new Operator("nameOperator", "firstNameOperator");

    }

    /**
     * Clean up all the variables needed for the tests
     */
    @After
    public void tearDown(){
        accesBD.close();
        ct = null;
        idPatient = 0;
        patient = null;
        operator = null;
    }

    /**
     * Test if add button navigate to the add patient page when the patient button is selected
     */
    @Test
    public void test_add_button_when_patient_is_selected(){
        //GIVEN

        //WHEN
        onView(withId(R.id.b_TogglePatient)).perform(click());
        onView(withId(R.id.b_Add)).perform(click());

        //THEN
        intended(allOf(
                hasComponent(CrudUserActivity.class.getName()),
                toPackage("com.example.ex_motricite")
        ));

        onView(withId(R.id.et_birthdate)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * Test if add button navigate to the add operator page when the operator button is selected
     */
    @Test
    public void test_add_button_when_operator_is_selected(){
        //GIVEN

        //WHEN
        onView(withId(R.id.b_ToggleOperator)).perform(click());
        onView(withId(R.id.b_Add)).perform(click());

        //THEN
        intended(allOf(
                hasComponent(CrudUserActivity.class.getName()),
                toPackage("com.example.ex_motricite")
        ));

        onView(withId(R.id.et_birthdate)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }


    /**
     * Test if the activity display correctly operators
     */

    /**
     * Test if the activity display correctly patients
     */
    @Test
    public void test_if_patients_are_displayed(){
        //GIVEN
        ContentValues patientValues = new ContentValues();
        patientValues.put("name",patient.getName() );
        patientValues.put("firstName", patient.getFirstName());
        patientValues.put("birthDate", patient.getBirthDate());
        patientValues.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("Patient", null, patientValues);

        try {
            recreateActivity();

            //WHEN
            //Click on the patient button
            onView(withId(R.id.b_TogglePatient)).perform(click());

            //THEN
            //Verify if the patient is displayed
            onView(withText("Birthdate: " + patient.getBirthDate())).check(matches(isDisplayed()));
        }finally {
            accesBD.getReadableDatabase().execSQL("delete from Patient where idPatient="+idPatient+";");
        }
    }

    /**
     * Test if the activity display correctly the edit button for the operator and if it navigate to the edit page of the operator
     */
    @Test
    public void test_if_operator_edit_button_navigate_to_edit_page(){
        //GIVEN
        ContentValues operatorValues = new ContentValues();
        operatorValues.put("name",operator.getName() );
        operatorValues.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("Operator", null, operatorValues);

        recreateActivity();

        //WHEN
        try {
            onView(withId(R.id.b_ToggleOperator)).perform(click());
            //Verify if the edit button is displayed with his content description because we can't set correctly the id of the button
            onView(allOf(withContentDescription("navigate to modify" + operator.getName()), isDisplayed()))
                    .check(matches(isDisplayed()))
                    .perform(click());

            //THEN
            intended(allOf(
                    hasComponent(CrudUserActivity.class.getName()),
                    toPackage("com.example.ex_motricite")
            ));

            //Verify if the fields are correctly filled with the operator's data and if he's focusable and clickable to see if its the edit page
            onView(withId(R.id.et_firstname))
                    .check(matches(isClickable()))
                    .check(matches(isFocusable()))
                    .check(matches(withText(operator.getFirstName())));

            //Verify if the birthdate field is not visible because it's an operator
            onView(withId(R.id.et_birthdate)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        }finally {
            accesBD.getReadableDatabase().execSQL("delete from Operator where idOperator="+idOperator+";");
        }
    }

    /**
     * Test if the activity display correctly the edit button for the patient and if it navigate to the edit page of the patient
     */
    @Test
    public void test_if_patient_edit_buton_navigate_to_edit_page(){
        //GIVEN
        ContentValues patientValues = new ContentValues();
        patientValues.put("name",patient.getName() );
        patientValues.put("firstName", patient.getFirstName());
        patientValues.put("birthDate", patient.getBirthDate());
        patientValues.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("patient", null, patientValues);

        recreateActivity();

        //WHEN
        try {
            onView(withId(R.id.b_TogglePatient)).perform(click());
            onView(allOf(withContentDescription("navigate to modify" + patient.getName()), isDisplayed()))
                    .check(matches(isDisplayed()))
                    .perform(click());

            //THEN
            intended(allOf(
                    hasComponent(CrudUserActivity.class.getName()),
                    toPackage("com.example.ex_motricite")
            ));
            //Verify if the fields are correctly filled with the patient's data and if he's focusable and clickable to see if its the edit page of the patient
            onView(withId(R.id.et_birthdate))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                    .check(matches(isClickable()))
                    .check(matches(isFocusable()))
                    .check(matches(withText(patient.getBirthDate())));
        }finally {
            accesBD.getReadableDatabase().execSQL("delete from Patient where idPatient="+idPatient+";");
        }
    }

    /**
     * Test if the operator layout is correctly displayed and if it navigate to the details page of the operator
     */
    @Test
    public void test_if_operator_layout_navigate_to_details(){
        //GIVEN
        ContentValues operatorValues = new ContentValues();
        operatorValues.put("name",operator.getName() );
        operatorValues.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("Operator", null, operatorValues);

        recreateActivity();

        //WHEN
        try {
            onView(withId(R.id.b_ToggleOperator)).perform(click());
            onView(allOf(withContentDescription("navigate to details of " + operator.getName()), isDisplayed()))
                    .check(matches(isDisplayed()))
                    .perform(click());

            //THEN
            intended(allOf(
                    hasComponent(CrudUserActivity.class.getName()),
                    toPackage("com.example.ex_motricite")
            ));
            //Verify if fields are not focusable and clickable to see if its the details page
            onView(withId(R.id.et_firstname))
                    .check(matches(not(isClickable())))
                    .check(matches(not(isFocusable())))
                    .check(matches(withText(operator.getFirstName())));

            onView(withId(R.id.et_birthdate)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
        }finally {
            accesBD.getReadableDatabase().execSQL("delete from Operator where idOperator="+idOperator+";");
        }
    }

    /**
     * Test if the patient layout is correctly displayed and if it navigate to the details page of the patient
     */
    @Test
    public void test_if_patient_layout_navigate_to_details(){
        //GIVEN
        ContentValues patientValues = new ContentValues();
        patientValues.put("name",patient.getName() );
        patientValues.put("firstName", patient.getFirstName());
        patientValues.put("birthDate", patient.getBirthDate());
        patientValues.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("patient", null, patientValues);

        recreateActivity();

        //WHEN
        try {
            onView(withId(R.id.b_TogglePatient)).perform(click());
            onView(allOf(withContentDescription("navigate to details of " + patient.getName()), isDisplayed()))
                    .check(matches(isDisplayed()))
                    .perform(click());

            //THEN
            intended(allOf(
                    hasComponent(CrudUserActivity.class.getName()),
                    toPackage("com.example.ex_motricite")
            ));
            //Verify if fields are not focusable and clickable to see if its the details page
            onView(withId(R.id.et_birthdate))
                    .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                    .check(matches(not(isClickable())))
                    .check(matches(not(isFocusable())))
                    .check(matches(withText(patient.getBirthDate())));
        }finally {
            accesBD.getReadableDatabase().execSQL("delete from Patient where idPatient="+idPatient+";");
        }
    }


    /**
     * Test if data is correctly refreshed after editing an operator
     */
    @Test
    public void test_if_data_is_correctly_refreshed_after_editing_an_operator(){
        //GIVEN
        ContentValues operatorValues = new ContentValues();
        operatorValues.put("name",operator.getName() );
        operatorValues.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("Operator", null, operatorValues);

        recreateActivity();

        try {
            onView(withId(R.id.b_ToggleOperator)).perform(click());
            //Verify if the operator is displayed
            onView(withText("Name : " + operator.getName())).check(matches(isDisplayed()));
        }finally{
            //WHEN
            accesBD.getWritableDatabase().execSQL("update Operator set name='newName' where idOperator="+idOperator+";");
        }
        recreateActivity();

        //Click on the operator button to simulate the refresh from the edit page
        try {
            onView(withId(R.id.b_TogglePatient)).perform(click());
            onView(withId(R.id.b_ToggleOperator)).perform(click());

            //THEN
            //Verify if the operator is displayed with his new name
            onView(withText("Name : newName")).check(matches(isDisplayed()));
        }finally {
            accesBD.getReadableDatabase().execSQL("delete from Operator where idOperator="+idOperator+";");
        }
    }

    /**
     * Test if data is correctly refreshed after editing a patient
     */
    @Test
    public void test_if_data_is_correctly_refreshed_after_editing_a_patient(){
        //GIVEN
        ContentValues patientValues = new ContentValues();
        patientValues.put("name",patient.getName() );
        patientValues.put("firstName", patient.getFirstName());
        patientValues.put("birthDate", patient.getBirthDate());
        patientValues.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("Patient", null, patientValues);

        recreateActivity();

        try {
            onView(withId(R.id.b_TogglePatient)).perform(click());
            //Verify if the operator is displayed
            onView(withText("Name : " + patient.getName())).check(matches(isDisplayed()));
        }finally {
            //WHEN
            accesBD.getWritableDatabase().execSQL("update Patient set name='newName' where idPatient="+idPatient+";");
        }
        recreateActivity();

        try {
            onView(withId(R.id.b_TogglePatient)).perform(click());

            //THEN
            //Verify if the operator is displayed with his new name
            onView(withText("Name : newName")).check(matches(isDisplayed()));
        }finally {
            accesBD.getReadableDatabase().execSQL("delete from Patient where idPatient="+idPatient+";");
        }
    }

    /**
     * Test if data is correctly deleted after deleting an operator
     */
    @Test
    public void test_if_data_is_correctly_deleted_after_deleting_an_operator(){
        //GIVEN
        ContentValues operatorValues = new ContentValues();
        operatorValues.put("name",operator.getName() );
        operatorValues.put("firstName", operator.getFirstName());
        idOperator = accesBD.getWritableDatabase().insert("Operator", null, operatorValues);

        recreateActivity();

        try {
            onView(withId(R.id.b_ToggleOperator)).perform(click());
            onView(withText("Name : " + operator.getName())).check(matches(isDisplayed()));
        }finally {
            //WHEN
            accesBD.getWritableDatabase().execSQL("delete from Operator where idOperator="+idOperator+";");
        }
        recreateActivity();

        onView(withId(R.id.b_TogglePatient)).perform(click());
        onView(withId(R.id.b_ToggleOperator)).perform(click());

        //THEN
        onView(withText("Name : " + operator.getName())).check(doesNotExist());
    }

    /**
     * Test if data is correctly deleted after deleting a patient
     */
    @Test
    public void test_if_data_is_correctly_deleted_after_deleting_a_patient(){
        //GIVEN
        ContentValues patientValues = new ContentValues();
        patientValues.put("name",patient.getName() );
        patientValues.put("firstName", patient.getFirstName());
        patientValues.put("birthDate", patient.getBirthDate());
        patientValues.put("remarks", patient.getRemarks());
        idPatient = accesBD.getWritableDatabase().insert("Patient", null, patientValues);

        recreateActivity();
        try {
            onView(withId(R.id.b_TogglePatient)).perform(click());
            onView(withText("Name : " + patient.getName())).check(matches(isDisplayed()));
        }
        finally {
            //WHEN
            accesBD.getWritableDatabase().execSQL("delete from Patient where idPatient=" + idPatient + ";");
        }
        recreateActivity();

        onView(withId(R.id.b_TogglePatient)).perform(click());

        //THEN
        onView(withText("Name : " + patient.getName())).check(doesNotExist());
    }

    /**
     * Recreate the activity to refresh data
     */
    public void recreateActivity() {
        mActivityRule.getActivity().runOnUiThread(() -> {
            mActivityRule.getActivity().recreate();
        });
    }
}