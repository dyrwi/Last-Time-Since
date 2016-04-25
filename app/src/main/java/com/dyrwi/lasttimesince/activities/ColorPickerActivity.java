package com.dyrwi.lasttimesince.activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.eventbus.ColorIconEvent;
import com.dyrwi.lasttimesince.fragments.ColorPickerFragment;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ben on 22-Mar-16.
 */
public class ColorPickerActivity extends AppCompatActivity{
    private static final String TAG = "ColorPickerActivity";
    private Toolbar vToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_toolbar);
        vToolbar = (Toolbar) findViewById(R.id.toolbar);
        vToolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.menu_item_color).sizeDp(MyApplication.MENU_ICON_SIZE));
        setSupportActionBar(vToolbar);
        getSupportActionBar().setTitle(R.string.choose_color);
        vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.scrollView, new ColorPickerFragment());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.color_picker, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        MenuItem item = menu.findItem(R.id.save);
        item.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_save).colorRes(R.color.menu_item_color).sizeDp(MyApplication.MENU_ICON_SIZE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                Log.i(TAG, "onSave");
                ColorIconEvent event = new ColorIconEvent(0,0);
                event.setTargetClass(ColorPickerFragment.class);
                EventBus.getDefault().post(event);
                return true;
            default:
                return true;
        }
    }
}
