package edu.nus.trailblazelearn.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nus.trailblazelearn.utility.dbUtil;

/**
 * Created by RMukherjee.
 */

public class LearningTrail {

    private static final String TAG = "LearningTrail.CLASS";

    private String trailName;
    private String trailCode;
    private String trailDescription;
    private Date startDate;
    private Date endDate;
    private String userId;

    public LearningTrail() {
        this.trailName = trailName;
        this.trailCode = trailCode;
        this.trailDescription = trailDescription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public String getTrailName() {
        return trailName;
    }

    public void setTrailName(String trailName) {
        this.trailName = trailName;
    }

    public String getTrailCode() {
        return trailCode;
    }

    public void setTrailCode(String trailCode) {
        this.trailCode = trailCode;
    }

    public String getTrailDescription() {
        return trailDescription;
    }

    public void setTrailDescription(String trailDescription) {
        this.trailDescription = trailDescription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
