package com.dyrwi.lasttimesince.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.eventbus.ColorIconEvent;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ben on 22-Mar-16.
 */
public class ColorPickerDialog extends DialogFragment implements ColorPicker.OnColorChangedListener {
    private static final String TAG = "ColorPickerDialog";
    public static final String INITIAL_COLOR = "INITIAL_COLOR";
    public static final String TARGET = "TARGET";

    private ColorPicker picker;
    private SVBar svBar;
    private OpacityBar opacityBar;

    private int initialColor;
    private Object target;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceStatus) {
        if (getArguments() != null) {
            if (getArguments().containsKey(INITIAL_COLOR))
                initialColor = getArguments().getInt(INITIAL_COLOR);
            if(getArguments().containsKey(TARGET))
                target = getArguments().get(TARGET);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker, null);

        builder.setView(view)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ColorIconEvent event = new ColorIconEvent(0, picker.getColor());
                        EventBus.getDefault().post(event);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        picker = (ColorPicker) view.findViewById(R.id.picker);
        svBar = (SVBar) view.findViewById(R.id.svbar);
        opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.setOnColorChangedListener(this);
        picker.setColor(initialColor);
        picker.setOldCenterColor(initialColor);

        return builder.create();
    }

    @Override
    public void onColorChanged(int color) {

    }
}
