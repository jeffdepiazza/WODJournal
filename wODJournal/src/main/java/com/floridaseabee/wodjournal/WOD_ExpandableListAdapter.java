package com.floridaseabee.wodjournal;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class WOD_ExpandableListAdapter extends BaseExpandableListAdapter {

	Date_Holder date_holder;
	LayoutInflater inflater = null;
	Context context;

	WOD_ExpandableListAdapter(Date_Holder date_holder, LayoutInflater inflater, Context context) {
		this.date_holder = date_holder;
		this.inflater = inflater;
		this.context = context;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		int size = 0;
		if (date_holder.WOD_Container_Holder.get(groupPosition).return_movements() != null)
			size = date_holder.WOD_Container_Holder.get(groupPosition).return_movements().size();
		return size;
	}

	@Override
	public Object getGroup(int groupPosition) {

		Log.v("Wod ExpandList Adapter", groupPosition + "=  getGroup ");
		return date_holder.WOD_Container_Holder.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		Log.v("Wod ExpandList Adapter", "getchild " + groupPosition + "=  groupPosition , " + childPosition
				+ "= childposition");
		final WOD_Container_Holder WOD_Container_Holder = date_holder.WOD_Container_Holder.get(groupPosition);
		final Movement_Container_Holder movement_container_holder = WOD_Container_Holder.return_movements().get(
				childPosition);
		return movement_container_holder;
	}

	@Override
	public long getGroupId(int groupPosition) {
		Log.v("Wod ExpanList Adapter", " get group id" + groupPosition + "=  groupPosition ");
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		Log.v("Wod ExpandList Adapter", " get childid " + groupPosition + "=  groupPosition , " + childPosition
				+ "= childposition");
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {

		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		final WOD_Container_Holder WOD_Container_Holder = date_holder.WOD_Container_Holder.get(groupPosition);

		TextView comments_view;
		if (!(WOD_Container_Holder.get_Timed() == 0)) {
			convertView = inflater.inflate(R.layout.wod_container_timed, parent, false);
			Log.v("get timed group", " " + WOD_Container_Holder.get_Timed());
			String timed_type;
			
			// setting this string so we can build the expression as the rounds finished may
			// not always be included, and if its zero, why bother displaying the rounds finished?
			timed_type = WOD_Container_Holder
					.get_Timed() + "min " + WOD_Container_Holder.get_Timed_Type();
			
			if (!(WOD_Container_Holder.get_Rounds_Finished() == 0)) {
				
				timed_type = timed_type + " <small>"
						+ context.getResources().getString(R.string.rounds_finished)
						+ " "
						+ WOD_Container_Holder.get_Rounds_Finished() + "</small>";
				
			}
			((TextView) convertView.findViewById(R.id.container_timed)).setText(Html.fromHtml(timed_type));

			// if the comments or named workout are zero length, hide the box as
			// there is nothing
			// in there
			if (WOD_Container_Holder.get_Workout_Name().length() == 0) {
                convertView.findViewById(R.id.container_timed_named_workout).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.container_timed_named_workout)).setText(context
						.getResources().getString(R.string.named_workout_text)
						+ " "
						+ WOD_Container_Holder.get_Workout_Name());

			}
            comments_view = convertView.findViewById(R.id.container_timed_comments);
			if (WOD_Container_Holder.get_Comments().length() == 0) {
				comments_view.setVisibility(View.GONE);
			} else {
				comments_view.setText(WOD_Container_Holder.get_Comments());
			}
		} else if (!(WOD_Container_Holder.get_Rounds() == 0)) {
			convertView = inflater.inflate(R.layout.wod_container_rounds, parent, false);
			String temp_text = "";
			//temp_text is used as we are trying to determine if the "rounds finished" text is needed
			//as we don't want to display it if there is none stored.  We don't want to do a whole
			//bunch of findviewbyID's...as its inefficient, so we have a temp text string that we
			//build and THEN send to the textbox.

			temp_text = WOD_Container_Holder.get_Rounds() + " " + context.getResources().getString(R.string.rounds_text);

			if (!((WOD_Container_Holder.get_Finish_Time().isEmpty()))) {
				temp_text = " <small> " + context.getResources().getString(R.string.finished_in) + " "
				+ WOD_Container_Holder.get_Finish_Time();
			}
			((TextView) convertView.findViewById(R.id.container_rounds_rounds_total)).setText(Html.fromHtml(temp_text));
			Log.v("Adapter view inflate", "rounds inflating");

			// if the comments or named workout are zero length, hide the box as
			// there is nothing
			// in there
			if (WOD_Container_Holder.get_Workout_Name().length() == 0) {
                convertView.findViewById(R.id.container_rounds_named_workout).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.container_rounds_named_workout)).setText(context
						.getResources().getString(R.string.named_workout_text)
						+ " "
						+ WOD_Container_Holder.get_Workout_Name());

			}

            comments_view = convertView.findViewById(R.id.container_rounds_comments);
			if (WOD_Container_Holder.get_Comments().length() == 0) {
				comments_view.setVisibility(View.GONE);
			} else {
				comments_view.setText(WOD_Container_Holder.get_Comments());
			}
		} else if (!(WOD_Container_Holder.get_Staggered_Rounds().trim().length() == 0)) {

			convertView = inflater.inflate(R.layout.wod_container_staggered, parent, false);
			Log.v("Adapter view inflate", "staggered rounds inflating");
			((TextView) convertView.findViewById(R.id.container_staggered_rounds)).setText(WOD_Container_Holder
					.get_Staggered_Rounds() + " ");

            comments_view = convertView.findViewById(R.id.container_staggered_comments);

			if (WOD_Container_Holder.get_Finish_Time() == ""  || WOD_Container_Holder.get_Finish_Time().isEmpty()) {
				Log.v("expandable list adapter", " empty finish time");
                convertView.findViewById(R.id.container_staggered_time_finished).setVisibility(View.GONE);
                convertView.findViewById(R.id.container_staggered_time_finished_text)
						.setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.container_staggered_time_finished)).setText(": "
						+ WOD_Container_Holder.get_Finish_Time() + "");
			}
			// if the comments or named workout are zero length, hide the box as
			// there is nothing
			// in there
			if (WOD_Container_Holder.get_Workout_Name().length() == 0) {
                convertView.findViewById(R.id.container_staggered_named_workout).setVisibility(View.GONE);
                convertView.findViewById(R.id.container_staggered_named_workout_text)
						.setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.container_staggered_named_workout)).setText(" "
						+ WOD_Container_Holder.get_Workout_Name());

			}
			if (WOD_Container_Holder.get_Comments().length() == 0) {
				comments_view.setVisibility(View.GONE);
			} else {
				comments_view.setText(WOD_Container_Holder.get_Comments());
			}

		} else if (!(WOD_Container_Holder.get_IsWeight_Workout() == "F")) {
			convertView = inflater.inflate(R.layout.wod_container_isweight, parent, false);
			Log.v("Adapter view inflate", "weight only/accessory inflating");
            comments_view = convertView.findViewById(R.id.container_accessory_comments);
			// if the comments are zero length, hide the box as there is nothing
			// in there

			if (WOD_Container_Holder.get_Comments().length() == 0) {
				comments_view.setVisibility(View.GONE);
			} else {
				comments_view.setText(WOD_Container_Holder.get_Comments());
			}

		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		final WOD_Container_Holder WOD_Container_Holder = date_holder.WOD_Container_Holder.get(groupPosition);
		final Movement_Container_Holder movement_container_holder = WOD_Container_Holder.return_movements().get(
				childPosition);
		String text_to_display = "";
		// Inflate .xml file for child rows

		if (!(movement_container_holder.return_Rep_Max() == 0)) {
			convertView = inflater.inflate(R.layout.movement_rep_max, parent, false);
			Log.v("wod expand list adapter", "get child view in Rep max");
			// Get childrow.xml file elements and set values
			text_to_display = "" + movement_container_holder.return_Rep_Max() + "RM "
					+ movement_container_holder.return_Movement();

			if (!(movement_container_holder.return_weight() == 0)) {
				text_to_display = text_to_display + " " + movement_container_holder.return_weight() + ""
						+ movement_container_holder.return_weight_units();
			}

			if (!(movement_container_holder.return_Percentage() == 0)) {
				text_to_display = text_to_display + " @" + movement_container_holder.return_Percentage() + "%";

			}
			((TextView) convertView.findViewById(R.id.rep_max)).setText(text_to_display);

			// if comments are empty (or zero length), hide the comments section
			// as there is no reason to display it.

			if (movement_container_holder.return_Comments().length() == 0) {
                convertView.findViewById(R.id.rep_max_comments).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.rep_max_comments)).setText(""
						+ movement_container_holder.return_Comments() + "");
			}
		} else if (!(movement_container_holder.return_sets() == 0)
				&& (!(movement_container_holder.return_reps() == 0) || !(movement_container_holder
						.return_reps_dynamic().length() == 0))) {
			Log.v("wod expand list adapter", "get child view in sets and reps");
			convertView = inflater.inflate(R.layout.movement_set_reps, parent, false);

			// Get childrow.xml file elements and set values
			((TextView) convertView.findViewById(R.id.set_reps_sets)).setText(""
					+ movement_container_holder.return_sets() + "");
			// we may have a static or dynamic reps, so we test to see if reps
			// is zero
			// if it is, then we know it has to be dynamic.

			text_to_display = "" + movement_container_holder.return_sets() + "X";

			if (!(movement_container_holder.return_reps() == 0)) {
				text_to_display = text_to_display + movement_container_holder.return_reps() + " ";
			} else {
				text_to_display = text_to_display + movement_container_holder.return_reps_dynamic() + " ";
			}
			text_to_display = text_to_display + movement_container_holder.return_Movement();
			if (!(movement_container_holder.return_weight() == 0)) {
				text_to_display = text_to_display + " " + movement_container_holder.return_weight() + ""
						+ movement_container_holder.return_weight_units();
			}

			if (!(movement_container_holder.return_Percentage() == 0)) {
				text_to_display = text_to_display + " @" + movement_container_holder.return_Percentage() + "%";

			}
			((TextView) convertView.findViewById(R.id.set_reps_sets)).setText(text_to_display);

			if (movement_container_holder.return_Comments().length() == 0) {
                convertView.findViewById(R.id.set_reps_comments).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.set_reps_comments)).setText(""
						+ movement_container_holder.return_Comments() + "");
			}
		} else if (!(movement_container_holder.return_length() == 0)) {

			convertView = inflater.inflate(R.layout.movement_length, parent, false);
			Log.v("wod expand list adapter", "get child view in length workout");
			// Get childrow.xml file elements and set values

			text_to_display = "" + movement_container_holder.return_length()
					+ movement_container_holder.return_length_units() + " "
					+ movement_container_holder.return_Movement();

			if (!(movement_container_holder.return_weight() == 0)) {
				text_to_display = text_to_display + " " + movement_container_holder.return_weight() + ""
						+ movement_container_holder.return_weight_units();
			}

			if (!(movement_container_holder.return_Percentage() == 0)) {
				text_to_display = text_to_display + " @" + movement_container_holder.return_Percentage() + "%";

			}
			((TextView) convertView.findViewById(R.id.length_movement_length)).setText(text_to_display);

			if (movement_container_holder.return_Comments().length() == 0) {
                convertView.findViewById(R.id.length_movement_comments).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.length_movement_comments)).setText(""
						+ movement_container_holder.return_Comments() + "");
			}
		} else if (!(movement_container_holder.return_AMRAP() == 0)) {

			Log.v("wod expand list adapter", "get child view in amrap");
			convertView = inflater.inflate(R.layout.movement_amrap, parent, false);
			text_to_display = "AMRAP " + movement_container_holder.return_Movement();

			if (!(movement_container_holder.return_weight() == 0)) {
				text_to_display = text_to_display + " " + movement_container_holder.return_weight() + ""
						+ movement_container_holder.return_weight_units();
			}

			if (!(movement_container_holder.return_Percentage() == 0)) {
				text_to_display = text_to_display + " @" + movement_container_holder.return_Percentage() + "%";

			}
			((TextView) convertView.findViewById(R.id.amrap_movement)).setText(text_to_display);

			// Get childrow.xml file elements and set values
			if (movement_container_holder.return_Comments().length() == 0) {
                convertView.findViewById(R.id.amrap_comments).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.amrap_comments)).setText(""
						+ movement_container_holder.return_Comments() + "");
			}

		} else if (!(movement_container_holder.return_Movement_Number() == 0)) {
			convertView = inflater.inflate(R.layout.movement_normal, parent, false);
			Log.v("wod expand list adapter",
					"get child view in movement number" + movement_container_holder.return_Movement_Number());
			// Get childrow.xml file elements and set values
			text_to_display = "" + movement_container_holder.return_Movement_Number() + " "
					+ movement_container_holder.return_Movement();

			if (!(movement_container_holder.return_weight() == 0)) {
				text_to_display = text_to_display + " " + movement_container_holder.return_weight() + ""
						+ movement_container_holder.return_weight_units();
			}

			if (!(movement_container_holder.return_Percentage() == 0)) {
				text_to_display = text_to_display + " @" + movement_container_holder.return_Percentage() + "%";

			}
			((TextView) convertView.findViewById(R.id.normal_movement)).setText(text_to_display);

			if (movement_container_holder.return_Comments().length() == 0) {
                convertView.findViewById(R.id.normal_weight_comments).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.normal_weight_comments)).setText(""
						+ movement_container_holder.return_Comments() + "");
			}
		} else if (!(movement_container_holder.return_Time_of_Movement() == 0)) {

			convertView = inflater.inflate(R.layout.movement_timed, parent, false);
			Log.v("wod expand list adapter", "get child view in normal movements with time of movement "
					+ movement_container_holder.return_Time_of_Movement());
			// Get childrow.xml file elements and set values
			text_to_display = "" + movement_container_holder.return_Time_of_Movement() + " "
					+ movement_container_holder.return_Time_of_Movement_Units() + " "
					+ movement_container_holder.return_Movement();

			if (!(movement_container_holder.return_weight() == 0)) {
				text_to_display = text_to_display + " " + movement_container_holder.return_weight() + ""
						+ movement_container_holder.return_weight_units();
			}

			if (!(movement_container_holder.return_Percentage() == 0)) {
				text_to_display = text_to_display + " @" + movement_container_holder.return_Percentage() + "%";

			}
			((TextView) convertView.findViewById(R.id.timed_text)).setText(text_to_display);

			if (movement_container_holder.return_Comments().length() == 0) {
                convertView.findViewById(R.id.timed_comments).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.timed_comments)).setText(""
						+ movement_container_holder.return_Comments() + "");
			}

		} else if (!(movement_container_holder.return_Staggered_Rounds() == 0)) {

			convertView = inflater.inflate(R.layout.movement_staggered, parent, false);
			Log.v("wod expand list adapter", "get child view in staggered movements ");
			// Get childrow.xml file elements and set values
			text_to_display = "" + movement_container_holder.return_Movement();

			if (!(movement_container_holder.return_weight() == 0)) {
				text_to_display = text_to_display + " " + movement_container_holder.return_weight() + ""
						+ movement_container_holder.return_weight_units();
			}

			if (!(movement_container_holder.return_Percentage() == 0)) {
				text_to_display = text_to_display + " @" + movement_container_holder.return_Percentage() + "%";

			}
			((TextView) convertView.findViewById(R.id.staggered_movement)).setText(text_to_display);

			if (movement_container_holder.return_Comments().length() == 0) {
                convertView.findViewById(R.id.staggered_weight_comments).setVisibility(View.GONE);
			} else {
				((TextView) convertView.findViewById(R.id.staggered_weight_comments)).setText(""
						+ movement_container_holder.return_Comments() + "");
			}

		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

	@Override
	public int getGroupCount() {

		return date_holder.WOD_Container_Holder.size();

	}

}
