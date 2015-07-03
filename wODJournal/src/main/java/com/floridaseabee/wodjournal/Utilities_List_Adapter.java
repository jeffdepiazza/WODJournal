package com.floridaseabee.wodjournal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Utilities_List_Adapter extends ArrayAdapter<String> {
	private String[] title;
	private String[] summary;

	public Utilities_List_Adapter(Context ctxt, String[] title, String[] summary) {
		super(ctxt, R.layout.utilities_row_layout, R.id.utilities_title, title);
		this.title = title;
		this.summary = summary;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = super.getView(position, convertView, parent);
		TextView title_tv = (TextView) row.findViewById(R.id.utilities_title);
		title_tv.setText(title[position]);

		TextView summary_tv = (TextView) row.findViewById(R.id.utilities_summary);
		summary_tv.setText(summary[position]);

		return row;
	}

}
