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
		//String accessKey = "cbb0edae9b2c0fdf99f7b395c5d8f5e2";
		String signatureKey = "zxcvbnmasdfghasdfasdfasdf";
		String encryptionKey = "zxcvbnmasdfghjklqwertyuiop123456";
		PwLog.setShowLog(true);
		
		PwCoreSession.installModules(PwAlertsModule.class);
		
		PwCoreSession.getInstance().registerKeys(getApplicationContext(), accessKey, signatureKey, encryptionKey);
		
		PwLog.setShowLog(true);
	}
}