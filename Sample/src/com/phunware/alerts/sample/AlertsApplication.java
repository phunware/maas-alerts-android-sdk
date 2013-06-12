package com.phunware.alerts.sample;

import android.app.Application;

import com.phunware.alerts.PwAlertsModule;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

public class AlertsApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		String appId = <YOUR_APP_ID>;
		String accessKey = <YOUR_ACCESS_KEY>; 
		String signatureKey = <YOUR_SIGNATURE_KEY>;
		String encryptionKey = "<YOUR_ENCRYPTION_KEY>";
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, appId, accessKey, signatureKey, encryptionKey);
	}
}