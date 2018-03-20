package edu.nus.trailblazelearn.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Class that represents a discussion.
 * @author Prannoy Sablok
 */

public class Post {

    private static final String TAG = "Post.CLASS";

    private String stationID="1234";

    private String trailCode="1212";

    private String userName;
    private String message;
    private Date createdDate = Calendar.getInstance().getTime();


    public String getTrailCode() {
        return trailCode;
    }

    public void setTrailCode(String trailCode) {
        this.trailCode = trailCode;
    }
    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedDate() {


        return createdDate;
    }

    public void setCreatedDate(Date createdDate) { this.createdDate=createdDate; }

}
