package com.dyrwi.lasttimesince.util;

import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ben on 04-Mar-16.
 *
 * When you need to ask activities about their events, this is the class you use.
 * This static class will help you retreive the data required from your events.
 */
public class EventManager {
    public final static int MOST_RECENT_EVENT_FOR_ACTIVITY = 1;
    public final static int OLDEST_EVENT_FOR_ACTIVITY = 2;


    private EventManager() {
    }

    public static Event get(Activity activity, int reference) {
        if(reference == MOST_RECENT_EVENT_FOR_ACTIVITY) {
            return getMostRecentEvent(activity);
        } else if (reference == OLDEST_EVENT_FOR_ACTIVITY) {
            return getOldestActivityForEvent(activity);
        }
        return null;
    }

    public static JodaEvent get(JodaActivity activity, int reference) {
        if(reference == MOST_RECENT_EVENT_FOR_ACTIVITY) {
            return getMostRecentEvent(activity);
        } else if (reference == OLDEST_EVENT_FOR_ACTIVITY) {
            return getOldestActivityForEvent(activity);
        }
        return null;
    }

    public static JodaEvent getMostRecentEvent(JodaActivity activity) {
        ForeignCollection<JodaEvent> events = activity.getEvents();
        CloseableIterator<JodaEvent> iterator = events.closeableIterator();
        /*
            Set the date to 1970. This way we have  starting point in the past.
            The first iteration will change to this, and from there we can simply
            compare the dates until we find the lastest one.
         */
        JodaEvent mostRecentEvent = new JodaEvent();
        mostRecentEvent.setDate(LocalDate.fromDateFields(new Date(0)));
        while(iterator.hasNext()) {
            JodaEvent currentEvent = iterator.next();
            if(mostRecentEvent.getDate().toDate().getTime() < currentEvent.getDate().toDate().getTime()) {
                mostRecentEvent = currentEvent;
            }
        }
        return mostRecentEvent;
    }

    public static JodaEvent getOldestActivityForEvent(JodaActivity activity) {
        ForeignCollection<JodaEvent> events = activity.getEvents();
        CloseableIterator<JodaEvent> iterator = events.closeableIterator();
        /*
            Set the date to the current date. This way we have starting point.
            The first iteration will change to this, and from there we can simply
            compare the dates until we find the oldest one.
         */
        JodaEvent mostRecentEvent = new JodaEvent();
        mostRecentEvent.setDate(LocalDate.now());
        while(iterator.hasNext()) {
            JodaEvent currentEvent = iterator.next();
            if(mostRecentEvent.getDate().toDate().getTime() > currentEvent.getDate().toDate().getTime()) {
                mostRecentEvent.setDate(currentEvent.getDate());
            }
        }
        return mostRecentEvent;
    }

    public static Event getMostRecentEvent(Activity activity) {
        ForeignCollection<Event> events = activity.getEvents();
        CloseableIterator<Event> iterator = events.closeableIterator();
        /*
            Set the date to 1970. This way we have  starting point in the past.
            The first iteration will change to this, and from there we can simply
            compare the dates until we find the lastest one.
         */
        Event mostRecentEvent = new Event();
        mostRecentEvent.setDate(new Date(0));
        while(iterator.hasNext()) {
            Event currentEvent = iterator.next();
            if(mostRecentEvent.getDate().getTime() < currentEvent.getDate().getTime()) {
                mostRecentEvent.setDate(currentEvent.getDate());
            }
        }
        return mostRecentEvent;
    }

    public static Event getOldestActivityForEvent(Activity activity) {
        ForeignCollection<Event> events = activity.getEvents();
        CloseableIterator<Event> iterator = events.closeableIterator();
        /*
            Set the date to the current date. This way we have starting point.
            The first iteration will change to this, and from there we can simply
            compare the dates until we find the oldest one.
         */
        Event mostRecentEvent = new Event();
        mostRecentEvent.setDate(Calendar.getInstance().getTime());
        while(iterator.hasNext()) {
            Event currentEvent = iterator.next();
            if(mostRecentEvent.getDate().getTime() > currentEvent.getDate().getTime()) {
                mostRecentEvent.setDate(currentEvent.getDate());
            }
        }
        return mostRecentEvent;
    }
}
