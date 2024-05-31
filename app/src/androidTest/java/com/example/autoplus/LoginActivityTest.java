package com.example.autoplus;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginInvalidCredentials() {
        onView(withId(R.id.editTextUsername)).perform(ViewActions.typeText("invalid@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText("wrongpassword"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(ViewActions.click());

            }

    @Test
    public void testLoginSuccess() {
        onView(withId(R.id.editTextUsername)).perform(ViewActions.typeText("admin@autoplus.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText("admin"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(ViewActions.click());
    }
}
