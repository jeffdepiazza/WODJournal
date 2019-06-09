//Jeff DePiazza
package com.floridaseabee.wodjournal;

import android.app.TimePickerDialog;
import android.graphics.LightingColorFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Countdown_Fragment extends Fragment implements OnClickListener {
	Calendar CountdownTime = Calendar.getInstance();
	static AutoResizeTextView tv;
	static ImageButton HoursUp;
	static ImageButton HoursDown;
	static ImageButton MinutesUp;
	static ImageButton MinutesDown;
	static ImageButton SecondsUp;
	static ImageButton SecondsDown;
	static ImageButton Reset;
	static ImageButton StartPausecount;
	static ImageButton Stopcount;
	static Boolean Arewecounting = false;
	static Boolean Arewecountingdown = false;
	static AutoResizeTextView ThreetwooneGO;
	static MyCountdownTimer Countdown_Timer_Activity;
	ThreetwooneCount Start_countdown;
	static Long MillstoPass = (long) 0;
	static boolean Arewepaused = false;
	
	static Countdown_Fragment newInstance() {

		Countdown_Fragment frag = new Countdown_Fragment();
		return (frag);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putCharSequence("TimerValue", tv.getText());
		Log.v("3..2..1. status", "" + Arewecountingdown);
		Log.v("Actually counting", "" + Arewecounting);
		if (Arewecountingdown == false && Arewecounting == false) {
			outState.putInt("Currentstatus", 0); // The fragment wasn't counting
													// at all, so its in state
													// ZERO
			Log.v("CountdownFragment status", "Not counting at all, state is 0");
		} else if (Arewecountingdown == true && Arewecounting == false) {
			// Using the same CurrentStatus Integer for both status here as I'm
			// using the Arewecounting and Arewecounting down
			// to tell me where we are at in the status....so If i know that
			// countingdown is TRUE...I'm doing the 3..2...1.. count
			// and I know that the Current count will be for the 3..2..1.. to
			// resume..and we go from there.
			//
			outState.putLong("Current count", Start_countdown.whatisourcount()); //
			outState.putInt("Currentstatus", 1);
			Log.v("what is our 3.2.1 count",
					"" + Start_countdown.whatisourcount());
			Log.v("Fragment State", "1");
			Start_countdown.Cancel();
		} else if (Arewecountingdown == false && Arewecounting == true && Arewepaused == false) {
			outState.putLong("Currentcount",
					Countdown_Timer_Activity.whatisourcount()); // what is
			// the
			// current
			// count for
			// the
			// counter
			// (even if
			// its 0)
			outState.putLong("Current count",
					Countdown_Timer_Activity.whatisourcount());
			outState.putInt("Currentstatus", 2);
			Log.v("Current count",
					"" + Countdown_Timer_Activity.whatisourcount());

			Log.v("Fragment State", "2");
			Countdown_Timer_Activity.Cancel();

		} else if(Arewecounting == true && Arewepaused == true ) {
			outState.putLong("Current count",
					Countdown_Timer_Activity.whatisourcount());
			outState.putInt("Currentstatus", 3);
			Log.v("Current count",
					"" + Countdown_Timer_Activity.whatisourcount());
			Log.v("Fragment State", "3");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setRetainInstance(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater.inflate(R.layout.countdown_timer_layout,
				container, false);
        tv = result.findViewById(R.id.timer_textview);

		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new TimePickerDialog(getActivity(), t, 0, 0, true).show();
			}
		});
		tv.resizeText();

        ThreetwooneGO = result
				.findViewById(R.id.timer_countdown_text);

        HoursUp = result.findViewById(R.id.hours_up);
        HoursDown = result.findViewById(R.id.hours_down);
        MinutesUp = result.findViewById(R.id.minutes_up);
        MinutesDown = result.findViewById(R.id.minutes_down);
        SecondsUp = result.findViewById(R.id.seconds_up);
        SecondsDown = result.findViewById(R.id.seconds_down);
        Reset = result.findViewById(R.id.reset);
        StartPausecount = result
				.findViewById(R.id.startpausecount);
        Stopcount = result.findViewById(R.id.stopcount);
		Stopcount.setEnabled(false);
		HoursUp.setOnClickListener(this);
		HoursDown.setOnClickListener(this);
		SecondsUp.setOnClickListener(this);
		SecondsDown.setOnClickListener(this);
		MinutesUp.setOnClickListener(this);
		MinutesDown.setOnClickListener(this);
		Reset.setOnClickListener(this);
		StartPausecount.setOnClickListener(this);
		Stopcount.setOnClickListener(this);

		if (savedInstanceState != null) { // if we have a good Bundle coming in,
											// we want to setup the
			// state of the fragment now...
			tv.setText(savedInstanceState.getCharSequence("TimerValue"));
			if (savedInstanceState.getInt("Currentstatus") == 0) {
				// not counting at all, nothing much to do
				Arewecountingdown = false;
				Arewecounting = false;
				Arewepaused = false;
				HoursUp.setEnabled(true);
				HoursUp.getBackground().setColorFilter(
						new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
				HoursDown.setEnabled(true);
				MinutesUp.setEnabled(true);
				MinutesDown.setEnabled(true);
				SecondsUp.setEnabled(true);
				SecondsDown.setEnabled(true);
				Reset.setEnabled(true);
				StartPausecount.setEnabled(true);
				Stopcount.setEnabled(false);
				ThreetwooneGO.setVisibility(View.GONE);
			}
			if ((savedInstanceState.getInt("Currentstatus") == 1)) {
				// We were in the middle of our 3..2..1.. count, so we set the
				// Counting down to TRUE
				// and Counting to FALSE, and pass in the MillstoPass as the
				// current 3..2..1.. count
				// I don't have to reset the 3..2..1.. text count, as the
				// Threetowone Class will still do that
				// for me when I pass it the count...
				HoursUp.setEnabled(false);
				HoursUp.getBackground().setColorFilter(
						new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
				HoursDown.setEnabled(false);
				MinutesUp.setEnabled(false);
				MinutesDown.setEnabled(false);
				SecondsUp.setEnabled(false);
				SecondsDown.setEnabled(false);
				Stopcount.setEnabled(true);
				Reset.setEnabled(false);
				StartPausecount.setEnabled(false);
				ThreetwooneGO.setVisibility(View.VISIBLE);
				Arewecountingdown = true;
				Arewecounting = false;
				Arewepaused = false;
				MillstoPass = savedInstanceState.getLong("Current count");
				Log.v("rebuilding save state",
						" we were 3...2...1. rebuilding and re running");
				resume_start(MillstoPass);
			}
			if ((savedInstanceState.getInt("Currentstatus") == 2)) {
				// We were in the middle of our normal count down, so we set the
				// Counting to FALSE
				// and Counting to FALSE, and pass in the MillstoPass as the
				// current count down
				// I don't have to reset the TextViews as they will be
				// automatically set by the OS
				// when the orientation occurs.
				Arewecountingdown = false;
				Arewecounting = true;
				Arewepaused = false;
				StartPausecount.setImageResource(R.drawable.icon_play);
				ThreetwooneGO.setVisibility(View.GONE);
				HoursUp.setEnabled(false);
				HoursUp.getBackground().setColorFilter(
						new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
				HoursDown.setEnabled(false);
				MinutesUp.setEnabled(false);
				MinutesDown.setEnabled(false);
				SecondsUp.setEnabled(false);
				SecondsDown.setEnabled(false);
				Reset.setEnabled(false);
				Stopcount.setEnabled(true);
				StartPausecount.setEnabled(false);
				MillstoPass = savedInstanceState.getLong("Current count");
				Log.v("rebuilding current count",
						"" + savedInstanceState.getLong("Current count"));
				Log.v("rebuilding save state",
						" we were counting down. rebuilding and re running");
				resume_start(MillstoPass);
			} else if ((savedInstanceState.getInt("Currentstatus") == 3)) {// still
																	// counting
																	// but
																	// paused...
				Arewecountingdown = false;
				Arewecounting = true;
				MillstoPass = savedInstanceState.getLong("Current count");
				HoursUp.setEnabled(false);
				HoursUp.getBackground().setColorFilter(
						new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
				HoursDown.setEnabled(false);
				MinutesUp.setEnabled(false);
				MinutesDown.setEnabled(false);
				SecondsUp.setEnabled(false);
				SecondsDown.setEnabled(false);
				Reset.setEnabled(true);
				Stopcount.setEnabled(false);
				StartPausecount.setEnabled(true);
			}
		}

		return (result);
	}

	public void chooseTime(View v) {
		new TimePickerDialog(getActivity(), t, 0, 0, true).show();
	}

	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			CountdownTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			CountdownTime.set(Calendar.MINUTE, minute);
			CountdownTime.set(Calendar.SECOND, 0);
			CountdownTime.set(Calendar.MILLISECOND, 0);

			updateLabel();
		}
	};

	private void updateLabel() {

		String entiretime;
		entiretime = String.format("%tT", CountdownTime);
		tv.setText(entiretime);

	}

	@Override
	public void onClick(View v) {
		String originaltext = (String) tv.getText();
		String catchnumbers;
		int id = v.getId();
		if (id == R.id.hours_up) {
			// convert the string of the text view to character array as that is
			// the only way valueOf works
			catchnumbers = String.valueOf(originaltext.toCharArray(), 0, 2);
			catchnumbers = number_up(catchnumbers, true);
			tv.setText(catchnumbers
					+ String.valueOf(originaltext.toCharArray(), 2, 6));
		} else if (id == R.id.hours_down) {
			// convert the string of the text view to character array as that is
			// the only way valueOf works
			catchnumbers = String.valueOf(originaltext.toCharArray(), 0, 2);
			catchnumbers = number_down(catchnumbers, true);
			tv.setText(catchnumbers
					+ String.valueOf(originaltext.toCharArray(), 2, 6));
		} else if (id == R.id.minutes_up) {
			// convert the string of the text view to character array as that is
			// the only way valueOf works
			catchnumbers = String.valueOf(originaltext.toCharArray(), 3, 2);
			catchnumbers = number_up(catchnumbers, false);
			tv.setText(String.valueOf(originaltext.toCharArray(), 0, 3)
					+ catchnumbers
					+ String.valueOf(originaltext.toCharArray(), 5, 3));
		} else if (id == R.id.minutes_down) {
			// convert the string of the text view to character array as that is
			// the only way valueOf works
			catchnumbers = String.valueOf(originaltext.toCharArray(), 3, 2);
			catchnumbers = number_down(catchnumbers, false);
			tv.setText(String.valueOf(originaltext.toCharArray(), 0, 3)
					+ catchnumbers
					+ String.valueOf(originaltext.toCharArray(), 5, 3));
		} else if (id == R.id.seconds_up) {
			// convert the string of the text view to character array as that is
			// the only way valueOf works
			catchnumbers = String.valueOf(originaltext.toCharArray(), 6, 2);
			catchnumbers = number_up(catchnumbers, false);
			// setup the text on the timer display to be 00:00: new seconds...we
			// grab the first part of the original text and append the new
			// seconds
			tv.setText(String.valueOf(originaltext.toCharArray(), 0, 6)
					+ catchnumbers);
		} else if (id == R.id.seconds_down) {
			// convert the string of the text view to character array as that is
			// the only way valueOf works
			catchnumbers = String.valueOf(originaltext.toCharArray(), 6, 2);
			catchnumbers = number_down(catchnumbers, false);
			// setup the text on the timer display to be 00:00: new seconds...we
			// grab the first part of the original text and append the new
			// seconds
			tv.setText(String.valueOf(originaltext.toCharArray(), 0, 6)
					+ catchnumbers);
		} else if (id == R.id.reset) {
			tv.setText("00:00:00");
			HoursUp.setEnabled(true);
			HoursDown.setEnabled(true);
			MinutesUp.setEnabled(true);
			MinutesDown.setEnabled(true);
			SecondsUp.setEnabled(true);
			SecondsDown.setEnabled(true);
			Reset.setEnabled(true);
			Stopcount.setEnabled(false);
			StartPausecount.setEnabled(true);
			Arewecounting = false;
			MillstoPass = (long) 0;
		} else if (id == R.id.startpausecount) {
			if (tv.getText() == "00:00:00") {
				Toast.makeText(getActivity().getApplicationContext(),
						"You must pick a time first!", Toast.LENGTH_SHORT)
						.show();
			}
			goto_startpausecount(MillstoPass);
		} else if (id == R.id.stopcount) {
			if (Arewecountingdown == true) {
				Log.v("countdown fragment", "stop countdown");
				Arewecountingdown = false;
				Arewecounting = false;
				Arewepaused = false;
				ThreetwooneGO.setVisibility(View.GONE);
				ThreetwooneGO.setText(R.string.count_start);
				Start_countdown.Cancel();

			}
			if (Arewecountingdown == false && Arewecounting == true) {
				Log.v("countdown fragment", "stop counting");
				MillstoPass = Countdown_Timer_Activity.whatisourcount();
				Countdown_Timer_Activity.Cancel();
				Arewepaused = true;

			}
			Reset.setEnabled(true);
			StartPausecount.setEnabled(true);
			Stopcount.setEnabled(false);
		}

	}

	public void resume_start(Long mMillstoPass) {

		if (Arewecounting == false && Arewecountingdown == true && Arewepaused == false) {
			HoursUp.setEnabled(false);
			HoursUp.getBackground().setColorFilter(
					new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
			HoursDown.setEnabled(false);
			MinutesUp.setEnabled(false);
			MinutesDown.setEnabled(false);
			SecondsUp.setEnabled(false);
			SecondsDown.setEnabled(false);
			Stopcount.setEnabled(true);
			Reset.setEnabled(false);
			StartPausecount.setEnabled(false);
			ThreetwooneGO.setVisibility(View.VISIBLE);
			Start_countdown = new ThreetwooneCount(MillstoPass, 100,
					ThreetwooneGO, tv, true);
			Start_countdown.Start();

			ThreetwooneGO.setText(R.string.count_start);
		} else if (Arewecounting == true && Arewecountingdown == false && Arewepaused == false) {
			StartPausecount.setImageResource(R.drawable.icon_play);
			ThreetwooneGO.setVisibility(View.GONE);
			HoursUp.setEnabled(false);
			HoursUp.getBackground().setColorFilter(
					new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
			HoursDown.setEnabled(false);
			MinutesUp.setEnabled(false);
			MinutesDown.setEnabled(false);
			SecondsUp.setEnabled(false);
			SecondsDown.setEnabled(false);
			Reset.setEnabled(false);
			Stopcount.setEnabled(true);
			StartPausecount.setEnabled(false);
			// No conversions are necessary before restarting after config
			// change as we already grabbed the 'milliseconds' version
			// of our count...so we just launch the count where it needs to be
			// and keep going.

			Countdown_Timer_Activity = new MyCountdownTimer(MillstoPass, 100,
					tv);
			Log.v("status", "restarting the counting");
			Countdown_Timer_Activity.Start();

		}
	}

	public void goto_startpausecount(Long currenttime) {

		if (Arewecounting == false && Arewecountingdown == false) {
			HoursUp.setEnabled(false);
			HoursUp.getBackground().setColorFilter(
					new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
			HoursDown.setEnabled(false);
			MinutesUp.setEnabled(false);
			MinutesDown.setEnabled(false);
			SecondsUp.setEnabled(false);
			SecondsDown.setEnabled(false);
			Stopcount.setEnabled(true);
			Reset.setEnabled(false);
			StartPausecount.setEnabled(false);
			ThreetwooneGO.setVisibility(View.VISIBLE);
			Arewecountingdown = true;
			Arewepaused = false;
			Start_countdown = new ThreetwooneCount(3000, 100, ThreetwooneGO,
					tv, true);
			Start_countdown.Start();

			ThreetwooneGO.setText(R.string.count_start);

			// set this so we know we are in the
			// middle of counting
			// we need to convert the HH:MM:SS format to milliseconds to
			// pass to the Timer Activity...it only accepts milliseconds.

		} else {
			// For restarting the count, I don't want to redo the MillstoPass,
			// as we still have that in the variable from when we stopped
			// counting
			// and its much more accurate than the
			StartPausecount.setImageResource(R.drawable.icon_play);
			HoursUp.setEnabled(false);
			HoursUp.getBackground().setColorFilter(
					new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
			HoursDown.setEnabled(false);
			MinutesUp.setEnabled(false);
			MinutesDown.setEnabled(false);
			SecondsUp.setEnabled(false);
			SecondsDown.setEnabled(false);
			Reset.setEnabled(false);
			Stopcount.setEnabled(true);
			StartPausecount.setEnabled(false);
			Arewepaused = false;
			Arewecounting = true; // set this so we know we are in the
									// middle of counting
			// we need to convert the HH:MM:SS format to milliseconds to
			// pass to the Timer Activity...it only accepts milliseconds.

			Log.v("Restart countdown",
					"restarting the counting from scratch from " + MillstoPass);

			Countdown_Timer_Activity = new MyCountdownTimer(MillstoPass, 100,
					tv);
			Countdown_Timer_Activity.Start();

		}
	}

	public static void done_counting() {

		HoursUp.setEnabled(true);
		HoursUp.getBackground().setColorFilter(
				new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
		HoursDown.setEnabled(true);
		MinutesUp.setEnabled(true);
		MinutesDown.setEnabled(true);
		SecondsUp.setEnabled(true);
		SecondsDown.setEnabled(true);
		Reset.setEnabled(true);
		StartPausecount.setEnabled(true);
		Stopcount.setEnabled(false);
		Arewecounting = false;
		Arewecountingdown = false;
		Arewepaused = false;
		tv.setText("00:00:00");
		ThreetwooneGO.setVisibility(View.GONE);

	}

	public static void start_counting(AutoResizeTextView atv) {
		String originaltext = (String) atv.getText();
		Arewecounting = true;
		Arewecountingdown = false;
		Arewepaused = false;
		MillstoPass = Long.parseLong(String.valueOf(originaltext.toCharArray(),
				0, 2)) * 3600000;
		MillstoPass = MillstoPass
				+ Long.parseLong(String.valueOf(originaltext.toCharArray(), 3,
						2)) * 60000;
		MillstoPass = MillstoPass
				+ Long.parseLong(String.valueOf(originaltext.toCharArray(), 6,
						2)) * 1000;
		Log.v("starting to count", "about to start");
		Countdown_Timer_Activity = new MyCountdownTimer(MillstoPass, 100, tv);
		Countdown_Timer_Activity.Start();

	}

	private String number_up(String str, Boolean limitcount) {
		Integer counting;
		counting = Integer.parseInt(str);
		if (counting == 59 && limitcount == false) {
			return ("00");
		} else if (counting >= 23 && limitcount == true) {
			return ("00");
		} else if (counting >= 0 && counting <= 8) {
			counting++;
			return ("0" + counting);
		} else if (counting > 8) {
			counting++;
			return ("" + counting);
		}
		return null;
	}

	private String number_down(String str, Boolean limitcount) {
		Integer counting;
		counting = Integer.parseInt(str);
		if (counting == 00 && limitcount == false) {
			return ("59");
		} else if (counting == 00 && limitcount == true) {
			return ("23");
		} else if (counting >= 1 && counting <= 10) {
			counting--;
			return ("0" + counting);
		} else if (counting >= 11) {
			counting--;
			return ("" + counting);
		}
		return null;
	}

}
