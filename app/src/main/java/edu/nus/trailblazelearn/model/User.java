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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        if (user == null) {
            context = (AppCompatActivity) context1;
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
        save();
    }

    public void grantParticipant() {
        data.put("isParticipant", true);
        save();
    }

    public void revokeTrainer() {
        data.remove("isTrainer");
        save();
    }

    public void revokeParticipant() {
        data.remove("isParticipant");
        save();
    }

    //  Saves to Firebase
    public void save() {
        FirebaseFirestore.getInstance().collection("users").document(mAuth.getUid()).set(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                context.startActivity(new Intent(context.getApplicationContext(), NetworkError.class));

            }
        });
    }

    //  data getter
    public Map<String, Object> getData() {
        return data;
    }

    //  data setter
    private void setData(Map<String, Object> in) {
        data = in;
    }

    //  Enrolls participant to the learning trail
    public void enrollforTrail(String trailID) {
        if (isParticipant()) {
            List<String> list = (List<String>) data.get("enrolledTrails");
            if (list != null) {
                list.add(trailID);
            } else {
                list = new ArrayList<>();
                list.add(trailID);
                data.put("enrolledTrails", list);
            }
            save();
        }
    }

    public void unenrollforTrail(String trailID) {
//        if (isParticipant()) {
            List<String> list = (List<String>) data.get("enrolledTrails");
            if (list != null) {
                list.remove(list.indexOf(trailID));
            }
//            else {
//                list = new ArrayList<>();
//                list.add(trailID);
//                data.put("enrolledTrails", list);
//            }
            save();
//        }
    }

    //  Returns true if user is trainer
    private boolean isTrainer() {
        return data.get("isTrainer") != null;
    }

    //  Returns true if user is participant
    private boolean isParticipant() {
        return data.get("isParticipant") != null;
    }
}