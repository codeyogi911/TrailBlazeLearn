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
    private static SimpleDateFormat sdfFormt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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

    /**
     * API to compare End Date should
     * be before Start Date
     *
     * @param trailStartDate
     * @return
     */
    public static boolean compareStartDateWithEndDate(Date trailStartDate, Date trailEndDate) {
        Log.d(TAG, "Start compareStartDateWithEndDate API");
        Log.d(TAG, "End Date :" + trailEndDate + " Start Date :" + trailStartDate);
        boolean endDateIsBeforeStartDate = false;


        if (trailEndDate.compareTo(trailStartDate) < 0) {
            endDateIsBeforeStartDate = true;
        } else {
            endDateIsBeforeStartDate = false;
        }
        Log.d(TAG, "End compareStartDateWithEndDate API");
        return endDateIsBeforeStartDate;
    }

    /**
     * API to compare End Date should
     * be before Start Date
     *
     * @param trailStartDate
     * @return
     */
    public static boolean compareStartDateWithEndDate(String trailStartDate, String trailEndDate) {
        Log.d(TAG, "Start compareStartDateWithEndDate API");
        Log.d(TAG, "End Date :" + trailEndDate + " Start Date :" + trailStartDate);
        boolean endDateIsBeforeStartDate = false;
        try {
            Date trailStartObjDate = sdfFormt.parse(trailEndDate);
            Date trailEndObjDate = sdfFormt.parse(trailStartDate);
            if (trailEndObjDate.compareTo(trailEndObjDate) < 0) {
                endDateIsBeforeStartDate = true;
            } else {
                endDateIsBeforeStartDate = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "End compareStartDateWithEndDate API");
        return endDateIsBeforeStartDate;
    }
  }
