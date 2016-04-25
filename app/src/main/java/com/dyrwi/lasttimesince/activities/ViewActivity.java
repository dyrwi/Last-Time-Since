package com.dyrwi.lasttimesince.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.dyrwi.lasttimesince.util.ItemTouchHelperClass;
import com.dyrwi.lasttimesince.util.RecyclerViewEmptySupport;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ben on 18-Mar-16.
 */
public class ViewActivity extends AppCompatActivity {
    public static final String ACTIVITY_ID = "activity_id";
    public static final String TAG = "ViewActivity";
    public static final String UPDATE_EVENT = "update_event";
    public static final String NEW_EVENT = "new_event";
    public static final String UPDATE_ACTIVITY = "update_activity";

    private JodaRepo repo;
    private JodaActivity activity;
    private Toolbar toolbar;
    private RecyclerViewEmptySupport mRecyclerView;
    private ArrayList<JodaEvent> events;
    private EventListAdapter mAdapter;
    private ItemTouchHelper itemTouchHelper;
    private JodaEvent mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;
    private CoordinatorLayout coordinatorLayout;
    private boolean isLastTimeSinceUpToDate = false;
    private String[] lastTimeSinceText;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        setContentView(R.layout.activity_details);

        repo = new JodaRepo(this);
        activity = repo.getActivities().findByID(getIntent().getLongExtra(ACTIVITY_ID, 0));

