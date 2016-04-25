package com.dyrwi.lasttimesince.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.fragments.CreateEditEventFragment;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ben on 19-Mar-16.
 */
public class CreateEditEvent extends AppCompatActivity {
    public static final String ACTIVITY_ID = "activity_id";
    public static final String EVENT_ID = "event_id";

    private final String TAG = "CreateEditEvent";

    // VIEW VARIABLES FROM XML FILE (activity_standard_toolbar.xml)
    private Toolbar vToolbar;
    private ScrollingView vScrollView;

    // CLASS VARIABLES
    private JodaRepo repo;
    private JodaEvent event;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_standard_toolbar);

        vToolbar = (Toolbar) findViewById(R.id.toolbar);
        vToolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.menu_item_color).sizeDp(MyApplication.MENU_ICON_SIZE));
        setSupportActionBar(vToolbar);

        vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle fragmentBundle = new Bundle();

        if(getIntent() != null) {
            if(getIntent().hasExtra(ACTIVITY_ID)) {
                fragmentBundle.putLong(CreateEditEventFragment.ACTIVITY_ID, getIntent().getLongExtra(ACTIVITY_ID, 0));
                getSupportActionBar().setTitle(R.string.create_new_event);
            }
            if(getIntent().hasExtra(EVENT_ID)) {
                fragmentBundle.putLong(CreateEditEventFragment.EVENT_ID, getIntent().getLongExtra(EVENT_ID, 0));
                getSupportActionBar().setTitle(R.string.edit_event);
            }
        }

        Fragment createEventFragment = new CreateEditEventFragment();
        createEventFragment.setArguments(fragmentBundle);
        ft.add(R.id.scrollView, createEventFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_activity, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        MenuItem mConfirm = menu.findItem(R.id.save);
        mConfirm.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_save).colorRes(R.color.menu_item_color).sizeDp(MyApplication.MENU_ICON_SIZE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                EventBus.getDefault().post(new JodaEvent());
                return true;
            default:
                return true;
        }
    }

}
