package edu.nus.trailblazelearn.helper;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.dbUtil;

/**
 * Created by dpak1 on 3/5/2018.
 */

public class AddParticipantItemHelper extends AsyncTask<ArrayList<Uri>, Void, Void>{
    String resultMessgae = null;
    StorageReference storageReferenceImage, storageReferenceVideo, storageReferenceAudio, storageReferenceDocument;// = FirebaseStorage.getInstance().getReference("activity/Images/"+);// = storageReference.child("activity").child("images").child(uris[i].getLastPathSegment());
    private Context context;
    private HashMap<String, Uri> uriHashMap;
    private ProgressBar progressBar;
    private int uriCount;
    private int count;
    private String userEmail;
    private Uri fileUri;


    public AddParticipantItemHelper(Context context, HashMap<String, Uri> uriHashMap, String userEmail, ProgressBar progressBar) {
        super();
        this.context = context;
        this.uriHashMap = uriHashMap;
        this.userEmail = userEmail;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground(ArrayList<Uri>... uris) {
        uriCount = uris.length;
        //userEmail = (String) user.getData().get("email");
        //Uri uri = uris[0].get(0);
        for(Uri uriLocal:uris[0]){
            if(uriHashMap.get("image") == uriLocal) {
                resultMessgae = dbUtil.addFilesToDB("Ragu","images/"+uriLocal.getPath(), uriLocal, context, "Image Uploaded Successfully", progressBar, "image");

            }
            if(uriHashMap.get("video") == uriLocal) {
                resultMessgae = dbUtil.addFilesToDB("Ragu","videos/"+uriLocal.getPath(), uriLocal, context, "Video Uploaded Successfully", progressBar, "video");
            }

            if(uriHashMap.get("audio") == uriLocal) {
                resultMessgae = dbUtil.addFilesToDB("Ragu","audios/"+uriLocal.getPath(), uriLocal, context, "Audio Uploaded Successfully", progressBar, "audio");
            }
            if(uriHashMap.get("document") == uriLocal) {
                resultMessgae = dbUtil.addFilesToDB(userEmail,"documents/"+uriLocal.getPath(), uriLocal, context, "Document Uploaded Successfully", progressBar,"document");
            }
        }
        return null;
    }

    public static Object getKeyFromValue(HashMap<String, Uri> hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    /*public Uri getFileUri() {
        fileUri = dbUtil.getFileUriFronDb(userEmail, "activity/images");
        return fileUri;
    }*/
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
