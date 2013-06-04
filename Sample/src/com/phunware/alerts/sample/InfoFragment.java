package com.phunware.alerts.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phunware.core.CoreModule;
import com.phunware.core.PwCoreModule;
import com.phunware.core.PwCoreSession;

public class InfoFragment extends Fragment {

	public static final String TAG = "InfoFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);

		setHasOptionsMenu(true);

//		Button test = (Button) view.findViewById(R.id.button1);
//		test.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				PwAlertsRegister.sendPositiveClick(InfoFragment.this.getActivity(), "111");
//			}
//		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v(TAG, "onStart");
		refreshInfo();
	}
	
	protected static final String PREFS_ALERTS_REGISTER = "com.phunware.alerts.register";
	private static final String PREFS_ALERTS_REGISTER_GCM_TOKEN = "com.phunware.alerts.register.gcm_token";
	
	public void refreshInfo() {
		String accessKey = PwCoreSession.getInstance().getAccessKey();
		((TextView) getView().findViewById(R.id.access_key_value))
				.setText(accessKey);

		String deviceId = PwCoreSession.getInstance().getSessionData()
				.getDeviceId();
		((TextView) getView().findViewById(R.id.device_id_value))
				.setText(deviceId);
		
		String sessionId = PwCoreSession.getInstance().getSessionId(getActivity());
		((TextView)getView().findViewById(R.id.session_id_value)).setText(sessionId);
		
		String token = deviceGCMToken();
		((TextView)getView().findViewById(R.id.device_token_value)).setText(token);
		
		String str = buildEnvString();
		((TextView)getView().findViewById(R.id.env_value)).setText(str);
		
		String appId = PwCoreSession.getInstance().getApplicationId();
		((TextView)getView().findViewById(R.id.appid_value)).setText(appId);
	
		String sigKey = PwCoreSession.getInstance().getSignatureKey();
		((TextView)getView().findViewById(R.id.sig_value)).setText(sigKey);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		menu.getItem(Utils.MENU_INDEX_CLEAR).setVisible(false);
		menu.getItem(Utils.MENU_INDEX_UPDATE).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.menu_refresh) {
			refreshInfo();
			return true;
		}

		else if (id == R.id.menu_email) {
			String body =
					"Application Id:\n"
					+ PwCoreSession.getInstance().getApplicationId()
					+ "\n\nEnvironment\n"
					+ buildEnvString()
					+ "\n\nAccess Key:\n"
					+ PwCoreSession.getInstance().getAccessKey()
					+ "\n\nDevice Id:\n"
					+ PwCoreSession.getInstance().getSessionData()
							.getDeviceId()
					+ "\n\nDevice GCM Token\n"
					+ deviceGCMToken();
			Utils.email(getActivity(), body);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static String buildEnvString()
	{
		CoreModule[] arr = PwCoreModule.getInstance().getCoreModuleManager().getInstalledModuleObjectArray();
		String str = "";
		for(CoreModule cm : arr)
		{
			if(!str.equals(""))
				str += "\n";
			str += cm;
		}
		return str;
	}
	
	public String deviceGCMToken()
	{
		SharedPreferences sp = com.phunware.core.internal.Utils
				.getSharedPreferences(getActivity(), PREFS_ALERTS_REGISTER);
		return sp.getString(PREFS_ALERTS_REGISTER_GCM_TOKEN, null);
	}
}
