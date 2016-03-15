package com.dyrwi.lasttimesince.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.eventbus.DateDialogEvent;
import com.dyrwi.lasttimesince.eventbus.TimeDialogEvent;
import com.dyrwi.lasttimesince.repo.Repo;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ben on 15-Mar-16.
 */
public class CreateActivityFragment extends Fragment {
    // JAVA VARIABLES
    private Repo repo;
    private Date date;
    private Date time;

    // VIEW VARIABLES FROM XML FILE (fragment_activity_create.xml)
    private EditText vTitle;
    private TextView vDate, vTime;
    private Button vChooseDate, vChooseTime;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        this.repo = new Repo(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.fragment_activity_create, container, false);

        vTitle = (EditText) view.findViewById(R.id.title);
        vDate = (TextView) view.findViewById(R.id.date);
        vTime = (TextView) view.findViewById(R.id.time);
        vChooseDate = (Button) view.findViewById(R.id.choose_date);
        vChooseTime = (Button) view.findViewById(R.id.choose_time);

        vChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dateDialogFragment = new DateDialogFragment();
                dateDialogFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        vChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
                timeDialogFragment.show(getActivity().getFragmentManager(), "timePicker");
            }
        });

        return view;
    }

    /*
        EventBus Related
        'org.greenrobot:eventbus:3.0.0'
    */
    @Subscribe
    public void onDateDialogEvent(DateDialogEvent event) {
        vDate.setText(event.getYear() + " - " + event.getMonthOfYear() + " - " + event.getDayOfMonth());
        Calendar c = Calendar.getInstance();
        c.set(event.getYear(), event.getMonthOfYear(), event.getDayOfMonth());
        date = c.getTime();
        Log.i(getClass().toString(), date.toString());
    }

    @Subscribe
    public void onTimeDialogEvent(TimeDialogEvent event) {
        vTime.setText(event.getHourOfDay() + ":" + event.getMinute());
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, event.getHourOfDay());
        c.set(Calendar.MINUTE, event.getMinute());
        time = c.getTime();
        Calendar.getInstance().setTime(time);
        Calendar c2 = Calendar.getInstance();
    }

    @Subscribe
    public void saveActivity(Activity event) {
        try {
            Activity activity = new Activity();
            activity.setName(vTitle.getText().toString());

            Event e = new Event();
            e.setActivity(activity);
            e.setDate(date);
            e.setTime(time);

            activity.getEvents().add(e);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            new ErrorDialog(
                    getResources().getString(R.string.not_saved_title),
                    getResources().getString(R.string.cannot_save_activity),
                    getActivity());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}
