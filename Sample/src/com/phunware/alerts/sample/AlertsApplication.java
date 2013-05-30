package com.phunware.alerts.sample;

import android.app.Application;

import com.phunware.alerts.PwAlertsModule;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

public class AlertsApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		String accessKey = "testappid"; 
		String signatureKey = "zxcvbnmasdfghasdfasdfasdf";
		String encryptionKey = "zxcvbnmasdfghjklqwertyuiop123456";
		PwLog.setShowLog(true);
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, accessKey, signatureKey, encryptionKey);
	}
}