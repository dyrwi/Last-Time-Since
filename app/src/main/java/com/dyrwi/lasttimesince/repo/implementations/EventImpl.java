package com.dyrwi.lasttimesince.repo.implementations;

import android.util.Log;

import com.dyrwi.lasttimesince.repo.DatabaseHelper;
import com.dyrwi.lasttimesince.repo.models.BaseEntity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 03-Mar-16.
 */
public class EventImpl implements Implementation<Event> {
    private String TAG = this.getClass().toString();
    Dao<Event, String> eventDao;

    public EventImpl(DatabaseHelper db) {
        try {
            eventDao = db.getEventDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retreiving event dao");
            e.printStackTrace();
        }
    }

    @Override
    public int createOrUpdate(Event event) {
        if (findByID(event.getId()) != null) {
            update(event);
        } else {
            create(event);
        }
        return 0;
    }

    private int create(Event event) {
        try {
            return eventDao.create(event);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating student: " + event.toString());
            e.printStackTrace();
        }
        return 0;
    }

    private int update(Event event) {
        try {
            return eventDao.update(event);
        } catch (SQLException e) {
            Log.e(TAG, "Error updating event: " + event.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Event event) {
        try {
            return eventDao.delete(event);
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting event: " + event.toString());
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Event findByID(String id) {
        try {
            QueryBuilder<Event, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<Event> pq = qb.prepare();
            return eventDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event findByID(long id) {
        try {
            QueryBuilder<Event, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.ID, id);
            PreparedQuery<Event> pq = qb.prepare();
            return eventDao.queryForFirst(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error find by id for id: " + id);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> findByDateCreated(Date dateCreated) {
        try {
            QueryBuilder<Event, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_CREATED, dateCreated);
            PreparedQuery<Event> pq = qb.prepare();
            return eventDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateCreated.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> findByDateModified(Date dateModified) {
        try {
            QueryBuilder<Event, String> qb = eventDao.queryBuilder();
            qb.where().eq(BaseEntity.DATE_MODIFIED, dateModified);
            PreparedQuery<Event> pq = qb.prepare();
            return eventDao.query(pq);
        } catch (SQLException e) {
            Log.e(TAG, "Error finding list for date: " + dateModified.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Event> getAll() {
        try {
            return eventDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving all events");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        for (Event e : getAll()) {
            delete(e);
        }
    }

    public void createFromList(List<Event> events) {
        for (Event e : events) {
            create(e);
        }
    }
}
