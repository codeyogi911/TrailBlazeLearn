package edu.nus.trailblazelearn.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by RMukherjee.
 */

public class LearningTrail implements Serializable {

    private String trailName;
    private String trailCode;
    private String trailDescription;
    private Date startDate;
    private Date endDate;
    private String userId;

    /**
     * Default Constructor
     */
    public LearningTrail() {

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
