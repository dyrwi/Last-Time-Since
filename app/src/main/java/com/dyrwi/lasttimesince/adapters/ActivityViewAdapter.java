package com.dyrwi.lasttimesince.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.dyrwi.lasttimesince.R;
import com.dyrwi.lasttimesince.repo.Repo;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.util.DateFormat;
import com.dyrwi.lasttimesince.util.EventManager;

import java.util.ArrayList;

/**
 * Created by Ben on 11-Mar-16.
 */
public class ActivityViewAdapter extends RecyclerSwipeAdapter<ActivityViewAdapter.SimpleViewHolder> {

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView txtActivityName;
        TextView txtLastTimeSince;
        ImageView imgTrash;
        ImageView imgOpen;
        //Button btnDelete;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            txtActivityName = (TextView) itemView.findViewById(R.id.activity_name);
            txtLastTimeSince = (TextView) itemView.findViewById(R.id.last_time_since);
            imgTrash = (ImageView) itemView.findViewById(R.id.trash);
            imgOpen = (ImageView) itemView.findViewById(R.id.open);
           //btnDelete = (Button) itemView.findViewById(R.id.delete);
        }
    }

    private Context mContext;
    private ArrayList<Activity> mActivitySet;
    private Activity mActivityTrash;
    private Repo repo;

    private boolean openActivity = true;

    public ActivityViewAdapter(Context context, ArrayList<Activity> objects) {
        this.mContext = context;
        this.mActivitySet = objects;
        this.repo = new Repo(context);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activityview_list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        Activity activity = mActivitySet.get(position);
        viewHolder.txtActivityName.setText(activity.getName());
        viewHolder.txtLastTimeSince.setText(DateFormat.lastTimeSince(mContext, EventManager.getMostRecentEvent(activity).getDate()));
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
            }

            @Override
            public void onClose(SwipeLayout layout) {
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                if (layout.getOpenStatus() == SwipeLayout.Status.Open)
                    layout.close();
                else
                    layout.open();
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                if (layout.getOpenStatus() == SwipeLayout.Status.Open)
                    layout.close();
                else
                    layout.open();
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
                    mActivityTrash = mActivitySet.get(position);
                    repo.getActivities().delete(mActivitySet.get(position));
                    mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                    mActivitySet.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mActivitySet.size());

                    Snackbar.make(view, "Deleted " + viewHolder.txtActivityName.getText(), Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    restoreActivity();
                                }
                            }).show();
                    mItemManger.closeAllItems();
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        });
        mItemManger.bindView(viewHolder.itemView, position);
    }

    private void restoreActivity() {
        repo.getActivities().createOrUpdate(mActivityTrash);
        mActivitySet.add(mActivityTrash);
        notifyItemInserted(mActivitySet.size() - 1);
        notifyItemRangeChanged(mActivitySet.size() - 1, mActivitySet.size());
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
