package com.floridaseabee.wodjournal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WOD_Calendar_Details extends FragmentActivity implements
		InsertAddDeleteEditDialogChild.Child_Return_Clicks,
		InsertAddDeleteEditDialogParent.Parent_Return_Clicks,
		Edit_Child_Dialog.Movement_Edit_Listener,
		Edit_Parent_Dialog.WOD_Container_Edit_Listener,
		InsertAdd_Child_Dialog.Movement_Add_Insert_Listener,
		InsertAdd_Parent_Dialog.WOD_Container_Add_Insert_Listener {
	public static final String YEAR = "YEAR";
	public static final String MONTH = "MONTH";
	public static final String DAY = "DAY";
	public static final String ChildPosition = "childPosition";
	public static final String GroupPosition = "groupPosition";
	public Integer Year = 0;
	public Integer Month = 0;
	public Integer Day = 0;
	public String current_date = "";
	public static Calendar_Holder_Fragment CalendarHolder;
	public static WOD_Display_Fragment wod_display_fragment;
	public Integer childPosition = 0;
	public Integer groupPosition = 0;
	public Date_Holder date_holder;
	private TextView current_date_view;
	private String current_status = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wod_details);

		// we passed this Fragment Activity the selected date from the previous
		// fragment activity
		// in order to be able to query the database and bring up anything we
		// may need. Right now this is empty
		// and only serves to prove we can pass the date

		// Right now Details give Listener to Holder Fragment, and Fragment
		// gives cmd to Async to load all WODs for Day
		// Async tells Fragment when done by the "Implements" on the Fragment
		// (really a listener). The Fragment then, through
		// the listener that the Details Activity gave it (as they share the
		// same DB_Day_Listener), sends the data to the
		// activity.
		current_date_view = (TextView) findViewById(R.id.current_date_text);
		current_date_view.setText(current_date);
		Year = getIntent().getIntExtra(YEAR, 1970);
		current_date = Year.toString();
		Month = getIntent().getIntExtra(MONTH, 1);
		if (Month < 10) {
			current_date = current_date + "-0" + Month;
		} else {
			current_date = current_date + "-" + Month;
		}

		Day = getIntent().getIntExtra(DAY, 1);
		if (Day < 10) {
			current_date = current_date + "-0" + Day;
		} else {
			current_date = current_date + "-" + Day;
		}

		Log.v("Wod Calendar Details On Create:", "current date= "
				+ current_date);
		current_date_view = (TextView) findViewById(R.id.current_date_text);
		SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = simpledate.parse(current_date);
			Log.v("date parsed in string", "" + current_date);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		SimpleDateFormat simpledate1 = new SimpleDateFormat("dd MMMM yyyy");
		Log.v("Date passed: ", "" + date);
		current_date_view.setText(simpledate1.format(date));
		// launching the Calendar Holder hidden fragment that will launch and
		// populate our data scheme in preparation to display.

		if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
			CalendarHolder = Calendar_Holder_Fragment.newInstance(current_date);
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, CalendarHolder).commit();
			wod_display_fragment = WOD_Display_Fragment.newInstance();
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, wod_display_fragment).commit();
		}

		final WOD_Day_DB_Listener Daypull_listener = new WOD_Day_DB_Listener() {

			public void NoWorkoutsForDayFound() {
				Log.v("WOD Calendar Details Activity",
						" No records found for this day");
				wod_display_fragment.no_data();
			}

			public void workoutsforDay(Date_Holder result) {

				Log.v("WOD Calendar Details Activity", "Found entries");
				date_holder = result;
				wod_display_fragment.is_data();
				wod_display_fragment.handover_data(result);
				
				// we are going to bring up the add movement screen automatically
				// if we either added/inserted a group (as the group is empty and the 
				// user should want to add an item automatically, or if we added a new
				// movement, as they may want to keep going on adding movements for longer
				// WODS.
				if (current_status == "parent add" ) {
					//groupPosition = groupPosition +2;
					groupPosition = date_holder.get_WOD_Container_size() -1;
					Log.v("activity ", "launching add movement at " + groupPosition);
					return_child_add();
					
				}
				if (current_status == "parent insert" || current_status == "child add") {
					return_child_add();
				}
				current_status = "";
			}

			public void requery_workout_adapter() {
				Log.v("back in activity", "returned and requery adapter");
				CalendarHolder.FindWODsinDayAsync();
			}
		};

		// setting the listener for the Calendar holder to allow callbacks.
		// especially important
		// if we rotated the screen and we have to reset the listener or get a
		// null exception.
		CalendarHolder.setWOD_Day_DB_Listener(Daypull_listener);

		final WOD_Date_edit_delete_add add_edit_delete_listener = new WOD_Date_edit_delete_add() {

			public void call_child_main_dialog(Date_Holder date_holder,
					Integer groupPosition, Integer childPosition) {
				Log.v("details activity",
						" back in activity in click listeners for add/edit about to go to public function");

				call_add_edit_delete_child_dialog(date_holder, groupPosition,
						childPosition);

			}

			public void call_parent_main_dialog(Date_Holder date_holder,
					Integer groupPosition) {

				call_add_edit_delete_group_dialog(date_holder, groupPosition);
			}
		};

		// Here we have to see if savedInstanceState is true, as it would be for
		// a screen rotation. Issue here is that if we have dialogs listed, we
		// will loose the callbacks. So we test to see if each fragment is up,
		// and if so, we will update the listener with the new isntance of the
		// activity. Otherwise we will get a fault as the listener is now set to
		// null as it was nulled out when the activity was killed when the
		// screen rotated.
		if (savedInstanceState != null) {
			Log.v("WOD Calendar Details", "non empty savedinstancestate");
			childPosition = savedInstanceState.getInt(ChildPosition, 0);
			groupPosition = savedInstanceState.getInt(GroupPosition, 0);

			InsertAddDeleteEditDialogParent dialog_parent = (InsertAddDeleteEditDialogParent) getFragmentManager()
					.findFragmentByTag("dialog_parent");
			if (dialog_parent != null) {
				Log.v("activity oncreate", " resetting dialog parent listener");
				dialog_parent.setListener(this);
			}
			InsertAddDeleteEditDialogChild dialog_child = (InsertAddDeleteEditDialogChild) getFragmentManager()
					.findFragmentByTag("dialog_child");
			if (dialog_child != null) {
				Log.v("activity oncreate", " resetting dialog child listener");
				dialog_child.setListener(this);

			}
			InsertAdd_Child_Dialog insertadd_dialog_child = (InsertAdd_Child_Dialog) getFragmentManager()
					.findFragmentByTag("dialog_child_addinsert");
			if (insertadd_dialog_child != null) {
				Log.v("activity oncreate",
						" resetting dialog insert add child listener");
				insertadd_dialog_child.setListener(this);

			}
			InsertAdd_Parent_Dialog insertadd_dialog_parent = (InsertAdd_Parent_Dialog) getFragmentManager()
					.findFragmentByTag("dialog_parent_addinsert");
			if (insertadd_dialog_parent != null) {
				Log.v("activity oncreate",
						" resetting dialog insert add parent listener");
				insertadd_dialog_parent.setListener(this);

			}

			Edit_Parent_Dialog edit_dialog_parent = (Edit_Parent_Dialog) getFragmentManager()
					.findFragmentByTag("dialog_parent_edit");
			if (edit_dialog_parent != null) {
				Log.v("activity oncreate",
						" resetting dialog insert add parent listener");
				edit_dialog_parent.setListener(this);

			}
			Edit_Child_Dialog edit_dialog_child = (Edit_Child_Dialog) getFragmentManager()
					.findFragmentByTag("dialog_child_edit");
			if (edit_dialog_child != null) {
				Log.v("activity oncreate",
						" resetting dialog insert add parent listener");
				edit_dialog_child.setListener(this);

			}
		}

		wod_display_fragment
				.setAdd_Edit_Delete_Listener(add_edit_delete_listener);
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putInt(GroupPosition, groupPosition);
		state.putInt(ChildPosition, childPosition);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wod_menu, menu);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v("activity main menu", "get new add wod container");
		if (date_holder == null) {
			date_holder = new Date_Holder(current_date);
		}
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_parent");
		if (prev != null) {
			ft.remove(prev);
		}

		InsertAdd_Parent_Dialog newFragment = InsertAdd_Parent_Dialog
				.newInstance("Add", groupPosition);
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_parent_addinsert");

		return false;

	}

	public void call_add_edit_delete_child_dialog(Date_Holder date_holder,
			Integer groupPosition, Integer childPosition) {
		Log.v("main details",
				"out of the click listener and into the create dialog view...");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		Log.v("activity main", "chosen group " + groupPosition
				+ " childPosition " + childPosition);
		this.childPosition = childPosition;
		this.groupPosition = groupPosition;
		this.date_holder = date_holder;
		Log.v("activity main", "chosen group " + this.groupPosition
				+ " childPosition " + this.childPosition);
		InsertAddDeleteEditDialogChild newFragment = InsertAddDeleteEditDialogChild
				.newInstance();
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_child");
	}

	public void call_add_edit_delete_group_dialog(Date_Holder date_holder,
			Integer groupPosition) {
		Log.v("main details",
				"out of the click listener and into the create dialog view...");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		this.childPosition = 0;
		this.groupPosition = groupPosition;
		this.date_holder = date_holder;
		InsertAddDeleteEditDialogParent newFragment = InsertAddDeleteEditDialogParent
				.newInstance();
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_parent");
	}

	@Override
	public void return_child_edit() {
		Log.v("activity main", "with edit child event");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_child");
		if (prev != null) {
			ft.remove(prev);
		}

		Edit_Child_Dialog newFragment = Edit_Child_Dialog.newInstance(
				date_holder.return_single_container(groupPosition)
						.return_single_movement(childPosition), groupPosition,
				childPosition);
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_child_edit");

	}

	@Override
	public void return_child_delete() {
		Log.v("activity main", "with delete child event");
		Log.v("activity main", "delete group " + groupPosition
				+ " childPosition " + childPosition);
		CalendarHolder.deletechild(date_holder, groupPosition, childPosition);
		current_status = "child delete";

	}

	@Override
	public void return_child_add() {
		Log.v("activity main", "with add child event");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_child");
		if (prev != null) {
			ft.remove(prev);
		}

		InsertAdd_Child_Dialog newFragment = InsertAdd_Child_Dialog
				.newInstance("Add", groupPosition, childPosition);
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_child_addinsert");

	}

	@Override
	public void return_child_insert() {
		Log.v("activity main", "with insert child event");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_child");
		if (prev != null) {
			ft.remove(prev);
		}

		InsertAdd_Child_Dialog newFragment = InsertAdd_Child_Dialog
				.newInstance("Insert", groupPosition, childPosition);
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_child_addinsert");

	}

	@Override
	public void return_parent_delete() {
		Log.v("activity main", "with parent delete event");
		CalendarHolder.deleteparent(date_holder, groupPosition);
		current_status = "parent delete";

	}

	@Override
	public void return_parent_insert() {
		Log.v("activity main", "with insert parent event");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_parent");
		if (prev != null) {
			ft.remove(prev);
		}

		InsertAdd_Parent_Dialog newFragment = InsertAdd_Parent_Dialog
				.newInstance("Insert", groupPosition);
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_parent_addinsert");

	}

	@Override
	public void return_parent_add() {
		Log.v("activity main", "with insert parent event");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_parent");
		if (prev != null) {
			ft.remove(prev);
		}

		InsertAdd_Parent_Dialog newFragment = InsertAdd_Parent_Dialog
				.newInstance("Add", groupPosition);
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_parent_addinsert");

	}

	@Override
	public void return_parent_edit() {
		Log.v("activity main", "with edit parent event");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_parent");
		if (prev != null) {
			ft.remove(prev);
		}
		Log.v("month day year", "" + Month + " " + Day + " " + Year);
		Log.v(" child and group positions", "" + childPosition + " "
				+ groupPosition);

		Edit_Parent_Dialog newEditParentFragment = Edit_Parent_Dialog
				.newInstance(
						date_holder.return_single_container(groupPosition),
						groupPosition);
		newEditParentFragment.setListener(this);
		newEditParentFragment.show(ft, "dialog_parent_edit");

	}

	@Override
	public void return_parent_movement_add() {
		Log.v("activity main", "with add movement event");
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_parent");
		if (prev != null) {
			ft.remove(prev);
		}

		InsertAdd_Child_Dialog newFragment = InsertAdd_Child_Dialog
				.newInstance("Add", groupPosition, childPosition);
		newFragment.setListener(this);
		newFragment.show(ft, "dialog_child_addinsert");

	}

	@Override
	public void send_back_edited_movement(
			Movement_Container_Holder movement_container_edit) {
		Log.v("activity main", "activity with edit child");
		CalendarHolder.editchild(movement_container_edit);
		current_status = "child edit";

	}

	@Override
	public void send_back_edited_wod_container(
			WOD_Container_Holder WOD_container_edit) {
		Log.v("activity main", "activity with edit parent");
		CalendarHolder.editparent(WOD_container_edit);
		current_status = "parent edit";
	}

	@Override
	public void send_back_add_movement(
			Movement_Container_Holder movement_container_edit) {
		CalendarHolder.addmovement(date_holder, movement_container_edit,
				groupPosition, childPosition, current_date, "Add");
		current_status = "child add";
	}

	@Override
	public void send_back_insert_movement(
			Movement_Container_Holder movement_container_edit) {
		Log.v("activity main", "activity with insert child");
		CalendarHolder.insertparent(date_holder, movement_container_edit,
				groupPosition, childPosition, current_date, "Insert");
		current_status = "child insert";
	}

	@Override
	public void send_back_add_wod_container(
			WOD_Container_Holder WOD_container_edit) {

		Log.v("activity main", "activity with add wod container");
		CalendarHolder.addparent(date_holder, WOD_container_edit,
				date_holder.get_WOD_Container_size(), current_date);
		current_status = "parent add";

	}

	@Override
	public void send_back_insert_wod_container(
			WOD_Container_Holder WOD_container_edit) {
		Log.v("activity main", "activity with insert wod container");
		CalendarHolder.insertparent(date_holder, WOD_container_edit,
				groupPosition, current_date);
		current_status = "parent insert";
	}

}
