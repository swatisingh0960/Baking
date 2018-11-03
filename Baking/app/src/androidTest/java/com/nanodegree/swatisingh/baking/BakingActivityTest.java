package com.nanodegree.swatisingh.baking;

import android.support.test.rule.ActivityTestRule;

import com.nanodegree.swatisingh.baking.activities.BakingActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

public class BakingActivityTest {

    @Rule
    public ActivityTestRule<BakingActivity> activityTestRule = new ActivityTestRule<>(BakingActivity.class);

    @Test
    public void clickItemOne(){
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        onData(anything()).inAdapterView(withId(R.id.recipesGridView)).atPosition(0).perform(click());

    }

    @Test

    public void clickItemFour(){
        try {
            Thread.sleep(3000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        onData(anything()).inAdapterView(withId(R.id.recipesGridView)).atPosition(3).perform(click());

    }
}
