package com.phunware.alerts.sample.db;

import android.provider.BaseColumns;

public final class Contracts {
	
	public abstract static class AlertEntry implements BaseColumns{
		
		public static final String TABLE_NAME = "alerts";
		public static final String C_PID = "pid";
		public static final String C_MESSAGE = "message";
		public static final String C_TARGET_GROUP = "target_group";
		public static final String C_TIMESTAMP = "timestamp";
		
		public static final int I_ID = 0;
		public static final int I_PID = 1;
		public static final int I_MESSAGE = 2;
		public static final int I_TARGET_GROUP = 3;
		public static final int I_TIMESTAMP = 4;
		
		public static final String[] PROJECTION = {
			_ID,
			C_PID,
			C_MESSAGE,
			C_TARGET_GROUP,
			C_TIMESTAMP
		};
		
		public static final String CREATE_SQL = 
				"CREATE TABLE " + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY,"
                        + C_PID + " INTEGER,"
                        + C_MESSAGE + " TEXT, "
                        + C_TARGET_GROUP + " TEXT, "
                        + C_TIMESTAMP + " DATETIME"
                        + ");";
		/**
		 * Trigger SQL to set the timestamp to "now" whenever a row is inserted.
		 */
		public static final String TIMESTAMP_TRIGGER = 
				"CREATE TRIGGER updateAlertTimestampOnInsert " +
                        "AFTER INSERT ON "+TABLE_NAME+" FOR EACH ROW BEGIN " +
                        "UPDATE "+TABLE_NAME+" SET "+C_TIMESTAMP+" = datetime('now') WHERE "+
                        _ID+" = last_insert_rowid(); " +
                        "END;";
	}
}
