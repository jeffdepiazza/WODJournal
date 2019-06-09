package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;

public class Analytics_Display_Fragment extends Fragment {

	private Spinner rep_max_spinner;
	private LayoutInflater inflater;
	private GraphView gv;
	private Button go_graph;
	
	protected static Analytics_Display_Fragment newInstance() {
		Analytics_Display_Fragment f = new Analytics_Display_Fragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.inflater = inflater;
		View v = this.inflater.inflate(R.layout.analytics_display, container, false);
		
		Log.v("Analytics Display Fragment", "setting the display for analytics display");
		
		return v;

	}

	public void handoff_rep_max_items(ArrayList<String> max_rep_list_items) {
		Log.v("Analytics Display Fragment", "got the adapter, now setting with count of " + max_rep_list_items.size());
		ArrayAdapter<String> spinner_pulldown = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
				max_rep_list_items);
		spinner_pulldown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rep_max_spinner.setAdapter(spinner_pulldown);
	}
}
