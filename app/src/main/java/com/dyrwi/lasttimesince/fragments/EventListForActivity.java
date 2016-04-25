package com.dyrwi.lasttimesince.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.dyrwi.lasttimesince.util.ItemTouchHelperClass;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Ben on 19-Mar-16.
 */
public class EventListForActivity extends Fragment {
    public static String ACTIVITY_ID = "activity_id";

    // Java VARS
    private JodaRepo repo;
    private JodaActivity activity;
    private JodaEvent mJustDeletedToDoItem;
    private int mIndexOfDeletedToDoItem;

    // View Variables from XML
    private TextView vEventName, vLastTimeSince;

    @Override
    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        this.repo = new JodaRepo(getActivity());
        activity = this.repo.getActivities().findByID(getArguments().getLong(ACTIVITY_ID));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStatus) {
        View view = inflater.inflate(R.layout.fragment_activity_create_edit, container, false);
        return view;
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

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @SuppressWarnings("deprecation")
        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        EventListAdapter(ArrayList<JodaEvent> items) {
            this.items = items;
        }
    }

}



