package edu.nus.seft2.trailblazelearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import edu.nus.seft2.trailblazelearn.snippets.snip;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check for Fb login
        if (checkLoginStatus()) {
            setContentView(R.layout.activity_main);

        } else {
            handleUserLogin();
        }
    }

    private boolean checkLoginStatus() {
        return AccessToken.getCurrentAccessToken() != null;
    }

    private void handleUserLogin() {
        setContentView(R.layout.login_window);
        final String EMAIL = "email";

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                snip.showToast("Login Successful", getApplicationContext());
                setContentView(R.layout.activity_main);
                Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(myToolbar);
//                getSupportActionBar().a;
            }

            @Override
            public void onCancel() {
                // App code
                snip.showToast("Please login to continue...", getApplicationContext());
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }

        });
    }

}
