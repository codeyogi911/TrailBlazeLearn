package edu.nus.trailblazelearn.activity;



import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import edu.nus.trailblazelearn.R;
import edu.nus.trailblazelearn.UserProfileActivity;
import edu.nus.trailblazelearn.exception.TrailActivityException;
import edu.nus.trailblazelearn.exception.TrailHelperException;
import edu.nus.trailblazelearn.helper.LearningTrailHelper;
import edu.nus.trailblazelearn.model.LearningTrail;
import edu.nus.trailblazelearn.model.User;
import edu.nus.trailblazelearn.utility.ApplicationConstants;
import edu.nus.trailblazelearn.utility.DateUtil;

import static edu.nus.trailblazelearn.utility.DateUtil.constructDateFromString;
import static edu.nus.trailblazelearn.utility.DateUtil.constructDateToStringDate;


public class CreateLearningTrailActivity extends AppCompatActivity {

    private static final String TAG = ApplicationConstants.trailActivityCreateClassName;
    DatePickerDialog datePickerDialog;
    Toolbar toolBarLearningActivity;
    private EditText edTrailName, edTrailDescription, edTrailStartDate, edTrailEndDate;
    private StringBuilder trailStartDate;
    private String trailCodeStr;
    private boolean editMode;
    private boolean addOperationSuccess;
    private boolean updateOperationSuccess;
    //    private User user;
    private String userEmail;
    private boolean isTrainer;


