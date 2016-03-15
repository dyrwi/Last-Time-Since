package com.dyrwi.lasttimesince.repo;

import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by Ben on 03-Mar-16.
 */
public class MasterInitialize {

    private ArrayList<Event> events;
    private ArrayList<Activity> activities;

    public MasterInitialize() {
        // Initialise
        events = new ArrayList<Event>();
        activities = new ArrayList<Activity>();
        Date calendar = Calendar.getInstance().getTime();

        // Coffee Activity
        Activity coffee = new Activity();
        coffee.setName("Coffee");
        activities.add(coffee);

        Random random = new Random();
        long ms;

        // Coffee Events
        for(int i = 0; i < 10; i++) {
            Event e = new Event();
            ms = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            e.setDate(new Date(ms));
            e.setTime(new Date());
            e.setActivity(coffee);
            events.add(e);
        }

        // Gym Activity
        Activity gym = new Activity();
        gym.setName("Gym");
        activities.add(gym);

        // Gym Events
        for(int i = 0; i < 5; i++) {
            Event e = new Event();
            ms = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            e.setDate(new Date(ms));
            e.setTime(new Date());
            e.setActivity(gym);
            events.add(e);
        }

        // Call Mum Activity
        Activity callMum = new Activity();
        callMum.setName("Call Mum");
        activities.add(callMum);

        // Call Mum Events
        for(int i = 0; i < 20; i++) {
            Event e = new Event();
            ms = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
            e.setDate(new Date(ms));
            e.setTime(new Date());
            e.setActivity(callMum);
            events.add(e);
        }
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

}
