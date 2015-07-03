package com.floridaseabee.wodjournal;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

public class Date_Holder {

	private Integer _id;
	private String date;
	private Integer Number_of_Containers;
	ArrayList<WOD_Container_Holder> WOD_Container_Holder;

	public Date_Holder(String date) {
		this.date = date;
		this.WOD_Container_Holder = new ArrayList<WOD_Container_Holder>();
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String get_date() {
		return date;
	}

	public Integer get_id() {
		return _id;
	}

	public void sendContainerCursor(Cursor c, Integer Number_of_Containers, Cursor movement_cursor) {
		Log.v("Date Holder", "Send Container Cursor");
		this.Number_of_Containers = Number_of_Containers;
		Log.v("Date Holder", "Size of cursor" + c.getCount());
		WOD_Container_Holder.add(new WOD_Container_Holder(c, movement_cursor));
	}

	public void delete_container(Integer groupPosition) {
		WOD_Container_Holder.remove(groupPosition);
	}

	public ArrayList<WOD_Container_Holder> return_containers() {
		return WOD_Container_Holder;
	}

	public WOD_Container_Holder return_single_container(Integer position) {
		return WOD_Container_Holder.get(position);
	}

	public Integer get_WOD_Container_size() {
		return WOD_Container_Holder.size();
	}

}
