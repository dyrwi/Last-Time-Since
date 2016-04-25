package com.dyrwi.lasttimesince.repo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dyrwi.lasttimesince.repo.models.JodaActivity;
import com.dyrwi.lasttimesince.repo.models.JodaEvent;
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
public class JodaDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String NAME = "jodalastimesince.db";
    private static final int VERSION = 8;
    private String TAG = this.getClass().toString();

    private Dao<JodaActivity, String> jodaActivityDao;
    private Dao<JodaEvent, String> jodaEventDao;

    public JodaDatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "onCreate()");
            TableUtils.createTableIfNotExists(connectionSource, JodaActivity.class);
            TableUtils.createTableIfNotExists(connectionSource, JodaEvent.class);
            Log.i(TAG, "Tables created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(TAG, "onUpgrade()");
            TableUtils.dropTable(connectionSource, JodaActivity.class, true);
            TableUtils.dropTable(connectionSource, JodaEvent.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Dao<JodaActivity, String> getJodaActivityDao() throws SQLException {
        if (jodaActivityDao == null) {
            jodaActivityDao = getDao(JodaActivity.class);
        }
        return jodaActivityDao;
    }

    public Dao<JodaEvent, String> getJodaEventDao() throws SQLException {
        if (jodaEventDao == null) {
            jodaEventDao = getDao(JodaEvent.class);
        }
        return jodaEventDao;
    }
}
