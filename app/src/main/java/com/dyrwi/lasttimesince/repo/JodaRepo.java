package com.dyrwi.lasttimesince.repo;

import android.content.Context;
import android.util.Log;

import com.dyrwi.lasttimesince.repo.implementations.ActivityImpl;
import com.dyrwi.lasttimesince.repo.implementations.EventImpl;
import com.dyrwi.lasttimesince.repo.implementations.JodaActivityImpl;
import com.dyrwi.lasttimesince.repo.implementations.JodaEventImpl;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.dyrwi.lasttimesince.util.EventManager;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Ben on 03-Mar-16.
 * <p/>
 * Repo is what Android sees for our repository. Through this, we can access all of our data from
 * our Repo. Use this class in the Android activities when you want to access the database.
 */
public class JodaRepo {

    private String TAG = this.getClass().toString();
    private JodaDatabaseHelper db;
    private JodaEventImpl events;
    private JodaActivityImpl activities;

    public JodaRepo(Context context) {
        db = OpenHelperManager.getHelper(context, JodaDatabaseHelper.class);
        events = new JodaEventImpl(db);
        activities = new JodaActivityImpl(db);
    }

    public JodaEventImpl getEvents() {
        return events;
    }

    public JodaActivityImpl getActivities() {
        return activities;
    }

    public void insertAllData() {
        JodaMasterInitialize mi = new JodaMasterInitialize();
        activities.createFromList(mi.getActivities());
        events.createFromList(mi.getEvents());
    }

    public void deleteAllData() {
        activities.deleteAll();
        events.deleteAll();
    }

    public void showMeTheData() {
        Log.i(TAG, "Deletion starting");
        deleteAllData();
        Log.i(TAG, "Deletion finished");
        Log.i(TAG, "Insert starting");
        insertAllData();
        Log.i(TAG, "Insert finished");
        /*Old test code that shows all events for the activities in the database in the console menu
        ArrayList<JodaActivity> arrayList = (ArrayList<JodaActivity>) activities.getAll();
        DateTimeFormatter dtfDate = DateTimeFormat.forPattern("EEEE, MMMM dd YYYY");
        DateTimeFormatter dtfTime = DateTimeFormat.forPattern("HH:mm");
        for (int i = 0; i < arrayList.size(); i++) {
            Log.i(TAG, "Activity: " + arrayList.get(i).getName());
            Log.i(TAG, "==================================================");
            Log.i(TAG, arrayList.get(i).getEvents().size() + " events for " + arrayList.get(i).getName());
            ForeignCollection<JodaEvent> events = arrayList.get(i).getEvents();
            CloseableIterator<JodaEvent> iterator = events.closeableIterator();
            while (iterator.hasNext()) {
                JodaEvent e = iterator.next();
                Log.i(TAG, "Date: " + e.getDate().toString(dtfDate));
                Log.i(TAG, "Time: " + e.getTime().toString(dtfTime));
                Log.i(TAG, "-------------------------------------------");
            }
            try {
                iterator.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "");
        }*/
    }
}
