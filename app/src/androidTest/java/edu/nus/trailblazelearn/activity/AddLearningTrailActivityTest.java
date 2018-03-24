package edu.nus.trailblazelearn.activity;

import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.model.User;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddLearningTrailActivityTest {

    @Rule
    public ActivityTestRule<CreateLearningTrailActivity> mActivityTestRule = new ActivityTestRule<CreateLearningTrailActivity>(CreateLearningTrailActivity.class);
    private CreateLearningTrailActivity mAddTrailActivity = null;
//    private User user;


    @Before
    public void setUp() throws Exception {
        mAddTrailActivity = mActivityTestRule.getActivity();
//        user = User.getInstance();

        Map<String, Object> temp = new HashMap<>();
        temp.put("name", "Romila Mukherjee");
        temp.put("email", "ms.romila@gmail.com");
        temp.put("isTrainer", true);

        User.grantTrainer();
    }

    @Test
    public void onCreate() {
        assertNotNull(mAddTrailActivity);
        String trailName = "Ganges Trails";
        String trailDescription = "Water";
        int startYear = 2018;
        int startMonth = 04;
        int startDay = 25;

        String startDate = "25-04-2018";
        String endDate = "27-04-2018";

        getInstrumentation().waitForIdleSync();
        //input text in edit text for Trail Name
        Espresso.onView(withId(R.id.et_trail_name)).perform(typeText(trailName));
        Espresso.onView(withId(R.id.et_trail_description)).perform(typeText(trailDescription));
        // Matches a view that is on the screen AND has the id R.id.some_button
        Espresso.onView(withId(R.id.et_trail_startdate)).perform(typeText(startDate));
        Espresso.onView(withId(R.id.et_trail_enddate)).perform(typeText(endDate));

        Espresso.onView(withId(R.id.btn_save)).perform(click());

    }


    public void populateCreateActivity() {

    }


    @After
    public void tearDown() throws Exception {
        mAddTrailActivity = null;
    }


}