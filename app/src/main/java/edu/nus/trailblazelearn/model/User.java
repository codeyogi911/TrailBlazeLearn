package edu.nus.trailblazelearn.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.utility.dbUtil;

public class User {
    private static final String TAG = "USERINT";
    private dbUtil db = new dbUtil();
    private String name;
    private String email;
    private String UID;

    public User(String name, String email, String UID) {
        setEmail(email);
        setName(name);
        setUID(UID);
        addUser();
    }

    public void addUser() {
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("uid", UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().isEmpty()) {
                                Map<String, Object> payload = new HashMap<>();
                                payload.put("name", name);
                                payload.put("email", email);
                                payload.put("uid", UID);
                                db.addToDB("users", payload);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void setName(String name1) {
        name = name1;
    }

    private void setEmail(String email1) {
        email = email1;
    }

    public String getUID() {
        return UID;
    }

    private void setUID(String UID1) {

        UID = UID1;
    }
}

