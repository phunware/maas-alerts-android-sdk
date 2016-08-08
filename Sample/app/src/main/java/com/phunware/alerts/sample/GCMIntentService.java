package com.phunware.alerts.sample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.phunware.alerts.PwAlertsIntentService;
import com.phunware.alerts.models.PwAlertExtras;
import com.phunware.core.exceptions.PwException;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * MaaS Alerts uses Google Cloud Messaging (GCM) and handle data delivery to
 * clients app. Use this class to receive responses from GCM. As mentioned part
 * of GCM integration, by default this class must be named .GCMIntentService.
 * 
 */
public class GCMIntentService extends PwAlertsIntentService {
    private static final String TAG = "GCMIntentService";

    @Override
    public void onDelete(Context context, Intent intent) {

    }

    @Override
    public void onError(Context context, Intent intent) {

    }

    @Override
    public void onMessage(Context context, PwAlertExtras extras) {
        Log.v(TAG, "in onMessageAlerts");
        Log.i(TAG, "Extras = " + extras.toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable("alertExtras", extras);
//		bundle.putString(Utils.INTENT_ALERT_EXTRA, extras.toString());
//		bundle.putString(Utils.INTENT_ALERT_EXTRA_PID, extras.getDataPID());
        JSONObject data = null;
        try {
            data = getExtraData(context, extras);
            try {
                bundle.putString(Utils.INTENT_ALERT_DATA, data.toString(2));
            } catch (JSONException e) {
                bundle.putString(Utils.INTENT_ALERT_DATA, data.toString());
            }
        } catch (PwException e) {
            Log.w(TAG, "Could not get alert extras");
            e.printStackTrace();
        }
        generateNotification(context, extras.getAlertMessage(), bundle,
                AlertsSample.class);
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

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Received Alert!")
                .setContentText(alertMsg)
                .setTicker(alertMsg)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[]{500, 500, 500, 500})
                .setLights(Color.BLUE, 500, 500)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        //make sure the id is unique so it won't be overwritten
        notificationManager.notify((int) when, notification);
    }


    public void onRegistered(boolean isSuccessful, String errMessage) {
        Log.v(TAG, "in onRegistered: success " + isSuccessful);
        if (!isSuccessful)
            Log.e(TAG, errMessage == null ? "" : errMessage);
        sendCommonLocalBroadcast(isSuccessful, errMessage, Utils.ACTION_ON_REGISTERED);
    }



    public void onUnregistered(boolean isSuccessful, String errMessage) {
        Log.v(TAG, "in onUnregistered: success " + isSuccessful);
        if (!isSuccessful)
            Log.e(TAG, errMessage == null ? "" : errMessage);
        sendCommonLocalBroadcast(isSuccessful, errMessage, Utils.ACTION_ON_UNREGISTERED);
    }

    private void sendCommonLocalBroadcast(boolean isSuccessful, String errMessage, String action) {
        Intent i = new Intent(action);
        i.putExtra(Utils.BROADCAST_SUCCESSFUL_KEY, isSuccessful);
        i.putExtra(Utils.BROADCAST_MESSAGE_KEY, errMessage);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
