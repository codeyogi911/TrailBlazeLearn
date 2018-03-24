package edu.nus.trailblazelearn.activity;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.model.User;

import static org.junit.Assert.assertNotNull;

/**
 * Created by SJain on 23-03-2018.
 * The test case should fail as the instance for this class can only exist
 * when getInstance() receives Activity class object.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersTest {

    //    User userObj;
    Map<String, Object> userMap = new HashMap<>();

    @Before
    public void setUp() {
//        userObj = User.getInstance();
    }


    @Test
    public void saveUser1() {
//        assertNotNull(userObj);
//        User userForTrailObj = pouplateUserForTrail(userObj);
    }

    /**
     * API to pouplate
     * learning trail
     *
     * @param userObj
     * @return
     */
    private User pouplateUserForTrail(User userObj) {

        //Grant user trainer role
        User.grantTrainer();
        //Create Hahmap and store User attributes
        userMap.put("name", "Sashwat Jain");
        userMap.put("email", "shashwatjain511@gmail.com");
        User.setData(userMap);
        return userObj;

    }

    /**
     * API to validate learning
     * trail fields
     *
     * @param
     */
    @Test
    public void validateUserObject2() {
        boolean valid = true;
        try {
            if (valid) {
                assertNotNull(User.getData());
                assertNotNull(User.getData().get("name"));
                assertNotNull(User.getData().get("email"));
                assertNotNull(User.getData().get("isTrainer"));
            }
        } catch (Exception e) {
            valid = false;
            System.out.print("Error occurred while validating user attributes");
        }


    }


}
