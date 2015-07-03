package com.floridaseabee.wodjournal;

import java.util.ArrayList;

public class XML_Imported_Database {
	private ArrayList<Date_Holder> date_holder;
	private String error_to_return = null;
	private Integer database_version = 0;

	public XML_Imported_Database(String error, Integer version) {

		this.date_holder = new ArrayList<Date_Holder>();
		database_version = version;
		if (!(error == null)) {
			error_to_return = error;
		}
	}

	public String read_error() {
		return error_to_return;

	}

	public void write_error(String error) {
		error_to_return = error;
	}

	public Integer read_database_version() {
		return database_version;
	}

	public void write_database_version(Integer version) {
		database_version = version;
	}

	public void set_date_holder_array(ArrayList<Date_Holder> DHArray) {
		date_holder = DHArray;
	}
	
	public Date_Holder return_single_date_holder(Integer position) {
		return date_holder.get(position);
	}

	public ArrayList<Date_Holder> return_date_holder() {
		return date_holder;
	}

	public Integer get_date_holder_size() {
		return date_holder.size();
	}
}
