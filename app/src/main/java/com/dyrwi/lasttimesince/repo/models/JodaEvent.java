package com.dyrwi.lasttimesince.repo.models;

import android.graphics.Color;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ben on 03-Mar-16.
 *
 * Event model. This shows the time when you did the activity.
 * This class shows the barebones of it. What you can set for it goes here.
 */
@DatabaseTable
public class JodaEvent extends BaseEntity {
    // STATIC Fields
    public final static String DATE = "date";
    public final static String TIME = "time";
    public final static String TITLE = "title";
    public final static String ACTIVITY_ID = "activity_id";
    public final static String ICON_COLOR = "icon_color";

    // Database Fields
    @DatabaseField(columnName = TITLE)
    private String title;

    @DatabaseField(columnName = DATE, dataType = DataType.SERIALIZABLE)
    private LocalDate date;

    @DatabaseField(columnName = TIME, dataType = DataType.SERIALIZABLE)
    private LocalTime time;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = ACTIVITY_ID)
    private JodaActivity activity;

    @DatabaseField(columnName = ICON_COLOR)
    private int iconColor;

    // Getters and Setters

    public JodaEvent() {
        this.date = new LocalDate();
        this.time = new LocalTime();
        this.title = "Event";
        this.iconColor = Color.GREEN;
    }

    public JodaActivity getActivity() {
        return activity;
    }

    public void setActivity(JodaActivity activity) {
        this.activity = activity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconColor() {
        return iconColor;
    }

    public void setIconColor(int iconColor) {
        this.iconColor = iconColor;
    }
}
