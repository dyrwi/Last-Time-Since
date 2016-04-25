package com.dyrwi.lasttimesince.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.eventbus.JodaActivityEvent;
import com.dyrwi.lasttimesince.eventbus.UpdateEvent;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.services.LoadRecentEventsService;
import com.dyrwi.lasttimesince.util.ItemTouchHelperClass;
import com.dyrwi.lasttimesince.util.RecyclerViewEmptySupport;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ben on 17-Mar-16.
 */
public class NewListViewActivity extends AppCompatActivity {
    //onSavedInstanceState
    private static final String UPDATE_MOST_RECENT_EVENT = "UPDATE_MOST_RECENT_EVENT";
    private static final String MOST_RECENT_EVENTS_ARRAY = "MOST_RECENT_EVENTS_ARRAY";

    public static final Object NEW_ACTIVITY = "NEW_ACTIVITY";
    public static final String TAG = "NewListViewActivity";
    public static final String UPDATE_ACTIVITY = "UPDATE_ACTIVITY";
    public static final String SERVICE_COMPLETION_NOTIFICATION = "SERVICE_COMPLETION_NOTIFICATION";
    public static final String CREATE_ACTIVITY = "CREATE_ACTIVITY";

    private RecyclerViewEmptySupport mRecyclerView;
    private BasicListAdapter adapter;
    private ArrayList<JodaActivity> activitiesList;
    private JodaActivity mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    private CoordinatorLayout mCoordLayout;
    private JodaRepo repo;
    private Toolbar toolbar;
    private ItemTouchHelper itemTouchHelper;
    private boolean isMostRecentEventUpdated = false;
    private String[] lastTimeSinceText;
    private FloatingActionButton mFab;
    private Intent serviceIntent;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_new_list);
        repo = new JodaRepo(this);
        activitiesList = (ArrayList<JodaActivity>) repo.getActivities().getAll();
        lastTimeSinceText = new String[repo.getActivities().getAll().size()];
        if (savedInstanceStatus != null) {
            isMostRecentEventUpdated = savedInstanceStatus.getBoolean(UPDATE_MOST_RECENT_EVENT);
            lastTimeSinceText = savedInstanceStatus.getStringArray(MOST_RECENT_EVENTS_ARRAY);
        } else {
            long[] activitiesID = new long[activitiesList.size()];
            for (int i = 0; i < activitiesList.size(); i++) {
                activitiesID[i] = activitiesList.get(i).getId();
            }
            startLoadRecentEventsService(activitiesID);
        }

        adapter = new BasicListAdapter(activitiesList);

        mRecyclerView = (RecyclerViewEmptySupport) findViewById(R.id.recycler_view_empty_support);
        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        mCoordLayout = (CoordinatorLayout) findViewById(R.id.coordlayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title);

        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(adapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).colorRes(R.color.md_white_1000).sizeDp(MyApplication.MENU_ICON_SIZE));
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewListViewActivity.this, CreateEditActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(UPDATE_MOST_RECENT_EVENT, isMostRecentEventUpdated);
        savedInstanceState.putStringArray(MOST_RECENT_EVENTS_ARRAY, lastTimeSinceText);
        super.onSaveInstanceState(savedInstanceState);
    }

    public class LoadMostRecentEvents extends AsyncTask<JodaActivity, String, String> {

        @Override
        protected String doInBackground(JodaActivity... params) {
            for (JodaActivity a : activitiesList) {
                Log.i(TAG, "Runnable: getMostRecentEvent()");
                a.getMostRecentEvent();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            isMostRecentEventUpdated = true;
        }
    }

    public class BasicListAdapter extends RecyclerView.Adapter<BasicListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
        private ArrayList<JodaActivity> items;
        private boolean canDeleteFromRepo = true;

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(items, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(items, i, i - 1);
                }
            }
            if (!isMostRecentEventUpdated)
                restartLoadRecentEventsService();
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(final int position) {
            mJustDeletedToDoItem = items.remove(position);
            mIndexOfDeletedToDoItem = position;
            notifyItemRemoved(mIndexOfDeletedToDoItem);

            /* Used for LoadRecentEventsService. This service can take a long time to process if there
            are many activities with many events. If one of the items is deleted while it is updating the status,
            it will need to restart.
             */
            if (!isMostRecentEventUpdated)
                restartLoadRecentEventsService();

            Snackbar snackbar = Snackbar.make(mCoordLayout, "Deleted " + mJustDeletedToDoItem.getName(), Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            canDeleteFromRepo = false;
                        }
                    });
            snackbar.setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (canDeleteFromRepo) {
                        Log.i(TAG, "DELETE");
                        repo.getActivities().delete(mJustDeletedToDoItem);
                        repo.getEvents().deleteEventsByActivity(mJustDeletedToDoItem.getId());
                    } else {
                        items.add(mIndexOfDeletedToDoItem, mJustDeletedToDoItem);
                        notifyItemInserted(mIndexOfDeletedToDoItem);
                        Log.i(TAG, "DO NOT DELETE");
                        canDeleteFromRepo = true;
                    }
                }
            });
            snackbar.show();

        }

        @Override
        public BasicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activities, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final BasicListAdapter.ViewHolder holder, final int position) {
            JodaActivity item = items.get(position);
            holder.txtActivityName.setText(item.getName());

            if (!isMostRecentEventUpdated) {
                holder.txtLastTimeSince.setText("Loading...");
            } else {
                holder.txtLastTimeSince.setText(lastTimeSinceText[position]);
            }


            ColorGenerator generator = ColorGenerator.DEFAULT;
            TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                    .textColor(Color.WHITE)
                    .useFont(Typeface.DEFAULT)
                    .toUpperCase()
                    .endConfig()
                    .buildRound(item.getName().substring(0, 1), item.getIconColor());

            holder.imgIcon.setImageDrawable(myDrawable);
            holder.id = item.getId();

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        BasicListAdapter(ArrayList<JodaActivity> items) {
            this.items = items;
        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtActivityName;
            TextView txtLastTimeSince;
            ImageView imgIcon;
            long id;

            public ViewHolder(View view) {
                super(view);
                txtActivityName = (TextView) view.findViewById(R.id.activity_name);
                txtLastTimeSince = (TextView) view.findViewById(R.id.last_time_since);
                imgIcon = (ImageView) view.findViewById(R.id.image);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isMostRecentEventUpdated) {
                            stopLoadRecentEventsService();
                        }
                        Intent i = new Intent(NewListViewActivity.this, ViewActivity.class);
                        i.putExtra(ViewActivity.ACTIVITY_ID, id);
                        startActivity(i);
                    }
                });
            }
        }
    }

    private void stopLoadRecentEventsService() {
        EventBus.getDefault().postSticky(new UpdateEvent(LoadRecentEventsService.SET_CONTINUE_FALSE));
        stopService(getServiceIntent());
        this.serviceIntent = null;
        isMostRecentEventUpdated = false;
    }

    private void restartLoadRecentEventsService() {
        stopLoadRecentEventsService();
        startLoadRecentEventsService(getAllActivityId());
    }

    private long[] getAllActivityId() {
        long[] activitiesID = new long[activitiesList.size()];
        Log.i(TAG, "Size is: " + activitiesList.size());
        for (int i = 0; i < activitiesList.size(); i++) {
            activitiesID[i] = activitiesList.get(i).getId();
        }
        return activitiesID;
    }

    private void startLoadRecentEventsService(long[] activitiesID) {
        getServiceIntent().putExtra(LoadRecentEventsService.ACTIVITIES, activitiesID);
        EventBus.getDefault().postSticky(new UpdateEvent(LoadRecentEventsService.SET_CONTINUE_TRUE));
        startService(getServiceIntent());
    }

    private Intent getServiceIntent() {
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, LoadRecentEventsService.class);
        }
        return serviceIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_all_data:
                repo.deleteAllData();
                adapter.notifyDataSetChanged();
                break;
            case R.id.menu_american_states:
                repo.showMeTheData();
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.i(TAG, "onStop");
        if (!isMostRecentEventUpdated) {
            stopLoadRecentEventsService();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void newActivityCreated(JodaActivityEvent event) {
        if (event != null && event.getTargetClass() == NewListViewActivity.class) {
            if (event.getTag().equals(CREATE_ACTIVITY)) {
                Log.i(TAG, "Creating Activity in List");
                JodaActivityEvent stickyEvent = EventBus.getDefault().getStickyEvent(JodaActivityEvent.class);
                JodaActivity newActivity = stickyEvent.getActivity();
                activitiesList.add(newActivity);
                adapter.notifyItemInserted(activitiesList.indexOf(newActivity));
                isMostRecentEventUpdated = false;
                EventBus.getDefault().removeStickyEvent(stickyEvent);
                startLoadRecentEventsService(new long[]{newActivity.getId()});
            } else if (event.getTag().equals(UPDATE_ACTIVITY)) {
                Log.i(TAG, "Updating Activity in List");
                JodaActivityEvent stickyEvent = EventBus.getDefault().getStickyEvent(JodaActivityEvent.class);
                JodaActivity newActivity = stickyEvent.getActivity();
                for (int i = 0; i < activitiesList.size(); i++) {
                    if (activitiesList.get(i).getId() == newActivity.getId()) {
                        activitiesList.set(i, newActivity);
                        adapter.notifyItemChanged(i);
                        isMostRecentEventUpdated = false;
                        EventBus.getDefault().removeStickyEvent(stickyEvent);
                        startLoadRecentEventsService(new long[]{newActivity.getId()});
                        break;
                    }
                }
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateEvent e) {
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void jodaActivityEvent(JodaActivityEvent event) {
        if (event != null) {
            if (event.getTargetClass() == NewListViewActivity.class && event.getTag().equals(SERVICE_COMPLETION_NOTIFICATION)) {
                Log.i(TAG, "Received Total Events: " + event.getActivities().size());
                String[] lastTimeSinceText = new String[activitiesList.size()];
                for (int i = 0; i < event.getActivities().size(); i++) {
                    for (int j = 0; j < activitiesList.size(); j++) {
                        if (event.getActivities().get(i).getId() == activitiesList.get(j).getId()) {
                            activitiesList.get(j).setMostRecentEvent(event.getActivities().get(i).getMostRecentEvent());
                        }
                    }
                }
                for (int i = 0; i < activitiesList.size(); i++) {
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    LocalDateTime mostRecentEventDateTime = LocalDateTime.now();
                    mostRecentEventDateTime = mostRecentEventDateTime.withYear(activitiesList.get(i).getMostRecentEvent().getDate().getYear());
                    mostRecentEventDateTime = mostRecentEventDateTime.withMonthOfYear(activitiesList.get(i).getMostRecentEvent().getDate().getMonthOfYear());
                    mostRecentEventDateTime = mostRecentEventDateTime.withDayOfYear(activitiesList.get(i).getMostRecentEvent().getDate().getDayOfYear());
                    mostRecentEventDateTime = mostRecentEventDateTime.withHourOfDay(activitiesList.get(i).getMostRecentEvent().getTime().getHourOfDay());
                    mostRecentEventDateTime = mostRecentEventDateTime.withMinuteOfHour(activitiesList.get(i).getMostRecentEvent().getTime().getMinuteOfHour());
                    mostRecentEventDateTime = mostRecentEventDateTime.withSecondOfMinute(activitiesList.get(i).getMostRecentEvent().getTime().getSecondOfMinute());
                    Period dateTimePeroid = new Period(mostRecentEventDateTime, currentDateTime);

                    PeriodFormatter years = new PeriodFormatterBuilder()
                            .appendYears()
                            .appendSuffix(" year", " years")
                            .toFormatter();

                    PeriodFormatter months = new PeriodFormatterBuilder()
                            .appendMonths()
                            .appendSuffix(" month", " months")
                            .toFormatter();

                    PeriodFormatter days = new PeriodFormatterBuilder()
                            .appendDays()
                            .appendSuffix(" day", " days")
                            .toFormatter();

                    PeriodFormatter hours = new PeriodFormatterBuilder()
                            .appendHours()
                            .appendSuffix(" hour", " hours")
                            .toFormatter();

                    PeriodFormatter minutes = new PeriodFormatterBuilder()
                            .appendMinutes()
                            .appendSuffix(" minute", " minutes")
                            .toFormatter();

                    PeriodFormatter seconds = new PeriodFormatterBuilder()
                            .appendSeconds()
                            .appendSuffix(" second", " seconds")
                            .toFormatter();

                    PeriodFormatter weeks = new PeriodFormatterBuilder()
                            .appendWeeks()
                            .appendSuffix(" week", " weeks")
                            .toFormatter();

                    PeriodFormatter hoursMinutesSeconds = new PeriodFormatterBuilder()
                            .appendHours()
                            .appendSuffix(" hour", " hours")
                            .appendSeparator(", ")
                            .appendMinutes()
                            .appendSuffix(" minute", " minutes")
                            .appendSeparator(", ")
                            .appendSeconds()
                            .appendSuffix(" second", " seconds")
                            .toFormatter();

                    //TODO
                    //Need to get this displaying correctly.
                    while (true) {
                        if (dateTimePeroid.getYears() >= 2) {
                            lastTimeSinceText[i] = years.print(dateTimePeroid.normalizedStandard());
                            break;
                        } else if (dateTimePeroid.getYears() == 1) {
                            lastTimeSinceText[i] = years.print(dateTimePeroid.normalizedStandard()) + " " + weeks.print(dateTimePeroid.normalizedStandard());
                            break;
                        } else {
                            if (dateTimePeroid.getMonths() >= 6) {
                                lastTimeSinceText[i] = months.print(dateTimePeroid.normalizedStandard()) + " " + weeks.print(dateTimePeroid.normalizedStandard());
                                break;
                            } else if (dateTimePeroid.getMonths() >= 1) {
                                lastTimeSinceText[i] = months.print(dateTimePeroid.normalizedStandard()) + " " + days.print(dateTimePeroid.normalizedStandard());
                                break;
                            } else {
                                if (dateTimePeroid.getWeeks() >= 1) {
                                    if(dateTimePeroid.getDays() == 0) {
                                        lastTimeSinceText[i] = weeks.print(dateTimePeroid.normalizedStandard()) + " " + hours.print(dateTimePeroid.normalizedStandard());
                                        break;
                                    } else {
                                        lastTimeSinceText[i] = weeks.print(dateTimePeroid.normalizedStandard()) + " " + days.print(dateTimePeroid.normalizedStandard());
                                        break;
                                    }
                                } else {
                                    if (dateTimePeroid.getDays() >= 1) {
                                        lastTimeSinceText[i] = days.print(dateTimePeroid.normalizedStandard()) + " " + hours.print(dateTimePeroid.normalizedStandard());
                                        break;
                                    } else {
                                        if (dateTimePeroid.getHours() >= 12) {
                                            lastTimeSinceText[i] = hours.print(dateTimePeroid.normalizedStandard()) + " " + minutes.print(dateTimePeroid.normalizedStandard());
                                            break;
                                        } else {
                                            lastTimeSinceText[i] = hoursMinutesSeconds.print(dateTimePeroid.normalizedStandard());
                                            break;
                                        }

                                    }

                                }
                            }
                        }
                    }
                }
                isMostRecentEventUpdated = true;
                this.lastTimeSinceText = lastTimeSinceText;
                EventBus.getDefault().post(new UpdateEvent());
            }
        }
    }
}
