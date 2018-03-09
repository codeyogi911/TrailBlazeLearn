package edu.nus.trailblazelearn.utility;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public final class dbUtil {
    private static final String TAG = "dbUtil";
    public static FirebaseFirestore db;

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

    /**
     * API to persist an object
     * into DB
     * @param collectionName
     * @param data
     */
    public static void addRecordForCollection(String collectionName, Object data) {
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
