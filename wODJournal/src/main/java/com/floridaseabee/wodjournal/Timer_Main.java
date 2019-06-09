package com.floridaseabee.wodjournal;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.WindowManager;

public class Timer_Main extends FragmentActivity {
	ViewPager pager;
	Timer_Pager mAdapter;
	FragmentManager fm;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String outputvalue;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_pager);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mAdapter = new Timer_Pager(this, getSupportFragmentManager());
		if (savedInstanceState != null) {
			outputvalue = savedInstanceState.getString("CurrentTimer");
			Log.v("current page viewed",
					"" + savedInstanceState.getInt("pageItem"));

		}
        pager = findViewById(R.id.timer_pager_display);
		pager.setAdapter(mAdapter);

	}

	public static String getTitle(Context ctxt, int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("pageItem", pager.getCurrentItem());
		//outState.putString("CountDownFragment",
			//	Timer_Pager.makeFragmentName(R.id.timer_pager_display, 0));
		//outState.putString("CountUpFragment",
				//Timer_Pager.makeFragmentName(R.id.timer_pager_display, 1));

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}
}