package com.dyrwi.lasttimesince.repo.implementations;

import android.util.Log;

import com.dyrwi.lasttimesince.repo.DatabaseHelper;
import com.dyrwi.lasttimesince.repo.JodaDatabaseHelper;
import com.dyrwi.lasttimesince.repo.models.BaseEntity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import org.joda.time.LocalDateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 */
public class JodaEventImpl implements JodaImplementation<JodaEvent> {
    private String TAG = this.getClass().toString();
    Dao<JodaEvent, String> eventDao;

    public JodaEventImpl(JodaDatabaseHelper db) {
        try {
            eventDao = db.getJodaEventDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retreiving event dao");
            e.printStackTrace();
        }
    }

    @Override
    public int createOrUpdate(JodaEvent event) {
        if (findByID(event.getId()) != null) {
            update(event);
        } else {
            create(event);
        }
        return 0;
    }

    private int create(JodaEvent event) {
        try {
            return eventDao.create(event);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating event: " + event.toString());
            //e.printStackTrace();
        }
        return 0;
    }

    private int update(JodaEvent event) {
        try {
            return eventDao.update(event);
        } catch (SQLException e) {
            Log.e(TAG, "Error updating event: " + event.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(JodaEvent event) {
        try {
            return eventDao.delete(event);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting event: " + event.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public JodaEvent findByID(String id) {
        try {
            QueryBuilder<JodaEvent, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<JodaEvent> pq = qb.prepare();
            return eventDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JodaEvent findByID(long id) {
        try {
            QueryBuilder<JodaEvent, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<JodaEvent> pq = qb.prepare();
            return eventDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JodaEvent> findByDateCreated(LocalDateTime dateCreated) {
        try {
            QueryBuilder<JodaEvent, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_CREATED, dateCreated);
            PreparedQuery<JodaEvent> pq = qb.prepare();
            return eventDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateCreated.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JodaEvent> findByDateModified(LocalDateTime dateModified) {
        try {
            QueryBuilder<JodaEvent, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_MODIFIED, dateModified);
            PreparedQuery<JodaEvent> pq = qb.prepare();
            return eventDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateModified.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JodaEvent> getAll() {
        try {
            return eventDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving all events");
            e.printStackTrace();
        }
        return new ArrayList<JodaEvent>();
    }

    @Override
    public JodaEvent get(JodaEvent event) {
        try {
            return eventDao.queryForSameId(event);
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving event");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        for (JodaEvent e : getAll()) {
            delete(e);
        }
    }

    public void createFromList(List<JodaEvent> events) {
        for (JodaEvent e : events) {
            create(e);
        }
    }

    //TODO
    // Create a method which takes an activity (the class or ID of that activity) as a parameter and returns
    // all the events for that given activity.
    public List<JodaEvent> findByActivity(long activityID) {
        try {
            QueryBuilder<JodaEvent, String> qb = eventDao.queryBuilder();
            qb.where().eq(JodaEvent.ACTIVITY_ID, activityID);
            PreparedQuery<JodaEvent> pq = qb.prepare();
            return eventDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for activity: " + activityID);
            e.printStackTrace();
        }
        return null;
    }

    public void deleteEventsByActivity(long activityID) {
        try {
            QueryBuilder<JodaEvent, String> qb = eventDao.queryBuilder();
            qb.where().eq(JodaEvent.ACTIVITY_ID, activityID);
            PreparedQuery<JodaEvent> pq = qb.prepare();
            ArrayList<JodaEvent> eventsToDelete = (ArrayList<JodaEvent>) eventDao.query(pq);
            eventDao.delete(eventsToDelete);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting events for " + activityID);
            e.printStackTrace();
        }
    }


}
