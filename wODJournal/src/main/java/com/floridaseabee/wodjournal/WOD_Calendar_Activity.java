package com.floridaseabee.wodjournal;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class WOD_Calendar_Activity extends FragmentActivity {

	private WODCalendarFragment calendarfragment;
	private DetailsFragment details;
	public static int absolutewindowheight;
	public static int absolutewindowwidth;
	public Boolean newlycreated = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wod_calendar_main);

		calendarfragment = new WODCalendarFragment();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		calendarfragment.pass_screen_height(metrics.heightPixels);
		// calendarfragment = (WODCalendarFragment) getSupportFragmentManager()
		// .findFragmentById(R.id.calendar);
		Log.v("onCreate", " Wod Calender Activity ");
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Log.v("WOD Calender Activity", "onChangeMonth");
				Log.v("WOD Calender Activity", text);
				// the month displayed changed, so we need to requery everything
				// and get any specific views
				// for the Calendar and display those days with WODs associated.
				calendarfragment.startupWODCalendarAsync();
			}

			@Override
			public void onLongClickDate(Date date, View view) {

			}

			// executed after the Fragment OnCreateView sends this even to the
			// listener. squeezing in our Async Task
			// would've required way too much duplication of items already in
			// the Calroid Fragment, so we just utilized
			// the listener to launch the async task to grab all the WOD dates
			// for the current month/year to show those
			// dates as having workouts associated.
			@Override
			public void onCaldroidViewCreated() {
				calendarfragment.startupWODCalendarAsync();

			}

			@Override
			public void onSelectDate(int year, int month, int day, View view) {

				// we need to see if details is currently visible, as we will
				// need to add the fragment in, rather than launch a new
				// activity.
				if (details == null) {
					// We have a selected date, and now we need to build the
					// intent to pass to the Calendar Details.
					// As it has no idea what date to grab until we tell it too.
					Intent Launch_details = new Intent(getApplicationContext(), WOD_Calendar_Details.class);
					Launch_details.putExtra("YEAR", year);
					Launch_details.putExtra("MONTH", month);
					Launch_details.putExtra("DAY", day);
					startActivity(Launch_details);

				}

			}

		};

		calendarfragment.setCaldroidListener(listener);

		if (savedInstanceState != null) {
			Log.v("Calendar Activity", "Reinstating from Saved State");
			// Log.v("Calendar Activity", "calendarfragement =" +
			// calendarfragment);
			newlycreated = savedInstanceState.getBoolean("newlycreated", false);
			calendarfragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
		}

		// If activity is created from fresh
		else {
			Log.v("Calendar Activity", "New instance of calendar");

			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			// Log.v("current height in pixels", "" + metrics.heightPixels);
			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday
			calendarfragment.setArguments(args);
		}
		getSupportFragmentManager().beginTransaction().add(R.id.calendar, calendarfragment).commit();

		details = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details);
		if (details == null && findViewById(R.id.details) != null) {

			details = new DetailsFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.calendar, details).commit();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (calendarfragment != null) {
			calendarfragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
			outState.putBoolean("newlycreated", false);
		}

	}

	@Override
	protected void onResume() {
		if (newlycreated == false) {
			calendarfragment.startupWODCalendarAsync();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// boolean value to set whether or not the calandar is
		// being created from scratch. We care as we will execute the
		// onResume of this activity once we return from the WOD display
		// activity and may need to requery the database to ensure that
		// the calander shows the latest data (did we delete all exercises
		// in a date, or did we add a new one??). We also don't want
		// to run this async twice, as the android lifecycle will do the
		// onCreate/onResume and may make us do the async twice as we
		// are running it again once we hit onResume when we come
		// back to this activity.

		newlycreated = false;

		super.onPause();
	}

}
