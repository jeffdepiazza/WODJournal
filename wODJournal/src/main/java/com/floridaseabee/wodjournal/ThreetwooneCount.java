package com.floridaseabee.wodjournal;

import android.os.Handler;
import android.util.Log;
import android.view.View;

public class ThreetwooneCount implements Runnable {

	private long millisInFuture;
	private long countDownInterval;
	AutoResizeTextView Countdown_edit;
	AutoResizeTextView Countdown_time;
	private volatile boolean IsStopped = false;
	static Handler handler;
	static Runnable counter;
	private boolean UporDown;

	public ThreetwooneCount(long pMillisInFuture, long pCountDownInterval,
			AutoResizeTextView ThreetwooneGo, AutoResizeTextView TimetoCount,
			Boolean UporDown) {
		this.millisInFuture = pMillisInFuture;
		this.countDownInterval = pCountDownInterval;
		Countdown_edit = ThreetwooneGo;
		this.Countdown_time = TimetoCount;
		this.UporDown = UporDown;
	}

	public void Start() {
		handler = new Handler();
		Log.v("status", "starting");
		counter = new Runnable() {

			@Override
			public void run() {
				if (IsStopped == false) {
					if (millisInFuture <= 0) {
						Countdown_edit.setVisibility(View.GONE);
						if (UporDown == true) { // False for counting down. True
												// for counting up
							Countdown_Fragment.start_counting(Countdown_time);
							Log.v("Three two one count",
									"at 0 going to fragment to count down");
						} else if (UporDown == false) {
							Countup_Fragment.start_counting();
							Log.v(" Three two one count", "at 0, goiing to count up now");
						}
					} else {
						long sec = millisInFuture / 1000;
						if (sec < 3 && sec >= 2) {
							Countdown_edit.setText("3");
						}
						if (sec < 2 && sec >= 1) {
							Countdown_edit.setText("2");
						}
						if (sec < 1) {
							Countdown_edit.setText("1");
						}
						// Log.v("three two one go", "" + millisInFuture);
						millisInFuture -= countDownInterval;
						handler.postDelayed(this, countDownInterval);
					}
				} else {
					IsStopped = false;
				}

			}

		};

		handler.postDelayed(counter, countDownInterval);
	}

	public void Cancel() {
		IsStopped = true;
		Countdown_edit = null;
		handler.removeCallbacks(this);
	}

	public long whatisourcount() {
		return (millisInFuture);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
