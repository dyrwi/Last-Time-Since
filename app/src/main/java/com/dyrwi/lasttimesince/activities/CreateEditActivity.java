package com.dyrwi.lasttimesince.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ScrollingView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.eventbus.JodaActivityEvent;
import com.dyrwi.lasttimesince.fragments.CreateEditActivityFragment;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ben on 14-Mar-16.
 */
public class CreateEditActivity extends AppCompatActivity {
    private final String TAG = getClass().toString();
    public static final String ACTIVITY_ID = "activity_id";

    // VIEW VARIABLES FROM XML FILE (activity_standard_toolbar.xml)
    private Toolbar vToolbar;
    private ScrollingView vScrollView;

    // CLASS VARIABLES
    private JodaRepo repo;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.add_activity);
        repo = new JodaRepo(this);
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

        if(getIntent() != null) {
            if(getIntent().hasExtra(ACTIVITY_ID)){
                getSupportActionBar().setTitle(R.string.edit_activity);
                CreateEditActivityFragment createEditActivityFragment = new CreateEditActivityFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(CreateEditActivityFragment.ACTIVITY_ID, getIntent().getLongExtra(ACTIVITY_ID, 0));
                createEditActivityFragment.setArguments(bundle);
                ft.add(R.id.add_activity_details, createEditActivityFragment);
            } else {
                getSupportActionBar().setTitle(R.string.create_new_activity);
                ft.add(R.id.add_activity_details, new CreateEditActivityFragment());
            }
        }
        ft.commit();




        /*TODO
        In the future, I would like to make it when the user creates a new activity, they can also
        set the first event for that activity. However, for the sake of just getting this app completed,
        this feature will not be implemented. The code below should help get me started.
         */
      /*  FragmentTransaction ft2 = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putLong(CreateEditEventFragment.ACTIVITY_ID, CreateEditEventFragment.NEW_ACTIVITY_ID);
        Fragment createEventFrag = new CreateEditActivityFragment();
        createEventFrag.setArguments(bundle);
        ft2.add(R.id.add_activity_add_event, createEventFrag);
        ft2.commit();*/

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
                if(getIntent().hasExtra(ACTIVITY_ID)) {
                    JodaActivityEvent event = new JodaActivityEvent();
                    event.setTargetClass(CreateEditActivityFragment.class);
                    EventBus.getDefault().post(event);
                } else {
                    EventBus.getDefault().post(new JodaActivity());
                }

                return true;
            default:
                return true;
        }
    }


}
