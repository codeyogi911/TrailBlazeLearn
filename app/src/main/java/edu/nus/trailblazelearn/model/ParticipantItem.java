package edu.nus.trailblazelearn.model;

import java.io.Serializable;
import java.util.ArrayList;



public class ParticipantItem implements Serializable {
    private String  userId;
    private String learningTrailId;
    private int trailStationId;


    private ArrayList<String> imageUri;
    private ArrayList<String> videoUri;
    private ArrayList<String> audioUri;
    private ArrayList<String> fileUri;
    private String description;

    public ParticipantItem() {

    }

    public ParticipantItem(String userId, String learningTrailId, int trailStationId, String description) {
        this.userId = userId;
        this.learningTrailId = learningTrailId;
        this.trailStationId = trailStationId;
        this.fileUri = fileUri;
        this.description = description;
    }
    public ArrayList<String> getImageUri() {
        return imageUri;
    }

    public void setImageUri(ArrayList<String> imageUri) {
        this.imageUri = imageUri;
    }

    public ArrayList<String> getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(ArrayList<String> videoUri) {
        this.videoUri = videoUri;
    }

    public ArrayList<String> getAudioUri() {
        return audioUri;
    }

    public void setAudioUri(ArrayList<String> audioUri) {
        this.audioUri = audioUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLearningTrailId() {
        return learningTrailId;
    }

    public void setLearningTrailId(String learningTrailId) {
        this.learningTrailId = learningTrailId;
    }

    public int getTrailStationId() {
        return trailStationId;
    }

    public void setTrailStationId(int trailStationId) {
        this.trailStationId = trailStationId;
    }

    public ArrayList<String> getFileUri() {
        return fileUri;
    }

    public void setFileUri(ArrayList<String> fileUrl) {
        this.fileUri = fileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
