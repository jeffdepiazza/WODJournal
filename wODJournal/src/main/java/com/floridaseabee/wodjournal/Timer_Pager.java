package com.floridaseabee.wodjournal;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Log;

public class Timer_Pager extends FragmentPagerAdapter{
	Context ctxt = null;
	public Timer_Pager(Context ctxt, FragmentManager fm) {
		super(fm);
		this.ctxt = ctxt;
	}
	

	@Override
	public Fragment getItem(int position) {
		
		switch (position) {
		case 0: {
			Log.v("Timer FragmentPagerAdapter", "creating new Countdown from scratch");
			return(Countdown_Fragment.newInstance());
		}
		case 1:
			Log.v("Timer FragmentPagerAdapter", "creating new Countup from scratch");
			return(Countup_Fragment.newInstance());
		}
		return null;
	}
	
	@Override
	public String getPageTitle(int position) {

		switch (position) {
		case 0: 
			return(String.format(ctxt.getString(R.string.Countdown_label)));
		case 1: 
			return(String.format(ctxt.getString(R.string.Countup_label)));
		}
		return null;
	}

	@Override
	public int getCount() {
		// doing only three pages, right now...one for countdown, one for countup, and one for Tabata.
		return 2;
	}
	
	public static String makeFragmentName(int viewId, int position)
	{
	     return "android:switcher:" + viewId + ":" + position;
	}
}
	