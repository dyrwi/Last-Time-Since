package com.dyrwi.lasttimesince.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.repo.JodaRepo;
import com.dyrwi.lasttimesince.repo.Repo;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.dyrwi.lasttimesince.util.DateFormat;
import com.dyrwi.lasttimesince.util.EventManager;

import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

/**
 * Created by Ben on 11-Mar-16.
 */
public class ListViewActivityAdapter extends RecyclerSwipeAdapter<ListViewActivityAdapter.SimpleViewHolder> {
    public final String TAG = "ListViewActivityAdapter";

    private Context mContext;
    private ArrayList<JodaActivity> mActivitySet;
    private JodaActivity mActivityTrash;
    private JodaRepo repo;
    private boolean ignoreOnClick = false;
    private int openCounter = 0;

    private boolean openActivity = true;
    private boolean hasBeenOpenedBefore = false;
    private int positionOfRemovedItem;
    private boolean callOnBindViewFromDelete = true;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView txtActivityName;
        TextView txtLastTimeSince;
        ImageView imgTrash;
        ImageView imgOpen;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txtActivityName = (TextView) itemView.findViewById(R.id.activity_name);
            txtLastTimeSince = (TextView) itemView.findViewById(R.id.last_time_since);
            imgTrash = (ImageView) itemView.findViewById(R.id.trash);
            imgOpen = (ImageView) itemView.findViewById(R.id.open);
        }
    }


    public ListViewActivityAdapter(Context context, ArrayList<JodaActivity> objects) {
        this.mContext = context;
        this.mActivitySet = objects;
        this.repo = new JodaRepo(context);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activityview_list_item, parent, false);
        Log.i(TAG, "onCreateViewHolder");
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        Log.i(TAG, "onBindViewHolder()");
        if(callOnBindViewFromDelete == true) {

            JodaActivity activity = mActivitySet.get(position);
            viewHolder.txtActivityName.setText(activity.getName());
            Log.i(TAG, "Name: " + viewHolder.txtActivityName.getText() + ". Position: " + position);
            JodaEvent mostRecentEvent = EventManager.getMostRecentEvent(activity);
            viewHolder.txtLastTimeSince.setText(
                    mostRecentEvent.getDate().toString(DateTimeFormat.forPattern("EEEE, MMMM dd YYYY"))
                            + " @ " +
                            mostRecentEvent.getTime().toString(DateTimeFormat.forPattern(("KK:mm aa"))));
            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    hasBeenOpenedBefore = true;
                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    if (viewHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Middle) {
                        hasBeenOpenedBefore = true;
                        openActivity = false;
                    } else if (viewHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Open) {
                        hasBeenOpenedBefore = true;
                        openActivity = false;
                    } else if (viewHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {
                        if (!hasBeenOpenedBefore)
                            openActivity = true;
                    }
                    //Log.i(TAG, "hasBeenOpenedBefore: " + hasBeenOpenedBefore + ", openActivity: " + openActivity);
                    if (hasBeenOpenedBefore == true) {
                        Log.i(TAG, "Name: " + viewHolder.txtActivityName.getText() + ". DO NOT OPEN");
                    } else if (hasBeenOpenedBefore == true && openActivity == false) {
                        Log.i(TAG, "Name: " + viewHolder.txtActivityName.getText() + ". DO NOT OPEN");
                    } else if (hasBeenOpenedBefore == false && openActivity == true) {
                        Log.i(TAG, "Name: " + viewHolder.txtActivityName.getText() + ". OPEN");
                    }

                    if (viewHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Close) {
                        hasBeenOpenedBefore = false;
                    } else if (viewHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Middle) {
                        hasBeenOpenedBefore = false;
                    }
                }
            });
            viewHolder.imgOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    // Implement link to EventActivity class.
                    Log.i(getClass().toString(), "Open clicked");
                }
            });

            viewHolder.imgTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        //positionOfRemovedItem = position;
                        repo.getActivities().delete(mActivitySet.get(position));
                        Log.i(TAG, "Removing: " + viewHolder.txtActivityName.getText() + ". Position: " + position);
                        mActivityTrash = mActivitySet.remove(position);

                        //Log.i(TAG, "List Size (After Remove): " + mActivitySet.size());
                        positionOfRemovedItem = position;
                        notifyItemRemoved(position);
                        //notifyItemRangeChanged(position, mActivitySet.size());
                        //mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                        //removeShownLayouts(viewHolder.swipeLayout);
                        //mItemManger.closeAllItems();
                        Snackbar.make(view, "Deleted " + viewHolder.txtActivityName.getText(), Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //<JodaActivity> tempActivitySet = new ArrayList<JodaActivity>();
                                        //tempActivitySet.addAll(mActivitySet);
                                        //tempActivitySet.add(activity);
                                        //swap(tempActivitySet);
                                        //callOnBindViewFromDelete = false;
                                        callOnBindViewFromDelete = false;
                                        mActivitySet.add(positionOfRemovedItem, mActivityTrash);
                                        //notifyDataSetChanged();
                                        notifyItemInserted(positionOfRemovedItem);
                                        repo.getActivities().createOrUpdate(mActivityTrash);
                                        //mItemManger.closeItem(positionOfRemovedItem);
                                        //Log.i(TAG, "List Size (After Add): " + mActivitySet.size());
                                        //Log.i(TAG, "mItemManger openLayoutSize: " + mItemManger.getOpenLayouts().size());
                                    }
                                }).show();
                    } catch (IndexOutOfBoundsException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            mItemManger.bindView(viewHolder.itemView, position);
        }
    }

    public void swap(ArrayList<JodaActivity> activities) {
        this.mActivitySet.clear();
        this.mActivitySet.addAll(activities);
        notifyDataSetChanged();
    }

    private void addActivity(JodaActivity activity, int position) {

    }

    @Override
    public int getItemCount() {
        return mActivitySet.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


}
