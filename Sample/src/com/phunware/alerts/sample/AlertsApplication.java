package com.phunware.alerts.sample;

import android.app.Application;

import com.phunware.alerts.PwAlertsModule;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

public class AlertsApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		String appId = "8";
		String accessKey = "71014513f265c91a2d4ad0b083e09b0db561741d"; 
		String signatureKey = "ffa238529d95875bee142addbc34a0f28b059ee2";
		String encryptionKey = "zxcvbnmasdfghjklqwertyuiop123456";
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, appId, accessKey, signatureKey, encryptionKey);
	}
}