package com.floridaseabee.wodjournal;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		GridView g = (GridView) findViewById(R.id.grid);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemClickListener(this);

		//
		if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			Log.v(" Main Activity", "Setting up strict mode");
			StrictMode.setThreadPolicy(buildPolicy());
		}
	}

	// sets up the strictmode that helps us look for bad coding practices and it
	// will push out on
	// the logcat anything that is slowing the activity down, see busy coders
	// guide under assets,files and data
	// parsing. REMOVE IN OPERATIONAL CODE!!!
	private StrictMode.ThreadPolicy buildPolicy() {
		return (new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		int itemId = item.getItemId();
		if (itemId == R.id.action_settings) {
			startActivity(new Intent(this, Preferences_Edit.class));
			return (true);
		} else if (itemId == R.id.action_utilities) {
			startActivity(new Intent(this, Utilities_Activity.class));
			return (true);
		}
		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

		switch (position) {
		case 0:
			Intent Time = new Intent(this, Timer_Main.class);
			startActivity(Time);
			break;
		case 1:
			Intent Analytics = new Intent(this, Analytics_Main.class);
			startActivity(Analytics);
			break;
		case 2:
			Intent WOD_C = new Intent(this, WOD_Calendar_Activity.class);
			startActivity(WOD_C);
			break;
		case 3:
			Intent PRs = new Intent(this, Personal_Records_Activity.class);
			startActivity(PRs);
			break;

		case 4:
			Intent WS = new Intent(this, Workout_Search_Activity.class);
			startActivity(WS);
			break;
		}

	}

}
