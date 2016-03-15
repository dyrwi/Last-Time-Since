package com.dyrwi.lasttimesince.repo.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ben on 03-Mar-16.
 *
 * Event model. This shows the time when you did the activity.
 * This class shows the barebones of it. What you can set for it goes here.
 */
public class Event extends BaseEntity {
    // STATIC Fields
    public final static String DATE = "date";
    public final static String TIME = "time";
    private final static String ACTIVITY_ID = "activity_id";

    // Database Fields
    @DatabaseField(columnName = DATE)
    private Date date;

    @DatabaseField(columnName = TIME)
    private Date time;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = ACTIVITY_ID)
    private Activity activity;

    // Getters and Setters

    public Event() {
        this.date = Calendar.getInstance().getTime();
        this.time = Calendar.getInstance().getTime();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
