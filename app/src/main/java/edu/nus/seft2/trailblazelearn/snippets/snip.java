package edu.nus.seft2.trailblazelearn.snippets;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by shashwatjain on 13/2/18.
 */

public class snip {
    public static void showToast(CharSequence text, Context context) {
//        CharSequence text = "Please login to continue...";
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
