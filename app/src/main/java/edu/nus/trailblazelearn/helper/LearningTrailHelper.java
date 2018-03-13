package edu.nus.trailblazelearn.helper;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    public void createTrail(LearningTrail trailObj) {
        dbUtil.addRecordForCollection("LearningTrail", trailObj,trailObj.getTrailCode());
    }

    /**
     * API to invoke the call for
     * fetching the trail object list
     * for logged in Trainer
     * @return
     */
    public List<LearningTrail> fetchTrailListForTrainer(){

        //List<LearningTrail> learningTrailLst = new ArrayList<LearningTrail>();
        List<LearningTrail> learningTrailLst = dbUtil.fetchTrailList("ms.romila@gmail.com");
        Log.d(TAG,"List size of LearningTrail::"+learningTrailLst.size());
        return learningTrailLst;
    }

}
