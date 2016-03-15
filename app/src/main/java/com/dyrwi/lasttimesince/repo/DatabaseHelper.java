package com.dyrwi.lasttimesince.repo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dyrwi.lasttimesince.repo.models.Activity;
import com.dyrwi.lasttimesince.repo.models.Event;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Ben on 03-Mar-16.
 *
 * DatabaseHelper is what links our repo to our main db.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String NAME = "lasttimesince.db";
    private static final int VERSION = 1;
    private String TAG = this.getClass().toString();

    private Dao<Activity, String> activityDao;
    private Dao<Event, String> eventDao;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate()");
            TableUtils.createTable(connectionSource, Activity.class);
            TableUtils.createTable(connectionSource, Event.class);
            Log.i(TAG, "Tables created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade()");
            TableUtils.dropTable(connectionSource, Activity.class, true);
            TableUtils.dropTable(connectionSource, Event.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Dao<Activity, String> getActivityDao() throws SQLException {
        if (activityDao == null) {
            activityDao = getDao(Activity.class);
        }
        return activityDao;
    }

    public Dao<Event, String> getEventDao() throws SQLException {
        if (eventDao == null) {
            eventDao = getDao(Event.class);
        }
        return eventDao;
    }
}
