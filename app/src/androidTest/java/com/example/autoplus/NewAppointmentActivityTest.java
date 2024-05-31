package com.example.autoplus;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NewAppointmentActivityTest {

    @Rule
    public ActivityTestRule<NewAppointmentActivity> activityRule = new ActivityTestRule<>(NewAppointmentActivity.class, true, false);

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSaveAppointmentWithValidData() {
        activityRule.launchActivity(new Intent());

        Espresso.onView(ViewMatchers.withId(R.id.editTextDateTime)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.editTextDateTime)).perform(ViewActions.replaceText("2024-06-01 10:30"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextOdoMeter)).perform(ViewActions.typeText("15000"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.spinnerVehicle)).perform(ViewActions.click());
        Espresso.onData(ViewMatchers.withText("YourVehicleNumber")).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.buttonSaveAppointment)).perform(ViewActions.click());

    }

}
