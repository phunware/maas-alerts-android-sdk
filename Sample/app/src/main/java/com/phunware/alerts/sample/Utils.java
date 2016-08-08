package com.phunware.alerts.sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

public class Utils {

	public static final int MENU_INDEX_REFRESH = 2;
	public static final int MENU_INDEX_UPDATE = 3;
	public static final int MENU_INDEX_CLEAR = 4;
	public static final int MENU_INDEX_EMAIL = 5;

	public static final String INTENT_ALERT_DATA = "com.phunware.alerts.sample.INTENT_ALERT_DATA";
	public static final String INTENT_ALERT_EXTRA = "com.phunware.alerts.sample.INTENT_ALERT_EXTRA";
	public static final String INTENT_ALERT_EXTRA_PID = "com.phunware.alerts.sample.PID";

	public static final String BROADCAST_MESSAGE_ACTION = "com.phunware.core.BROADCAST_MESSAGE_ACTION";
	public static final String BROADCAST_MESSAGE_KEY = "com.phunware.core.BROADCAST_MESSAGE_KEY";
	public static final String BROADCAST_SUCCESSFUL_KEY = "com.phunware.alerts.BROADCAST_SUCCESSFUL_KEY";
	public static final String ACTION_ON_REGISTERED = "com.phunware.alerts.sample.ACTION_ON_REGISTERED";
	public static final String ACTION_ON_UNREGISTERED = "com.phunware.alerts.sample.ACTION_ON_UNREGISTERED";

	public static void email(Context packageContext, String body) {
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "ytran@phunware.com", "rszabo@phunware.com" });
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MaaS Alerts Console "+getCurrentTimeFormatted());
		packageContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}
	
	 /**
     * Given a date, convert the string into a long format.
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long convertDateToLong(String date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar uc = Calendar.getInstance(TimeZone.getDefault());
            long offset = uc.get(Calendar.ZONE_OFFSET) + uc.get(Calendar.DST_OFFSET);
            try {
                    uc.setTime(sdf.parse(date + offset));
            } catch (ParseException e) {
                    e.printStackTrace();
            }
            return uc.getTimeInMillis();
    }
    
    public static String convertLongToString(long timestamp)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    	Calendar uc = Calendar.getInstance();
    	uc.setTimeInMillis(timestamp);
    	return sdf.format(uc.getTime());
    }
    
    /**
	 * Convenience method to get the current time as a string formatted as
	 * <code>yyyy-MM-dd'T'HH:mm:ss.SSS'Z'</code>.
	 * 
	 * @return The current time formatted as
	 *         <code>yyyy-MM-dd'T'HH:mm:ss'Z'</code>; RFC3339 format
	 */
	public static String getCurrentTimeFormatted() {
		long now = System.currentTimeMillis();
		TimeZone utc = TimeZone.getTimeZone("UTC");
		GregorianCalendar cal = new GregorianCalendar(utc);
		cal.setTimeInMillis(now);

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		formatter.setTimeZone(utc);
		return formatter.format(cal.getTime());
	}
}
