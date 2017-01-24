package com.nicosb.apps.ehcofan;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.graphics.drawable.ColorDrawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.View;

import com.nicosb.apps.ehcofan.activities.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by Nico on 21.01.2017.
 */

@RunWith(AndroidJUnit4.class)
public class HomeActivityEspressoTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void shouldHighlightOnClick(){
        onView(withId(R.id.po_date_1)).perform(click())
                .check(matches(withBackgroundColor(R.color.lightGreen)));
    }

    @Test
    public void shouldUnhighlightAfterClickOnOther(){
        onView(withId(R.id.po_date_1)).perform(click());
        onView(withId(R.id.po_date_2)).perform(click());
        onView(withId(R.id.po_date_1)).check(matches(not(withBackgroundColor(R.color.lightGreen))));
    }


    public static Matcher<View> withBackgroundColor(final int backgroundColor) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                int color = ((ColorDrawable) view.getBackground().getCurrent()).getColor();
                Log.w("espresso", "view has color  " + color);
                return color == view.getResources().getColor(backgroundColor);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with background color from id: " + backgroundColor);
            }
        };
    }

}
