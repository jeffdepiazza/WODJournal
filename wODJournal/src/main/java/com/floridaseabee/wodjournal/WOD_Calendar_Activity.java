package com.floridaseabee.wodjournal;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;

public class WOD_Calendar_Activity extends FragmentActivity implements Calendar_Delete_Dialog.Calendar_Delete_dialog_Listener{

	private WODCalendarFragment calendarfragment;
	private DetailsFragment details;
	public static int absolutewindowheight;
	public static int absolutewindowwidth;
	private int selected_day =0;
	private int selected_month=0;
	private int selected_year=0;
	public Boolean newlycreated = true;
	private Calendar_Delete_Dialog calendar_delete_dialog = null;
	private Boolean dialog_displayed;
    private int status_bar_height = 0;
    private int resourceId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.wod_calendar_main);

		calendarfragment = new WODCalendarFragment();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

        TypedValue tv = new TypedValue();

        //try to get the status bar height.
        resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            status_bar_height = getResources().getDimensionPixelSize(resourceId);
        }

        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {


            Log.v("calender activity", "has an actionbar");
            calendarfragment.pass_screen_height(metrics.heightPixels - status_bar_height - TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics()));
        } else {
            Log.v("calender activity", "No actionbar");
            calendarfragment.pass_screen_height(metrics.heightPixels - status_bar_height);
        }
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
			public void onLongClickDate(int year, int month, int day, View view) {

				Log.v("calender activity", " on long click date");
				selected_day = day;
				selected_month = month;
				selected_year = year;
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				calendar_delete_dialog = Calendar_Delete_Dialog.newInstance(selected_year, selected_month, selected_day);
				calendar_delete_dialog.setListener(WOD_Calendar_Activity.this);
				calendar_delete_dialog.show(ft, "dialog");
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
            selected_day = savedInstanceState.getInt("SELECTED_DAY", 0);
            selected_month = savedInstanceState.getInt("SELECTED_MONTH", 0);
            selected_year = savedInstanceState.getInt("SELECTED_YEAR", 0);
			calendarfragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
            dialog_displayed = savedInstanceState.getBoolean("DIALOG_DISPLAYED", false);
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
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, true);
            dialog_displayed = false;
			calendarfragment.setArguments(args);
		}
		getSupportFragmentManager().beginTransaction().add(R.id.calendar, calendarfragment).commit();

		details = (DetailsFragment) getSupportFragmentManager().findFragmentById(R.id.details);
		if (details == null && findViewById(R.id.details) != null) {

			details = new DetailsFragment();
			getSupportFragmentManager().beginTransaction().add(R.id.calendar, details).commit();
		}

        if (dialog_displayed == true) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            calendar_delete_dialog = Calendar_Delete_Dialog.newInstance(selected_year, selected_month, selected_day);
            calendar_delete_dialog.setListener(this);
            calendar_delete_dialog.show(ft, "dialog");
        }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (calendarfragment != null) {
			calendarfragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
			outState.putBoolean("newlycreated", false);
            outState.putInt("SELECTED_DAY", selected_day);
            outState.putInt("SELECTED MONTH", selected_month);
            outState.putInt("SELECTED YEAR", selected_year);

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

    @Override
    public void head_back_to_activity() {
        //  we are assuming that if we are here, that the dialog came back as a positive/yes to
        // delete the date, so we are going to start the async task and head on down that road.
		Log.v("got positive response", "heading to async task");
        calendarfragment.delete_selected_date(selected_year, selected_month, selected_day);
    }
}
