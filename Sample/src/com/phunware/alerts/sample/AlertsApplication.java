package com.phunware.alerts.sample;

import android.app.Application;

import com.phunware.alerts.PwAlertsModule;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

public class AlertsApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
//		DEV - Another Game With Zombies In It
		String appId = "28";
		String accessKey = "4f395b9ece015603a936abe74f5916c13e899101"; 
		String signatureKey = "09a2d6b7c6b50a5d1fcaedcac3f08198a6847a58";
		
		PwLog.setShowLog(true);
		
		String encryptionKey = "zxcvbnmasdfghjklqwertyuiop123456";
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, appId, accessKey, signatureKey, encryptionKey);
	}
}