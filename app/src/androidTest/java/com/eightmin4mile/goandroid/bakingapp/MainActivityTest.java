package com.eightmin4mile.goandroid.bakingapp;

/**
 * Created by goandroid on 6/25/18.
 */

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import com.eightmin4mile.goandroid.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void setup(){
        mActivityRule.getActivity();
    }

    @Before
    public void registerIdlingResource(){
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void testClickGridViewItem() throws Exception {
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));


        // check if the "Recipe Ingredients" button is displayed
        onView(withId(R.id.bt_ingredients)).check(matches(isDisplayed()));
        onView(withText(containsString("Ingredients")))
                .check(matches(isDisplayed()));
    }

    // Test the Ingredients button to show the ingredient list
    @Test
    public void clickOnIngredientsButton() throws Exception {

        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.bt_ingredients)).perform(click());
        onView(withText(containsString("TBLSP"))).check(matches(isDisplayed()));
    }


    @After
    public void unregisterIdlingResource(){
        if(mIdlingResource != null){
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
