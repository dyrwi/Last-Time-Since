package com.dyrwi.lasttimesince.activities;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.swipe.util.Attributes;
import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.adapters.ListViewActivityAdapter;
import com.dyrwi.lasttimesince.eventbus.UpdateEvent;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.Repo;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by Ben on 11-Mar-16.
 */
public class ListViewActivity extends AppCompatActivity {

    public static final String TAG = "ListViewActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private JodaRepo jodaRepo;
    private Repo repo;
    private ArrayList<JodaActivity> mActivityList;
    private Toolbar toolbar;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle("Last Time Since");
        setSupportActionBar(toolbar);

        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).colorRes(R.color.md_white_1000));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListViewActivity.this, CreateEditActivity.class);
                startActivity(i);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        /*
        repo = new Repo(this);
        repo.showMeTheData();
        */
        jodaRepo = new JodaRepo(this);
        jodaRepo.showMeTheData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Last Time Since");
            }
        }

        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Item Decorator:
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Adapter:
        //mActivityList = (ArrayList<Activity>) repo.getActivities().getAll();
        mActivityList = (ArrayList<JodaActivity>) jodaRepo.getActivities().getAll();
        mAdapter = new ListViewActivityAdapter(this, mActivityList);
        ((ListViewActivityAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);
    }

    /**
     * Substitute for our onScrollListener for RecyclerView
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.i(TAG, "EventBus registered");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.i(TAG, "EventBus unregistered");
        super.onStop();
    }

    @Subscribe
    public void onActivityEvent(Activity activity) {

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(UpdateEvent event) {
        if(event.getTag() == TAG) {
            ((ListViewActivityAdapter) mAdapter).swap((ArrayList<JodaActivity>)jodaRepo.getActivities().getAll());
            UpdateEvent updateEvent = EventBus.getDefault().getStickyEvent(UpdateEvent.class);
            if(updateEvent != null) {
                EventBus.getDefault().removeStickyEvent(updateEvent);
            }

        }
    }

}
