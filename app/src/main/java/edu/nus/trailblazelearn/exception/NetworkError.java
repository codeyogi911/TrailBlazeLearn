package edu.nus.trailblazelearn.exception;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.nus.trailblazelearn.R;

/**
 * Created by shashwatjain on 9/3/18.
 */

public class NetworkError extends AppCompatActivity {
//    public static void catchException(Exception e) {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_error);
    }
}
