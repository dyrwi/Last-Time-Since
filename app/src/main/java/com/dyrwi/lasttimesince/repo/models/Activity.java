package com.dyrwi.lasttimesince.repo.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 *
 * Activities are what you are talking about. For example, if you want to remember the last time you
 * had Coffee, this would be the Coffee class.
 * The events attached to this are when you had that coffee (Friday, Saturday, this morning, etc...)
 */
public class Activity extends BaseEntity {
    //Static Fields
    public final static String NAME = "name";
    public final static String EVENT_COLLECTION = "event_collection";

    //Database Fields
    @DatabaseField(columnName = NAME)
    private String name;

    @ForeignCollectionField(columnName = EVENT_COLLECTION)
    private ForeignCollection<Event> events;

    public Activity() {
        this.name = "";
    }

    public ForeignCollection<Event> getEvents() {
        return events;
    }

    public void setEvents(ForeignCollection<Event> events) {
        this.events = events;
    }

    public void addEvent(Event e) {
        getEvents().add(e);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
