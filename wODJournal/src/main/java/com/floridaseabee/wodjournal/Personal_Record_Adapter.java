package com.floridaseabee.wodjournal;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class Personal_Record_Adapter extends ArrayAdapter<Personal_Record_Holder> {
	private ArrayList<Personal_Record_Holder> pcr;

	public Personal_Record_Adapter(Context ctxt, ArrayList<Personal_Record_Holder> pcr) {
		super(ctxt, R.layout.personal_record_row_layout, R.id.pr_movement, pcr);
		Log.v("PR Adapter", "constructor");
		this.pcr = pcr;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = super.getView(position, convertView, parent);

		// setting the view holder items here so save on processing time for
		// always looking up the rows
		Personal_Record_View_Holder holder = (Personal_Record_View_Holder) row.getTag();
		if (holder == null) {
			holder = new Personal_Record_View_Holder(row);
			row.setTag(holder);
		}

		holder.movement.setText(pcr.get(position).get_movement());
		holder.weight.setText(" " + pcr.get(position).get_weight());
		holder.weight_units.setText(pcr.get(position).get_weight_units());
		holder.date.setText(" " + pcr.get(position).get_date());
		holder.comments.setText(pcr.get(position).get_comments());
		if (holder.comments.getText().toString().trim().length() == 0) {
			holder.comments.setVisibility(View.GONE);
		} else {
			holder.comments.setVisibility(View.VISIBLE);
		}

		return (row);

	}

}
