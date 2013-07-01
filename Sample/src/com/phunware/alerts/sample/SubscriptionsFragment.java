package com.phunware.alerts.sample;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.phunware.alerts.PwAlertsSubscriptions;
import com.phunware.alerts.models.PwSubscription;
import com.phunware.alerts.sample.ConsoleOutput.ConsoleLogger;
import com.phunware.core.PwLog;
import com.phunware.core.exceptions.NoInternetException;

public class SubscriptionsFragment extends ListFragment {

	public final static String TAG = "SubscriptionsFragment";
	private ArrayList<PwSubscription> mAllSubscriptions;
	private SubscriptionAdapter mAdapter;
	private View mListLayout, mProgressLayout;
	private ConsoleLogger mConsole;

	private static final String SP_SUBSCRIPTION_PREFIX = "!@#";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_subscriptions,
				container, false);

		setHasOptionsMenu(true);

		mListLayout = view.findViewById(R.id.list_layout);
		mProgressLayout = view.findViewById(R.id.progress_layout);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		if (!(activity instanceof ConsoleLogger))
			throw new RuntimeException(
					"SubscriptionsFragment's Activity must implement ConsoleLogger");
		mConsole = (ConsoleLogger) activity;
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// check for saved list
		boolean getSubscriptions = false;
		if (savedInstanceState != null) {
			mAllSubscriptions = savedInstanceState
					.getParcelableArrayList("subs");
			if (mAllSubscriptions == null) {
				getSubscriptions = true;
				mAllSubscriptions = new ArrayList<PwSubscription>();
			} else
				updateSubscriptionListState();
		} else // no saved data
		{
			getSubscriptions = true;
			mAllSubscriptions = new ArrayList<PwSubscription>();
		}

		// set list adapter
		mAdapter = new SubscriptionAdapter();
		setListAdapter(mAdapter);

		// get subscriptions if they haven't been obtained already
		// this is being called after the list has been initialized.
		if (getSubscriptions)
			fetchSubscriptions();
		else {
			mAdapter.notifyDataSetChanged();
			hideLoading();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("subs", mAllSubscriptions);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		menu.getItem(Utils.MENU_INDEX_CLEAR).setVisible(false);
		menu.getItem(Utils.MENU_INDEX_EMAIL).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.menu_refresh) {
			mAllSubscriptions.clear();
			fetchSubscriptions();
			return true;
		}

		else if (id == R.id.menu_update) {
			saveSubscriptions();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// ListView Adapter
	private class SubscriptionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mAllSubscriptions.size();
		}

		@Override
		public PwSubscription getItem(int position) {
			return mAllSubscriptions.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.subscription, parent, false);
				holder = new ViewHolder();
				holder.switchView = (CompoundButton) convertView
						.findViewById(R.id.subscriptionSwitch);
				holder.title = (TextView) convertView
						.findViewById(R.id.subscriptionText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// Update UI
			String name = getItem(position).getName();
			holder.title.setText(name);
			holder.switchView
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// Update the changes made by the user to the
							// subscription list.
							PwLog.d("Phun_Alerts",
									"OnCheckedChanged: Position=" + position
											+ " isChecked=" + isChecked);
							mAllSubscriptions.get(position).setSubscribed(
									isChecked);
						}
					});
			holder.switchView.setChecked(getItem(position).isSubscribed());

			return convertView;
		}

		private final class ViewHolder {
			CompoundButton switchView;
			TextView title;
		}
	}

	/**
	 * Helper method to asynchronously get subscriptions
	 */
	private void fetchSubscriptions() {

		showLoading();

		new AsyncTask<Void, String, String>() {
			@Override
			protected String doInBackground(Void... params) {
				try {
					// Retrieve the subscription list.
					mAllSubscriptions = new ArrayList<PwSubscription>(
							PwAlertsSubscriptions
									.getSubscriptionGroups(getActivity()));
				} catch (IOException e) {
					return "Failed! " + e.getMessage();
				} catch (NoInternetException e) {
					return "Failed! " + e.getMessage();
				}
				return "Success! Found subscription group.";
			}

			@Override
			protected void onPostExecute(String result) {
				PwLog.d(TAG, "Fetch subscription lists. Result is " + result);
				mConsole.printToConsole("Fetch subscription lists. Result is "
						+ result);

				updateSubscriptionListState();
				mAdapter.notifyDataSetChanged();

				hideLoading();
			}
		}.execute();
	}

	/**
	 * Helper method to asynchronously save the subscription data
	 */
	private void saveSubscriptions() {
		showLoading();
		new AsyncTask<Void, String, String>() {
			@Override
			protected String doInBackground(Void... params) {

				try {
					// Save the subscription list. This call will tell the
					// server to wipe out the subscription settings and replace
					// it with the one from the client.
					PwAlertsSubscriptions.saveSubscriptions(getActivity(),
							mAllSubscriptions);

				} catch (IOException e) {
					return "Failed to update subscription list! "
							+ e.getMessage();
				} catch (NoInternetException e) {
					return "No Internet, failed to update subscription list! "
							+ e.getMessage();
				}
				return "Success! Updated subscription list.";
			}

			@Override
			protected void onPostExecute(String result) {
				PwLog.d(TAG, "Update subscription lists. Result is " + result);
				mConsole.printToConsole("Update subscription lists. Result is "
						+ result);
				if (result.contains("Success")) {
					// save settings to shared prefs
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(getActivity());
					SharedPreferences.Editor edit = sp.edit();
					for (PwSubscription s : mAllSubscriptions) {
						edit.putBoolean(SP_SUBSCRIPTION_PREFIX + s.getId(),
								s.isSubscribed());
						Log.v(TAG,
								"saving preference: "
										+ SP_SUBSCRIPTION_PREFIX
										+ s.getId()
										+ " = "
										+ sp.getBoolean(SP_SUBSCRIPTION_PREFIX
												+ s.getId(), false)
										+ " and should be " + s.isSubscribed());
					}
					edit.commit();
				} else {
					Toast.makeText(
							getActivity(),
							"Failed to update subscriptions, please try again later...",
							Toast.LENGTH_SHORT).show();
				}
				hideLoading();
			}

		}.execute();
	}

	private void updateSubscriptionListState() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		for (PwSubscription s : mAllSubscriptions) {
			s.setSubscribed(sp.getBoolean(SP_SUBSCRIPTION_PREFIX + s.getId(),
					false));
			Log.v(TAG, "updating from preference: " + SP_SUBSCRIPTION_PREFIX
					+ s.getId() + " - " + s.isSubscribed());
		}
	}

	/**
	 * Hide the list, show the loading indicator
	 */
	public void showLoading() {
		mListLayout.setVisibility(View.GONE);
		mProgressLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * Show the List, hide the loading indicator
	 */
	public void hideLoading() {
		mProgressLayout.setVisibility(View.GONE);
		mListLayout.setVisibility(View.VISIBLE);
	}
}
