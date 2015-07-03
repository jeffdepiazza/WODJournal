package com.floridaseabee.wodjournal;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class Personal_Records_Activity extends Activity {

	public Personal_Records_Display_Fragment pr_display_fragment;
	public Personal_Records_Holder_Fragment pr_holder_fragment;
	private Boolean AlreadyRunning = false;
	private final String current_status = "CURRENT_STATUS";
	private String Current_Status = "Running";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		if (getFragmentManager().findFragmentById(android.R.id.content) == null) {

			pr_display_fragment = Personal_Records_Display_Fragment.newInstance();
			getFragmentManager().beginTransaction().add(android.R.id.content, pr_display_fragment, "pr_display")
					.commit();
			pr_holder_fragment = Personal_Records_Holder_Fragment.newInstance();
			getFragmentManager().beginTransaction().add(android.R.id.content, pr_holder_fragment, "pr_search").commit();

		} else {
			Log.v("activity", "non-null holder");
			pr_display_fragment = (Personal_Records_Display_Fragment) getFragmentManager().findFragmentByTag(
					"pr_display");
			pr_holder_fragment = (Personal_Records_Holder_Fragment) getFragmentManager().findFragmentByTag("pr_search");
		}

		// our link into the DB holder (non display fragment) that will return
		// the results to the activity.

		final Personal_Record_DB_Listener DB_Listener = new Personal_Record_DB_Listener() {
			public void NoPRsFound() {
				Current_Status = "No Data";
				pr_display_fragment.no_data();
			}

			public void PRsdatafound(ArrayList<Personal_Record_Holder> result) {
				Current_Status = "Has Data";
				pr_display_fragment.is_data();
				pr_display_fragment.handover_data(result);
			}

			public void requery_PR_adapter() {

			}

		};
		// setting the listener for the above.
		if (pr_holder_fragment == null)
			Log.v("Personal Records Activity", "pr holder fragment= null");
		pr_holder_fragment.setListener(DB_Listener);
		if (savedInstanceState != null) {
			Current_Status = savedInstanceState.getString(current_status, "");
		}
	}

	@Override
	public void onResume() {

		// we are setting this status as the actual search results holder
		// is being held in the holder fragment. We want to save this and
		// understand
		// where we are at in the search cycle...and reset the display fragment
		// appropriately.
		// if we dont do this, we get a spinning display and no results as the
		// display
		// fragment has been reset to a default state upon rotation and we need
		// to reset things.
		// if this is the initial creation of the activity, no harm done as none
		// of these statements
		// yield anything. We HAVE to wait for here as the display fragment has
		// NOT been drawn yet.
		// executing the below code will give us a NULL POINTER EXCEPTION as the
		// fragment has not
		// executed its OnCreateView yet.

		if (Current_Status == "No Data") {
			pr_display_fragment.no_data();
		} else if (Current_Status == "Has Data") {
			pr_display_fragment.is_data();
			pr_display_fragment.handover_data(pr_holder_fragment.get_prh());

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
