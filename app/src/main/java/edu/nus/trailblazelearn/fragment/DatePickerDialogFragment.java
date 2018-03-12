package edu.nus.trailblazelearn.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    public static final int FLAG_START_DATE = 0;
    public static final int FLAG_END_DATE = 1;

    private int flag = 0;
    private EditText edTrailStartDate;
    private EditText edTrailEndDate;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void setFlag(int i) {
        flag = i;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (flag == FLAG_START_DATE) {
            edTrailStartDate.setText(format.format(calendar.getTime()));
        } else if (flag == FLAG_END_DATE) {
            edTrailEndDate.setText(format.format(calendar.getTime()));
        }
    }
}
