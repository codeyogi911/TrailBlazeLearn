package edu.nus.trailblazelearn.activity;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.model.User;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Ragu on 23/3/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ParticipantAddItemActivityTest {

    @Rule
    public ActivityTestRule<ParticipantAddItemActivity> activityActivityTestRule = new ActivityTestRule<ParticipantAddItemActivity>(ParticipantAddItemActivity.class);
    private ParticipantAddItemActivity participantAddItemActivity = null;
//    private User user;

    @Before
    public void setUp() throws Exception {
        participantAddItemActivity = activityActivityTestRule.getActivity();
//        user = User.getInstance(participantAddItemActivity);

        Map<String, Object> temp = new HashMap<>();
        temp.put("name", "Ragu");
        temp.put("email", "ragupathy@gmail.com");
        temp.put("isTrainer", true);

        User.grantTrainer();
    }

    @Test
    public void onCreate() {
        assertNotNull(participantAddItemActivity);

    }
}
