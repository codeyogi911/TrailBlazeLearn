package edu.nus.trailblazelearn.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.utility.dbUtil;

public class User {
    protected static final String TAG = "USER.CLASS";
    protected dbUtil db = new dbUtil();
    protected Map<String, Object> data = new HashMap<>();
    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();


    public User() {
        data.put("name", mAuth.getDisplayName());
        data.put("email", mAuth.getEmail());
//        addUser();
    }

    public Task<Void> addUser() {
        return FirebaseFirestore.getInstance().collection("users").document(mAuth.getUid()).set(data);
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
    }

    public Map<String, Object> getData() {
        return data;
    }
}

