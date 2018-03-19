package edu.nus.trailblazelearn.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import edu.nus.trailblazelearn.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hp on 17-03-2018.
 */
public class CreateLearningTrailActivityTest {

    @Rule
    public ActivityTestRule<LearningTrailListActivity> mActivityTestRule = new ActivityTestRule<LearningTrailListActivity>(LearningTrailListActivity.class);
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(CreateLearningTrailActivity.class.getCanonicalName(), null, false);
    private LearningTrailListActivity mListTrailActivity = null;
    private Activity createLearningActivity;

    @Before
    public void setUp() throws Exception {
        mListTrailActivity = mActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mListTrailActivity = null;
    }

    @Test
    public void launchCreateActivityButton() throws Exception {
        assertNotNull(mListTrailActivity.findViewById(R.id.fab));
        onView(withId(R.id.fab)).perform(click());
        createLearningActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNotNull(createLearningActivity);
    }


}