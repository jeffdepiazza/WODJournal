package com.floridaseabee.wodjournal;

import android.os.Handler;
import android.util.Log;

public class MyCountupTimer implements Runnable {

	private long millisInPast = 0;
	private long countDownInterval;
	AutoResizeTextView Timer_edit;
	private volatile boolean IsStopped = false;
	static Handler handler;
	static Runnable counter;

	public MyCountupTimer(long pMillisInPast, long pCountDownInterval,
			AutoResizeTextView Artv) {
		this.millisInPast = pMillisInPast;
		this.countDownInterval = pCountDownInterval;

		Timer_edit = Artv;

	}

	public void Start() {

		handler = new Handler();
		counter = new Runnable() {

			@Override
			public void run() {

				if (IsStopped == false) {
					long sec = millisInPast / 1000;
					Timer_edit.setText(String.format("%02d:%02d:%02d",
							sec / 3600, (sec % 3600) / 60, (sec % 60)));
					millisInPast += countDownInterval;
					handler.postDelayed(this, countDownInterval);
				} else {
					IsStopped = false;
				}
			}
		};

		handler.postDelayed(counter, countDownInterval);
	}

	public void Cancel() {
		IsStopped = true;
		Timer_edit = null;
		handler.removeCallbacks(this);
		Log.v("status", "Main Timer count up cancelled");
		// this.ctxt = null;

	}

	public long whatisourcount() {
		return (millisInPast);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	
}
