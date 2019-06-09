package com.floridaseabee.wodjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Edit_Parent_Dialog extends DialogFragment implements
		AdapterView.OnItemSelectedListener, OnClickListener {

	private static final String _id = "_id";
	private static final String Date_Holder_id = "Date_Holder_ID";
	private static final String Timed = "Timed";
	private static final String Timed_Type = "Timed_Type";
	private static final String Rounds = "Rounds";
	private static final String Finish_Time = "Finish_Time";
	private static final String Rounds_Finished = "Rounds_Finished";
	private static final String Container_Order = "Container_Order";
	private static final String IsWeight_Workout = "IsWeight_Workout";
	private static final String Workout_Name = "Workout_Name";
	private static final String Comments = "Comments";
	private static final String GroupPosition = "groupPosition";
	private static final String Staggered_Rounds = "Staggered_Rounds";
	private WOD_Container_Holder wod_container_edit;
	private WOD_Container_Edit_Listener listener;
	private Spinner wod_type_spinner;
	private static final String[] wod_types = { "Timed Workout (AMRAP)",
			"Rounds", "Staggered Rounds", "Weight Only/Accessory" };
	private ArrayAdapter<String> wod;
	private Boolean any_errors;
	private EditText timed;
	private EditText timed_type;
	private TextView timed_type_units;
	private TextView timed_rounds_completed_text;
	private EditText timed_type_rounds_completed;
	private EditText rounds;
	private TextView rounds_text;
	private TextView rounds_time_completed_text;
	private EditText rounds_time_completed;
	private EditText named;
	private TextView named_text;
	private TextView weight_only;
	private EditText comments;
	private EditText staggered_rounds;
	private TextView staggered_rounds_text;

	static Edit_Parent_Dialog newInstance(WOD_Container_Holder wod,
			Integer groupPosition) {

		Edit_Parent_Dialog frag = new Edit_Parent_Dialog();
		Bundle args = new Bundle();
		args.putInt(_id, wod.get_id());
		args.putInt(Date_Holder_id, wod.get_Date_Holder_id());
		args.putInt(Timed, wod.get_Timed());
		args.putString(Timed_Type, wod.get_Timed_Type());
		args.putInt(Rounds, wod.get_Rounds());
		args.putString(Finish_Time, wod.get_Finish_Time());
		args.putFloat(Rounds_Finished, wod.get_Rounds_Finished());
		args.putInt(Container_Order, wod.get_WOD_Container_Order());
		args.putString(IsWeight_Workout, wod.get_IsWeight_Workout());
		args.putString(Workout_Name, wod.get_Workout_Name());
		args.putString(Comments, wod.get_Comments());
		args.putInt(GroupPosition, groupPosition);
		args.putString(Staggered_Rounds, wod.get_Staggered_Rounds());
		frag.setArguments(args);
		return frag;

	}

	interface WOD_Container_Edit_Listener {
        void send_back_edited_wod_container(
                WOD_Container_Holder WOD_container_edit);

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = getActivity().getLayoutInflater().inflate(
				R.layout.wod_container_edit, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        wod_type_spinner = v.findViewById(R.id.edit_wod_type_spinner);
		wod = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, wod_types);
		wod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wod_type_spinner.setAdapter(wod);
		wod_type_spinner.setOnItemSelectedListener(this);

		wod_container_edit = new WOD_Container_Holder();
		wod_container_edit.set_id(getArguments().getInt(_id, 0));
		wod_container_edit.set_Date_Holder_id(getArguments().getInt(
				Date_Holder_id, 0));
		wod_container_edit.set_WOD_Container_Order(getArguments().getInt(
				Container_Order, 0));
		wod_container_edit.set_Timed(getArguments().getInt(Timed, 0));
		wod_container_edit.set_Timed_Type(getArguments().getString(Timed_Type,
				""));
		wod_container_edit.set_Rounds(getArguments().getInt(Rounds, 0));
		wod_container_edit.set_Rounds_Finished(getArguments().getFloat(
				Rounds_Finished, 0));
		wod_container_edit.set_Finish_Time(getArguments().getString(
				Finish_Time, ""));
		wod_container_edit.set_IsWeight_Workout(getArguments().getString(
				IsWeight_Workout, ""));
		wod_container_edit.set_Workout_Name(getArguments().getString(
				Workout_Name, ""));
		wod_container_edit.set_Comments(getArguments().getString(Comments, ""));
		wod_container_edit.set_Staggered_Rounds(getArguments().getString(
				Staggered_Rounds, ""));

        timed = v.findViewById(R.id.edit_wod_Timed);
		timed.setText("" + wod_container_edit.get_Timed() + "");

        timed_type_units = v
				.findViewById(R.id.edit_wod_Timed_time_units);

        timed_type = v.findViewById(R.id.edit_wod_Timed_Type);
		timed_type.setText(wod_container_edit.get_Timed_Type());

        timed_rounds_completed_text = v
				.findViewById(R.id.edit_Timed_Rounds_Completed_text);

        timed_type_rounds_completed = v
				.findViewById(R.id.edit_Timed_Rounds_Completed);
		timed_type_rounds_completed.setText(""
				+ wod_container_edit.get_Rounds_Finished() + "");

        rounds = v.findViewById(R.id.edit_wod_Rounds);
		rounds.setText("" + wod_container_edit.get_Rounds() + "");
        rounds_text = v.findViewById(R.id.edit_wod_Rounds_text);

		// the rounds time completed is a special case where we are expecting to
		// see the format in a HH:MM:SS and we need to ensure that the user is
		// giving us the input in the way we would expect. We will be testing
		// for this. we got this code from an existing project that handled MAC
		// addresses, which is similar to our date time format....so the code
		// works the same and we are just using it now.

        rounds_time_completed_text = v
				.findViewById(R.id.edit_Rounds_Time_Completed_text);

        rounds_time_completed = v
				.findViewById(R.id.edit_Rounds_Time_Completed);
		rounds_time_completed.setText(wod_container_edit.get_Finish_Time());
        staggered_rounds = v
				.findViewById(R.id.edit_wod_staggered_rounds);
		staggered_rounds.setText(wod_container_edit.get_Staggered_Rounds());
        staggered_rounds_text = v
				.findViewById(R.id.edit_wod_staggered_rounds_text);

		// BORROWED CODE BEGINS HERE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		rounds_time_completed.addTextChangedListener(new TextWatcher() {
			String mPreviousTime = "";

			@Override
			public void afterTextChanged(Editable arg0) {
			}

			/*
			 * (non-Javadoc) Does nothing.
			 * 
			 * @see
			 * android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence
			 * , int, int, int)
			 */
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			/*
			 * (non-Javadoc) Formats the MAC address and handles the cursor
			 * position.
			 * 
			 * @see
			 * android.text.TextWatcher#onTextChanged(java.lang.CharSequence,
			 * int, int, int)
			 */

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String enteredTime = rounds_time_completed.getText().toString()
						.toUpperCase();
				String cleanTime = clearNonTimeCharacters(enteredTime);
				String formattedTime = formatTime(cleanTime);

				int selectionStart = rounds_time_completed.getSelectionStart();
				formattedTime = handleColonDeletion(enteredTime, formattedTime,
						selectionStart);
				int lengthDiff = formattedTime.length() - enteredTime.length();

				setTimeEdit(cleanTime, formattedTime, selectionStart,
						lengthDiff);
			}

			/**
			 * Strips all characters from a string except A-F and 0-9.
			 * 
			 * @param time
			 *            User input string.
			 * @return String containing MAC-allowed characters.
			 */
			private String clearNonTimeCharacters(String time) {
                return time.replaceAll("[^0-9]", "");
			}

			/**
			 * Adds a colon character to an unformatted MAC address after every
			 * second character (strips full MAC trailing colon)
			 * 
			 * @param cleantime
			 *            Unformatted MAC address.
			 * @return Properly formatted MAC address.
			 */
			private String formatTime(String cleanTime) {
				int grouppedCharacters = 0;
				String formattedTime = "";

				for (int i = 0; i < cleanTime.length(); ++i) {
					formattedTime += cleanTime.charAt(i);
					++grouppedCharacters;

					if (grouppedCharacters == 2) {
						formattedTime += ":";
						grouppedCharacters = 0;
					}
				}

				// Removes trailing colon for complete MAC address
				if (cleanTime.length() == 6)
					formattedTime = formattedTime.substring(0,
							formattedTime.length() - 1);

				return formattedTime;
			}

			/**
			 * Upon users colon deletion, deletes MAC character preceding
			 * deleted colon as well.
			 * 
			 * @param enteredTime
			 *            User input MAC.
			 * @param formattedTime
			 *            Formatted MAC address.
			 * @param selectionStart
			 *            MAC EditText field cursor position.
			 * @return Formatted MAC address.
			 */
			private String handleColonDeletion(String enteredTime,
					String formattedTime, int selectionStart) {
				if (mPreviousTime != null && mPreviousTime.length() > 1) {
					int previousColonCount = colonCount(mPreviousTime);
					int currentColonCount = colonCount(enteredTime);

					if (currentColonCount < previousColonCount) {
						formattedTime = formattedTime.substring(0,
								selectionStart - 1)
								+ formattedTime.substring(selectionStart);
						String cleanTime = clearNonTimeCharacters(formattedTime);
						formattedTime = formatTime(cleanTime);
					}
				}
				return formattedTime;
			}

			/**
			 * Gets MAC address current colon count.
			 * 
			 * @param formattedMac
			 *            Formatted MAC address.
			 * @return Current number of colons in MAC address.
			 */
			private int colonCount(String formattedTime) {
				return formattedTime.replaceAll("[^:]", "").length();
			}

			/**
			 * Removes TextChange listener, sets Time EditText field value, sets
			 * new cursor position and re-initiates the listener.
			 * 
			 * @param cleanTime
			 *            Clean Time.
			 * @param formattedTime
			 *            Formatted Time .
			 * @param selectionStart
			 *            Time EditText field cursor position.
			 * @param lengthDiff
			 *            Formatted/Entered MAC number of characters difference.
			 */
			private void setTimeEdit(String cleanTime, String formattedTime,
					int selectionStart, int lengthDiff) {
				rounds_time_completed.removeTextChangedListener(this);
				if (cleanTime.length() <= 6) {
					rounds_time_completed.setText(formattedTime);
					rounds_time_completed.setSelection(selectionStart
							+ lengthDiff);
					mPreviousTime = formattedTime;
				} else {
					rounds_time_completed.setText(mPreviousTime);
					rounds_time_completed.setSelection(mPreviousTime.length());
				}
				rounds_time_completed.addTextChangedListener(this);
			}
		});
		// BORROWED CODE ENDS HERE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        named_text = v.findViewById(R.id.edit_wod_named);

        named = v.findViewById(R.id.edit_wod_named_text);
		named.setText(wod_container_edit.get_Workout_Name());

        comments = v.findViewById(R.id.edit_wod_comments);
		comments.setText(wod_container_edit.get_Comments());

        weight_only = v.findViewById(R.id.edit_wod_weightonly);

		if (!(wod_container_edit.get_Timed() == 0)) {
			// this is a timed container
			Log.v("edit parent dialog ", "timed container");
			wod_type_spinner.setSelection(0); // set the spinner to amrap

		} else if (!(wod_container_edit.get_Rounds() == 0)) {
			// this is a rounds container
			Log.v("edit parent dialog ", "rounds container");
			wod_type_spinner.setSelection(1); // set the spinner to rounds

		} else if (!(wod_container_edit.get_Staggered_Rounds().trim().length() == 0)) {
			Log.v("edit parent dialog ", "staggered  container");
			wod_type_spinner.setSelection(2); // set the spinner to staggered
												// rounds

		} else if (!(wod_container_edit.get_IsWeight_Workout() == "F")) {
			Log.v("edit parent dialog ", "weight only  container");
			wod_type_spinner.setSelection(3); // set the spinner to weight
		}

		wod_type_spinner.setOnItemSelectedListener(this);
		return (builder.setTitle("Edit Container").setView(v)
				.setPositiveButton(android.R.string.ok, this)
				.setNegativeButton(android.R.string.cancel, null).create());
	}

	public void go_to_other_click(View v) {
		Log.v("edit parent dialog", "on click");
		any_errors = false;
		switch (wod_type_spinner.getSelectedItemPosition()) {
		case 0:
			try {
				if (Integer.parseInt(timed.getText().toString()) == 0) {
					timed.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					wod_container_edit.set_Timed(Integer.parseInt(timed
							.getText().toString()));
					timed.setBackgroundColor(getTheme());
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Could not parse " + nfe);
				timed.setBackgroundColor(Color.RED);
				any_errors = true;
			}

			try {
				wod_container_edit.set_Rounds_Finished(Float
						.parseFloat(timed_type_rounds_completed.getText()
								.toString()));

			} catch (NumberFormatException nfe) {
				// i dont want to give an error for the rounds completed as the
				// user may not have executed the workout yet and may not know
				// what the final rounds were...so we just give it a value of 0
				// and allow him to continue on.
				System.out.println("Could not parse " + nfe);
				wod_container_edit.set_Rounds_Finished((float) 0);
			}

			Log.v("edit parent dialog", "amrap");
			wod_container_edit.set_Timed_Type(timed_type.getText().toString());
			wod_container_edit.set_Rounds(0);
			wod_container_edit.set_Finish_Time("");
			wod_container_edit.set_IsWeight_Workout("F");
			wod_container_edit.set_Staggered_Rounds("");
			break;

		case 1:

			try {
				if (Integer.parseInt(rounds.getText().toString()) == 0) {
					rounds.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					wod_container_edit.set_Rounds(Integer.parseInt(rounds
							.getText().toString()));
					rounds.setBackgroundColor(getTheme());
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Could not parse " + nfe);
				rounds.setBackgroundColor(Color.RED);
				any_errors = true;
			}
			Log.v("edit parent dialog", "rounds");
			wod_container_edit.set_Finish_Time(rounds_time_completed.getText()
					.toString());
			wod_container_edit.set_Timed(0);
			wod_container_edit.set_Timed_Type("");
			wod_container_edit.set_Rounds_Finished((float) 0);
			wod_container_edit.set_IsWeight_Workout("F");
			wod_container_edit.set_Staggered_Rounds("");
			break;
		case 2:
			// Staggered Rounds 21-15-9 type.
			Log.v("edit parent dialog", "staggered rounds");
			try {
				if (staggered_rounds.getText().toString().length() == 0) {
					staggered_rounds.setBackgroundColor(Color.RED);
					any_errors = true;
				} else {
					wod_container_edit.set_Staggered_Rounds(staggered_rounds.getText()
							.toString());
					staggered_rounds.setBackgroundColor(getTheme());
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Could not parse " + nfe);
				staggered_rounds.setBackgroundColor(Color.RED);
				any_errors = true;
			}
			
			wod_container_edit.set_Finish_Time(rounds_time_completed.getText()
					.toString());
			wod_container_edit.set_Rounds(0);
			wod_container_edit.set_Timed(0);
			wod_container_edit.set_Timed_Type("");
			wod_container_edit.set_Rounds_Finished((float) 0);
			wod_container_edit.set_Workout_Name("");
			wod_container_edit.set_IsWeight_Workout("F");
			break;

		case 3:

			try {
				wod_container_edit.set_IsWeight_Workout("T");
			} catch (NumberFormatException nfe) {
				System.out.println("Could not parse " + nfe);
			}
			Log.v("edit parent dialog", "weightonly accessory");
			wod_container_edit.set_Rounds(0);
			wod_container_edit.set_Finish_Time("");
			wod_container_edit.set_Timed(0);
			wod_container_edit.set_Timed_Type("");
			wod_container_edit.set_Rounds_Finished((float) 0);
			wod_container_edit.set_Workout_Name("");
			wod_container_edit.set_Staggered_Rounds("");
			break;

		}
		wod_container_edit.set_Workout_Name(named.getText().toString());
		wod_container_edit.set_Comments(comments.getText().toString());
		Log.v("Edit parent Dialog onclick", "comments are:"
				+ wod_container_edit.get_Comments() + ";");
		if (any_errors == true) {
			Toast.makeText(getActivity(), "Errors in selections...",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			Log.v("edit parent dialog", "heading back to listener");
			listener.send_back_edited_wod_container(wod_container_edit);
			dismiss();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Log.v("edit parent dialog", "handeling  on item selected listener");
		switch (position) {
		case 0:
			Log.v("edit parent dialog", "timed workout");
			staggered_rounds.setVisibility(View.GONE);
			staggered_rounds_text.setVisibility(View.GONE);
			timed.setVisibility(View.VISIBLE);
			timed_type.setVisibility(View.VISIBLE);
			timed_type_units.setVisibility(View.VISIBLE);
			timed_rounds_completed_text.setVisibility(View.VISIBLE);
			timed_type_rounds_completed.setVisibility(View.VISIBLE);
			named.setVisibility(View.VISIBLE);
			named_text.setVisibility(View.VISIBLE);
			rounds.setVisibility(View.GONE);
			rounds_text.setVisibility(View.GONE);
			rounds_time_completed_text.setVisibility(View.GONE);
			rounds_time_completed.setVisibility(View.GONE);
			weight_only.setVisibility(View.GONE);
			break;
		case 1:
			Log.v("edit parent dialog", "rounds type workout");
			staggered_rounds.setVisibility(View.GONE);
			staggered_rounds_text.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_type.setVisibility(View.GONE);
			timed_type_units.setVisibility(View.GONE);
			timed_rounds_completed_text.setVisibility(View.GONE);
			timed_type_rounds_completed.setVisibility(View.GONE);
			named.setVisibility(View.VISIBLE);
			named_text.setVisibility(View.VISIBLE);
			rounds.setVisibility(View.VISIBLE);
			rounds_text.setVisibility(View.VISIBLE);
			rounds_time_completed_text.setVisibility(View.VISIBLE);
			rounds_time_completed.setVisibility(View.VISIBLE);
			weight_only.setVisibility(View.GONE);
			break;
		case 2: // staggered rounds type of workout
			Log.v("edit parent dialog", "staggered round type workout");
			staggered_rounds.setVisibility(View.VISIBLE);
			staggered_rounds_text.setVisibility(View.VISIBLE);
			timed.setVisibility(View.GONE);
			timed_type.setVisibility(View.GONE);
			timed_type_units.setVisibility(View.GONE);
			timed_rounds_completed_text.setVisibility(View.GONE);
			timed_type_rounds_completed.setVisibility(View.GONE);
			named.setVisibility(View.VISIBLE);
			named_text.setVisibility(View.VISIBLE);
			rounds.setVisibility(View.GONE);
			rounds_text.setVisibility(View.GONE);
			rounds_time_completed_text.setVisibility(View.VISIBLE);
			rounds_time_completed.setVisibility(View.VISIBLE);
			weight_only.setVisibility(View.GONE);
			break;

		case 3: // accessory only workout
			Log.v("edit parent dialog", "accessory/weight only type workout");
			staggered_rounds.setVisibility(View.GONE);
			staggered_rounds_text.setVisibility(View.GONE);
			timed.setVisibility(View.GONE);
			timed_type.setVisibility(View.GONE);
			timed_type_units.setVisibility(View.GONE);
			timed_rounds_completed_text.setVisibility(View.GONE);
			timed_type_rounds_completed.setVisibility(View.GONE);
			named.setVisibility(View.GONE);
			named_text.setVisibility(View.GONE);
			rounds.setVisibility(View.GONE);
			rounds_text.setVisibility(View.GONE);
			rounds_time_completed_text.setVisibility(View.GONE);
			rounds_time_completed.setVisibility(View.GONE);
			weight_only.setVisibility(View.GONE);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	public void setListener(WOD_Calendar_Details listener) {
		Log.v("Edit parent dialog", "set listener");
		this.listener = listener;

	}

	// overridden as we don't want the have the dialog close automatically.
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually
							// called on the underlying dialog, so we have to do
							// it after this point
		AlertDialog d = (AlertDialog) getDialog();
		if (d != null) {
            Button positiveButton = d
					.getButton(Dialog.BUTTON_POSITIVE);
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
