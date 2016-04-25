package com.dyrwi.lasttimesince.repo.models;

import android.graphics.Color;
import android.util.Log;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 * <p>
 * Activities are what you are talking about. For example, if you want to remember the last time you
 * had Coffee, this would be the Coffee class.
 * The events attached to this are when you had that coffee (Friday, Saturday, this morning, etc...)
 */
@DatabaseTable
public class JodaActivity extends BaseEntity {
    //Static Fields
    public final static String TAG = "JodaActivity";
    public final static String NAME = "name";
    public final static String EVENT_COLLECTION = "event_collection";
    public final static String ICON_COLOR = "icon_color";

    //Database Fields
    @DatabaseField(columnName = NAME)
    private String name;

    @ForeignCollectionField(columnName = EVENT_COLLECTION)
    private ForeignCollection<JodaEvent> events;

    @DatabaseField(columnName = ICON_COLOR)
    private int iconColor;

    private JodaEvent mostRecentEvent;

    private List<JodaEvent> eventList;

    public JodaActivity() {
        this.iconColor = Color.BLUE;
        this.name = "Activity" + getId();
    }

    public ForeignCollection<JodaEvent> getEvents() {
        return events;
    }

    public ArrayList<JodaEvent> getEventList() {
        if(eventList == null) {
            this.eventList = new ArrayList<JodaEvent>();
        }
        try {
            CloseableIterator<JodaEvent> iterator = getEvents().closeableIterator();
            while (iterator.hasNext()) {
                eventList.add(iterator.next());
            }
            return (ArrayList<JodaEvent>)eventList;
        } catch (Exception ex) {
            Log.i(TAG, "Exception has arisen. Returning empty list");
            ex.printStackTrace();
            return new ArrayList<JodaEvent>();
        }
    }

    public void setEvents(ForeignCollection<JodaEvent> events) {
        this.events = events;
    }

    public void addEvent(JodaEvent e) {
        getEvents().add(e);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JodaEvent getMostRecentEvent() {
        if (mostRecentEvent != null)
            return this.mostRecentEvent;

        try {
            CloseableIterator<JodaEvent> iterator = getEvents().closeableIterator();

            JodaEvent tempEvent = new JodaEvent();
            tempEvent.setDate(new LocalDate(-946771200000L));

            while (iterator.hasNext()) {
                JodaEvent currentEvent = iterator.next();
                if (tempEvent.getDate().isBefore(currentEvent.getDate())) {
                    tempEvent = currentEvent;
                } else if (tempEvent.getDate().isEqual(currentEvent.getDate())) {
                    if (tempEvent.getTime().isBefore(currentEvent.getTime()))
                        tempEvent = currentEvent;
                }
            }
            this.mostRecentEvent = tempEvent;
            return tempEvent;

        } catch (Exception ex) {
            Log.e(TAG, getName() + " has no events or error occured. Creating default");
            JodaEvent event = new JodaEvent();
            event.setDate(LocalDate.now());
            return event;
        }
    }

    public void setMostRecentEvent(JodaEvent event) {
        this.mostRecentEvent = event;
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
    }


}
