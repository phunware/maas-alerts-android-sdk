package com.phunware.alerts.sample;

import android.app.Application;

import com.phunware.alerts.PwAlertsModule;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

public class AlertsApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
//		STAGE - Jenkins LITE
		String appId = getResources().getString(R.string.app_id);
		String accessKey = getResources().getString(R.string.access_key);
		String signatureKey = getResources().getString(R.string.signature_key);
        String encryptionKey = getResources().getString(R.string.encrypt_key);

		PwLog.setShowLog(true);
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, appId, accessKey, signatureKey, encryptionKey);
	}
}