package com.floridaseabee.wodjournal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.Locale;

public class Countup_Fragment extends Fragment implements OnClickListener {
	static Boolean Arewecounting = false;
	static Boolean Arewecountingdown = false;
	static AutoResizeTextView ThreetwooneGO;
	static MyCountupTimer Countup_Timer_Activity;
	ThreetwooneCount Start_countdown;
	static Long MillstoPass = (long) 0;
	Calendar CountdownTime = Calendar.getInstance();
	static AutoResizeTextView tv;
	static ImageButton Reset;
	static ImageButton StartPausecount;
	static ImageButton Stopcount;
	static boolean Arewepaused = false;

	static Countup_Fragment newInstance() {
		Countup_Fragment frag = new Countup_Fragment();
		return (frag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater.inflate(R.layout.countup_timer_layout,
				container, false);

        tv = result
				.findViewById(R.id.countuptimer_textview);
		tv.resizeText();

        ThreetwooneGO = result
				.findViewById(R.id.timer_countup_text);
        Reset = result.findViewById(R.id.reset);
        StartPausecount = result
				.findViewById(R.id.startpausecount);
        Stopcount = result.findViewById(R.id.stopcount);
		Stopcount.setEnabled(false);
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

				Stopcount.setEnabled(true);
				Reset.setEnabled(false);
				StartPausecount.setEnabled(false);
				ThreetwooneGO.setVisibility(View.VISIBLE);
				Arewecountingdown = true;
				Arewecounting = false;
				MillstoPass = savedInstanceState.getLong("Current count");
				Log.v("rebuilding save state",
						" we were 3...2...1. rebuilding and re running");
				resume_start();
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
				StartPausecount.setImageResource(R.drawable.icon_play);
				ThreetwooneGO.setVisibility(View.GONE);
				Reset.setEnabled(false);
				Stopcount.setEnabled(true);
				StartPausecount.setEnabled(false);
				MillstoPass = savedInstanceState.getLong("Current count");
				Log.v("rebuilding current count",
						"" + savedInstanceState.getLong("Current count"));
				Log.v("rebuilding save state",
						" we were counting down. rebuilding and re running count up");
				resume_start();
			} else if ((savedInstanceState.getInt("Currentstatus") == 3)) {// still
				// counting
				// but
				// paused...
				Arewecountingdown = false;
				Arewecounting = true;
				MillstoPass = savedInstanceState.getLong("Current count");
				Reset.setEnabled(true);
				Stopcount.setEnabled(false);
				StartPausecount.setEnabled(true);
			}
		}

