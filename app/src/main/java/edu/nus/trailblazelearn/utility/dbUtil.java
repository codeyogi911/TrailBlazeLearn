package edu.nus.trailblazelearn.utility;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nus.trailblazelearn.adapter.ParticipantItemAdapter;
import edu.nus.trailblazelearn.exception.TrailDaoException;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.ParticipantItem;
import edu.nus.trailblazelearn.model.UploadedFiles;

public final class dbUtil {
    private static final String TAG = "dbUtil";
    public static FirebaseFirestore db;
    private static StorageReference storageReference;
    public static Uri fileUri;
    public static ArrayList<ParticipantItem> participantItemArrayList = new ArrayList<>();
    public static ArrayList<String> imageUriList = new ArrayList<>();
    public static ArrayList<String> videoUriList = new ArrayList<>();
    public static ArrayList<String> audioUriList = new ArrayList<>();
    public static ArrayList<String> documentUriList = new ArrayList<>();
    private static ArrayList<String> fileTypes;

    static{
        db = FirebaseFirestore.getInstance();
    }

    QuerySnapshot lastRead;

    /**
     * API to add map with
     * values to be saved in database
     * @param collectionName
     * @param data
     */

    public static void addToDB(String collectionName, Map<String, Object> data) {
        db.collection(collectionName)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static void addObjectToDB(String collectionName, Object data) {
        db.collection(collectionName)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    //To do.. saving image/video/audio/document files to db
    public static String addFilesToDB(final String userEmail, final String child, final Uri uri, final Context context, final String resultMessage, final ProgressBar progressBar, final String check) {
        storageReference = FirebaseStorage.getInstance().getReference("activity").child(userEmail).child(child);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, resultMessage, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                if(check.equals("image")) {
                    imageUriList.add(taskSnapshot.getDownloadUrl().toString());
                }
                if(check.equals("video")) {
                    videoUriList.add(taskSnapshot.getDownloadUrl().toString());
                }
                if(check.equals("audio")) {
                    audioUriList.add(taskSnapshot.getDownloadUrl().toString());
                }
                if(check.equals("document")) {
                    documentUriList.add(taskSnapshot.getDownloadUrl().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error Uploading", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        return resultMessage;
    }

    public static void getParticipantItems(String userEmail, ProgressBar progressBar, ParticipantItemAdapter participantItemAdapter) {
        final ProgressBar progressBarFinal = progressBar;
        final ParticipantItemAdapter participantItemAdapterFinal = participantItemAdapter;
        db.collection("participantActivities").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                participantItemArrayList = (ArrayList<ParticipantItem>) documentSnapshots.toObjects(ParticipantItem.class);
                participantItemAdapterFinal.notifyDataSetChanged();
                progressBarFinal.setVisibility(View.INVISIBLE);
            }

        });

    }

    public static Uri getFileUriFronDb(String userEmail, String child) {
        storageReference = storageReference.child(userEmail).child(child);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                fileUri = uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("", "Error getting file URI");
            }
        });
        return fileUri;
    }

    /**
     * API to persist an object based on
     * passed reference id
     * into DB
     * @param collectionName
     * @param data
     */
    public static void addRecordForCollection(String collectionName, Object data,String referenceId) throws TrailDaoException {
        db.collection(collectionName).document(referenceId).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    /**
     * API to fetch list of learning trails
     * for logged in Trainer
     * @param trainerId
     * @return
     */
    public static List<LearningTrail> fetchTrailList(String trainerId){
        final List<LearningTrail> learningTrailLst = new ArrayList<LearningTrail>();

        // [START listen_multiple]
        db.collection("LearningTrail")
                .whereEqualTo("userId","ms.romila@gmail.com" )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        Log.w(TAG,"Query snapshot :"+value.getQuery());
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            LearningTrail learningTrailObj = doc.toObject(LearningTrail.class);
                            learningTrailLst.add(learningTrailObj);
                        }

                        Log.d(TAG, "Current learning trail list size for trainer: "+learningTrailLst.size());
                    }
                });
        // [END listen_multiple]

        return learningTrailLst;
    }
    //      Query DB using key and value
    public Task<QuerySnapshot> readWithKey(String collectionName, String key, String value) {
        return db.collection(collectionName).whereEqualTo(key, value)
                .get();
    }

    //      Query DB using docID
    public Task<DocumentSnapshot> readWithDocID(String collectionName, String docID) {
        return db.collection(collectionName).document(docID).get();
    }
}
