package edu.nus.trailblazelearn.utility;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Romila on 03-03-2018.
 */

public class DateUtil {

    private static final String TAG = "DateUtil";
    private static SimpleDateFormat sdfFormt = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    /**
     * API to return Date
     * from String date object
     * @param date
     * @return
     */
    public static Date constructDateFromString(String date){
        Log.d(TAG, "Start of constructDateFromString API ");
        Log.d(TAG,"Date value is :"+date);

        Date dateObj = new Date();
        try {
            dateObj = sdfFormt.parse(date);
        }catch(ParseException parseExceptObj){
            Log.e(TAG, "Error while parsing" + parseExceptObj);
        }
        Log.d(TAG, "Formatted date is:" + dateObj);
        Log.d(TAG, "End of constructDateFromString API ");
        return dateObj;
    }

    /**
     * API to convert date to String
     *
     * @param date
     * @return
     */

    public static String constructDateToStringDate(Date date) {
        Log.d(TAG, "Started constructDateToStringDate API:" + date);
        String dateStr = null;
        if (date != null) {
            dateStr = sdfFormt.format(date);
            Log.d(TAG, "Date converted to String:" + dateStr);
        }
        Log.d(TAG, "End constructDateToStringDate API:" + date);
        return dateStr;
    }

    /**
     * API to compare startTrailDate with CurrentDate
     *
     * @param trailStartDate
     * @return
     */
    public static boolean compareStartDateWithCurrentDate(String trailStartDate) {
        Log.d(TAG, "Start compareStartDateWithCurrentDate API");
        boolean startDateisBeforeCurrentDate = false;

        String currDateStr = sdfFormt.format(new Date());
        Date currDate = constructDateFromString(currDateStr);

        Date trailStartDateObj = constructDateFromString(trailStartDate);

        if (trailStartDateObj.compareTo(currDate) < 0) {
            startDateisBeforeCurrentDate = true;
        } else {
            startDateisBeforeCurrentDate = false;
        }
        Log.d(TAG, "End compareStartDateWithCurrentDate API");
        return startDateisBeforeCurrentDate;
    }
  }
