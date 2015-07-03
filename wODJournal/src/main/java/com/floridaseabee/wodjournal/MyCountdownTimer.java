package com.floridaseabee.wodjournal;

import android.os.Handler;
import android.util.Log;

public class MyCountdownTimer implements Runnable {

	private long millisInFuture = 0;
	private long countDownInterval;
	AutoResizeTextView Timer_edit;
	private volatile boolean IsStopped = false;
	static Handler handler;
	static Runnable counter;

	public MyCountdownTimer(long pMillisInFuture, long pCountDownInterval,
			AutoResizeTextView Artv) {
		this.millisInFuture = pMillisInFuture;
		this.countDownInterval = pCountDownInterval;

		Timer_edit = Artv;

	}

	public void Start() {

		handler = new Handler();
		counter = new Runnable() {

			@Override
			public void run() {
			
				if (IsStopped == false) {
					if (millisInFuture <= 0) {
						Log.v("Inside MYCOUNTDOWNTIMER runnable", "runnable done counting");
						Countdown_Fragment.done_counting();
					} else {
						long sec = millisInFuture / 1000;
						Timer_edit.setText(String.format("%02d:%02d:%02d",
								sec / 3600, (sec % 3600) / 60, (sec % 60)));
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
		Timer_edit = null;
		handler.removeCallbacks(this);
		Log.v("status", "Main Timer cancelled");
		// this.ctxt = null;

	}
	public long whatisourcount(){
		return(millisInFuture);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
