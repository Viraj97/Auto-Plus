package com.example.autoplus;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AddNewVehicleActivityTest {

    @Rule
    public ActivityScenarioRule<AddNewVehicleActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AddNewVehicleActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @Test
    public void testSaveVehicleButtonDisplayed() {
        onView(withId(R.id.buttonSave)).check(matches(isDisplayed()));
    }

    @Test
    public void testSaveVehicleWithValidData() {
        onView(withId(R.id.editTextVehicleNumber)).perform(ViewActions.typeText("123ABC"));
        onView(withId(R.id.editTextBrand)).perform(ViewActions.typeText("Toyota"));
        onView(withId(R.id.editTextModel)).perform(ViewActions.typeText("Camry"));
        onView(withId(R.id.editTextManufactureYear)).perform(ViewActions.typeText("2020"));
        onView(withId(R.id.editTextODO)).perform(ViewActions.typeText("10000"),ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextColor)).perform(ViewActions.typeText("Black"),ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonSave)).perform(ViewActions.click());
//        onView(withText("Vehicle saved successfully")).check(matches(isDisplayed()));
    }


    @Test
    public void testSaveVehicleWithEmptyFields() {
        onView(withId(R.id.buttonSave)).perform(ViewActions.click());
        onView(withText("All fields are required")).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() {
        Intents.release();
    }
}
