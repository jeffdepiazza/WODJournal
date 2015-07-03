package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WOD_Display_Fragment extends Fragment {

	private static Date_Holder date_holder;
	private ProgressBar pg;
	private TextView tv;
	private ExpandableListView lv;
	private static WOD_ExpandableListAdapter list_adapter;
	private LayoutInflater inflater;
	private static Integer childPosition = 0;
	private static Integer groupPosition = 0;
	private static WOD_Date_edit_delete_add listener;
	private Integer count =0;
	private Integer position =1;
	
	public static WOD_Display_Fragment newInstance() {

		WOD_Display_Fragment f = new WOD_Display_Fragment();
		return f;

	}

	public WOD_Date_edit_delete_add getListener() {
		return listener;
	}

	public void setAdd_Edit_Delete_Listener(WOD_Date_edit_delete_add listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("wod display fragment", "Inflating display view");
		View v = inflater.inflate(R.layout.wod_details, container, false);
		lv = (ExpandableListView) v.findViewById(R.id.wod_expandablelistview);
		pg = (ProgressBar) v.findViewById(R.id.LoadFromCalendar);
		tv = (TextView) v.findViewById(R.id.Nothingfound);
		// pg.setVisibility(View.VISIBLE);
		this.inflater = inflater;
		setRetainInstance(true);

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				int itemType = ExpandableListView.getPackedPositionType(id);

				if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
					childPosition = ExpandableListView
							.getPackedPositionChild(id);
					groupPosition = ExpandableListView
							.getPackedPositionGroup(id);
					Log.v("on item long click", "child position");
					listener.call_child_main_dialog(date_holder, groupPosition,
							childPosition);

					return true; // true if we consumed the click, false if
					// not

				} else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
					groupPosition = ExpandableListView
							.getPackedPositionGroup(id);
					// do your per-group callback here
					Log.v("on item long click", "group position");

					listener.call_parent_main_dialog(date_holder, groupPosition);

					return true; // true if we consumed the click, false if
					// not

				} else {
					// null item; we don't consume the click
					return false;
				}
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

	public void handover_data(Date_Holder date_holder) {
		this.date_holder = date_holder;
		pg.setVisibility(View.GONE);
		list_adapter = new WOD_ExpandableListAdapter(date_holder, inflater, getActivity());
		lv.setAdapter(list_adapter);
		// expand all groups to show the user what is in there...most likely the will want to do this anyway...
		// especially if they are adding items.
		count = list_adapter.getGroupCount();
		Log.v(" creating display fragment view", "current group count is " + list_adapter.getGroupCount());
			for (position = 1; position <= count; position++) {
			    lv.expandGroup(position - 1); 
			}
	
	}

	
	// do we realy need this?  we aren't even interfacing with this at the activity level!!!!
	
	interface database_fragment_interface {

		public void delete_entry(Integer groupPosition, Integer childPosition);
	}

}
