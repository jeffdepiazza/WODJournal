package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Workout_Search_Holder_Fragment extends Fragment implements
		DatabaseHelper.Workout_Search_Listener {
	private String Search_String = "";
	private String Search_Type = "";
	private Boolean Search_Started = false;
	private final static String search_string = "SEARCH_STRING";
	private final static String search_type = "SEARCH_TYPE";
	private ArrayList<Search_Results_Holder> srh;
	private Workout_Search_Activity_Listener listener;

	protected static Workout_Search_Holder_Fragment newInstance(
			String Search_String, String Search_Type, Boolean Search_Started) {
		Workout_Search_Holder_Fragment f = new Workout_Search_Holder_Fragment();

		Bundle args = new Bundle();

		args.putString(search_string, Search_String);
		args.putString(search_type, Search_Type);
		f.setArguments(args);
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
		Log.v("workout search holder fragment", "onCreate");
		Search_String = getArguments().getString(search_string, null);
		Search_Type = getArguments().getString(search_type, null);
		if (Search_Started == false)
			Log.v("workout search holder fragment",
					"false search started, going to sendSearch()");
		sendSearch();
		Search_Started = true;
	}

	public Workout_Search_Activity_Listener getListener() {
		return listener;
	}

	public void setListener(Workout_Search_Activity_Listener listener) {
		this.listener = listener;
	}

	public void sendSearch() {
		DatabaseHelper.getInstance(getActivity()).getSearchResultsAsync(
				Search_String, Search_Type, this);

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
	public void return_no_results_found() {
		listener.NothingFound();
	}

	@Override
	public void return_search_results(ArrayList<Search_Results_Holder> results) {
		this.srh = results;
		Log.v("workout search holder fragment",
				"results found in fragment, handing off to activity with count =" + srh.size());
		listener.results_found(results);

	}

	public ArrayList<Search_Results_Holder> get_srh(){
		Log.v("workout search holder fragment",
				"giving back srh with count =" + srh.size());
		return srh;
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);

	}

}
