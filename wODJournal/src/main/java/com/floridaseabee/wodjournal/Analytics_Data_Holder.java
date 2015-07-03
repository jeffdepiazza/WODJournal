package com.floridaseabee.wodjournal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Analytics_Data_Holder implements Comparable<Analytics_Data_Holder> {
	private String movement = "";
	private Calendar date;
	private Integer weight;

	public Analytics_Data_Holder(String movement) {
		this.movement = movement;
		date = Calendar.getInstance();
		weight = 0;
	}

	public String get_movement() {
		return movement;
	}

	public void set_date(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			this.date.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Calendar get_date() {
		return date;
	}

	public void set_weight(Integer weight) {
		this.weight = weight;
	}

	public Integer get_weight() {
		return weight;
	}

	public String get_month(Integer month) {
		switch (month) {

		case 1:
			return "JAN";
		case 2:
			return "FEB";
		case 3:
			return "MAR";
		case 4:
			return "APR";
		case 5:
			return "MAY";
		case 6:
			return "JUN";
		case 7:
			return "JUL";
		case 8:
			return "AUG";
		case 9:
			return "SEP";
		case 10:
			return "OCT";
		case 11:
			return "NOV";
		case 12:
			return "DEC";

		}
		return "XXX";
	}

	@Override
	public int compareTo(Analytics_Data_Holder another) {
		if (get_date() == null || another.get_date() == null)
			return 0;
		return get_date().compareTo(another.get_date());
	}
}
