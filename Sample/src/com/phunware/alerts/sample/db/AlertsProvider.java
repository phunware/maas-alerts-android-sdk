package com.phunware.alerts.sample.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlertsProvider {
	private static final String TAG = "AlertsProvider";

	/**
	 * The database that the provider uses as its underlying data store
	 */
	private static final String DATABASE_NAME = "alerts.db";

	/**
	 * The database version
	 */
	private static final int DATABASE_VERSION = 1;

	private DatabaseHelper mDb;

	/**
	 * Open a new database object
	 * 
	 * @param context
	 */
	public AlertsProvider(Context context) {
		mDb = new DatabaseHelper(context);
	}

	/**
	 * Close any open database object
	 */
	public void close() {
		mDb.close();
	}

	/**
     * Return a cursor for all alert entries, default order by last modified descending
     * @param orderBy
     * @return
     */
    public Cursor getAllAlerts(String orderBy)
    {
            SQLiteDatabase db = mDb.getReadableDatabase();
            
            if(orderBy == null)
                    orderBy = Contracts.AlertEntry.C_TIMESTAMP +" DESC";
            return db.query(Contracts.AlertEntry.TABLE_NAME, Contracts.AlertEntry.PROJECTION, 
                            null, null, null, null, orderBy);
    }

	/**
	 * Insert a new Alert into the db
	 * 
	 * @param values
	 * @return the row id or -1 if an error
	 */
	public long insertAlert(ContentValues values) {
		SQLiteDatabase db = mDb.getWritableDatabase();
		return db.insert(Contracts.AlertEntry.TABLE_NAME, null, values);
	}
	
	/**
	 * Delete a single alert by its id
	 * @param alertId
	 * @return the number of rows affected.
	 */
	public int deleteAlert(long alertId)
	{
		SQLiteDatabase db = mDb.getWritableDatabase();
		String whereClause = Contracts.AlertEntry._ID + " LIKE ?";
		String whereArgs[] = new String[]{String.valueOf(alertId)};
		return db.delete(Contracts.AlertEntry.TABLE_NAME, whereClause, whereArgs);
	}

	static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {

			// calls the super constructor, requesting the default cursor
			// factory.
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Contracts.AlertEntry.CREATE_SQL);
			db.execSQL(Contracts.AlertEntry.TIMESTAMP_TRIGGER);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Logs that the database is being upgraded
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

			// Kills the table and existing data
			db.execSQL("DROP TABLE IF EXISTS " + Contracts.AlertEntry.TABLE_NAME);

			// Recreates the database with a new version
			onCreate(db);
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onUpgrade(db, oldVersion, newVersion);
		}
	}
}
