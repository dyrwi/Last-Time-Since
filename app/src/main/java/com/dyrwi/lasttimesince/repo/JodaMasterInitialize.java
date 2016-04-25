package com.dyrwi.lasttimesince.repo;

import android.util.Log;

import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.dyrwi.lasttimesince.util.EventManager;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Ben on 03-Mar-16.
 */
public class JodaMasterInitialize {

    private static final String TAG = "JodaMasterInitialize";
    private ArrayList<JodaEvent> events;
    private ArrayList<JodaActivity> activities;

    public JodaMasterInitialize() {
        // Initialise
        Log.i(TAG, "Starting");
        String[] americanStates = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        events = new ArrayList<JodaEvent>();
        activities = new ArrayList<JodaActivity>();

        Random random = new Random();
        final int millisInDay = 24 * 60 * 60 * 1000;
        long date;
        int maxEvents;
        /*// Coffee Activity
        JodaActivity coffee = new JodaActivity();
        Log.i(TAG, "===================Coffee===================");
        // Coffee Events
        for (int i = 0; i < 1; i++) {
            JodaEvent e = new JodaEvent();
            date = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            e.setDate(new LocalDate(date));
            Log.i(TAG, e.getDate().toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));
            e.setTime(new LocalTime(random.nextInt(millisInDay)));
            e.setActivity(coffee);
            events.add(e);
        }
        coffee.setName("Coffee");
        activities.add(coffee);
        Log.i(TAG, "======================================");

        // Gym Activity
        JodaActivity gym = new JodaActivity();
        Log.i(TAG, "===================Gym===================");
        // Gym Events
        for (int i = 0; i < 1; i++) {
            JodaEvent e = new JodaEvent();
            date = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            e.setDate(new LocalDate(date));
            Log.i(TAG, e.getDate().toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));
            e.setTime(new LocalTime(random.nextInt(millisInDay)));
            e.setActivity(gym);
            events.add(e);
        }
        gym.setName("Gym");
        activities.add(gym);
        Log.i(TAG, "======================================");

        // Call Mum Activity
        JodaActivity callMum = new JodaActivity();
        Log.i(TAG, "===================Call Mum===================");
        // Call Mum Events
        for (int i = 0; i < 1; i++) {
            JodaEvent e = new JodaEvent();
            date = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            e.setDate(new LocalDate(date));
            Log.i(TAG, e.getDate().toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY")));
            e.setTime(new LocalTime(random.nextInt(millisInDay)));
            e.setActivity(callMum);
            events.add(e);
        }
        callMum.setName("Call Mum");
        activities.add(callMum);
        Log.i(TAG, "======================================");*/

        //Other Activities

        for(int i = 0; i < americanStates.length; i++) {
            JodaActivity a = new JodaActivity();
            // Events
            maxEvents = random.nextInt(30)+20;
            Log.i(TAG, "===" + americanStates[i] + "=== Events: " + maxEvents + " =========");
            for (int j = 0; j < maxEvents; j++) {
                JodaEvent e = new JodaEvent();
                date = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
                e.setDate(new LocalDate(date));
                e.setTime(new LocalTime(random.nextInt(millisInDay)));
                e.setActivity(a);
                events.add(e);
            }
            a.setName(americanStates[i]);
            activities.add(a);
        }
    }

    public ArrayList<JodaEvent> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<JodaEvent> events) {
        this.events = events;
    }

    public ArrayList<JodaActivity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<JodaActivity> activities) {
        this.activities = activities;
    }

}
