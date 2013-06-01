package com.phunware.alerts.sample.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.phunware.alerts.models.PwAlertExtras;
import com.phunware.alerts.sample.Utils;

public class AlertModel implements Parcelable{

	private static final String TAG = "AlertModel";
	public long id;
	public long pid;
	public String message;
	public String targetGroup;
	public long timestamp;

	public AlertModel()
	{
		id = -1;
		pid = -1;
		message = "";
		targetGroup = "";
		timestamp = -1;
	}
	
	public AlertModel(long pid, String message, String targetGroup) {
		this();
		this.pid = pid;
		this.message = message;
		this.targetGroup = targetGroup;
	}

	/**
     * Create an object with a {@link Cursor}.
     * The cursor must already be at the desired position
     * @param c
     */
    public AlertModel(Cursor c)
    {
            id = c.getLong(Contracts.AlertEntry.I_ID);
            pid = c.getLong(Contracts.AlertEntry.I_PID);
            message = c.getString(Contracts.AlertEntry.I_MESSAGE);
            targetGroup = c.getString(Contracts.AlertEntry.I_TARGET_GROUP);
            timestamp = Utils.convertDateToLong(c.getString(Contracts.AlertEntry.I_TIMESTAMP));
    }
    
    public AlertModel(PwAlertExtras extras, JSONObject data)
    {
    	this();
    	Log.v(TAG, extras.toString());
    	this.pid = Long.parseLong(extras.getDataPID());
    	this.message = extras.getAlertMessage();
    	
    	try {
			JSONArray subs = data.getJSONArray("subscriptionGroups");
			int size = subs.length();
			String t = "";
			for(int i=0; i<size; i++)
			{
				String tt = "";
				if(!t.equals(""))
					tt = ", ";
				t += tt + subs.getJSONObject(i).getString("name");
			}
			this.targetGroup = t;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
    }
    
    public ContentValues toContentValues(boolean includeId)
    {
    	ContentValues values = new ContentValues();
    	if(includeId)
    		values.put(Contracts.AlertEntry._ID, id);
    	values.put(Contracts.AlertEntry.C_PID, pid);
    	values.put(Contracts.AlertEntry.C_MESSAGE, message);
    	values.put(Contracts.AlertEntry.C_TARGET_GROUP, targetGroup);
    	return values;
    }
    
    /**
     * Save this alert model into the database. This does not run asynchronously and should be run as such.
     * @param db Database provider
     * @return id of the inserted row, or -1 if error
     */
    public long save(AlertsProvider db)
    {
    	id = db.insertAlert(toContentValues(false));
    	return id;
    }
	
	@Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(pid);
        dest.writeString(message);
        dest.writeString(targetGroup);
        dest.writeLong(timestamp);
    }

    public static final Parcelable.Creator<AlertModel> CREATOR = new Parcelable.Creator<AlertModel>() {
        public AlertModel createFromParcel(Parcel in) {
            return new AlertModel(in);
        }

        public AlertModel[] newArray(int size) {
            return new AlertModel[size];
        }
    };

    private AlertModel(Parcel in) {
        id = in.readLong();
        pid = in.readLong();
        message = in.readString();
        targetGroup = in.readString();
        timestamp = in.readLong();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + (int) (pid ^ (pid >>> 32));
		result = prime * result + ((targetGroup == null) ? 0 : targetGroup.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlertModel other = (AlertModel) obj;
		if (id != other.id)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (pid != other.pid)
			return false;
		if (targetGroup == null) {
			if (other.targetGroup != null)
				return false;
		} else if (!targetGroup.equals(other.targetGroup))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AlertModel [id=" + id + ", pid=" + pid + ", message=" + message + ", targetGroup=" + targetGroup + ", timestamp=" + timestamp + "]";
	}
}
