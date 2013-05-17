package com.phunware.alerts.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.phunware.alerts.PwAlertsRegister;
import com.phunware.core.PwCoreSession;

public class InfoFragment extends Fragment {

	public static final String TAG = "InfoFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);

		setHasOptionsMenu(true);

		Button test = (Button) view.findViewById(R.id.button1);
		test.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PwAlertsRegister.sendPositiveClick(InfoFragment.this.getActivity(), "111");
			}
		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		refreshInfo();
	}

	public void refreshInfo() {
		String accessKey = PwCoreSession.getInstance().getAccessKey();
		((TextView) getView().findViewById(R.id.app_id_value))
				.setText(accessKey);

		String deviceId = PwCoreSession.getInstance().getSessionData()
				.getDeviceId();
		((TextView) getView().findViewById(R.id.device_id_value))
				.setText(deviceId);
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
			String body = "MaaS Application Key:\n"
					+ PwCoreSession.getInstance().getAccessKey()
					+ "\n\nDevice Id:\n"
					+ PwCoreSession.getInstance().getSessionData()
							.getDeviceId();
			Utils.email(getActivity(), body);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