    @Override protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Start of onCreate API");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trail);
//        user = User.getInstance();
        userEmail = (String) User.getData().get(ApplicationConstants.email);
        isTrainer = (boolean) User.getData().get("isTrainer");
        toolBarLearningActivity = findViewById(R.id.tb_trail_header);
        setSupportActionBar(toolBarLearningActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        edTrailName = findViewById(R.id.et_trail_name);
        edTrailDescription = findViewById(R.id.et_trail_description);
        edTrailStartDate = findViewById(R.id.et_trail_startdate);
        edTrailEndDate = findViewById(R.id.et_trail_enddate);

        try {

            final LearningTrail editTrailObj = (LearningTrail) getIntent().getSerializableExtra(ApplicationConstants.trailCodeParam);
            if (editTrailObj != null) {
                editMode = true;
            }


            if (editMode) {
                Log.d(TAG, "Edit Trail Obj data for Trail Code::" + editTrailObj.getTrailCode());
                getSupportActionBar().setTitle(getString(R.string.page_edit_heading_learning));
                edTrailName.setText(editTrailObj.getTrailName());
                edTrailName.setEnabled(false);
                edTrailDescription.setText(editTrailObj.getTrailDescription());
                trailCodeStr = editTrailObj.getTrailCode();
                if (null != editTrailObj.getStartDate()) {
                    edTrailStartDate.setText(constructDateToStringDate(editTrailObj.getStartDate()));
                    edTrailStartDate.setEnabled(false);
                }
                if (null != editTrailObj.getEndDate()) {
                    edTrailEndDate.setText(constructDateToStringDate(editTrailObj.getEndDate()));
                }


            } else {
                getSupportActionBar().setTitle(getString(R.string.page_heading_learning));
                Log.d(TAG, "edTrailStartDate..." + edTrailStartDate.getText().toString());
                if (edTrailStartDate.getText().toString().equals("") || edTrailStartDate.getText().toString() == null) {
                    edTrailEndDate.setEnabled(false);
                }

            }

            /**Invoke Date Picker for selecting start date of Trail**/
            edTrailEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(CreateLearningTrailActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // set day of month , month and year value in the edit text
                                    int monthForEndDate = monthOfYear + 1;
                                    String finalMonthForEndDate = "" + monthForEndDate;
                                    String finalDayForEndDate = "" + dayOfMonth;
                                    if (monthForEndDate < 10) {
                                        finalMonthForEndDate = "0" + monthForEndDate;
                                    }
                                    if (dayOfMonth < 10) {
                                        finalDayForEndDate = "0" + dayOfMonth;
                                    }

                                    edTrailEndDate.setText(finalDayForEndDate + "/"
                                            + finalMonthForEndDate + "/" + year);

                                    Log.d(TAG, "edTrailEndDate set to: " + edTrailEndDate.getText().toString());

                                }
                            }, mYear, mMonth, mDay);
                    Date edTrailStartDateObj = constructDateFromString(edTrailStartDate.getText().toString());
                    datePickerDialog.getDatePicker().setMinDate(edTrailStartDateObj.getTime());
                    datePickerDialog.show();
                }
            });

            /**Invoke Date Picker for selecting start date of Trail**/
            edTrailStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // calender class's instance and get current date , month and year from calender
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // current year
                    int mMonth = c.get(Calendar.MONTH); // current month
                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(CreateLearningTrailActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // set day of month , day and year value in the edit text
                                    int month = monthOfYear + 1;
                                    String finalMonth = "" + month;
                                    String finalDay = "" + dayOfMonth;
                                    if (month < 10) {
                                        finalMonth = "0" + month;
                                    }
                                    if (dayOfMonth < 10) {
                                        finalDay = "0" + dayOfMonth;
                                    }

                                    edTrailStartDate.setText(finalDay + "/"
                                            + finalMonth + "/" + year);
                                    edTrailEndDate.setEnabled(true);
                                    trailStartDate = new StringBuilder();
                                    trailStartDate.append(year);
                                    trailStartDate.append(finalMonth);
                                    trailStartDate.append(finalDay);

                                    Log.d(TAG, "edTrailStartDate set to : " + edTrailStartDate.getText().toString());
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                }
            });


            Button btnSave = findViewById(R.id.btn_save);
            if (!editMode) {
                btnSave.setText(getString(R.string.save));
            } else {
                btnSave.setText(getString(R.string.update));
            }
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editMode) {
                        try {
                            addTrailDetails();
                        } catch (TrailActivityException e) {
                            Log.e(TAG, "Error occurred while invoking addTrailDetails from onCreate");
                            Toast.makeText(CreateLearningTrailActivity.this, ApplicationConstants.toastMessageForAddingTrailFailure,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            updateTrailDetails();
                        } catch (TrailActivityException e) {
                            Log.e(TAG, "Error occurred while invoking updateTrailDetails from onCreate");
                            Toast.makeText(CreateLearningTrailActivity.this, ApplicationConstants.getToastMessageForUpdateTrailFailure,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (addOperationSuccess || updateOperationSuccess) {
                        finish();
                    }
                }
            });

        } catch (Exception activityExceptObj) {
            Log.e(TAG, "Error occurred while invoking onCreate");
            Toast.makeText(CreateLearningTrailActivity.this, ApplicationConstants.toastMessageForDbFailure,
                    Toast.LENGTH_SHORT).show();
        }

        Log.d(TAG, "End of onCreate API");

    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    public void onIconSelect(MenuItem menuItem) {
        Intent intent = new Intent(getApplicationContext(),
                UserProfileActivity.class);
        startActivity(intent);
    }

    /**
     * API to update trail
     * details for a selected
     * trail code
     */
    private void updateTrailDetails() throws TrailActivityException {
        Log.d(TAG, "Start of updateTrailDetails API");
        try {
            LearningTrail updatedTrailObj = pouplateTrail();
            if (isValidForEditTrail(updatedTrailObj)) {
                //Construct Helper to call DB and persist data
                LearningTrailHelper trailHelper = new LearningTrailHelper();
                trailHelper.createTrail(updatedTrailObj);
                updateOperationSuccess = true;
                Toast.makeText(CreateLearningTrailActivity.this, getString(R.string.trail_update_successful),
                        Toast.LENGTH_SHORT).show();
            } else {
                updateOperationSuccess = false;
            }

            Log.d(TAG, "End of updateTrailDetails API");
        } catch (TrailHelperException helperExceptObj) {
            updateOperationSuccess = false;
            throw new TrailActivityException("Exception occurred during invoking updateTrailDetails", helperExceptObj);
        }


    }

    /**
     * API call to add Trail details in
     * database
     */
    private void addTrailDetails() throws TrailActivityException {
        Log.d(TAG, "Start of  addTrailDetails API");
        try {

            if (isValid()) {
                Log.d(TAG, "Fields value are validated to true before creating trail");
                trailCodeStr = constructTrailCode(trailStartDate.toString(), edTrailName.getText().toString());

                LearningTrail pouplatedTrailObj = pouplateTrail();

                //Construct Helper to call DB and persist data
                LearningTrailHelper trailHelper = new LearningTrailHelper();
                trailHelper.createTrail(pouplatedTrailObj);
                addOperationSuccess = true;

                Toast.makeText(CreateLearningTrailActivity.this, getString(R.string.trail_save_successful),
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "End of addTrailDetails API");

            } else {
                addOperationSuccess = false;
                Log.w(TAG, "Error while adding a learning trail");
            }
        } catch (TrailHelperException helperExceptObj) {
            throw new TrailActivityException("Exception occurred during invoking addTrailDetails", helperExceptObj);
        }


    }

    /**
     * API to validate each field on trail learn page
     * @return boolean
     */
    private boolean isValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(edTrailName.getText().toString().trim())) {
            edTrailName.setError(getString(R.string.trail_name_validation_msg));
            isValid = false;
        }
        if (TextUtils.isEmpty(edTrailDescription.getText().toString().trim())) {
            edTrailDescription.setError(getString(R.string.trail_description_validation_msg));
            isValid = false;
        }
        if (TextUtils.isEmpty(edTrailStartDate.getText().toString().trim())) {
            edTrailStartDate.setError(getString(R.string.trail_startDate_validation_msg));
            isValid = false;
        }
        /*if (DateUtil.compareStartDateWithCurrentDate(edTrailStartDate.getText().toString().trim())) {
            edTrailStartDate.setError(getString(R.string.trail_startDate_validation_less_currDate_msg));
        }*/
        if (TextUtils.isEmpty(edTrailEndDate.getText().toString().trim())) {
            edTrailEndDate.setError(getString(R.string.trail_endDate_validation_msg));
            isValid = false;
        }
        if (DateUtil.compareStartDateWithEndDate(edTrailStartDate.getText().toString().trim(), edTrailEndDate.getText().toString().trim())) {
            edTrailEndDate.setError(getString(R.string.trail_endDate_validation_less_startDate_msg));
            isValid = false;
        }

        return isValid;
    }

    /**
     * API to validate the fields
     * edited during edit mode.
     *
     * @return boolean
     */

    private boolean isValidForEditTrail(LearningTrail editObj) {
        boolean isEditValid = true;
        if (TextUtils.isEmpty(editObj.getTrailDescription().trim())) {
            edTrailDescription.setError(getString(R.string.trail_description_validation_msg));
            isEditValid = false;
        }
        if (editObj.getEndDate() == null) {
            edTrailEndDate.setError(getString(R.string.trail_endDate_validation_msg));
            isEditValid = false;
        }
        if (DateUtil.compareStartDateWithEndDate(editObj.getStartDate(), editObj.getEndDate())) {
            edTrailEndDate.setError(getString(R.string.trail_endDate_validation_less_startDate_msg));
            isEditValid = false;
        }

        return isEditValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_role_icon, menu);
        MenuItem participant = menu.findItem(R.id.participant_icon);
        MenuItem trainer = menu.findItem(R.id.trainer_icon);
        if(isTrainer) {
            participant.setVisible(false);
        }
        else {
            trainer.setVisible(false);
        }
        return true;
    }

    /**
     * API pouplating Learning Trail
     * from the UI fields
     * @return boolean
     */
    private LearningTrail pouplateTrail(){
        Log.d(TAG, "Start of pouplateTrail API");
        //Populate learning trail
        LearningTrail trailObj = new LearningTrail();
        trailObj.setTrailName(edTrailName.getText().toString());
        trailObj.setTrailDescription(edTrailDescription.getText().toString());

        Log.d(TAG, "Value of startDate:" + DateUtil.constructDateFromString(edTrailStartDate.getText().toString()
                + "Value of endDate:" + DateUtil.constructDateFromString(edTrailEndDate.getText().toString())));

        trailObj.setStartDate(DateUtil.constructDateFromString(edTrailStartDate.getText().toString()));
        trailObj.setEndDate(DateUtil.constructDateFromString(edTrailEndDate.getText().toString()));
        if (trailCodeStr != null) {
            trailObj.setTrailCode(trailCodeStr);
        } else {
            Log.e(TAG, "Error in pouplateTrail as trail code is null ");
        }
        trailObj.setUserId(userEmail);
        Log.d(TAG, "End of pouplateTrail API");
        return trailObj;
    }


    /**
     * API to form trail code
     * @param startDate
     * @param trailName Name of trail
     * @return String
     */
    private String constructTrailCode(String startDate, String trailName) throws TrailActivityException {
        Log.d(TAG, "Start of constructTrailCode API");
        StringBuilder trailCodeStr = new StringBuilder();
        trailCodeStr.append(startDate);
        trailCodeStr.append(ApplicationConstants.underScoreConstants);
        trailCodeStr.append(trailName.toUpperCase());
        Log.d(TAG, "End of constructTrailCode API");
        return trailCodeStr.toString();
    }

}
