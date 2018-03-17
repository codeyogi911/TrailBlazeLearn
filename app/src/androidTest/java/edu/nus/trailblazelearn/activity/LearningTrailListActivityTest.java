package edu.nus.trailblazelearn.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.nus.trailblazelearn.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by RMukherjee on 16-03-2018.
 */
public class LearningTrailListActivityTest {

    @Rule
    public ActivityTestRule<LearningTrailListActivity> mActivityTestRule = new ActivityTestRule<LearningTrailListActivity>(LearningTrailListActivity.class);
    private LearningTrailListActivity mListTrailActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CreateLearningTrailActivity.class.getCanonicalName(),null,false);


    @Before
    public void setUp() throws Exception {
        mListTrailActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mListTrailActivity = null;
    }

    @Test
    public void onCreate() throws Exception {
        View view = mListTrailActivity.findViewById(R.id.learning_trail_recycler_view);
        assertNotNull(view);
     }



}