        mRecyclerView = (RecyclerViewEmptySupport) findViewById(R.id.activity_details_recycler);
        mRecyclerView.setEmptyView(findViewById(R.id.empty_view));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        events = activity.getEventList();
        mAdapter = new EventListAdapter(events);
        mRecyclerView.setAdapter(mAdapter);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_details_coordlayout);

        ItemTouchHelper.Callback callback = new ItemTouchHelperClass(mAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_arrow_back).colorRes(R.color.menu_item_color).sizeDp(MyApplication.MENU_ICON_SIZE));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(activity.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        updateEventList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view, menu);
        getSupportActionBar().setHomeButtonEnabled(true);
        MenuItem mAddEvent = (MenuItem) menu.findItem(R.id.add_event);
        mAddEvent.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_add).colorRes(R.color.menu_item_color).sizeDp(MyApplication.MENU_ICON_SIZE));
        MenuItem mEditActivity = (MenuItem) menu.findItem(R.id.edit);
        mEditActivity.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_edit).colorRes(R.color.menu_item_color).sizeDp(MyApplication.MENU_ICON_SIZE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_event:
                Intent i = new Intent(ViewActivity.this, CreateEditEvent.class);
                i.putExtra(CreateEditEvent.ACTIVITY_ID, activity.getId());
                startActivity(i);
                return true;
            case R.id.edit:
                Log.i(TAG, "Edit");
                Intent j = new Intent(ViewActivity.this, CreateEditActivity.class);
                j.putExtra(CreateEditActivity.ACTIVITY_ID, activity.getId());
                startActivity(j);
                return true;
            default:
                return true;
        }
    }

    public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> implements ItemTouchHelperClass.ItemTouchHelperAdapter {
        private ArrayList<JodaEvent> items;

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
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(int position) {
            mJustDeletedToDoItem = items.remove(position);
            mIndexOfDeletedToDoItem = position;
            repo.getEvents().delete(mJustDeletedToDoItem);
            repo.getActivities().createOrUpdate(activity);
            notifyItemRemoved(mIndexOfDeletedToDoItem);

            Snackbar.make(coordinatorLayout, "Deleted " + mJustDeletedToDoItem.getTitle(), Snackbar.LENGTH_SHORT)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            items.add(mIndexOfDeletedToDoItem, mJustDeletedToDoItem);
                            repo.getEvents().createOrUpdate(mJustDeletedToDoItem);
                            repo.getActivities().createOrUpdate(activity);
                            notifyItemInserted(mIndexOfDeletedToDoItem);
                        }
                    }).show();
        }

        @Override
        public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_events, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            JodaEvent item = items.get(position);
            holder.txtEventTitle.setText(item.getTitle());
            if(!isLastTimeSinceUpToDate) {
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
                    .buildRound(item.getTitle().substring(0, 1), item.getIconColor());

            holder.imgIcon.setImageDrawable(myDrawable);
            holder.id = item.getId();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtEventTitle;
            TextView txtLastTimeSince;
            ImageView imgIcon;
            long id;

            public ViewHolder(View itemView) {
                super(itemView);
                txtEventTitle = (TextView) itemView.findViewById(R.id.event_name);
                txtLastTimeSince = (TextView) itemView.findViewById(R.id.last_time_since);
                imgIcon = (ImageView) itemView.findViewById(R.id.image);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(ViewActivity.this, CreateEditEvent.class);
                        i.putExtra(CreateEditEvent.EVENT_ID, id);
                        startActivity(i);
                    }
                });
            }
        }

        EventListAdapter(ArrayList<JodaEvent> items) {
            this.items = items;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        updateEventList();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        forceUpdateOnMainList();
        super.onStop();
    }

    private void forceUpdateOnMainList() {
        JodaActivityEvent e = new JodaActivityEvent();
        e.setTargetClass(NewListViewActivity.class);
        e.setTag(NewListViewActivity.UPDATE_ACTIVITY);
        e.setActivity(activity);
        EventBus.getDefault().postSticky(e);
    }

    private void updateEventList() {
        String[] lastTimeSinceText = new String[events.size()];
        for (int i = 0; i < events.size(); i++) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime mostRecentEventDateTime = LocalDateTime.now();
            mostRecentEventDateTime = mostRecentEventDateTime.withYear(events.get(i).getDate().getYear());
            mostRecentEventDateTime = mostRecentEventDateTime.withMonthOfYear(events.get(i).getDate().getMonthOfYear());
            mostRecentEventDateTime = mostRecentEventDateTime.withDayOfYear(events.get(i).getDate().getDayOfYear());
            mostRecentEventDateTime = mostRecentEventDateTime.withHourOfDay(events.get(i).getTime().getHourOfDay());
            mostRecentEventDateTime = mostRecentEventDateTime.withMinuteOfHour(events.get(i).getTime().getMinuteOfHour());
            mostRecentEventDateTime = mostRecentEventDateTime.withSecondOfMinute(events.get(i).getTime().getSecondOfMinute());
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
        isLastTimeSinceUpToDate = true;
        this.lastTimeSinceText = lastTimeSinceText;
    }

    @Subscribe(sticky = true)
    public void onJodaActivityEvent(JodaActivityEvent activityEvent) {
        JodaActivityEvent stickyEvent = EventBus.getDefault().getStickyEvent(JodaActivityEvent.class);
        if (stickyEvent != null && stickyEvent.getTargetClass() == ViewActivity.class) {
            if (stickyEvent.getTag().equals(NEW_EVENT)) {
                JodaEvent newEvent = activityEvent.getEvent();
                events.add(newEvent);
                mAdapter.notifyItemInserted(events.indexOf(newEvent));
            } else if (stickyEvent.getTag().equals(UPDATE_EVENT)) {
                for (int i = 0; i < events.size(); i++) {
                    JodaEvent newEvent = activityEvent.getEvent();
                    if (events.get(i).getId() == newEvent.getId()) {
                        events.set(i, newEvent);
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();
            } else if (stickyEvent.getTag().equals(UPDATE_ACTIVITY)) {
                if (stickyEvent.getActivity() != null) {
                    JodaActivity tempActivity = stickyEvent.getActivity();
                    activity.setName(tempActivity.getName());
                    activity.setIconColor(tempActivity.getIconColor());
                    getSupportActionBar().setTitle(activity.getName());
                }
            }
        }
        EventBus.getDefault().removeStickyEvent(stickyEvent);
    }

}
