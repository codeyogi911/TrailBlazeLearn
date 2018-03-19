package edu.nus.trailblazelearn.utility;

/**
 * Created by RMukherjee on 15-03-2018.
 */

public interface ApplicationConstants {

    /**Activity and Adapter related constants**/
    String trailActivityListClassName = "LearningTrailListActivity";
    String trailActivityCreateClassName ="CreateLearningTrailActivity";
    String trailCodeParam = "trailCode";
    String underScoreConstants = "_";

    /***DB Collection related constants**/
    String learningTrailCollection ="LearningTrail";
    String userId="userId";
    String trailCode = "trailCode";
    String email = "email";

    /**Toast messages**/
    String toastMessageForDbFailure = "An error occurred,please try later!";
    String toastMessageForAddingTrailFailure="New trail creation failed,please try later!";
    String getToastMessageForUpdateTrailFailure = "Trail update failed,please try later!";
    String toastMessageForDuplicateEntryForLearningTrailCode = "Learning trail cannot be created,as record exist for trail code!";

    /**Error messages*/
    String errorDbSelectionForCollectionFailed = "Listen failed while selecting from collection Learning Trail.";


}
