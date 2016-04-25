package com.dyrwi.lasttimesince.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.activities.NewListViewActivity;
import com.dyrwi.lasttimesince.activities.ViewActivity;
import com.dyrwi.lasttimesince.eventbus.ColorIconEvent;
import com.dyrwi.lasttimesince.eventbus.JodaActivityEvent;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by Ben on 15-Mar-16.
 */
public class CreateEditActivityFragment extends Fragment {
    private static final String TAG = "CreateEditActivityFrag";
    public static final String ACTIVITY_ID = "activity_id";

    // JAVA VARIABLES
    private JodaRepo repo;
    private JodaActivity activity;

    // VIEW VARIABLES FROM XML FILE (fragment_activity_create_edit.xmlt.xml)
    private EditText vName;
    private ImageView vColor;
    private LinearLayout vColorIconLayout;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        this.repo = new JodaRepo(getActivity());

        if (getArguments() != null) {
            if (getArguments().containsKey(ACTIVITY_ID))
                activity = repo.getActivities().findByID(getArguments().getLong(ACTIVITY_ID));
        } else {
            activity = new JodaActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceStatus) {
        View view = inflater.inflate(R.layout.fragment_activity_create_edit, container, false);

        vName = (EditText) view.findViewById(R.id.name);
        vName.setText(activity.getName());
        vName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    activity.setName(" ");
                } else {
                    activity.setName(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        vColor = (ImageView) view.findViewById(R.id.image_icon_color);
        vColor.setBackgroundColor(activity.getIconColor());
        vColorIconLayout = (LinearLayout) view.findViewById(R.id.layout_color_icon);
        vColorIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerFragment = new ColorPickerDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(ColorPickerDialog.INITIAL_COLOR, activity.getIconColor());
                colorPickerFragment.setArguments(bundle);
                colorPickerFragment.show(getActivity().getSupportFragmentManager(), "colorPicker");
            }
        });

        return view;
    }

    @Subscribe
    public void onIconColorEvent(ColorIconEvent event) {
        activity.setIconColor(event.getColor());
        vColor.setBackgroundColor(activity.getIconColor());
    }

    @Subscribe
    public void saveActivity(JodaActivity event) {
        try {
            repo.getActivities().createOrUpdate(activity);
            JodaActivityEvent e = new JodaActivityEvent();
            e.setTargetClass(NewListViewActivity.class);
            e.setTag(NewListViewActivity.CREATE_ACTIVITY);
            e.setActivity(activity);
            EventBus.getDefault().postSticky(e);
            getActivity().finish();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            new ErrorDialog(
                    getResources().getString(R.string.not_saved_title),
                    getResources().getString(R.string.cannot_save_activity),
                    getActivity());
        }

    }

    @Subscribe
    public void saveActivity(JodaActivityEvent event) {
        if (event.getTargetClass() == CreateEditActivityFragment.class) {
            try {
                repo.getActivities().createOrUpdate(activity);
                JodaActivityEvent e = new JodaActivityEvent();
                e.setTargetClass(ViewActivity.class);
                e.setTag(ViewActivity.UPDATE_ACTIVITY);
                e.setActivity(activity);
                EventBus.getDefault().postSticky(e);
                getActivity().finish();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                new ErrorDialog(
                        getResources().getString(R.string.not_saved_title),
                        getResources().getString(R.string.cannot_save_activity),
                        getActivity());
            }
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

    @Subscribe
    public void onColorSelection(ColorIconEvent event) {
        Log.i(TAG, "ColorIconEvent");
        activity.setIconColor(event.getColor());
        vColor.setBackgroundColor(activity.getIconColor());
    }


}
