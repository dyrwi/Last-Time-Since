package com.dyrwi.lasttimesince.repo;

import android.content.Context;
import android.util.Log;

import com.dyrwi.lasttimesince.repo.implementations.ActivityImpl;
import com.dyrwi.lasttimesince.repo.implementations.EventImpl;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Ben on 03-Mar-16.
 * <p/>
 * Repo is what Android sees for our repository. Through this, we can access all of our data from
 * our Repo. Use this class in the Android activities when you want to access the database.
 */
public class Repo {

    private String TAG = this.getClass().toString();
    private DatabaseHelper db;
    private EventImpl events;
    private ActivityImpl activities;

    public Repo(Context context) {
        db = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        events = new EventImpl(db);
        activities = new ActivityImpl(db);
    }

    public EventImpl getEvents() {
        return events;
    }

    public ActivityImpl getActivities() {
        return activities;
    }

    public void insertAllData() {
        MasterInitialize mi = new MasterInitialize();
        activities.createFromList(mi.getActivities());
        events.createFromList(mi.getEvents());
    }

    private void deleteAllData() {
        activities.deleteAll();
        events.deleteAll();
    }

    public void showMeTheData() {
        deleteAllData();
        insertAllData();
        ArrayList<Activity> arrayList = (ArrayList<Activity>) activities.getAll();
        for (int i = 0; i < arrayList.size(); i++) {
            Log.i(TAG, "Activity: " + arrayList.get(i).getName());
            Log.i(TAG, "==================================================");
            Log.i(TAG, arrayList.get(i).getEvents().size() + " events for " + arrayList.get(i).getName());
            ForeignCollection<Event> events = arrayList.get(i).getEvents();
            CloseableIterator<Event> iterator = events.closeableIterator();
            while (iterator.hasNext()) {
                Event e = iterator.next();
                Log.i(TAG, "Date: " + e.getDate().toString());
                Log.i(TAG, "Time: " + e.getDate().toString());
                Log.i(TAG, "-------------------------------------------");
            }
            try {
                iterator.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "");
        }
    }


}
