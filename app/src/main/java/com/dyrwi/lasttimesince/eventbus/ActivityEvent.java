package com.dyrwi.lasttimesince.eventbus;

import com.dyrwi.lasttimesince.repo.models.JodaActivity;

/**
 * Created by Ben on 19-Mar-16.
 */
public class ActivityEvent extends BaseEvent{
    private JodaActivity activity;

    public ActivityEvent(JodaActivity activity) {
        this.activity = activity;
    }

    public JodaActivity getActivity() {
        return activity;
    }
}
