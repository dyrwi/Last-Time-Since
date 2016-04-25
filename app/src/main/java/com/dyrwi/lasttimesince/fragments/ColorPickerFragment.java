package com.dyrwi.lasttimesince.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.eventbus.ColorIconEvent;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Ben on 22-Mar-16.
 */
public class ColorPickerFragment extends Fragment implements ColorPicker.OnColorChangedListener {
    private static final String TAG = "ColorPickerFragment";
    private ColorPicker picker;
    private SVBar svBar;
    private OpacityBar opacityBar;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStauts) {
        View view = inflater.inflate(R.layout.color_picker, container, false);

        picker = (ColorPicker) view.findViewById(R.id.picker);
        svBar = (SVBar) view.findViewById(R.id.svbar);
        opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.setOnColorChangedListener(this);
        picker.setColor(Color.BLUE);

        return view;
    }

    @Override
    public void onColorChanged(int color) {
        //gives the color when it's changed.
    }

    @Subscribe
    public void onSave(ColorIconEvent event) {
        if(event.getTargetClass() == ColorPickerFragment.class) {
            Log.i(TAG, "onSave");
            event.setColor(picker.getColor());
            event.setPosition(0);
            Log.i(TAG, "postSticky");
            ColorIconEvent stickyEvent = new ColorIconEvent(event.getPosition(), event.getColor());
            stickyEvent.setTargetClass(CreateEditActivityFragment.class);
            EventBus.getDefault().postSticky(stickyEvent);
            getActivity().finish();
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
