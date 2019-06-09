package com.floridaseabee.wodjournal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Workout_Search_Display_Activity extends Activity {
	private final String search_string = "SEARCH_STRING";
	private final String search_type = "SEARCH_TYPE";
	private final String current_status = "CURRENT_STATUS";
	private String Current_Status = "Running";
	private String Search_String = "";
	private String Search_Type = "";
	private Workout_Search_Holder_Fragment SearchHolder;
	private Workout_Search_Display_Fragment search_display_fragment;
	private Boolean AlreadyRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Search_String = getIntent().getStringExtra(search_string);
		Search_Type = getIntent().getStringExtra(search_type);

		if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
			SearchHolder = Workout_Search_Holder_Fragment.newInstance(
					Search_String, Search_Type, false);
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, SearchHolder, "searchholder")
					.commit();
			search_display_fragment = Workout_Search_Display_Fragment
					.newInstance();
			getFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, search_display_fragment,
							"displayholder").commit();
		} else {
			Log.v("activity", "non-null holder");
		SearchHolder = (Workout_Search_Holder_Fragment) getFragmentManager()
				.findFragmentByTag("searchholder");
		search_display_fragment = (Workout_Search_Display_Fragment) getFragmentManager()
				.findFragmentByTag("displayholder");
		}
		
		final Workout_Search_Activity_Listener WSAL = new Workout_Search_Activity_Listener() {
			public void NothingFound() {
				Log.v("workout search display activity", "No results but passing to display fragment");
				Current_Status = "No Data";
				search_display_fragment.no_data();

			}

			public void results_found(ArrayList<Search_Results_Holder> results) {
				Log.v("workout search display activity", "got my results and passing to display fragment");
				Current_Status = "Has Data";
				search_display_fragment.is_data();
				search_display_fragment.handover_data(results);

			}

		};
		Log.v("workout search display activity",
				"search holder setting listener");
		SearchHolder.setListener(WSAL);
		if (savedInstanceState != null) {
			Current_Status = savedInstanceState.getString(current_status, "");
 		}
			
	}

	@Override
	public void onResume() {
	
		// we are setting this status as the actual search results holder
		// is being held in the holder fragment.  We want to save this and understand
		// where we are at in the search cycle...and reset the display fragment appropriately.
		// if we dont do this, we get a spinning display and no results as the display
		// fragment has been reset to a default state upon rotation and we need to reset things.
		// if this is the initial creation of the activity, no harm done as none of these statements
		// yield anything.  We HAVE to wait for here as the display fragment has NOT been drawn yet.
		// executing the below code will give us a NULL POINTER EXCEPTION as the fragment has not
		// executed its OnCreateView yet.
		
		if (Current_Status == "No Data") {
			search_display_fragment.no_data();
		} else if (Current_Status == "Has Data") {
			search_display_fragment.is_data();
			search_display_fragment.handover_data(SearchHolder.get_srh());
			
		}
		super.onResume();
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putBoolean("AlreadyRunning", AlreadyRunning);
		state.putString(current_status, Current_Status);

	}
	
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		AlreadyRunning = state.getBoolean("AlreadyRunning", AlreadyRunning);
		Current_Status = state.getString(current_status, Current_Status);
	}
}
