package com.floridaseabee.wodjournal;

public class Personal_Record_Holder {
	private String date;
	private Integer WOD_Container_ID;
	private String movement;
	private Integer weight;
	private String weight_units;
	private String comments;

	
	public Personal_Record_Holder() {
		date="";
		WOD_Container_ID = 0;
		movement = "";
		weight = 0;
		weight_units = "lbs";
		comments="";
	}
	
	public void set_date(String date) {
		this.date = date;
	}

	public String get_date() {
		return date;
	}

	public void set_WOD_Container_ID(Integer WCI) {
		this.WOD_Container_ID = WCI;
	}

	public Integer get_WOD_Container_ID() {
		return WOD_Container_ID;
	}

	public void set_movement(String movement) {
		this.movement = movement;
	}

	public String get_movement() {
		return movement;
	}

	public void set_weight(Integer weight) {
		this.weight = weight;
	}

	public Integer get_weight() {
		return weight;
	}

	public void set_weight_units(String wu) {
		this.weight_units = wu;
	}

	public String get_weight_units() {
		return weight_units;
	}


	public void set_comments(String comments) {
		this.comments = comments;
	}

	public String get_comments() {
		return comments;
	}
}
