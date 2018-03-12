package edu.nus.trailblazelearn.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;
import android.widget.DatePicker;

/**
 * Created by Romila on 03-03-2018.
 */

public class DateUtil {

    private static final String TAG = "DateUtil";
    /**
     * API to return Date
     * from String date object
     * @param date
     * @return
     */
    public static Date constructDateFromString(String date){
        Log.d(TAG, "constructDateFromString started: ");
        Log.d(TAG,"Date value is :"+date);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = new Date();
        try {
            dateObj = dateFormat.parse(date);

        }catch(ParseException parseExceptObj){
            Log.e(TAG,"Exception while parsing"+parseExceptObj);
        }
        Log.i(TAG,"Formatted date is:"+dateObj);
        return dateObj;
    }

  }
