package com.floridaseabee.wodjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InsertAdd_Child_Dialog extends DialogFragment implements AdapterView.OnItemSelectedListener,
		OnClickListener {

	private static final String Type = "Type";
	private static final String[] movement_types = { "Sets x Reps", "Length (Run/Row)", "AMRAP", "Reps Only", "Timed",
			"Reps Max", "Staggered" };
	private static final String[] length_unit_types = { "m", "ft", "yds", "km", "mi" };
	private static final String[] weight_unit_types = { "lbs", "kgs" };
	private static final String[] time_unit_types = { "min", "sec" };
	private static final String[] rep_types = { "Static", "Dynamic" };
	private Movement_Container_Holder movement_container_edit;
	private Boolean any_errors;
	private String Add_Insert;
	private EditText sets;
	private TextView sets_text;
	private EditText reps;
	private EditText reps_dynamic;
	private TextView reps_text;
	private EditText movement;
	private EditText timed;
	private EditText rep_max;
	private TextView rep_max_text;
	private EditText length;
	private TextView length_text;
	private EditText weight;
	private TextView edit_AMRAP;
	private EditText comments;
	private EditText movement_number;
	private TextView movement_number_text;
	private EditText percentage;
	private Spinner movement_type_spinner;
	private Spinner length_units_text;
	private Spinner weight_units_text;
	private Spinner timed_units_text;
	private Spinner rep_type_text;
	private ArrayAdapter<String> mv;
	private ArrayAdapter<String> we;
	private ArrayAdapter<String> tm;
	private ArrayAdapter<String> le;
	private ArrayAdapter<String> rt;
	private Movement_Add_Insert_Listener listener;

	static InsertAdd_Child_Dialog newInstance(String type, Integer groupPosition, Integer childPosition) {

		InsertAdd_Child_Dialog frag = new InsertAdd_Child_Dialog();
		Bundle args = new Bundle();
		args.putString(Type, type);
		frag.setArguments(args);
		return frag;

	}

	interface Movement_Add_Insert_Listener {
        void send_back_add_movement(Movement_Container_Holder movement_container_edit);

        void send_back_insert_movement(Movement_Container_Holder movement_container_edit);
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString(Type, Add_Insert);

	}

	// in the on create dialog, we are reusing the Edit Child dialog stuff as
	// the .xml and most of the logic is exactly what we need
	// We want to tailor the view we are
	// passing to the activity so it only shows the proper fields
	// based upon the structure of the data, in additon to showing the proper
	// spinner selection. When the user clicks on the spinner to select another
	// movement type, we hide/unhide the proper cells ONLY when the user selects
	// save, do we pass the values for the selected movement type
	// (and zero out the others to prevent any confusion later) up to the
	// activity.
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		View v = getActivity().getLayoutInflater().inflate(R.layout.movement_edit, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		Add_Insert = getArguments().getString(Type, "");
        movement_type_spinner = v.findViewById(R.id.edit_movement_type_spinner);
		mv = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, movement_types);
		mv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		movement_type_spinner.setAdapter(mv);
		movement_type_spinner.setOnItemSelectedListener(this);

		// populate the timed units spinner
        timed_units_text = v.findViewById(R.id.edit_timed_units_spinner);
		tm = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time_unit_types);
		tm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timed_units_text.setAdapter(tm);

		// populate the length units spinner
        length_units_text = v.findViewById(R.id.edit_length_units_spinner);
		le = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, length_unit_types);
		le.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		length_units_text.setAdapter(le);

		// populate the weights units spinners
        weight_units_text = v.findViewById(R.id.edit_weight_units_spinner);
		we = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, weight_unit_types);
		we.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weight_units_text.setAdapter(we);

		// populate the set/reps dynamic spinner
        rep_type_text = v.findViewById(R.id.edit_movement_reps_type_spinner);
		rt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, rep_types);
		rt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rep_type_text.setAdapter(rt);

		// populating the container units with what was in the bundle, as we
		// cannot access it outside of this
		// function...so if we want to do any saving/editing, we gotta grab the
		// data here.
		movement_container_edit = new Movement_Container_Holder();
		length_units_text.setSelection(0);
		weight_units_text.setSelection(0);
		timed_units_text.setSelection(0);

        sets = v.findViewById(R.id.edit_sets);
        sets_text = v.findViewById(R.id.edit_sets_text);
        reps = v.findViewById(R.id.edit_reps);
        reps_dynamic = v.findViewById(R.id.edit_reps_dynamic);
        reps_text = v.findViewById(R.id.edit_reps_text);
        movement = v.findViewById(R.id.edit_movement);
        timed = v.findViewById(R.id.edit_timed);
        percentage = v.findViewById(R.id.edit_percentage);
        rep_max = v.findViewById(R.id.edit_rep_max);
        rep_max_text = v.findViewById(R.id.edit_rep_max_text);
        length = v.findViewById(R.id.edit_length);
        length_text = v.findViewById(R.id.edit_length_text);
        weight = v.findViewById(R.id.edit_weight);
        edit_AMRAP = v.findViewById(R.id.edit_AMRAP_text);
        comments = v.findViewById(R.id.edit_comments);
        movement_number = v.findViewById(R.id.edit_movement_number);
        movement_number_text = v.findViewById(R.id.edit_movement_number_text);
		movement_type_spinner.setSelection(0); // set the spinner to sets and
												// reps as we have no idea what
												// the user wants anyway, just
												// set stuff to a static item
												// and wait for input

		// the set on item even fires after we set a selection, so we don't
		// really need to set individual items here
		// I'm not even sure we need to set the onitem selected listener as it
		// was still firing off even without
		// setting it, which may be a function of the implement above.
		Log.v("add insert child dialog", "about to set the on item listener");
		movement_type_spinner.setOnItemSelectedListener(this);
		rep_type_text.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					reps.setVisibility(View.VISIBLE);
					reps_dynamic.setVisibility(View.GONE);
					break;
				case 1:
					reps.setVisibility(View.GONE);
					reps_dynamic.setVisibility(View.VISIBLE);
					break;
				default:
					reps.setVisibility(View.VISIBLE);
					reps_dynamic.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		if (savedInstanceState != null) {
			Add_Insert = savedInstanceState.getString(Type, "");
		}

		if (Add_Insert == "Add") {
			return (builder.setTitle("Add Movement").setView(v).setPositiveButton(android.R.string.ok, this)
					.setNegativeButton(android.R.string.cancel, null).create());
		} else {
			return (builder.setTitle("Insert Movement").setView(v).setPositiveButton(android.R.string.ok, this)
					.setNegativeButton(android.R.string.cancel, null).create());
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Log.v("add/insert dialog", "handeling  on item selected listener");
		switch (position) {
		case 0:
			Log.v("add/insert child dialog", "sets and reps");
			sets.setVisibility(View.VISIBLE);
			sets_text.setVisibility(View.VISIBLE);

			if (movement_container_edit.return_reps_dynamic().length() == 0) {
				reps.setVisibility(View.VISIBLE);
				reps_dynamic.setVisibility(View.GONE);
			} else {
				reps.setVisibility(View.GONE);
				reps_dynamic.setVisibility(View.VISIBLE);
			}
			rep_type_text.setVisibility(View.VISIBLE);
			reps_text.setVisibility(View.VISIBLE);
			timed.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_units_text.setVisibility(View.GONE);
			movement_number.setVisibility(View.GONE);
			rep_max.setVisibility(View.GONE);
			rep_max_text.setVisibility(View.GONE);
			length_text.setVisibility(View.GONE);
			length_units_text.setVisibility(View.GONE);
			length.setVisibility(View.GONE);
			edit_AMRAP.setVisibility(View.GONE);
			movement_number_text.setVisibility(View.GONE);
			break;
		case 1:
			Log.v("add/insert child dialog", "length");
			sets.setVisibility(View.GONE);
			sets_text.setVisibility(View.GONE);
			reps.setVisibility(View.GONE);
			reps_text.setVisibility(View.GONE);
			reps_dynamic.setVisibility(View.GONE);
			rep_type_text.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_units_text.setVisibility(View.GONE);
			rep_max.setVisibility(View.GONE);
			rep_max_text.setVisibility(View.GONE);
			length_text.setVisibility(View.VISIBLE);
			length_units_text.setVisibility(View.VISIBLE);
			length.setVisibility(View.VISIBLE);
			movement_number.setVisibility(View.GONE);
			edit_AMRAP.setVisibility(View.GONE);
			movement_number_text.setVisibility(View.GONE);
			break;
		case 2:
			Log.v("add/insert child dialog", "amrap");
			sets.setVisibility(View.GONE);
			sets_text.setVisibility(View.GONE);
			reps.setVisibility(View.GONE);
			reps_dynamic.setVisibility(View.GONE);
			rep_type_text.setVisibility(View.GONE);
			reps_text.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_units_text.setVisibility(View.GONE);
			rep_max.setVisibility(View.GONE);
			rep_max_text.setVisibility(View.GONE);
			length_text.setVisibility(View.GONE);
			length_units_text.setVisibility(View.GONE);
			length.setVisibility(View.GONE);
			edit_AMRAP.setVisibility(View.VISIBLE);
			movement_number.setVisibility(View.GONE);
			movement_number_text.setVisibility(View.GONE);
			break;
		case 3:
			Log.v("add/insert child dialog", "normal reps");
			sets.setVisibility(View.GONE);
			sets_text.setVisibility(View.GONE);
			reps.setVisibility(View.GONE);
			reps_dynamic.setVisibility(View.GONE);
			rep_type_text.setVisibility(View.GONE);
			reps_text.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_units_text.setVisibility(View.GONE);
			rep_max.setVisibility(View.GONE);
			rep_max_text.setVisibility(View.GONE);
			length_text.setVisibility(View.GONE);
			length_units_text.setVisibility(View.GONE);
			length.setVisibility(View.GONE);
			movement_number.setVisibility(View.VISIBLE);
			edit_AMRAP.setVisibility(View.GONE);
			movement_number_text.setVisibility(View.VISIBLE);
			break;
		case 4:
			Log.v("add/insert child dialog", "Time of Movement");
			timed.setVisibility(View.VISIBLE);
			timed_units_text.setVisibility(View.VISIBLE);
			sets.setVisibility(View.GONE);
			sets_text.setVisibility(View.GONE);
			reps.setVisibility(View.GONE);
			rep_type_text.setVisibility(View.GONE);
			reps_dynamic.setVisibility(View.GONE);
			reps_text.setVisibility(View.GONE);
			rep_max.setVisibility(View.GONE);
			rep_max_text.setVisibility(View.GONE);
			length_text.setVisibility(View.GONE);
			length_units_text.setVisibility(View.GONE);
			length.setVisibility(View.GONE);
			movement_number.setVisibility(View.GONE);
			edit_AMRAP.setVisibility(View.GONE);
			movement_number_text.setVisibility(View.GONE);
			break;
		case 5:
			Log.v("add/insert child dialog", "Rep Max");
			sets.setVisibility(View.GONE);
			sets_text.setVisibility(View.GONE);
			reps.setVisibility(View.GONE);
			rep_type_text.setVisibility(View.GONE);
			reps_dynamic.setVisibility(View.GONE);
			reps_text.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_units_text.setVisibility(View.GONE);
			rep_max.setVisibility(View.VISIBLE);
			rep_max_text.setVisibility(View.VISIBLE);
			movement_number.setVisibility(View.GONE);
			length_text.setVisibility(View.GONE);
			length_units_text.setVisibility(View.GONE);
			length.setVisibility(View.GONE);
			edit_AMRAP.setVisibility(View.GONE);
			movement_number_text.setVisibility(View.GONE);
			break;
		case 6:
			sets.setVisibility(View.GONE);
			sets_text.setVisibility(View.GONE);
			reps.setVisibility(View.GONE);
			rep_type_text.setVisibility(View.GONE);
			reps_text.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_units_text.setVisibility(View.GONE);
			rep_max.setVisibility(View.GONE);
			rep_max_text.setVisibility(View.GONE);
			movement_number.setVisibility(View.GONE);
			length_text.setVisibility(View.GONE);
			length_units_text.setVisibility(View.GONE);
			length.setVisibility(View.GONE);
			edit_AMRAP.setVisibility(View.GONE);
			movement_number_text.setVisibility(View.GONE);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void go_to_other_click(View v) {
		// this used to be the onClick event of the Positive button, until we
		// found out that the
		// positive button automatically closes the dialog no matter what
		// happens inside the onClick event
		// We wanted to check for the proper inputs and values first, so we
		// didn't want the dialog to close until we found out that we have
		// the proper inputs...so we have to also do some craziness on the
		// onStart to point the onClick for the positive button elsewhere.

		Log.v("add/insert child dialog", "on click");
		any_errors = false;
		switch (movement_type_spinner.getSelectedItemPosition()) {
		case 0:
			try {
				if (Integer.parseInt(sets.getText().toString()) == 0) {
					sets.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					movement_container_edit.set_sets(Integer.parseInt(sets.getText().toString()));
					sets.setBackgroundColor(getTheme());
				}
			} catch (NumberFormatException nfe) {
				sets.setBackgroundColor(Color.RED);
				any_errors = true;

			}

			switch (rep_type_text.getSelectedItemPosition()) {
			case 0: // static type of reps, so we need to test the reps integer
					// and set the dynamic to empty
				try {
					if (Integer.parseInt(reps.getText().toString()) == 0) {
						reps.setBackgroundColor(Color.RED);
						any_errors = true;
					} else {
						movement_container_edit.set_reps(Integer.parseInt(reps.getText().toString()));
						movement_container_edit.set_reps_dynamic("");
						reps.setBackgroundColor(getTheme());
					}
				} catch (NumberFormatException nfe) {
					reps.setBackgroundColor(Color.RED);
					any_errors = true;
				}
				break;
			case 1: // dynamic type of reps, so we need to test for a zero
					// length string and set the static to zero.
				try {
					if (reps_dynamic.getText().toString().length() == 0) {
						reps_dynamic.setBackgroundColor(Color.RED);
						any_errors = true;
					} else {
						movement_container_edit.set_reps_dynamic(reps_dynamic.getText().toString());
						movement_container_edit.set_reps(0);
						reps_dynamic.setBackgroundColor(getTheme());
					}

				} catch (NumberFormatException nfe) {
					reps_dynamic.setBackgroundColor(Color.RED);
					any_errors = true;
				}

			}
			Log.v("add/insert child dialog onclick", "sets reps");
			movement_container_edit.set_Movement(movement.getText().toString());
			movement_container_edit.set_AMRAP(0);
			movement_container_edit.set_length(0);
			movement_container_edit.set_Time_of_Movement(0);
			movement_container_edit.set_Rep_Max(0);
			movement_container_edit.set_Movement_Number(0);
			break;
		case 1:
			try {
				if (Integer.parseInt(length.getText().toString()) == 0) {
					length.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					movement_container_edit.set_length(Integer.parseInt(length.getText().toString()));
					length.setBackgroundColor(getTheme());
				}

			} catch (NumberFormatException nfe) {
				length.setBackgroundColor(Color.RED);
				any_errors = true;

			}
			Log.v("add/insert child dialog onclick", "length");
			movement_container_edit.set_length_units(length_units_text.getSelectedItem().toString());
			movement_container_edit.set_AMRAP(0);
			movement_container_edit.set_reps(0);
			movement_container_edit.set_sets(0);
			movement_container_edit.set_Time_of_Movement(0);
			movement_container_edit.set_Rep_Max(0);
			movement_container_edit.set_Movement_Number(0);
			movement_container_edit.set_Staggered_Rounds(0);
			break;
		case 2:
			Log.v("add/insert child dialog onclick", "amrap");
			movement_container_edit.set_AMRAP(1);
			movement_container_edit.set_length(0);
			movement_container_edit.set_reps(0);
			movement_container_edit.set_sets(0);
			movement_container_edit.set_Time_of_Movement(0);
			movement_container_edit.set_Rep_Max(0);
			movement_container_edit.set_Movement_Number(0);
			movement_container_edit.set_Staggered_Rounds(0);
			break;

		case 3:
			try {
				if (Integer.parseInt(movement_number.getText().toString()) == 0) {
					movement_number.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					movement_container_edit.set_Movement_Number(Integer.parseInt(movement_number.getText().toString()));
					movement_number.setBackgroundColor(getTheme());
				}
			} catch (NumberFormatException nfe) {
				movement_number.setBackgroundColor(Color.RED);
				any_errors = true;

			}
			Log.v("add/insert child dialog onclick", "normal movement number");
			movement_container_edit.set_AMRAP(0);
			movement_container_edit.set_reps(0);
			movement_container_edit.set_sets(0);
			movement_container_edit.set_length(0);
			movement_container_edit.set_Time_of_Movement(0);
			movement_container_edit.set_Rep_Max(0);
			movement_container_edit.set_Staggered_Rounds(0);
			break;
		case 4:

			try {
				if (Integer.parseInt(timed.getText().toString()) == 0) {
					timed.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					movement_container_edit.set_Time_of_Movement(Integer.parseInt(timed.getText().toString()));
					timed.setBackgroundColor(getTheme());
				}
			} catch (NumberFormatException nfe) {
				timed.setBackgroundColor(Color.RED);
				any_errors = true;

			}
			Log.v("add/insert child dialog onclick", "timed movements");
			movement_container_edit.set_Time_of_Movement_Units(timed_units_text.getSelectedItem().toString());
			movement_container_edit.set_AMRAP(0);
			movement_container_edit.set_reps(0);
			movement_container_edit.set_sets(0);
			movement_container_edit.set_length(0);
			movement_container_edit.set_Movement_Number(0);
			movement_container_edit.set_Rep_Max(0);
			movement_container_edit.set_Staggered_Rounds(0);
			break;
		case 5:
			try {
				if (Integer.parseInt(rep_max.getText().toString()) == 0) {
					rep_max.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					movement_container_edit.set_Rep_Max(Integer.parseInt(rep_max.getText().toString()));
					rep_max.setBackgroundColor(getTheme());
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Could not parse case 5" + nfe);
				rep_max.setBackgroundColor(Color.RED);
				any_errors = true;

			}
			Log.v("add/insert child dialog onclick", "rep max");
			movement_container_edit.set_Staggered_Rounds(0);
			movement_container_edit.set_AMRAP(0);
			movement_container_edit.set_reps(0);
			movement_container_edit.set_sets(0);
			movement_container_edit.set_length(0);
			movement_container_edit.set_Movement_Number(0);
			movement_container_edit.set_Time_of_Movement(0);
			break;
		case 6:
			Log.v("add/insert child dialog onclick", "staggered");
			movement_container_edit.set_Staggered_Rounds(1);
			movement_container_edit.set_AMRAP(0);
			movement_container_edit.set_reps(0);
			movement_container_edit.set_sets(0);
			movement_container_edit.set_length(0);
			movement_container_edit.set_Movement_Number(0);
			movement_container_edit.set_Time_of_Movement(0);
			movement_container_edit.set_Rep_Max(0);
		}

		try {
			movement_container_edit.set_weight(Integer.parseInt(weight.getText().toString()));
			weight.setBackgroundColor(getTheme());
		} catch (NumberFormatException e) {
			movement_container_edit.set_weight(0);

		}

		movement_container_edit.set_weight_units(weight_units_text.getSelectedItem().toString());

		try {
			movement_container_edit.set_percentage(Integer.parseInt(percentage.getText().toString()));
			percentage.setBackgroundColor(getTheme());
		} catch (NumberFormatException e) {
			movement_container_edit.set_percentage(0);

		}
		movement_container_edit.set_Movement(movement.getText().toString());
		movement_container_edit.set_Comments(comments.getText().toString());

		if (any_errors == true) {
			Toast.makeText(getActivity(), "Errors in selections...", Toast.LENGTH_LONG).show();
			return;
		} else if (Add_Insert == "Add") {
			Log.v("add insert child dialog", "heading back to add listener");
			listener.send_back_add_movement(movement_container_edit);
			dismiss();
		} else if (Add_Insert == "Insert") {
			Log.v("add insert child dialog", "heading back to insert listener");
			listener.send_back_insert_movement(movement_container_edit);
			dismiss();
		}
	}

	public void Error_in_setting(String errortodisplay) {
		// listener.send_back_error_movement(errortodisplay);
	}

	public void setListener(WOD_Calendar_Details listener) {
		this.listener = listener;

	}

	@Override
	// overridden as we don't want the have the dialog close automatically.
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually
							// called on the underlying dialog, so we have to do
							// it after this point
		AlertDialog d = (AlertDialog) getDialog();
		if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
			positiveButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					go_to_other_click(v);
				}
			});
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		// empty as we take care of things elsewhere in the onStart event.
	}

}
