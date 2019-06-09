package com.floridaseabee.wodjournal;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Workout_Search_Activity extends FragmentActivity {

	private ImageButton General_Search_IB;
	private final String search_string = "SEARCH_STRING";
	private final String search_type = "SEARCH_TYPE";
	private static final String[] movement_types = { "Sets x Reps", "Length (Run/Row)", "AMRAP", "Reps Only", "Timed",
			"Reps Max", "Staggered" };
	private static final String[] length_unit_types = { "m", "ft", "yds", "km", "mi" };
	private static final String[] weight_unit_types = { "lbs", "kgs" };
	private static final String[] time_unit_types = { "min", "sec" };
	private static final String[] rep_types = { "Static", "Dynamic" };
	private static final String[] search_types = { "Workout Container", "Movement" };
	private static final String[] wod_types = { "Timed Workout (AMRAP)", "Rounds", "Staggered Rounds" };
	private static final String[] gle_types = { ">", "<", "=" };
	private EditText reps;
	private EditText reps_dynamic;
	private Spinner wod_type_spinner;
	private Spinner movement_type_spinner;
	private Spinner search_types_spinner;
	private Spinner rep_type_text;
	private Spinner length_units_text;
	private Spinner timed_units_text;
	private Spinner weight_units_text;
	private Spinner gle_weight;
	private Spinner gle_percentage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_search);

        reps = findViewById(R.id.search_reps);
        reps_dynamic = findViewById(R.id.search_reps_dynamic);

		ArrayAdapter<String> wod = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wod_types);
		wod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wod_type_spinner = findViewById(R.id.search_container_type_spinner);
		wod_type_spinner.setAdapter(wod);
		wod_type_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {

				case 0:
                    findViewById(R.id.search_wod_timed_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_wod_timed_container2).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_rounds_container).setVisibility(View.GONE);
                    findViewById(R.id.search_staggered_container).setVisibility(View.GONE);
					break;
				case 1:
                    findViewById(R.id.search_wod_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_wod_timed_container2).setVisibility(View.GONE);
                    findViewById(R.id.search_rounds_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_staggered_container).setVisibility(View.GONE);
					break;
				case 2:
                    findViewById(R.id.search_wod_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_wod_timed_container2).setVisibility(View.GONE);
                    findViewById(R.id.search_rounds_container).setVisibility(View.GONE);
                    findViewById(R.id.search_staggered_container).setVisibility(View.VISIBLE);
					break;
				case 3:
                    findViewById(R.id.search_wod_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_wod_timed_container2).setVisibility(View.GONE);
                    findViewById(R.id.search_rounds_container).setVisibility(View.GONE);
                    findViewById(R.id.search_staggered_container).setVisibility(View.GONE);
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}

		});

        movement_type_spinner = findViewById(R.id.search_movement_type_spinner);
		ArrayAdapter<String> mv = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, movement_types);
		mv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		movement_type_spinner.setAdapter(mv);
		movement_type_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
                    findViewById(R.id.search_sets_reps_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_length_container).setVisibility(View.GONE);
                    findViewById(R.id.search_repmax_container).setVisibility(View.GONE);
                    findViewById(R.id.search_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_movement_number_container).setVisibility(View.GONE);
					break;
				case 1:
                    findViewById(R.id.search_sets_reps_container).setVisibility(View.GONE);
                    findViewById(R.id.search_length_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_repmax_container).setVisibility(View.GONE);
                    findViewById(R.id.search_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_movement_number_container).setVisibility(View.GONE);
					break;
				case 2: // amrap is the same as staggered
				case 6:
                    findViewById(R.id.search_sets_reps_container).setVisibility(View.GONE);
                    findViewById(R.id.search_length_container).setVisibility(View.GONE);
                    findViewById(R.id.search_repmax_container).setVisibility(View.GONE);
                    findViewById(R.id.search_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_movement_number_container).setVisibility(View.GONE);
					break;
				case 3:
                    findViewById(R.id.search_sets_reps_container).setVisibility(View.GONE);
                    findViewById(R.id.search_length_container).setVisibility(View.GONE);
                    findViewById(R.id.search_repmax_container).setVisibility(View.GONE);
                    findViewById(R.id.search_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_movement_number_container).setVisibility(View.VISIBLE);
					break;
				case 4:
                    findViewById(R.id.search_sets_reps_container).setVisibility(View.GONE);
                    findViewById(R.id.search_length_container).setVisibility(View.GONE);
                    findViewById(R.id.search_repmax_container).setVisibility(View.GONE);
                    findViewById(R.id.search_timed_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_movement_number_container).setVisibility(View.GONE);
					break;
				case 5:
                    findViewById(R.id.search_sets_reps_container).setVisibility(View.GONE);
                    findViewById(R.id.search_length_container).setVisibility(View.GONE);
                    findViewById(R.id.search_repmax_container).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_timed_container).setVisibility(View.GONE);
                    findViewById(R.id.search_movement_number_container).setVisibility(View.GONE);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		ArrayAdapter<String> type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, search_types);
		type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_types_spinner = findViewById(R.id.search_type_spinner);
		search_types_spinner.setAdapter(type);
		search_types_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					movement_type_spinner.setVisibility(View.GONE);
                    findViewById(R.id.search_movement_type_textview).setVisibility(View.GONE);
                    findViewById(R.id.search_container_type_textview).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_movement_container).setVisibility(View.GONE);
                    findViewById(R.id.search_wod_container).setVisibility(View.VISIBLE);
					wod_type_spinner.setVisibility(View.VISIBLE);
				} else {
					movement_type_spinner.setVisibility(View.VISIBLE);
                    findViewById(R.id.search_movement_type_textview).setVisibility(View.VISIBLE);
                    findViewById(R.id.search_container_type_textview).setVisibility(View.GONE);
					wod_type_spinner.setVisibility(View.GONE);
                    findViewById(R.id.search_wod_container).setVisibility(View.GONE);
                    findViewById(R.id.search_movement_container).setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}

		});

		// populate the timed units spinner
        timed_units_text = findViewById(R.id.search_timed_units_spinner);
		ArrayAdapter<String> tm = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time_unit_types);
		tm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timed_units_text.setAdapter(tm);

		// populate the length units spinner
        length_units_text = findViewById(R.id.search_length_units_spinner);
		ArrayAdapter<String> le = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				length_unit_types);
		le.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		length_units_text.setAdapter(le);

		// populate the weights units spinners
        weight_units_text = findViewById(R.id.search_weight_units_spinner);
		ArrayAdapter<String> we = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				weight_unit_types);
		we.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weight_units_text.setAdapter(we);

		// populate the set/reps dynamic spinner
        rep_type_text = findViewById(R.id.search_movement_reps_type_spinner);
		ArrayAdapter<String> rt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, rep_types);
		rt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rep_type_text.setAdapter(rt);

		// populate the greater than less than or equal to spinner
        gle_weight = findViewById(R.id.search_gle_weight);
		ArrayAdapter<String> gw = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gle_types);
		gw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gle_weight.setAdapter(gw);

		// populate the set/reps dynamic spinner
        gle_percentage = findViewById(R.id.search_gle_percentage);
		ArrayAdapter<String> ge = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gle_types);
		ge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gle_percentage.setAdapter(ge);

		// initial search values.
		length_units_text.setSelection(0);
		weight_units_text.setSelection(0);
		timed_units_text.setSelection(0);

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
        General_Search_IB = findViewById(R.id.workout_general_search_go);
		General_Search_IB.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (!(((TextView) findViewById(R.id.workout_search)).getText().toString().trim().length() == 0)) {

					Intent Launch_details = new Intent(getApplicationContext(), Workout_Search_Display_Activity.class);
					Launch_details.putExtra(search_string, ((TextView) findViewById(R.id.workout_search)).getText()
							.toString().trim());
					Launch_details.putExtra(search_type, "General");
					startActivity(Launch_details);
				} else {
					Toast.makeText(Workout_Search_Activity.this, "Search field cannot be empty", Toast.LENGTH_LONG)
							.show();
				}
			}

		});

        Button advanced_button_go = findViewById(R.id.search_advanced_go);
		advanced_button_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				advanced_go();

			}

		});
		// set the initial selection as a movement
		search_types_spinner.setSelection(1);
		gle_weight.setSelection(2);
		gle_percentage.setSelection(2);
	}

	public void advanced_go() {
		String sqlstatement = "";
		String searchtype = "";
		if (search_types_spinner.getSelectedItemPosition() == 0) {
			// workout/wod container type of search
			searchtype = "WOD_Container";

			switch (wod_type_spinner.getSelectedItemPosition()) {
			case 0:
				// Timed
				if (((EditText) findViewById(R.id.search_wod_Timed)).getText().toString().trim().length() == 0)
					sqlstatement = "WOD_Container.Timed > 0";
				else {
					sqlstatement = "WOD_Container.Timed = "
							+ ((EditText) findViewById(R.id.search_wod_Timed)).getText().toString().trim();
				}

				if (!(((EditText) findViewById(R.id.search_wod_Timed_Type)).getText().toString().trim().length() == 0)) {
					if (!(sqlstatement.length() == 0))
						sqlstatement = sqlstatement + " AND ";
					sqlstatement = sqlstatement + "WOD_Container.Timed_Type LIKE '"
							+ ((EditText) findViewById(R.id.search_wod_Timed_Type)).getText().toString().trim() + "'";
				}
				break;
			case 1:
				// Rounds
				if (((EditText) findViewById(R.id.search_wod_Rounds)).getText().toString().trim().length() == 0)
					sqlstatement = "WOD_Container.Rounds > 0";
				else {
					sqlstatement = "WOD_Container.Rounds = "
							+ ((EditText) findViewById(R.id.search_wod_Rounds)).getText().toString().trim();
				}
				break;
			case 2:
				// Staggered Rounds
				if (((EditText) findViewById(R.id.search_wod_staggered_rounds)).getText().toString().trim().length() == 0)
					sqlstatement = "WOD_Container.Staggered_Rounds LIKE *";
				else {
					sqlstatement = "WOD_Container.Staggered_Rounds LIKE '"
							+ ((EditText) findViewById(R.id.search_wod_staggered_rounds)).getText().toString().trim()
							+ "'";
				}
				break;
			}
			if (!(((EditText) findViewById(R.id.search_wod_named)).getText().toString().trim().length() == 0)) {
				if (!(sqlstatement.length() == 0))
					sqlstatement = sqlstatement + " AND ";
				sqlstatement = sqlstatement + "WOD_Container.Workout_Name Like '"
						+ ((EditText) findViewById(R.id.search_wod_named)).getText().toString().trim() + "'";
			}

		} else {
			// movement container type of search
			searchtype = "Movement";
			switch (movement_type_spinner.getSelectedItemPosition()) {
			case 0:
				// sets and reps
				Log.v("workout search advanced search", "sets and reps");
				if (((EditText) findViewById(R.id.search_sets)).getText().toString().trim().length() == 0)
					sqlstatement = "Movement_Template.Sets > 0 ";
				else {
					sqlstatement = "Movement_Template.Sets = "
							+ ((EditText) findViewById(R.id.search_sets)).getText().toString().trim();
				}

				if (rep_type_text.getSelectedItem().equals("Static")) {
					if (((EditText) findViewById(R.id.search_reps)).getText().toString().trim().length() == 0) {
						if (!(sqlstatement.length() == 0)) {
							sqlstatement = sqlstatement + " AND ";
						}
						sqlstatement = sqlstatement + "Movement_Template.Reps > 0 ";
					} else {
						sqlstatement = sqlstatement + "Movement_Template.Reps = "
								+ ((EditText) findViewById(R.id.search_reps)).getText().toString().trim();
					}
				} else {
					if (((EditText) findViewById(R.id.search_reps_dynamic)).getText().toString().trim().length() == 0) {
						if (!(sqlstatement.length() == 0)) {
							sqlstatement = sqlstatement + " AND ";
						}
						sqlstatement = sqlstatement + "Movement_Template.Reps LIKE * ";
					} else {
						sqlstatement = sqlstatement + "Movement_Template.Reps LIKE ' "
								+ ((EditText) findViewById(R.id.search_reps_dynamic)).getText().toString().trim() + "'";
					}
				}
				break;

			case 1:
				// Length
				Log.v("workout search advanced search", "length");
				if (((EditText) findViewById(R.id.search_length)).getText().toString().trim().length() == 0)
					sqlstatement = "Movement_Template.Length > 0";
				else {
					sqlstatement = "Movement_Template.Length = "
							+ ((EditText) findViewById(R.id.search_length)).getText().toString().trim()
							+ " AND Movement_Template.Length_Units LIKE '" + length_units_text.getSelectedItem() + "'";
				}
				break;
			case 2:
				// AMRAP
				Log.v("workout search advanced search", "amrap");
				sqlstatement = "Movement_Template.AMRAP = 1";
				break;
			case 3:
				// Reps Only
				Log.v("workout search advanced search", "movement numbers");
				if (((EditText) findViewById(R.id.search_movement_number)).getText().toString().trim().length() == 0)
					sqlstatement = "Movement_Template.Movement_Number > 0";
				else {
					sqlstatement = "Movement_Template.Movement_Number = "
							+ ((EditText) findViewById(R.id.search_movement_number)).getText().toString().trim();
				}
				break;
			case 4:
				// Timed
				Log.v("workout search advanced search", "timed");
				if (((EditText) findViewById(R.id.search_timed)).getText().toString().trim().length() == 0)
					sqlstatement = "Movement_Template.Time_of_Movement > 0 ";
				else {
					sqlstatement = "Movement_Template.Timed_of_Movement = "
							+ ((EditText) findViewById(R.id.search_timed)).getText().toString().trim()
							+ " AND Movement_Template.Timed_Units LIKE '" + timed_units_text.getSelectedItem() + "'";
				}
				break;
			case 5:
				// Rep Max
				Log.v("workout search advanced search", "rep max");
				if (((EditText) findViewById(R.id.search_rep_max)).getText().toString().trim().length() == 0)
					sqlstatement = "Movement_Template.Rep_Max > 0 ";
				else {
					sqlstatement = "Movement_Template.Rep_Max = "
							+ ((EditText) findViewById(R.id.search_rep_max)).getText().toString().trim();
				}
				break;
			case 6:
				// Staggered
				Log.v("workout search advanced search", "staggered");
				sqlstatement = "Movement_Template.Staggered_Rounds = 1";
			}

			if (!(((EditText) findViewById(R.id.search_movement)).getText().toString().trim().length() == 0)) {
				if (!(sqlstatement.length() == 0)) {
					sqlstatement = sqlstatement + " AND ";
				}
				sqlstatement = sqlstatement + "Movement_Template.Movement LIKE '%"
						+ ((EditText) findViewById(R.id.search_movement)).getText().toString().trim() + "%'";
			}

			if (((EditText) findViewById(R.id.search_weight)).getText().toString().trim().length() == 0) {
				if (!(sqlstatement.length() == 0)) {
					sqlstatement = sqlstatement + " AND ";
				}
				sqlstatement = sqlstatement + "Movement_Template.Weight >= 0";
			} else {
				if (!(sqlstatement.length() == 0)) {
					sqlstatement = sqlstatement + " AND ";
				}
				sqlstatement = sqlstatement + "Movement_Template.Weight " + gle_weight.getSelectedItem() + " "
						+ ((EditText) findViewById(R.id.search_weight)).getText().toString().trim()
						+ " AND Movement_Template.Weight_Units = '" + weight_units_text.getSelectedItem() + "'";
			}

			if (((EditText) findViewById(R.id.search_percentage)).getText().toString().trim().length() == 0) {
				if (!(sqlstatement.length() == 0)) {
					sqlstatement = sqlstatement + " AND ";
				}
				sqlstatement = sqlstatement + "Movement_Template.Percentage >= 0";
			} else {
				if (!(sqlstatement.length() == 0)) {
					sqlstatement = sqlstatement + " AND ";
				}
				sqlstatement = sqlstatement + "Movement_Template.Percentage " + gle_percentage.getSelectedItem() + " "
						+ ((EditText) findViewById(R.id.search_percentage)).getText().toString().trim();
			}
		}
		Intent Launch_details = new Intent(getApplicationContext(), Workout_Search_Display_Activity.class);
		Launch_details.putExtra(search_string, sqlstatement);
		Launch_details.putExtra(search_type, searchtype);
		startActivity(Launch_details);
	}
}
