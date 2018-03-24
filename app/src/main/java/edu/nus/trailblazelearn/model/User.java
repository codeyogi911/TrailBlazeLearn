package edu.nus.trailblazelearn.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.nus.trailblazelearn.activity.LearningTrailListActivity;
import edu.nus.trailblazelearn.activity.LoginActivity;
import edu.nus.trailblazelearn.activity.ParticipantDefault;
import edu.nus.trailblazelearn.activity.RoleSelectActivity;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DbUtil;

public class User {
    private static User user;
    private static Map<String, Object> data = new HashMap<>();

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public static void signOut(final AppCompatActivity context) {
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        user = null;
                        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        context.startActivity(intent);
                        context.finish();
                    }
                });
    }

    public static Task<DocumentSnapshot> initialize() {
        return DbUtil.readWithDocID(ApplicationConstants.usersCollection_key, FirebaseAuth.getInstance()
                .getCurrentUser().getUid());
    }

    public static void loginTrainer(final AppCompatActivity context) {
        grantTrainer().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(context.getApplicationContext(), LearningTrailListActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });
    }

    public static void loginParticipant(final AppCompatActivity context) {
        grantParticipant().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(context.getApplicationContext(), ParticipantDefault.class);
                context.startActivity(intent);
                context.finish();
            }
        });
    }

    public static void loginRoleSelect(final AppCompatActivity context) {
        Intent intent = new Intent(context.getApplicationContext(), RoleSelectActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    public static Task<Void> grantTrainer() {
        Map<String, Object> map = new HashMap<>();
        map.put(ApplicationConstants.isTrainer_key, true);
        map.put(ApplicationConstants.isParticipant_key, false);
        return DbUtil.MergeData(ApplicationConstants.usersCollection_key, FirebaseAuth.getInstance().getCurrentUser().getUid(), map);
    }

    public static Task<Void> grantParticipant() {
        Map<String, Object> map = new HashMap<>();
        map.put(ApplicationConstants.isTrainer_key, false);
        map.put(ApplicationConstants.isParticipant_key, true);
        return DbUtil.MergeData(ApplicationConstants.usersCollection_key, FirebaseAuth.getInstance().getCurrentUser().getUid(), map);
    }

    //  Saves to Firebase
    public static Task<Void> save() {
        if (data.get(ApplicationConstants.username_key) != null)
            return FirebaseFirestore.getInstance().collection(ApplicationConstants.usersCollection_key)
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(data);
        else
            return null;
    }

    //  data getter
    public static Map<String, Object> getData() {
        return data;
    }

    //  data setter
    public static void setData(Map<String, Object> in) {
        data = in;
    }

    /**
     * API to enroll participant for
     * searched learning trail
     *
     * @param trailID which is trailCode
     */
    public static void enrollforTrail(final String trailID) {

        if (isParticipant()) {
            DbUtil.readWithDocID(ApplicationConstants.usersCollection_key, FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> trails = (Map<String, Object>) documentSnapshot.get(ApplicationConstants.enrolledTrails_key);
                    if (trails != null) {
                        if (trails.get(trailID.toUpperCase()) == null)
                            trails.put(trailID, true);
                    } else {
                        trails = new HashMap<>();
                        trails.put(trailID, true);
                    }
                    Map<String, Object> map = documentSnapshot.getData();
                    map.put(ApplicationConstants.enrolledTrails_key, trails);
                    DbUtil.MergeData(ApplicationConstants.usersCollection_key, FirebaseAuth.getInstance().getCurrentUser().getUid(), map);
                }
            });
        }
    }

    //  Returns true if user is participant
    private static boolean isParticipant() {
        return data.get(ApplicationConstants.isParticipant_key) != null;
    }

    //  Returns true if user is trainer
    private boolean isTrainer() {
        return data.get(ApplicationConstants.isTrainer_key) != null;
    }
}