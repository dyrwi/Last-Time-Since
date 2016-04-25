package com.dyrwi.lasttimesince.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.activities.ViewActivity;
import com.dyrwi.lasttimesince.eventbus.ColorIconEvent;
import com.dyrwi.lasttimesince.eventbus.DateDialogEvent;
import com.dyrwi.lasttimesince.eventbus.JodaActivityEvent;
import com.dyrwi.lasttimesince.eventbus.TimeDialogEvent;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Ben on 15-Mar-16.
 */
public class CreateEditEventFragment extends Fragment {
    private static final String TAG = "CreateEditActivityFrag";
    public static final String ACTIVITY_ID = "activity_id";
    public static final String EVENT_ID = "event_id";

    // JAVA VARIABLES
    private JodaRepo repo;
    private JodaEvent event;
    private int iconColor;

    // VIEW VARIABLES FROM XML FILE (fragment_activity_create_edit.xmlt.xml)
    private EditText vTitle;
    private LinearLayout layoutDate, layoutTime, layoutColor;
    private TextView vDate, vTime;
    private CheckBox vSetDateAsTitle;
    private ImageView vIconColor;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new JodaRepo(getActivity());
        event = new JodaEvent();

        if(getArguments() != null) {
            if(getArguments().containsKey(ACTIVITY_ID)) {
                event.setActivity(repo.getActivities().findByID(getArguments().getLong(ACTIVITY_ID)));
            }
            if (getArguments().containsKey(EVENT_ID)) {
                event = repo.getEvents().findByID(getArguments().getLong(EVENT_ID));
            }
        }

        iconColor = event.getIconColor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.fragment_event_create_edit, container, false);

        vTitle = (EditText) view.findViewById(R.id.title);
        vTitle.setText(event.getTitle());
        vTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() <= 0) {
                    event.setTitle(" ");
                } else {
                    event.setTitle(s.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        vDate = (TextView) view.findViewById(R.id.date);
        vDate.setText(event.getDate().toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));
        vTime = (TextView) view.findViewById(R.id.time);
        vTime.setText(event.getTime().toString(DateTimeFormat.forPattern("KK:mm aa")));
        vIconColor = (ImageView) view.findViewById(R.id.image_icon_color);
        vIconColor.setBackgroundColor(this.event.getIconColor());
        vSetDateAsTitle  =(CheckBox)view.findViewById(R.id.check_set_date_to_title);
        vSetDateAsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vSetDateAsTitle.isChecked()) {
                    Log.i(TAG, "is not checked");
                    vSetDateAsTitle.setChecked(false);
                } else {
                    Log.i(TAG, "is checked");
                    vTitle.setText(vDate.getText());
                    vSetDateAsTitle.setChecked(true);
                }
            }
        });

        layoutDate = (LinearLayout) view.findViewById(R.id.layoutDate);
        layoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dateDialogFragment = new DateDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(DateDialogFragment.YEAR, event.getDate().getYear());
                bundle.putInt(DateDialogFragment.MONTH, event.getDate().getMonthOfYear());
                bundle.putInt(DateDialogFragment.DAY_OF_MONTH, event.getDate().getDayOfMonth());
                dateDialogFragment.setArguments(bundle);
                dateDialogFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });
        layoutTime = (LinearLayout) view.findViewById(R.id.layoutTime);
        layoutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeDialogFragment timeDialogFragment = new TimeDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(TimeDialogFragment.HOUR_OF_DAY, event.getTime().getHourOfDay());
                bundle.putInt(TimeDialogFragment.MINUTE_OF_HOUR, event.getTime().getMinuteOfHour());
                timeDialogFragment.setArguments(bundle);
                timeDialogFragment.show(getActivity().getFragmentManager(), "timePicker");
            }
        });

        layoutColor = (LinearLayout) view.findViewById(R.id.layout_color_icon);
        layoutColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(ColorPickerDialog.INITIAL_COLOR, event.getIconColor());
                colorPickerDialog.setArguments(bundle);
                colorPickerDialog.show(getActivity().getSupportFragmentManager(), "colorPicker");
            }
        });

        return view;
    }

    @Subscribe
    public void onDateDialogEvent(DateDialogEvent e) {
        event.setDate(new LocalDate(e.getYear(), e.getMonthOfYear() + 1, e.getDayOfMonth()));
        vDate.setText(event.getDate().toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));
        if(vSetDateAsTitle.isChecked()) {
            vTitle.setText(event.getDate().toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));
        }
    }

    @Subscribe
    public void onTimeDialogEvent(TimeDialogEvent e) {
        event.setTime(new LocalTime(e.getHourOfDay(), e.getMinute()));
        vTime.setText(event.getTime().toString(DateTimeFormat.forPattern("KK:mm aa")));
    }

    @Subscribe
    public void onColorIconSelection(ColorIconEvent e) {
        vIconColor.setBackgroundColor(e.getColor());
        iconColor = e.getColor();
        event.setIconColor(iconColor);
    }

    @Subscribe
    public void saveEvent(JodaEvent e) {
        try {
            repo.getEvents().createOrUpdate(event);
            repo.getActivities().createOrUpdate(event.getActivity());
            // Edit Event
            if(getArguments() != null) {
                JodaActivityEvent jae = new JodaActivityEvent();
                jae.setEvent(event);
                jae.setTargetClass(ViewActivity.class);
                if (getArguments().containsKey(EVENT_ID)) {
                    jae.setTag(ViewActivity.UPDATE_EVENT);
                    EventBus.getDefault().postSticky(jae);
                } else if(getArguments().containsKey(ACTIVITY_ID)) {
                    jae.setTag(ViewActivity.NEW_EVENT);
                    EventBus.getDefault().postSticky(jae);
                }
            }
            getActivity().finish();
        } catch (Exception ex) {
            ex.printStackTrace();
            new ErrorDialog(
                    getResources().getString(R.string.not_saved_title),
                    getResources().getString(R.string.cannot_save_event),
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
