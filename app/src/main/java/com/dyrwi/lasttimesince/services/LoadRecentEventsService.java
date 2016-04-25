package com.dyrwi.lasttimesince.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dyrwi.lasttimesince.activities.NewListViewActivity;
import com.dyrwi.lasttimesince.eventbus.JodaActivityEvent;
import com.dyrwi.lasttimesince.eventbus.UpdateEvent;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class LoadRecentEventsService extends IntentService implements Handler.Callback{
    public static final String TAG = "LoadRecentEventsService";
    public static final String SET_CONTINUE_FALSE = "SET_CONTINUE_FALSE";
    public static final String SET_CONTINUE_TRUE = "SET_CONTINUE_TRUE";
    public static volatile boolean shouldContinue = true;
    public static String ACTIVITIES = "ACTIVITIES";
    ArrayList<JodaActivity> activitiesList;


    public LoadRecentEventsService() {
        super("LoadRecentEventsService");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Starting...");
        long startTime = System.currentTimeMillis();
        long[] activitiesID = intent.getLongArrayExtra(ACTIVITIES);

        JodaRepo repo = new JodaRepo(getApplicationContext());
        activitiesList = (ArrayList<JodaActivity>) repo.getActivities().getAll();
        ArrayList<JodaActivity> activitiesToSend = new ArrayList<JodaActivity>();

        for (int i = 0; i < activitiesID.length; i++) {
            for (int j = 0; j < activitiesList.size(); j++) {
                if (shouldContinue == true) {
                    if (activitiesID[i] == activitiesList.get(j).getId()) {
                        Log.i(TAG, "Working ... " + j);
                        activitiesToSend.add(i, activitiesList.get(j));
                        activitiesToSend.get(i).getMostRecentEvent();
                    }
                } else {
                    Log.i(TAG, "Stopping prematurely ...");
                    stopSelf();
                    EventBus.getDefault().unregister(this);
                    return;
                }
            }
        }

        if (shouldContinue == true) {
            JodaActivityEvent eventToPost = new JodaActivityEvent();
            eventToPost.setActivities(activitiesToSend);
            eventToPost.setTargetClass(NewListViewActivity.class);
            eventToPost.setTag(NewListViewActivity.SERVICE_COMPLETION_NOTIFICATION);
            EventBus.getDefault().post(eventToPost);
            EventBus.getDefault().unregister(this);
            Log.i(TAG, "Finished. Total Time: " + (System.currentTimeMillis() - startTime) + " milliseconds");
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.i(TAG, "MESSAGE");
        return false;
    }

    @Subscribe(sticky = true)
    public void onUpdate(UpdateEvent e) {
        if(e.getTag().equals(TAG)) {
            Log.i(TAG, "shouldContinue = false");
            shouldContinue = false;
        } else if (e.getTag().equals(SET_CONTINUE_TRUE)) {
            Log.i(TAG, "shouldContinue = true");
            shouldContinue = true;
        }
    }
}



