package edu.nus.trailblazelearn.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import edu.nus.trailblazelearn.model.User;

/**
 * Created by Dharini.
 */

public class TrailStation implements Serializable {
    private static final String TAG = "TrailStation.CLASS";

    private String trailStationName;
    private String trailCode;
    private String stationInstructions;
    private String userId;
    private Integer stationId;
    private int sequence;
    private User user;
    private LatLng latLng;
    private String stationAddress;

    public String getLocationName() {
        return locationName;
    }

    private String locationName;


    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getStationId() {

        return stationId;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public TrailStation() {
        this.trailStationName = trailStationName;
        this.trailCode = trailCode;
        this.stationInstructions = stationInstructions;
        this.userId = userId;
        this.sequence = sequence;

        this.stationId = stationId;
        this.stationAddress= stationAddress;
        this.latLng= latLng;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSequence() {

        return sequence;
    }

    public void setSequence(int sequence) {

        this.sequence = sequence;
    }

    public String getStationInstructions() {

        return stationInstructions;
    }

    public void setStationInstructions(String stationInstructions) {

        this.stationInstructions = stationInstructions;
    }

    public String getTrailCode() {

        return trailCode;
    }

    public void setTrailCode(String trailCode) {

        this.trailCode = trailCode;
    }

    public String getTrailStationName() {
        return trailStationName;
    }

    public void setTrailStationName(String trailStationName) {

        this.trailStationName = trailStationName;
    }
}
