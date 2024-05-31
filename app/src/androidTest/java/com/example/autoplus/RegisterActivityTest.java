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
public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityScenarioRule = new ActivityScenarioRule<>(RegisterActivity.class);

    @Test
    public void testEmptyFields() {
        onView(withId(R.id.buttonRegister)).perform(ViewActions.click());
    }

    @Test
    public void testRegisterSuccess() {
        onView(withId(R.id.editTextName)).perform(ViewActions.typeText("Test User"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(ViewActions.typeText("test@example.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(ViewActions.typeText("password"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPhoneNumber)).perform(ViewActions.typeText("1234567890"), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonRegister)).perform(ViewActions.click());
    }

    @Test
    public void testBackToLogin() {
        onView(withId(R.id.textViewBackToLogin)).perform(ViewActions.click());

    }
}
