package com.phunware.alerts.sample;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.phunware.alerts.sample.db.AlertModel;
import com.phunware.alerts.sample.db.Contracts;

public class PastAlertsFragment extends ListFragment {
	
	public static final String TAG = "PastAlertsFragment";
	
	private PastAlertsFragmentListener mActivityLink;
	private PastAlertsFragmentAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		if(!(activity instanceof PastAlertsFragmentListener))
			throw new RuntimeException("PastAlertsFragment's parent activity must implement PastAlertsFragmentListener");
		mActivityLink = (PastAlertsFragmentListener)activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setEmptyText(getResources().getString(R.string.empty_past_alerts_list));
		registerForContextMenu(getListView());
		mActivityLink.loadAllAlerts();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		getActivity().openContextMenu(v);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.alerts_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        int id = item.getItemId();
        if(id == R.id.delete)
        {
        	Cursor c = (Cursor) getListView().getItemAtPosition(position);
        	mActivityLink.deleteAlert(c.getLong(Contracts.AlertEntry.I_ID));
        	mActivityLink.loadAllAlerts();
        }
		return super.onContextItemSelected(item);
	}
	
	/**
     * Given a {@link Cursor}, set the data in the list
     *
     * @param cursor
     */
    public void doSetData(Cursor cursor) {
        Log.v(TAG, "onCursorLoaded: "+cursor.getCount());
        if (mAdapter == null) {
            mAdapter = new PastAlertsFragmentAdapter(getActivity(), cursor);
            setListAdapter(mAdapter);
        } else //list adapter already set
        {
            //Log.v(TAG, "changing cursor");
        	mAdapter.changeCursor(cursor);
        }
//        hideLoading();
    }
    
    /**
     * Return the number of items in this list view
     *
     * @return number of items in this list view. -1 is returned if there is no list adapter set.
     */
    public int getListCount() {
        ListAdapter adapter = getListAdapter();
        if (adapter == null)
            return -1;
        return adapter.getCount();
    }

	public interface PastAlertsFragmentListener {
		/**
		 * This is called when all alerts should be loaded. Once loaded
		 * {@link PastAlertsFragment#doSetData(Cursor)} should be called.
		 */
		public void loadAllAlerts();
		/**
		 * Called when an alert should be deleted from the DB.
		 * @param alertId the id to delete
		 */
		public void deleteAlert(long alertId);
	}
	
	public class PastAlertsFragmentAdapter extends CursorAdapter {
		private final LayoutInflater mInflater;

		public PastAlertsFragmentAdapter(Context context, Cursor c) {
			super(context, c, 0);
			mInflater = LayoutInflater.from(context);
			mContext = context;
		}

		private class ViewHolder {
			TextView timestamp;
			TextView pid;
			TextView message;
			TextView targetGroup;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {

			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder();
				holder.timestamp = (TextView) view.findViewById(R.id.timestamp);
				holder.pid = (TextView) view.findViewById(R.id.pid_value);
				holder.message = (TextView) view.findViewById(R.id.message_value);
				holder.targetGroup = (TextView) view.findViewById(R.id.target_group_value);
				view.setTag(holder);
			}

			AlertModel alert = new AlertModel(cursor);

			holder.timestamp.setText(Utils.convertLongToString(alert.timestamp));
			holder.pid.setText(String.valueOf(alert.pid));
			holder.message.setText(alert.message);
			holder.targetGroup.setText(alert.targetGroup);
			alert = null;
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final View view = mInflater.inflate(R.layout.alert_row, parent, false);
			return view;
		}
	}
}
