package com.dyrwi.lasttimesince.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import com.dyrwi.lasttimesince.eventbus.TimeDialogEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

/**
 * Created by Ben on 15-Mar-16.
 */
public class TimeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    public static final String HOUR_OF_DAY = "hour_of_day";
    public static final String MINUTE_OF_HOUR = "minute";

    public TimeDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if (getArguments() != null) {
            if (getArguments().containsKey(HOUR_OF_DAY))
                hourOfDay = getArguments().getInt(HOUR_OF_DAY);
            if (getArguments().containsKey(MINUTE_OF_HOUR))
                minute = getArguments().getInt(MINUTE_OF_HOUR);
        }

        // Create a new instance of DatePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EventBus.getDefault().post(new TimeDialogEvent(hourOfDay, minute));
    }
}
