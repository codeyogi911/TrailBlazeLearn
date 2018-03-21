package edu.nus.trailblazelearn.helper;

import android.util.Log;

import java.util.List;

import edu.nus.trailblazelearn.exception.TrailDaoException;
import edu.nus.trailblazelearn.exception.TrailHelperException;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DbUtil;

/**
 * Created by RMukherjee on 06-03-2018.
 */

public class LearningTrailHelper {
    private static final String TAG = "LearningTrailHelper.class";

    /**
     * Constructor
     */
    public LearningTrailHelper(){

    }
    /**
     * API to invoke DB passing the
     * object which needs to be persisted.
     * @param trailObj of type LearningTrail
     */
    public void createTrail(LearningTrail trailObj) throws TrailHelperException {
        try {
            DbUtil.addRecordForCollection(ApplicationConstants.learningTrailCollection, trailObj, trailObj.getTrailCode());
        } catch (TrailDaoException daoExcept) {
            throw new TrailHelperException("Error occurred in createTrail invoking addRecordForCollection ", daoExcept);
        }


    }

    /**
     * API to invoke DB passing the
     * object which needs to be persisted.
     * @param trailObj of type LearningTrail
     */
    public void updateTrail(LearningTrail trailObj) throws TrailHelperException {
        try {
            DbUtil.addRecordForCollection(ApplicationConstants.learningTrailCollection, trailObj, trailObj.getTrailCode());
        } catch (TrailDaoException daoExcept) {
            throw new TrailHelperException("Error occurred in updateTrail invoking addRecordForCollection ", daoExcept);
        }
    }

    /**
     * API which check for duplicate
     * trail code against DB for Collection
     * LearningTrail
     *
     * @param trailCodeStr
     * @return
     */

    public boolean IsTrailCodeDuplicate(String trailCodeStr) {
        List<LearningTrail> learningTrailsLstObj = DbUtil.fetchTrailListForTrailCode(trailCodeStr);
        if (learningTrailsLstObj.size() > 0) {
            Log.i(TAG, "Trail code already exist" + trailCodeStr);
            return true;
        }
        Log.i(TAG, "Trail code does not exist" + trailCodeStr);
        return false;
    }
}
