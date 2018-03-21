package edu.nus.trailblazelearn.helper;

import android.util.Log;

import edu.nus.trailblazelearn.model.Post;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DbUtil;

/**
 * Helper class interacting with DB layer
 * @author Prannoy Sablok
 */

public class PostHelper {

    private static final String TAG = "PostHelper";


    /**
     * API to invoke DB call
     * for adding discussion
     *
     * @param post
     */
    public void addPost(Post post) {
        try {
            DbUtil.addObjectToDB(ApplicationConstants.Post, post);
        } catch (Exception e) {
            Log.e(TAG, "Error occurred while adding post");
        }
    }

}


