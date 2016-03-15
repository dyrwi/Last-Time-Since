package com.dyrwi.lasttimesince.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.repo.Repo;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.dyrwi.lasttimesince.util.DateFormat;
import com.dyrwi.lasttimesince.util.EventManager;
import com.dyrwi.lasttimesince.util.RecyclerViewEmptySupport;

import java.util.List;

/**
 * Created by Ben on 04-Mar-16.
 */
public class SwipeToRefreshListFragment extends Fragment {

    private String TAG = this.getClass().toString();
    private Repo repo;
    private List<Activity> activityList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerViewEmptySupport recyclerViewEmptySupport;
    private ActivityAdapter activityAdapter;

    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        repo = new Repo(getActivity());
        activityList = repo.getActivities().getAll();
        getActivity().registerReceiver(new Receiver(), new IntentFilter("ActivityList"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.activity_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        activityAdapter = new ActivityAdapter(getContext(), activityList);
        recyclerViewEmptySupport = (RecyclerViewEmptySupport) view.findViewById(R.id.recycler_view);

        TextView emptyMessage = (TextView) view.findViewById(R.id.empty_message);
        emptyMessage.setText(R.string.no_activities);
        recyclerViewEmptySupport.setEmptyView(view.findViewById(R.id.empty));

        recyclerViewEmptySupport.setHasFixedSize(true);
        recyclerViewEmptySupport.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEmptySupport.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewEmptySupport.setAdapter(activityAdapter);

        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewEmptySupport.getLayoutManager();
        recyclerViewEmptySupport.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });


        return view;

    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private List<Activity> activityList;
        private Context context;

        public ActivityAdapter(Context context, List<Activity> activityList) {
            this.activityList = activityList;
            this.context = context;
        }


        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_activities, null);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
            Activity activity = activityList.get(position);
            holder.activityName.setText(activity.getName());
            holder.lastTimeSince.setText(DateFormat.lastTimeSince(getActivity(), EventManager.getMostRecentEvent(activity).getDate()));
            Event e = EventManager.get(activity, EventManager.OLDEST_EVENT_FOR_ACTIVITY);
        }

        @Override
        public int getItemCount() {
            return (activityList != null ? activityList.size() : 0);
        }
    }

    private class ActivityHolder extends RecyclerView.ViewHolder {
        private TextView activityName;
        private TextView lastTimeSince;

        public ActivityHolder(View itemView) {
            super(itemView);
            activityName = (TextView)itemView.findViewById(R.id.activity_name);
            lastTimeSince = (TextView)itemView.findViewById(R.id.last_time_since);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Activity: " + activityName.getText());
                }
            });
        }
    }

    /**
     * Receiver used so we can update the list when the user adds
     * a new student from the AddEditStudent class. There may be a better
     * way to implement this, however I do not know it.
     */
    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }
}
