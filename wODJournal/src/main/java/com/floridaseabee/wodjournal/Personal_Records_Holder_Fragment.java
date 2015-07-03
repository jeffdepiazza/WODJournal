package com.floridaseabee.wodjournal;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

public class Personal_Records_Holder_Fragment extends Fragment implements DatabaseHelper.Personal_Records_Listener {
	private Personal_Record_DB_Listener listener;
	private ArrayList<Personal_Record_Holder> prh; 
	
	public static Personal_Records_Holder_Fragment newInstance() {

		Personal_Records_Holder_Fragment f = new Personal_Records_Holder_Fragment();
		return f;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// We are saving the instance so it not only holds onto the AsyncTask
		// to get our data out of the database, but we are also using it to hold 
		// on to our data member that we are passing to the activity.  This allows us
		// to save it during rotation and then pass it back to the activity during its 
		// onResume to reset the state of the Activity.
		
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		GetPRs();

	}

	public Personal_Record_DB_Listener getListener() {
		return listener;
	}

	public void setListener(Personal_Record_DB_Listener listener) {
		this.listener = listener;
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
	public void return_no_PRs_found() {
		listener.NoPRsFound();
		
	}

	@Override
	public void return_PR_results(ArrayList<Personal_Record_Holder> prh) {
		this.prh = prh;
		listener.PRsdatafound(prh);
		
	}
	
	public void GetPRs() {
		DatabaseHelper.getInstance(getActivity()).GetPRs(this);
	}
	
	public ArrayList<Personal_Record_Holder> get_prh() {
		return prh;
	}
}
