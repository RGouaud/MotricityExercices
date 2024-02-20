package com.example.ex_motricite;


import static android.os.SystemClock.sleep;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import android.content.ContentValues;
import android.content.Context;

import androidx.test.espresso.intent.rule.IntentsTestRule;
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

    /**
     * The rule for the activity
     */
    @Rule
    public IntentsTestRule<HomePageActivity> mActivityRule = new IntentsTestRule<>(HomePageActivity.class);


    /**
     * Test when the user click on the exercises button
     */
    @Test
    public void test_navigation_to_exercises() {
        //GIVEN


        //WHEN
        onView(withId(R.id.exercises)).perform(click());

        //THEN
       onView(withId(R.id.b_start_exercise)).check(matches(isDisplayed()));

    }

    /**
     * Test when the user click on the consult profiles button
     */
    @Test
    public void test_navigation_to_profiles() {
        //GIVEN

        //WHEN
        onView(withId(R.id.profiles)).perform(click());

        //THEN
        onView(withId(R.id.ll_profiles)).check(matches(isDisplayed()));

    }

    @Test
    public void test_navigation_to_tests() {
        //GIVEN

        //WHEN
        onView(withId(R.id.tests)).perform(click());

        //THEN
        onView(withId(R.id.b_filters)).check(matches(isDisplayed()));

    }

    /*@Test
    public void test_navigation_to_parameters(){
        //GIVEN

        //WHEN
        //onView(withId(R.id.parameters)).perform(click());

        //THEN
        //onView(withId(R.id.element)).check(matches(isDisplayed()));

    }*/
}