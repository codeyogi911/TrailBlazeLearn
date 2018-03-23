package edu.nus.trailblazelearn.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.activity.LearningTrailListActivity;
import edu.nus.trailblazelearn.activity.ParticipantDefault;
import edu.nus.trailblazelearn.activity.RoleSelectActivity;
import edu.nus.trailblazelearn.exception.NetworkError;
import edu.nus.trailblazelearn.utility.DbUtil;

public class User {
    private static final String TAG = "User.CLASS";
    private static User user;
    private static AppCompatActivity context;
    private Map<String, Object> data = new HashMap<>();
    private FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

    private User() {
        initialize();
    }

    public static User getInstance(Object context1) {
        if (context1 != null)
            context = (AppCompatActivity) context1;
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static User getInstance() {
        return getInstance(null);
    }

    public static void signOut() {
        user = null;
    }

    private void initialize() {
        DbUtil.readWithDocID("users", mAuth.getUid())
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                                setData(document.getData());
                            } else {
                                Log.d(TAG, "No such document, creating new user in DB");
                                Map<String, Object> temp = new HashMap<>();
                                temp.put("name", mAuth.getDisplayName());
                                temp.put("email", mAuth.getEmail());
                                setData(temp);
                                save();
                            }
                            redirect();
                        } else {
//                    Add exception handling
                            Log.d(TAG, "get failed with ", task.getException());
                            context.startActivity(new Intent(context.getApplicationContext(), NetworkError.class));
//                            NetworkError.catchException(task.getException());
//                            Intent intent = new Intent(this,NetworkError.class);
                        }
                    }
                });
    }

    private void redirect() {
        if (getData().get("isTrainer") != null && (boolean) getData().get("isTrainer")) {
            loginTrainer();
        } else if (getData().get("isParticipant") != null && (boolean) getData().get("isParticipant")) {
            loginParticipant();
        } else {
            loginRoleSelect();
        }
    }

    private void loginTrainer() {
        Intent intent = new Intent(context.getApplicationContext(), LearningTrailListActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    private void loginParticipant() {
        Intent intent = new Intent(context.getApplicationContext(), ParticipantDefault.class);
        context.startActivity(intent);
        context.finish();
    }

    private void loginRoleSelect() {
        Intent intent = new Intent(context.getApplicationContext(), RoleSelectActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    public void grantTrainer() {
        data.put("isTrainer", true);
        data.put("isParticipant", false);
        save();
    }

    public void grantParticipant() {
        data.put("isParticipant", true);
        data.put("isTrainer", false);
        save();
    }

    //  Saves to Firebase
    public Task<Void> save() {
        if (data.get("name") != null)
            return FirebaseFirestore.getInstance().collection("users").document(mAuth.getUid()).set(data).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    context.startActivity(new Intent(context.getApplicationContext(), NetworkError.class));

                }
            });
        else
            return null;
    }

    //  data getter
    public Map<String, Object> getData() {
        return data;
    }

    //  data setter
    public void setData(Map<String, Object> in) {
        data = in;
    }

    /**
     * API to enroll participant for
     * searched learning trail
     *
     * @param trailID which is trailCode
     */
    public Task<Void> enrollforTrail(String trailID) {
        if (isParticipant()) {
            Map<String, Object> map = (Map<String, Object>) data.get("enrolledTrails");
            if (map != null) {
                if (map.get(trailID.toUpperCase()) == null)
                    map.put(trailID, true);
            } else {
                map = new HashMap<>();
                map.put(trailID, true);
                data.put("enrolledTrails", map);
            }
        }
        return save();
    }

//    /**
//     * API to unenroll for trailCode
//     * while trainer tries to delete
//     * any learning trail
//     * @param trailID
//     */
//
//    public void deleteTrail(String trailID) {
//        List<String> list = (List<String>) data.get("enrolledTrails");
//        if (list != null) {
//            if (list.indexOf(trailID) > 0) {
//                list.remove(list.indexOf(trailID));
//            }
//        }
//        save();
//    }

    //  Returns true if user is trainer
    private boolean isTrainer() {
        return data.get("isTrainer") != null;
    }

    //  Returns true if user is participant
    private boolean isParticipant() {
        return data.get("isParticipant") != null;
    }
}