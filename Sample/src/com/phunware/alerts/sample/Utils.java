package com.phunware.alerts.sample;

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
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MaaS Alerts Console "+com.phunware.core.internal.Utils.getCurrentTimeFormatted());
		packageContext.startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}
}
