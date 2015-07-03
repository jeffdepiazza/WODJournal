package com.floridaseabee.wodjournal;

import hirondelle.date4j.DateTime;

import java.util.Date;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Build;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CalendarHelper;

public class WODCalendarFragment extends CaldroidFragment implements
		DatabaseHelper.WOD_DB_Listener {

	/**
	 * Set backgroundForDateMap
	 */
	@Override
	public void setBackgroundResourceForDates(
			HashMap<Date, Integer> backgroundForDateMap) {
		// Clear first
		backgroundForDateTimeMap.clear();

		if (backgroundForDateMap == null || backgroundForDateMap.size() == 0) {
			return;
		}

		for (Date date : backgroundForDateMap.keySet()) {
			Integer resource = backgroundForDateMap.get(date);
			DateTime dateTime = CalendarHelper.convertDateToDateTime(date);
			backgroundForDateTimeMap.put(dateTime, resource);
		}
	}

	
	/** taken out of the EMPUB lite as it allows the ability to execute in parallel for different API versions
	 * .  while we are not going on anything else but Jellybean, i left this in there as it should help out with keeping
	 *  with keeping parallel tasks in addition 
	 *  
	 * @param task
	 * @param params
	 */
	static public <T> void executeAsyncTask(AsyncTask<T, ?, ?> task,
			T... params) {
		if (Build.VERSION.SDK_INT >+ Build.VERSION_CODES.HONEYCOMB ) {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		
		} else {
			task.execute(params);
		}
	}

	//this is called from the database helper as part of the interface...we have to call the setBackgroundResourceForDates, but
	// we need to followup with a refreshView call to make sure the dates get handled.
	public void CALLsetBackgroundResourceForDates(
			HashMap<Date, Integer> backgroundForDateMap) {
		setBackgroundResourceForDates(backgroundForDateMap);
		refreshView();
		
	}
	
	// called from the activity after it gets notification that the fragment has finished loading caldroid items
	// this is the only way to let the fragment know that it needs to start loading up as I didn't want to override the
	// onCreateView as i would've had to duplicate a TON Of of other stuff for no damn reason. So this is the next best thing...
	//  Caldroid fragment lets the activity know that its done setting everything up, and then the activity tells the fragment
	// to start the Async task of loading up the Hashmap required to setup the individual textviews that let the user know 
	// if a date has a workout associated with it.
 public void startupWODCalendarAsync() {
		DatabaseHelper.getInstance(getActivity()).getWODAsync(year, month, this);
	}

}
