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
import com.dyrwi.lasttimesince.fragments.CreateActivityFragment;
import com.dyrwi.lasttimesince.repo.Repo;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;

/**
 * Created by Ben on 14-Mar-16.
 */
public class CreateActivity extends AppCompatActivity {
    private final String TAG = getClass().toString();

    // VIEW VARIABLES FROM XML FILE (activity_standard_toolbar.xml)
    private Toolbar vToolbar;
    private ScrollingView vScrollView;


    // CLASS VARIABLES
    private Repo repo;
    private DateTime date, time;
    private Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_standard_toolbar);
        repo = new Repo(this);
        vToolbar = (Toolbar) findViewById(R.id.toolbar);
        vToolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.menu_item_color).sizeDp(R.dimen.menu_item_size));
        setSupportActionBar(vToolbar);
        getSupportActionBar().setTitle(R.string.create_new_activity);
        vToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.scrollView, new CreateActivityFragment());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_activity, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        MenuItem mConfirm = menu.findItem(R.id.save);
        mConfirm.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_save).colorRes(R.color.menu_item_color).sizeDp(R.dimen.menu_item_size));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.save:
                EventBus.getDefault().post(new Activity());
                finish();
                return true;
            default:
                return true;
        }
    }

    private void saveActivity() {
        try {
            Activity activity = new Activity();
        } catch (NullPointerException ex) {

        }
    }


}
