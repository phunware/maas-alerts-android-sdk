package com.phunware.alerts.sample;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.phunware.alerts.PwAlertsIntentService;
import com.phunware.alerts.models.PwAlertExtras;

/*
 * PRAISEAlerts uses Google Cloud Messaging (GCM) and handle data delivery to
 * clients app. Use this class to receive responses from GCM. As mentioned part
 * of GCM integration, by default this class must be named .GCMIntentService.
 * 
 */
public class GCMIntentService extends PwAlertsIntentService {
	private static final String TAG = "GCMIntentService";

	@Override
	public void onMessageAlertsError(Context context, PwAlertExtras extras,
			Exception e) {
		Log.v(TAG, "in onMessageAlertsError: " + e.getMessage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.phunware.praisealerts.PRAISEAlertsIntentService#onMessageAlerts(android
	 * .content.Context, com.phunware.praisealerts.models.AlertExtras,
	 * org.json.JSONObject)
	 */
	@Override
	public void onMessageAlerts(Context context, PwAlertExtras extras,
			JSONObject data) {

		Log.v(TAG, "in onMessageAlerts");
		Log.i(TAG, "Extras = " + extras.toString());
		Bundle bundle = new Bundle();
		bundle.putString(Utils.INTENT_ALERT_EXTRA, extras.toString());
		bundle.putString(Utils.INTENT_ALERT_EXTRA_PID, extras.getDataPID());
		if (data != null) {
			try {
				bundle.putString(Utils.INTENT_ALERT_DATA, data.toString(2));
			} catch (JSONException e) {
				bundle.putString(Utils.INTENT_ALERT_DATA, data.toString());
			}
		}
		generateNotification(context, extras.getAlertMessage(), bundle,
				TabActivity.class);
	}

	private void generateNotification(Context context, String alertMsg,
			Bundle extras, Class<?> activityClass) {
		long when = System.currentTimeMillis();

		// Package intent with Alert string data set.
		Intent notificationIntent = new Intent(context, activityClass);
		notificationIntent.putExtras(extras);

		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder notifBuilder = getNotificationBuilder(
				context, alertMsg, intent);
		Notification notification = notifBuilder.build();
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify((int) when, notification);
	}

	private NotificationCompat.Builder getNotificationBuilder(Context context,
			String alertsMessage, PendingIntent intent) {
		long when = System.currentTimeMillis();
		int icon = R.drawable.ic_launcher;
		String title = getApplicationContext().getString(R.string.app_name);

		NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(
				context);
		notifBuilder.setSmallIcon(icon).setWhen(when).setContentTitle(title)
				.setContentText(alertsMessage).setContentIntent(intent);

		return notifBuilder;
	}
}
