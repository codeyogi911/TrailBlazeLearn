package edu.nus.trailblazelearn.utility;

/**
 * Created by RMukherjee on 15-03-2018.
 */

public interface ApplicationConstants {

    /**Activity and Adapter related constants**/
    String trailActivityListClassName = "LearningTrailListActivity";
    String trailActivityCreateClassName ="CreateLearningTrailActivity";
    String trailStaionListActivity = "TrailStationListActivity";
    String participantItemListActivity = "ParticipantItemList";
    String createTrailStationActivity="CreateTrailStationActivity";
    String trailStationDetailsActivity="TrailStationDetailsActivity";
    String trailStationAdapter="TrailStationAdapter";
    String mapsActivity ="MapsActivity";
    String trailCodeParam = "trailCode";
    String trailCodeMap="trailCodeMap";
    String underScoreConstants = "_";
    String trailStation = "TrailStation";
    String dateFormat = "yyyy-MM-dd";


    /***DB Collection related constants**/
    String learningTrailCollection ="LearningTrail";
    String userId="userId";
    String trailCode = "trailCode";
    String email = "email";
    String stationID = "stationID";
    String Post = "Post";
    String createdDate = "createdDate";
    String users = "users";
    String stationSize="stationSize";
    String stationLocation="stationLocation";
    String address="address";
    String stationName = "stationName";
    String latlng="latlng";
    String TrailStation="TrailStation";

    /**Toast messages**/
    String toastMessageForDbFailure = "An error occurred,please try later!";
    String toastMessageForAddingTrailFailure="New trail creation failed,please try later!";
    String getToastMessageForUpdateTrailFailure = "Trail update failed,please try later!";
    String toastMessageForDuplicateEntryForLearningTrailCode = "Learning trail cannot be created,as record exist for trail code!";

    /**Error messages*/
    String errorDbSelectionForCollectionFailed = "Listen failed while selecting from collection Learning Trail.";

    /*result message on upload of files*/
    String imageUploadResult = "Image Uploaded Successfully";
    String videoUploadResult = "Video Uploaded Successfully";
    String audioUploadResult = "Audio Uploaded Successfully";
    String documentUploadResult = "Document Uploaded Successfully";

}
