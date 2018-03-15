package edu.nus.trailblazelearn.helper;

import edu.nus.trailblazelearn.exception.TrailDaoException;
import edu.nus.trailblazelearn.exception.TrailHelperException;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.utility.dbUtil;

/**
 * Created by Romila on 06-03-2018.
 */

public class LearningTrailHelper {
    private static final String TAG = "LearningTrailHelper";

    /**
     * Constructor
     */
    public LearningTrailHelper(){

    }
    /**
     * API to invoke DB passing the
     * object which needs to be persisted.
     * @param trailObj
     */
    public void createTrail(LearningTrail trailObj) throws TrailHelperException {
        try {
            dbUtil.addRecordForCollection("LearningTrail", trailObj, trailObj.getTrailCode());
        } catch (TrailDaoException daoExcept) {
            throw new TrailHelperException("Error occurred in createTrail invoking addRecordForCollection ", daoExcept);
        }


    }

    /**
     * API to invoke DB passing the
     * object which needs to be persisted.
     * @param trailObj
     */
    public void updateTrail(LearningTrail trailObj) throws TrailHelperException {
        try {
            dbUtil.addRecordForCollection("LearningTrail", trailObj, trailObj.getTrailCode());
        } catch (TrailDaoException daoExcept) {
            throw new TrailHelperException("Error occurred in updateTrail invoking addRecordForCollection ", daoExcept);
        }
    }


}
