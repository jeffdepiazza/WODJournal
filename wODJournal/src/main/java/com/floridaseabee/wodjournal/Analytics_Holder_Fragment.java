package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Analytics_Holder_Fragment extends Fragment implements DatabaseHelper.Rep_Max_Item_Listener{
	private Analytics_DB_Listener listener;
	private ArrayList<String> rep_max_items = null;
	private ArrayList<Analytics_Data_Holder> analytics_data = null; 
	
	protected static Analytics_Holder_Fragment newInstance() {
		Analytics_Holder_Fragment f = new Analytics_Holder_Fragment();
		Bundle args = new Bundle();
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	
		get_rep_max_items();
		
	}

	public ArrayList<Analytics_Data_Holder> getGraphData(){
		return analytics_data;
	}
	
	public Analytics_DB_Listener getListener() {
		return listener;
	}

	public void setListener(Analytics_DB_Listener listener) {
		this.listener = listener;
	}	
	
	private void get_rep_max_items(){
		DatabaseHelper.getInstance(getActivity()).GetRepMaxItems(this);
	}
	
	/**
	 * taken out of the EMPUB lite as it allows the ability to execute in
	 * parallel for different API versions . while we are not going on anything
	 * else but Jellybean, i left this in there as it should help out with
	 * keeping with keeping parallel tasks in addition
	 * 
	 * @param task
	 * @param params
	 */
	static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task,
			T... params) {
		if (Build.VERSION.SDK_INT > +Build.VERSION_CODES.HONEYCOMB) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		} else {
			task.execute(params);
		}
	}

	@Override
	public void return_Rep_Max_Items(ArrayList<String> rep_max_items) {
		if (rep_max_items == null)
			this.rep_max_items = null;
		else{
			this.rep_max_items = rep_max_items;
			listener.return_rep_max_items(rep_max_items);
		}
		Log.v("Analytics Holder Fragment", "got the details and calling back to activity");
	}

	public void get_data(String movement) {
		DatabaseHelper.getInstance(getActivity()).GetAnalyticsData(this, movement);
		
	}

	@Override
	public void return_Analytics_data(ArrayList<Analytics_Data_Holder> analytics_data) {
		if (analytics_data == null)
			this.analytics_data = null;
		else{
			this.analytics_data = analytics_data;
			listener.return_analytics_data_to_activity(analytics_data);
		}
		
	}
}
