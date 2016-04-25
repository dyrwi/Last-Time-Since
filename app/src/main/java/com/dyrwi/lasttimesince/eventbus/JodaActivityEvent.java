package com.dyrwi.lasttimesince.eventbus;

import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;

import java.util.ArrayList;

/**
 * Created by Ben on 18-Mar-16.
 */
public class JodaActivityEvent extends BaseEvent{
    public ArrayList<JodaActivity> activities;
    public JodaActivity activity;
    public JodaEvent event;
    public String tag;

    public JodaActivityEvent(ArrayList<JodaActivity> activitiesList) {
        this.activities = activitiesList;
        this.tag = "TAG";
    }

    public JodaActivityEvent(ArrayList<JodaActivity> activitiesList, String tag) {
        this.activities = activitiesList;
        this.tag = tag;
    }

    public JodaActivityEvent(JodaEvent event, String tag) {
        this.event = event;
        this.tag = tag;
    }

    public JodaActivityEvent(JodaActivity activity, String tag) {
        this.activity = activity;
        this.tag = tag;
    }

    public JodaActivityEvent() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public JodaActivityEvent(JodaActivity activity) {
        this.activity = activity;
    }

    public ArrayList<JodaActivity> getActivities() {
        return activities;
    }

    public JodaActivity getActivity() {
        return this.activity;
    }

    public JodaEvent getEvent() {
        return event;
    }

    public void setEvent(JodaEvent event) {
        this.event = event;
    }

    public void setActivities(ArrayList<JodaActivity> activities) {
        this.activities = activities;
    }

    public void setActivity(JodaActivity activity) {
        this.activity = activity;
    }
}
