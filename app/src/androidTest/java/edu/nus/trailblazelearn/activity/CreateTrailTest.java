package edu.nus.trailblazelearn.activity;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.nus.trailblazelearn.model.LearningTrail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by RMukherjee on 23-03-2018.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreateTrailTest {

    LearningTrail trailObj;
    SimpleDateFormat sdfFormt = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Before
    public void setUp() {
        trailObj = new LearningTrail();
    }


    @Test

    public void createTrail1() {
        assertNotNull(trailObj);
        LearningTrail learnTrailObj = pouplateLearningTrail(trailObj);
    }

    /**
     * API to pouplate
     * learning trail
     *
     * @param trailObj
     * @return
     */
    private LearningTrail pouplateLearningTrail(LearningTrail trailObj) {

        trailObj.setTrailName("UtownTrail");
        trailObj.setUserId("ms.romila@gmail.com");
        trailObj.setTrailDescription("Trail for Utown garbage collection");
        try {
            trailObj.setStartDate(sdfFormt.parse("30-03-2018"));
            trailObj.setEndDate(sdfFormt.parse("05-04-2018"));
        } catch (ParseException e) {
            System.out.print("Exception while parsing date");
        }

        return trailObj;

    }

    /**
     * API to validate learning
     * trail fields
     *
     * @param
     */
    @Test
    public void validateTrailObject2() {
        boolean valid = true;
        try {
            if (valid) {

                assertNotNull(trailObj.getTrailName());
                assertNotNull(trailObj.getEndDate());
                assertNotNull(trailObj.getStartDate());
                assertNotNull(trailObj.getTrailDescription());
            }
        } catch (Exception e) {
            valid = false;
            System.out.print("Error occurred while validating fields");
        }


    }

    @Test
    public void createTrailCode3() {
        StringBuilder strCode = new StringBuilder();
        strCode.append(trailObj.getStartDate());
        strCode.append("_");
        strCode.append(trailObj.getTrailName());
        assertNotNull(strCode);
    }


    @Test
    public void validateStartDateLessThanEndDate4() {
        boolean endDateIsBeforeStartDate;
        if (trailObj.getStartDate().compareTo(trailObj.getEndDate()) < 0) {
            endDateIsBeforeStartDate = false;
        } else {
            endDateIsBeforeStartDate = true;
        }
        assertTrue(!endDateIsBeforeStartDate);
    }


}
