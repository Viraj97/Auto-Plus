package com.example.autoplus;

import android.content.Intent;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testUserProfileInfo() {
        onView(withId(R.id.nav_header_title)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_header_subtitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testMyVehiclesButton() {
        onView(withId(R.id.myVehicles)).perform(ViewActions.click());

        intended(hasComponent(MyVehiclesActivity.class.getName()));
    }

    @Test
    public void testEmergencyServiceButton() {
        onView(withId(R.id.emagency)).perform(ViewActions.click());

        intended(hasComponent(EmergencyServicesActivity.class.getName()));
    }

    @Test
    public void testPromosDiscountButton() {
        onView(withId(R.id.discounts)).perform(ViewActions.click());

        intended(hasComponent(PromosDiscountsActivity.class.getName()));
    }

    @Test
    public void testNewAppointmentButton() {
        onView(withId(R.id.appointment)).perform(ViewActions.click());

        intended(hasComponent(NewAppointmentActivity.class.getName()));
    }

    @Test
    public void testRequestHistoryButton() {
        onView(withId(R.id.history)).perform(ViewActions.click());

        intended(hasComponent(ActivityRequetsHistory.class.getName()));
    }

    @Test
    public void testStatusButton() {
        onView(withId(R.id.status)).perform(ViewActions.click());

        intended(hasComponent(ActivityOngoingStatus.class.getName()));
    }

    @Test
    public void testLogoutOption() {

        onView(withId(R.id.action_logout)).perform(ViewActions.click());

        intended(hasComponent(LoginActivity.class.getName()));
    }
}
