package com.dyrwi.lasttimesince.repo.implementations;

import android.util.Log;

import com.dyrwi.lasttimesince.repo.DatabaseHelper;
import com.dyrwi.lasttimesince.repo.JodaDatabaseHelper;
import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.BaseEntity;
import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.joda.time.LocalDateTime;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 */
public class JodaActivityImpl implements JodaImplementation<JodaActivity> {
    private String TAG = this.getClass().toString();
    Dao<JodaActivity, String> activityDao;

    public JodaActivityImpl(JodaDatabaseHelper db) {
        try {
            activityDao = db.getJodaActivityDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retreiving event dao");
            e.printStackTrace();
        }
    }

    @Override
    public int createOrUpdate(JodaActivity activity) {
        if (findByID(activity.getId()) != null) {
            update(activity);
        } else {
            create(activity);
        }
        return 0;
    }

    private int create(JodaActivity activity) {
        try {
            return activityDao.create(activity);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating activity: " + activity.toString());
            e.printStackTrace();
        }
        return 0;
    }

    private int update(JodaActivity activity) {
        try {
            return activityDao.update(activity);
        } catch (SQLException e) {
            Log.e(TAG, "Error updating activity: " + activity.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(JodaActivity activity) {
        try {
            return activityDao.delete(activity);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting activity: " + activity.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public JodaActivity findByID(String id) {
        try {
            QueryBuilder<JodaActivity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<JodaActivity> pq = qb.prepare();
            return activityDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JodaActivity findByID(long id) {
        try {
            QueryBuilder<JodaActivity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<JodaActivity> pq = qb.prepare();
            return activityDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<JodaActivity> findByDateCreated(LocalDateTime dateCreated) {
        try {
            QueryBuilder<JodaActivity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_CREATED, dateCreated);
            PreparedQuery<JodaActivity> pq = qb.prepare();
            return activityDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateCreated.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JodaActivity> findByDateModified(LocalDateTime dateModified) {
        try {
            QueryBuilder<JodaActivity, String> qb = activityDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_MODIFIED, dateModified);
            PreparedQuery<JodaActivity> pq = qb.prepare();
            return activityDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateModified.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JodaActivity> getAll() {
        try {
            return activityDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving all activities");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JodaActivity get(JodaActivity activity) {
        try {
            return activityDao.queryForSameId(activity);
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving activity");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        for (JodaActivity e : getAll()) {
            delete(e);
        }
    }

    public void createFromList(List<JodaActivity> activities) {
        for (JodaActivity a : activities) {
            create(a);
        }
    }
}
