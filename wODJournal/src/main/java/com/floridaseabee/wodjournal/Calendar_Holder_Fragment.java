package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class Calendar_Holder_Fragment extends Fragment implements
		DatabaseHelper.WOD_Main_Listener {

	private static final String KEY_DATE = "date";
	private String current_date;
	private WOD_Day_DB_Listener listener;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		current_date = getArguments().getString(KEY_DATE, null);
		Log.v("CalendarHolder_Fragment", "Current date " + current_date);
		FindWODsinDayAsync();
	}

	public WOD_Day_DB_Listener getListener() {
		return listener;
	}

	public void setWOD_Day_DB_Listener(WOD_Day_DB_Listener listener) {
		this.listener = listener;
	}

	protected static Calendar_Holder_Fragment newInstance(String current_date) {
		Calendar_Holder_Fragment f = new Calendar_Holder_Fragment();

		Bundle args = new Bundle();
		Log.v("HolderFragmentnewInstan", current_date);
		args.putString(KEY_DATE, current_date);
		f.setArguments(args);
		return (f);
	}

	public void returnNoWorkoutsforDayFound() {
		Log.v("Calendar Holder fragmen",
				"noworkouts found in db helper, now in fragment about to pass to activity listener");
		listener.NoWorkoutsForDayFound();

	}

	public void FindWODsinDayAsync() {
		DatabaseHelper.getInstance(getActivity()).getWODsinDayAsync(
				current_date, this);
	}

	@Override
	public void pulledWODsforDay(Date_Holder result) {
		listener.workoutsforDay(result);

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

	public void deletechild(Date_Holder date_holder, Integer groupPosition,
			Integer childPosition) {
		DatabaseHelper.getInstance(getActivity()).DeleteChildAsync(date_holder,
				groupPosition, childPosition, this);

	}

	public void deleteparent(Date_Holder date_holder, Integer groupPosition) {
		DatabaseHelper.getInstance(getActivity()).DeleteParentAsync(
				date_holder, groupPosition, this);

	}

	@Override
	public void success_add_edit_delete() {
		listener.requery_workout_adapter();

	}

	public void editchild(Movement_Container_Holder movement_container_holder) {
		DatabaseHelper.getInstance(getActivity()).EditChildAsync(
				movement_container_holder, this);

	}

	public void copychild(Date_Holder date_holder, Integer groupPosition, Integer childPosition) {
		DatabaseHelper.getInstance(getActivity()).CopyChildAsync(date_holder,
				groupPosition, childPosition, this);
	}

	public void editparent(WOD_Container_Holder WOD_container_edit) {
		DatabaseHelper.getInstance(getActivity()).EditParentAsync(
				WOD_container_edit, this);

	}

	public void addparent(Date_Holder date_holder, WOD_Container_Holder WOD_container_add,
			Integer groupPosition, String current_date) {
		DatabaseHelper.getInstance(getActivity()).AddInsertParentAsync(date_holder, 
				WOD_container_add, groupPosition, this, "Add", current_date);
	}

	public void insertparent(Date_Holder date_holder, WOD_Container_Holder WOD_container_insert,
			Integer groupPosition, String current_date) {
		DatabaseHelper.getInstance(getActivity()).AddInsertParentAsync(date_holder, 
				WOD_container_insert, groupPosition, this, "Insert", current_date);
	}

	public void addmovement(Date_Holder date_holder,
			Movement_Container_Holder movement_container_edit,
			Integer groupPosition, Integer childPosition, String current_date,
			String type) {
		DatabaseHelper.getInstance(getActivity()).AddInsertChildAsync(date_holder, movement_container_edit, groupPosition, childPosition, this, current_date, type);
		
	}

	public void insertparent(Date_Holder date_holder,
			Movement_Container_Holder movement_container_edit,
			Integer groupPosition, Integer childPosition, String current_date,
			String type) {
		DatabaseHelper.getInstance(getActivity()).AddInsertChildAsync(date_holder, movement_container_edit, groupPosition, childPosition, this,  current_date, type);
		
	}

}
