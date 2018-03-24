package edu.nus.trailblazelearn.activity;

import org.junit.Before;
import org.junit.Test;


import edu.nus.trailblazelearn.model.TrailStation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



public class TrailStationTest {

    TrailStation trailStationObj;

    @Before
    public void setUp() {
        trailStationObj = new TrailStation();
    }


    @Test

    public void createStation() {
        assertNotNull(trailStationObj);
        TrailStation trailStation = showStationDetails(trailStationObj);
    }

    /**
     * API to show trail Station
     *
     * @param trailStationObj
     * @return
     */
    private TrailStation showStationDetails(TrailStation trailStationObj) {

        trailStationObj.setTrailStationName("ISS");
        trailStationObj.setStationInstructions("Get introduced with coarses available and the infrastructure");
        trailStationObj.setStationAddress("Heng Mui Keng Terrace,Singapore");
        trailStationObj.setTrailCode("20180322_UNIVERSITY");
        trailStationObj.setGps("lat/lng: (51.07459833333334,16.962598333333336");
        boolean valid= true;
        try {
            if (valid) {

                assertNotNull(trailStationObj.getTrailStationName());
                assertNotNull(trailStationObj.getStationInstructions());
                assertNotNull(trailStationObj.getStationAddress());
                assertNotNull(trailStationObj.getTrailCode());
                assertNotNull(trailStationObj.getGps());
            }
        } catch (Exception e) {
            valid = false;
            System.out.print("Error occurred while validating fields");
        }
        return trailStationObj;

    }

    @Test
    public   void updateTrailStation() {
        TrailStation editTrailStation = showStationDetails(trailStationObj);
        try {
            assertNotNull(trailStationObj);
        } catch (Exception e) {
            System.out.print("Error occured while updating Trail Station");
        }
    }

}
