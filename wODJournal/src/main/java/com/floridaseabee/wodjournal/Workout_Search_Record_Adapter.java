package com.floridaseabee.wodjournal;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Workout_Search_Record_Adapter extends ArrayAdapter<Search_Results_Holder> {
	private ArrayList<Search_Results_Holder> srh;

	public Workout_Search_Record_Adapter(Context ctxt, ArrayList<Search_Results_Holder> srh) {
		super(ctxt, R.layout.workout_search_row_layout, R.id.search_bigger_text, srh);
		Log.v("Workout Search Adapter", "constructor");
		this.srh = srh;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.v("workout search record adapter", "Get View");
		View row = super.getView(position, convertView, parent);

		// setting the view holder items here so save on processing time for
		// always looking up the rows
		Workout_Search_View_Holder holder = (Workout_Search_View_Holder) row.getTag();
		if (holder == null) {
			holder = new Workout_Search_View_Holder(row);
			row.setTag(holder);
		}
		
		holder.bigger_text.setText(srh.get(position).get_bigger_text());
		holder.medium_text.setText(srh.get(position).get_medium_text());
		holder.smaller_text.setText(srh.get(position).get_smaller_text());
		
		if (holder.medium_text.getText().toString().trim().length() == 0) {
			holder.medium_text.setVisibility(View.GONE);
		} else
			holder.medium_text.setVisibility(View.VISIBLE);

		if (holder.smaller_text.getText().toString().trim().length() == 0) {
			holder.smaller_text.setVisibility(View.GONE);
		} else
			holder.smaller_text.setVisibility(View.VISIBLE);

		holder.date.setText(" " + srh.get(position).get_date().get(Calendar.YEAR) + "-"
				+ srh.get(position).get_month(srh.get(position).get_date().get(Calendar.MONTH) + 1) + "-"
				+ srh.get(position).get_date().get(Calendar.DAY_OF_MONTH));

		return (row);

	}

}
