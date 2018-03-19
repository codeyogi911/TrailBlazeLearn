package edu.nus.trailblazelearn.activity;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.nus.trailblazelearn.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

/**
 * Created by RMukherjee on 18-03-2018.
 */
public class AddLearningTrailActivityTest {

    @Rule
    public ActivityTestRule<CreateLearningTrailActivity> mActivityTestRule = new ActivityTestRule<CreateLearningTrailActivity>(CreateLearningTrailActivity.class);
    private CreateLearningTrailActivity mAddTrailActivity = null;

    @Before
    public void setUp() throws Exception {
        mAddTrailActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void addLearningTrailByTrainer() {
        assertNotNull(mAddTrailActivity);
        try {
            populateCreateActivity();
        } catch (Exception e) {
            System.out.print("Error occurred while adding Trail");
        }

    }


    public void populateCreateActivity() throws Exception {
        String trailName = "Himalaya Trails";
        String trailDescription = "Trail to Himalaya";
        int startYear = 2017;
        int startMonth = 04;
        int startDay = 25;


        //input text in edit text for Trail Name
        Espresso.onView(withId(R.id.et_trail_name)).perform(typeText(trailName));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.et_trail_description)).perform(typeText(trailDescription));
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.et_trail_startdate)).perform(click());
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(startYear, startMonth + 1, startDay));
        onView(withId(android.R.id.button1)).perform(click());

        Espresso.onView(withId(R.id.et_trail_enddate)).perform(click());
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(startYear, startMonth + 1, startDay));
        onView(withId(android.R.id.button1)).perform(click());

        Espresso.onView(withId(R.id.btn_save)).perform(click());
        mAddTrailActivity.finish();

    }


    @After
    public void tearDown() throws Exception {
        mAddTrailActivity = null;
    }


}