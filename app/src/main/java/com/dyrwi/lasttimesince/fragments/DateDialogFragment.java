package com.dyrwi.lasttimesince.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import com.dyrwi.lasttimesince.eventbus.DateDialogEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by Ben on 15-Mar-16.
 */
public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public DateDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
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