		return (result);
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
		} else if (Arewecountingdown == true && Arewecounting == false && Arewecounting == false) {
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
					Countup_Timer_Activity.whatisourcount()); // what is
			// the
			// current
			// count for
			// the
			// counter
			// (even if
			// its 0)
			outState.putLong("Current count",
					Countup_Timer_Activity.whatisourcount());
			outState.putInt("Currentstatus", 2);
			Log.v("Current count", "" + Countup_Timer_Activity.whatisourcount());

			Log.v("Fragment State", "2");
			Countup_Timer_Activity.Cancel();

		}else if(Arewecounting == true && Arewepaused == true ) {
			outState.putLong("Current count",
					Countup_Timer_Activity.whatisourcount());
			outState.putInt("Currentstatus", 3);
			Log.v("Current count",
					"" + Countup_Timer_Activity.whatisourcount());
			Log.v("Fragment State", "3");
		}
	}

	public static void start_counting() {
		Log.v("starting to count", "about to start");
		Arewecounting = true;
		Arewecountingdown = false;
		Arewepaused = false;
		Countup_Timer_Activity = new MyCountupTimer(0, 100, tv);
		Countup_Timer_Activity.Start();

	}

	public static void done_counting() {
		Reset.setEnabled(true);
		StartPausecount.setEnabled(true);
		Stopcount.setEnabled(false);
		Arewecounting = false;
		Arewecountingdown = false;
		Arewepaused = false;
		tv.setText("00:00:00");
		ThreetwooneGO.setVisibility(View.GONE);

	}

	private void updateLabel() {

		String entiretime;
		entiretime = String.format(Locale.US, "%tT", CountdownTime);
		tv.setText(entiretime);

	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.reset) {
			tv.setText("00:00:00");
			Reset.setEnabled(true);
			Stopcount.setEnabled(false);
			StartPausecount.setEnabled(true);
			Arewecounting = false;
			MillstoPass = (long) 0;
		} else if (id == R.id.startpausecount) {
			Log.v("passing in mills count", "" + MillstoPass);
			goto_startpausecount(MillstoPass);
		} else if (id == R.id.stopcount) {
			Log.v("Arewecountingdown status", "" + Arewecountingdown);
			Log.v("ARewecounting up status", "" + Arewecounting);
			if (Arewecountingdown == true) {
				Log.v("countup fragment", "stop 3..2..1.. countdown");
				Arewecountingdown = false;
				Arewecounting = false;
				ThreetwooneGO.setVisibility(View.GONE);
				ThreetwooneGO.setText(R.string.count_start);
				Start_countdown.Cancel();
			}
			if (Arewecountingdown == false && Arewecounting == true) {
				Log.v("countup fragment", "stop counting up");
				Arewepaused = true;
				MillstoPass = Countup_Timer_Activity.whatisourcount();
				Countup_Timer_Activity.Cancel();
			}
			Reset.setEnabled(true);
			StartPausecount.setEnabled(true);
			Stopcount.setEnabled(false);
		}
	}

	public void resume_start() {
		Log.v("inside resume start", "going onward down");
		if (Arewecounting == false && Arewecountingdown == true) {
			Log.v("resuming 3...2..1... count with", "" + MillstoPass);
			Stopcount.setEnabled(true);
			Reset.setEnabled(false);
			StartPausecount.setEnabled(false);
			ThreetwooneGO.setVisibility(View.VISIBLE);
			Start_countdown = new ThreetwooneCount(MillstoPass, 100,
					ThreetwooneGO, tv, false);
			Start_countdown.Start();

			ThreetwooneGO.setText(R.string.count_start);
		} else if (Arewecounting == true && Arewecountingdown == false) {
			Log.v("Resuming countup with ", "" + MillstoPass);
			StartPausecount.setImageResource(R.drawable.icon_play);
			ThreetwooneGO.setVisibility(View.GONE);
			Reset.setEnabled(false);
			Stopcount.setEnabled(true);
			StartPausecount.setEnabled(false);
			// No conversions are necessary before restarting after config
			// change as we already grabbed the 'milliseconds' version
			// of our count...so we just launch the count where it needs to be
			// and keep going.
			Log.v("status in fragment", "passing MillstoPass" + MillstoPass);
			Countup_Timer_Activity = new MyCountupTimer(MillstoPass, 100, tv);
			Log.v("status", "restarting the counting");
			Countup_Timer_Activity.Start();

		}
	}

	public void goto_startpausecount(Long currenttime) {

		if (Arewecounting == false && Arewecountingdown == false) {
			Stopcount.setEnabled(true);
			Reset.setEnabled(false);
			StartPausecount.setEnabled(false);
			ThreetwooneGO.setVisibility(View.VISIBLE);
			Arewecountingdown = true;

			Start_countdown = new ThreetwooneCount(3000, 100, ThreetwooneGO,
					tv, false);
			Start_countdown.Start();

			ThreetwooneGO.setText(R.string.count_start);

		} else {
			// For restarting the count, I don't want to redo the MillstoPass,
			// as we still have that in the variable from when we stopped
			// counting
			// and its much more accurate than the
			StartPausecount.setImageResource(R.drawable.icon_play);
			Reset.setEnabled(false);
			Stopcount.setEnabled(true);
			StartPausecount.setEnabled(false);
			Arewecounting = true; // set this so we know we are in the
									// middle of counting
			// we need to convert the HH:MM:SS format to milliseconds to
			// pass to the Timer Activity...it only accepts milliseconds.

			Log.v("Restart countdown",
					"restarting the counting from scratch from " + MillstoPass);

			Countup_Timer_Activity = new MyCountupTimer(MillstoPass, 100, tv);
			Countup_Timer_Activity.Start();

		}
	}

}
