package edu.nus.trailblazelearn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import edu.nus.trailblazelearn.R;

public class RoleSelectActivity extends AppCompatActivity {
//    Map<String,Object> map;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUI();
    }

    private void createUI() {
//        map = (Map<String, Object>) getIntent().getExtras().getSerializable("user");
        setContentView(R.layout.activity_role_select);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    public void loginTrainer(View view) {
        Intent intent = new Intent(this, TrainerDefault.class);
//        intent.putExtra("user",(Serializable) map);
        startActivity(intent);
        finish();
    }

    public void loginParticipant(View view) {
        Intent intent = new Intent(this, ParticipantDefault.class);
//        intent.putExtra("user",(Serializable) map);
        startActivity(intent);
        finish();
    }

    public void signOut(MenuItem menuItem) {
//        localDB localDB = new localDB();
//        edu.nus.trailblazelearn.model.User user = new edu.nus.trailblazelearn.model.User();
//        user.setData(localDB.getFromLocal(this,"user"));
////        localDB.getFromLocal("user")
//                user.save().addOnCompleteListener(
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
        navtoLogin();
//                        } else {
////                          Handle error
//                        }
//                    }
//                });
//    }
    }

    public void navtoLogin() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        edu.nus.trailblazelearn.model.User.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
