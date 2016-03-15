package com.dyrwi.lasttimesince.repo.implementations;

import android.util.Log;

import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.DatabaseHelper;
import com.dyrwi.lasttimesince.repo.models.BaseEntity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 */
public class ActivityImpl implements Implementation<Activity> {
    private String TAG = this.getClass().toString();
    Dao<Activity, String> activityDao;

    public ActivityImpl(DatabaseHelper db) {
        try {
            activityDao = db.getActivityDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retreiving event dao");
            e.printStackTrace();
        }
    }

    @Override
    public int createOrUpdate(Activity activity) {
        if (findByID(activity.getId()) != null) {
            update(activity);
        } else {
            create(activity);
        }
        return 0;
    }

    private int create(Activity activity) {
        try {
            return activityDao.create(activity);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating activity: " + activity.toString());
            e.printStackTrace();
        }
        return 0;
    }

    private int update(Activity activity) {
        try {
            return activityDao.update(activity);
        } catch (SQLException e) {
            Log.e(TAG, "Error updating activity: " + activity.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Activity activity) {
        try {
            return activityDao.delete(activity);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting activity: " + activity.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Activity findByID(String id) {
        try {
            QueryBuilder<Activity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<Activity> pq = qb.prepare();
            return activityDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Activity findByID(long id) {
        try {
            QueryBuilder<Activity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<Activity> pq = qb.prepare();
            return activityDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Activity> findByDateCreated(Date dateCreated) {
        try {
            QueryBuilder<Activity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_CREATED, dateCreated);
            PreparedQuery<Activity> pq = qb.prepare();
            return activityDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateCreated.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Activity> findByDateModified(Date dateModified) {
        try {
            QueryBuilder<Activity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_MODIFIED, dateModified);
            PreparedQuery<Activity> pq = qb.prepare();
            return activityDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateModified.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Activity> getAll() {
        try {
            return activityDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving all activities");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        for (Activity e : getAll()) {
            delete(e);
        }
    }

    public void createFromList(List<Activity> activities) {
        for (Activity a : activities) {
            create(a);
        }
    }
}
