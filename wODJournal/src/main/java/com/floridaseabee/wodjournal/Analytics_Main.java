package com.floridaseabee.wodjournal;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class Analytics_Main extends Activity {

	public Analytics_Holder_Fragment ahf;
	private Spinner rep_max_spinner;
	private LinearLayout gv;
	private Button go_graph;
	private TextView nothingToDisplay;
	private ArrayList<String> max_rep_list_items;
	private GraphView graphView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
			ahf = Analytics_Holder_Fragment.newInstance();
			getFragmentManager().beginTransaction().add(android.R.id.content, ahf, "ahf_search").commit();

		} else {
			Log.v("activity", "non-null holder");
			ahf = (Analytics_Holder_Fragment) getFragmentManager().findFragmentByTag("ahf_search");
		}
		setContentView(R.layout.analytics_display);
		final Analytics_DB_Listener analytics_listener = new Analytics_DB_Listener() {
			public void return_rep_max_items(ArrayList<String> rep_max_items) {
				handoff_rep_max_items(rep_max_items);
			}

			public void return_analytics_data_to_activity(ArrayList<Analytics_Data_Holder> analytics_data) {
				display_the_graph(analytics_data);
			}
		};

		ahf.setListener(analytics_listener);

		nothingToDisplay = (TextView) findViewById(R.id.analytics_nothing_to_display);
		rep_max_spinner = (Spinner) findViewById(R.id.analytics_rep_max_spinner);
		gv = (LinearLayout) findViewById(R.id.analytics_graph);
		go_graph = (Button) findViewById(R.id.analytics_go);
		go_graph.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (rep_max_spinner.getSelectedItem() == null) {
					Toast.makeText(Analytics_Main.this, "Please select a movement", Toast.LENGTH_LONG).show();
				} else {
					ahf.get_data(rep_max_spinner.getSelectedItem().toString());

				}

			}

		});
		if (!(savedInstanceState == null)) {

			this.max_rep_list_items = savedInstanceState.getStringArrayList("spinner_list");
			ArrayAdapter<String> spinner_pulldown = new ArrayAdapter<String>(Analytics_Main.this,
					android.R.layout.simple_spinner_item, this.max_rep_list_items);
			spinner_pulldown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			rep_max_spinner.setAdapter(spinner_pulldown);

			if (!(ahf.getGraphData() == null)) {
				display_the_graph(ahf.getGraphData());
			}
		}

	}

	public void handoff_rep_max_items(ArrayList<String> max_rep_list_items) {
		Log.v("Analytics Main", "got the adapter, now setting with count of " + max_rep_list_items.size());
		this.max_rep_list_items = max_rep_list_items;
		ArrayAdapter<String> spinner_pulldown = new ArrayAdapter<String>(Analytics_Main.this,
				android.R.layout.simple_spinner_item, this.max_rep_list_items);
		spinner_pulldown.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rep_max_spinner.setAdapter(spinner_pulldown);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("spinner_list", max_rep_list_items);

	}

	private void display_the_graph(ArrayList<Analytics_Data_Holder> analytics_data) {
		String[] dates_for_label;
		Integer i = 0;
		nothingToDisplay.setVisibility(View.GONE);
		GraphViewSeries gvs = null;
		GraphViewData[] gvd;
		Log.v("Analytics Main", "size of passed data holder is " + analytics_data.size());
		dates_for_label = new String[analytics_data.size()];
		while (i <= analytics_data.size() - 1) {

			dates_for_label[i] = "" + analytics_data.get(i).get_date().get(Calendar.DAY_OF_MONTH)
					+ analytics_data.get(i).get_month(analytics_data.get(i).get_date().get(Calendar.MONTH) + 1)
					+ analytics_data.get(i).get_date().get(Calendar.YEAR);
			Log.v("analytics Main", "adding dates for label with i = " + i + " " + dates_for_label[i]);
			i++;
		}

		if (!(graphView == null)) {
			Log.v("Analytics Activity", " found graphview data and removing before setting");
			// graphView.removeAllSeries();
			// graphView.removeSeries(0);
			graphView.removeSeries(0);
			gv.removeView(graphView);
		} else {
			graphView = new LineGraphView(this, "");
			// the empty string is here as we need a string for the display to
			// work
			// and we don't need it as we already have the workout selected in
			// the
			// spinner. it would be duplicating things
			// gv = (LinearLayout) findViewById(R.id.analytics_graph);

		}
		gvd = new GraphViewData[analytics_data.size()];
		i = 0;
		while (i <= analytics_data.size() - 1) {
			gvd[i] = new GraphViewData(i + 1, analytics_data.get(i).get_weight());
			Log.v("analytics main", "adding weight for display at i = " + i + " " + gvd[i]);
			i++;
		}
		gvs = new GraphViewSeries(gvd);

		graphView.setHorizontalLabels(dates_for_label);
		// we want to dynamically set the level of points on the axis to account
		// for the size difference between portrait and landscape

		/*
		 * we will need to dynamically set up the labels as we cannot set the
		 * labels dynamically AND set the number of labels. GraphView doesn't
		 * work like that. Will need math to figure this out.
		 */
		if (Configuration.ORIENTATION_PORTRAIT == getScreenOrientation()) {
			//graphView.getGraphViewStyle().setNumHorizontalLabels(3);
			graphView.getGraphViewStyle().setNumVerticalLabels(7);
		} else {
			//graphView.getGraphViewStyle().setNumHorizontalLabels(5);
			graphView.getGraphViewStyle().setNumVerticalLabels(4);
		}
		((LineGraphView) graphView).setDrawDataPoints(true);
		((LineGraphView) graphView).setDataPointsRadius(15f);

		graphView.addSeries(gvs);
		gv.addView(graphView);
	}

	public int getScreenOrientation() {

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int screenWidth = displaymetrics.widthPixels;
		int screenHeight = displaymetrics.heightPixels;
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		if (screenWidth < screenHeight) {
			orientation = Configuration.ORIENTATION_PORTRAIT;
		} else {
			orientation = Configuration.ORIENTATION_LANDSCAPE;
		}

		return orientation;
	}
}
