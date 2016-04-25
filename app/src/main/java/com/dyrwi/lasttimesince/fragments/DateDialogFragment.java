package com.dyrwi.lasttimesince.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import com.dyrwi.lasttimesince.eventbus.DateDialogEvent;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Ben on 15-Mar-16.
 */
public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY_OF_MONTH = "day_of_month";
    private static final String TAG = "DateDialogFragment";
    private DatePicker picker;
    private DatePickerDialog fragment;
    private LocalDate localDate;

    public DateDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year;
        int monthOfYear;
        int dayOfMonth;

        localDate = LocalDate.now();
        Log.i(TAG, "Before: " + localDate.toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));

        if (getArguments() != null) {
            if (getArguments().containsKey(YEAR))
                localDate = localDate.withYear(getArguments().getInt(YEAR));
            if (getArguments().containsKey(MONTH))
                localDate = localDate.withMonthOfYear(getArguments().getInt(MONTH));
            if (getArguments().containsKey(DAY_OF_MONTH))
                localDate = localDate.withDayOfMonth(getArguments().getInt(DAY_OF_MONTH));
        }

        Log.i(TAG, "After: " + localDate.toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));

        monthOfYear = localDate.getMonthOfYear();
        monthOfYear--;
        Log.i(TAG, "monthOfYear = " + monthOfYear);

        fragment = new DatePickerDialog(getActivity(), this, localDate.getYear(), monthOfYear, localDate.getDayOfMonth());
        fragment.setTitle(localDate.toString(DateTimeFormat.forPattern("EEEE")));
        picker = fragment.getDatePicker();
        picker.init(localDate.getYear(), monthOfYear, localDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                localDate = localDate.withYear(year);
                localDate = localDate.withMonthOfYear(monthOfYear + 1);
                localDate = localDate.withDayOfMonth(dayOfMonth);
                fragment.setTitle(localDate.toString(DateTimeFormat.forPattern("EEEE")));
            }
        });
        return fragment;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        EventBus.getDefault().post(new DateDialogEvent(year, monthOfYear, dayOfMonth));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
