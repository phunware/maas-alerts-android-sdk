package com.phunware.alerts.sample;

import android.app.Application;

import com.phunware.alerts.PwAlertsModule;
import com.phunware.core.PwCoreSession;

public class AlertsApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
//		STAGE -- QA Test App
		String appId = "8";
		String accessKey = "71014513f265c91a2d4ad0b083e09b0db561741d"; 
		String signatureKey = "ffa238529d95875bee142addbc34a0f28b059ee2";
		
//		DEV -- Test App
//		String appId = "8";
//		String accessKey = "fa0757223f9eb4b66a0f1959a5d0801869c7465e"; 
//		String signatureKey = "784c57caa5b0abd2255444389a49be00ac8316e7";
		
		String encryptionKey = "zxcvbnmasdfghjklqwertyuiop123456";
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, appId, accessKey, signatureKey, encryptionKey);
	}
}