package com.example.autoplus;

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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@RunWith(AndroidJUnit4.class)
public class MyVehiclesActivityTest {

    @Rule
    public ActivityScenarioRule<MyVehiclesActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MyVehiclesActivity.class);

    @Rule
    public IntentsTestRule<MyVehiclesActivity> intentsTestRule =
            new IntentsTestRule<>(MyVehiclesActivity.class);

    @Before
    public void setUp() {
        Intents.init();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddVehicleButton() {

        onView(withId(R.id.fabAddVehicle)).check(matches(isDisplayed())).perform(click());

        intended(hasComponent(AddNewVehicleActivity.class.getName()));
    }

    @Test
    public void testVehicleTile() {

        onView(withText("SampleVehicleNumber")).check(matches(isDisplayed()));

        onView(withText("SampleVehicleNumber"))
                .perform(ViewActions.scrollTo())
                .check(matches(isDisplayed()));
        onView(withId(R.id.buttonEdit)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonDelete)).check(matches(isDisplayed()));
    }

    @Test
    public void testBackButton() {

        onView(withId(R.id.buttonBack)).perform(click());

    }

    @Test
    public void testSwipeToRefresh() {
        onView(withId(R.id.swipeRefreshLayout)).perform(ViewActions.swipeDown());

    }
}
