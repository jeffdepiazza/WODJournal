package com.floridaseabee.wodjournal;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Workout_Search_Display_Fragment extends Fragment {

	private ProgressBar pg;
	private TextView tv;
	private ListView lv;
	private LayoutInflater inflater;
	private static Workout_Search_Record_Adapter list_adapter;
	private ArrayList<Search_Results_Holder> search_holder;

	public static Workout_Search_Display_Fragment newInstance() {

		Workout_Search_Display_Fragment f = new Workout_Search_Display_Fragment();
		return f;

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("record search display fragment", "Inflating display view");
		this.inflater = inflater;
		View v = this.inflater.inflate(R.layout.personal_records, container,
				false);
		lv = (ListView) v.findViewById(R.id.PR_listview);
		pg = (ProgressBar) v.findViewById(R.id.Load_Personal_Records);
		tv = (TextView) v.findViewById(R.id.No_Personal_Records);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Calendar date = null;
				date = Calendar.getInstance();
				date = search_holder.get(position)
							.get_date();
			
				Log.v("date being passed:", "" + date.getTime());
				Intent Launch_details = new Intent(getActivity()
						.getApplicationContext(), WOD_Calendar_Details.class);
				Launch_details.putExtra("YEAR", date.get(Calendar.YEAR));
				Log.v("date being passed year", "" + date.get(Calendar.YEAR));
				// due to stupidity in the java api, month 0 is January, so we
				// had to increase by one in order to get the correct month with what
				// the wod display activity is expecting.
				Launch_details.putExtra("MONTH", date.get(Calendar.MONTH) + 1);
				Log.v("date being passed month", "" + date.get(Calendar.MONTH));
				Launch_details.putExtra("DAY", date.get(Calendar.DAY_OF_MONTH));
				Log.v("date being passed day of monnth",
						"" + date.get(Calendar.DAY_OF_MONTH));
				startActivity(Launch_details);
			}
		});

		return v;

	}

	public void no_data() {
		pg.setVisibility(View.GONE);
		tv.setVisibility(View.VISIBLE);
		lv.setVisibility(View.GONE);
	}

	public void is_data() {
		lv.setVisibility(View.VISIBLE);
		pg.setVisibility(View.VISIBLE);
		tv.setVisibility(View.GONE);
		
	}

	public void handover_data(ArrayList<Search_Results_Holder> search_holder) {
		this.search_holder = search_holder;
		pg.setVisibility(View.GONE);
		list_adapter = new Workout_Search_Record_Adapter(getActivity(),
				this.search_holder);
		lv.setAdapter(list_adapter);

	}

}
