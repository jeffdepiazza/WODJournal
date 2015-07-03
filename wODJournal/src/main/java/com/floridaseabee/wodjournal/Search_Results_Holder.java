package com.floridaseabee.wodjournal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Search_Results_Holder implements Comparable<Search_Results_Holder> {
	private Calendar date;
	private String bigger_text;
	private String medium_text;
	private String smaller_text;

	public Search_Results_Holder() {
		date = Calendar.getInstance();
		bigger_text = "";
		smaller_text = "";
		medium_text = "";
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

	public void set_bigger_text(String bigger_text) {
		this.bigger_text = bigger_text;
	}

	public String get_bigger_text() {
		return bigger_text;
	}

	public void set_medium_text(String medium_text) {
		this.medium_text = medium_text;
	}

	public String get_medium_text() {
		return medium_text;
	}

	public void set_smaller_text(String smaller_text) {
		this.smaller_text = smaller_text;
	}

	public String get_smaller_text() {
		return smaller_text;
	}

	@Override
	public int compareTo(Search_Results_Holder another) {
		if (get_date() == null || another.get_date() == null)
			return 0;
		return get_date().compareTo(another.get_date());
	}

}
