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
		String accessKey = "fa0757223f9eb4b66a0f1959a5d0801869c7465e"; 
		String signatureKey = "784c57caa5b0abd2255444389a49be00ac8316e7";
		String encryptionKey = "zxcvbnmasdfghjklqwertyuiop123456";
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, appId, accessKey, signatureKey, encryptionKey);
	}
}