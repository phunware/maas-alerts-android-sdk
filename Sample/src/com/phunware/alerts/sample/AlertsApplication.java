package com.phunware.alerts.sample;

import android.app.Application;

import com.phunware.alerts.PwAlertsModule;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

public class AlertsApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
//		PROD -- Prod Test (Android)
//		String appId = "15";
//		String accessKey = "d04397f4c0b491f7c6d0f6bcf5c339ab58927cc0"; 
//		String signatureKey = "23fc11a456de9a0d9202ca381463baab23283c4c";
		
//		PROD -- ProdTest 06/17/13
//		String appId = "39";
//		String accessKey = "77dd297ccfdee5f944953552776a4d0ac9cf192d"; 
//		String signatureKey = "ba718b3b7295c240711c70c6a42dfddbcc2e1c97";
		
//		STAGE -- QA Test App
		String appId = "22";
		String accessKey = "9bd1f5e5af70ca93e16cf77dd1818662a0ae7fbb"; 
		String signatureKey = "dedff15103549af4780e6bc22314f3de0156f66e";
		
//		DEV -- Test App
//		String appId = "17";
//		String accessKey = "f466f27ba8b5031ecda93ddcb9baf76b5e0cd1f0 "; 
//		String signatureKey = "f37c6da8df6f265802b7faa465dafc297e13572d";
		
		PwLog.setShowLog(true);
		
		String encryptionKey = "zxcvbnmasdfghjklqwertyuiop123456";
		
		PwCoreSession.getInstance().installModules(PwAlertsModule.getInstance());
		
		PwCoreSession.getInstance().registerKeys(this, appId, accessKey, signatureKey, encryptionKey);
	}
}