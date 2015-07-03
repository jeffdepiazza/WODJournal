package com.floridaseabee.wodjournal;

import android.os.CountDownTimer;

public class JournalCountdownTimer extends CountDownTimer {
	Countdown_Fragment ctxt;
	AutoResizeTextView Timer_edit;

	public JournalCountdownTimer(Long millstoPass, long interval,
			Countdown_Fragment ctxt, AutoResizeTextView Artv) {

		super(millstoPass, interval);
		this.ctxt = ctxt;
		Timer_edit = Artv;
	}

	@Override
	public void onFinish() {
		Timer_edit.setText("00:00:00");
		Countdown_Fragment.done_counting();

	}

	@Override
	public void onTick(long leftTimeInMilliseconds) {

		long durationSeconds = leftTimeInMilliseconds / 1000;
		// Timer_edit.setText("" + durationSeconds / 3600 + ":" +
		// durationSeconds % 3600 / 60 + ":" +durationSeconds % 60);
		Timer_edit.setText(String.format("%02d:%02d:%02d",
				durationSeconds / 3600, (durationSeconds % 3600) / 60,
				(durationSeconds % 60)));
		if (leftTimeInMilliseconds < 1500) {
			Timer_edit.setText("00:00:00");
		}
	}

	public void Safecancel() {
		Timer_edit = null;
		super.cancel();
	}

}
