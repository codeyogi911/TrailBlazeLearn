package edu.nus.trailblazelearn.helper;

import android.util.Log;

import edu.nus.trailblazelearn.model.Post;
import edu.nus.trailblazelearn.utility.dbUtil;

/**
 * Class that has various methods to interact with database for discussion purposes
 * @author Prannoy Sablok
 */

public class PostHelper {

    private static final String TAG = "PostHelper";


    public void addPost(Post post)
    {
        try{
            dbUtil.addObjectToDB("Posts", post);
        }
        catch (Exception e)
        {
            Log.e(TAG,"Error occurred while adding post");
        }


    }

   }


