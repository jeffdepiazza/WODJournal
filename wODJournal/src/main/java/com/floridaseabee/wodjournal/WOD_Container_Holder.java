package com.floridaseabee.wodjournal;

import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

public class WOD_Container_Holder {
	private Integer _id;
	private Integer Date_Holder_id = 0;
	private Integer Timed = 0;
	private String Timed_Type = "";
	private String Staggered_Rounds = "";
	private Integer Rounds = 0;
	private String Finish_Time = "";
	private Float Rounds_Finished = (float) 0;
	private Integer Container_Order = 0;
	private String IsWeight_Workout = "";
	private String Workout_Name = "";
	private String Comments = "";
	ArrayList<Movement_Container_Holder> Movement_Container_Holder;

	public WOD_Container_Holder(Cursor c, Cursor movement_cursor) {
		this._id = c.getInt(0);
		this.Date_Holder_id = c.getInt(1);
		this.Container_Order = c.getInt(2);
		this.Timed = c.getInt(3);
		this.Timed_Type = c.getString(4);
		this.Rounds = c.getInt(5);
		this.Finish_Time = c.getString(6);
		this.Rounds_Finished = c.getFloat(7);
		this.IsWeight_Workout = c.getString(8);
		this.Workout_Name = c.getString(9);
		this.Comments = c.getString(10);
		this.Staggered_Rounds = c.getString(11);
		this.Movement_Container_Holder = new ArrayList<Movement_Container_Holder>();

		// we take the second cursor and go ahead and grab all of our movements
		// for the array immediately
		while (!movement_cursor.isAfterLast()) {
			Movement_Container_Holder.add(new Movement_Container_Holder(
					movement_cursor));
			movement_cursor.moveToNext();
		}
	}

	public WOD_Container_Holder() {
		Movement_Container_Holder = new ArrayList<Movement_Container_Holder>();
		// empty constructor
	}

	public ArrayList<Movement_Container_Holder> return_movements() {
		return Movement_Container_Holder;

	}

	public Movement_Container_Holder return_single_movement(Integer position) {
		return Movement_Container_Holder.get(position);
	}

	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer id) {
		this._id = id;
	}
	
	public Integer get_Date_Holder_id() {
		return Date_Holder_id;
	}

	public void set_Date_Holder_id(Integer DHID) {
		this.Date_Holder_id = DHID;
	}
	
	public Integer get_WOD_Container_Order() {
		return Container_Order;
	}

	public void set_WOD_Container_Order(Integer WCO) {
		this.Container_Order = WCO;
	}
	
	public Integer get_Timed() {
		return Timed;
	}

	public void set_Timed(Integer Timed) {
		this.Timed = Timed;
	}
	
	public String get_Timed_Type() {
		return Timed_Type;
	}

	public void set_Timed_Type(String TT) {
		this.Timed_Type = TT;
	}
	
	public String get_Staggered_Rounds() {
		return Staggered_Rounds;
	}

	public void set_Staggered_Rounds(String SR) {
		this.Staggered_Rounds = SR;
	}
	public Integer get_Rounds() {
		return Rounds;
	}

	public void set_Rounds(Integer Rounds) {
		this.Rounds = Rounds;
	}

	public String get_Finish_Time() {
		return Finish_Time;
	}

	public void set_Finish_Time(String FT) {
		this.Finish_Time = FT;
	}

	public Float get_Rounds_Finished() {
		return Rounds_Finished;
	}

	public void set_Rounds_Finished(Float RF) {
		this.Rounds_Finished = RF;
	}

	public String get_IsWeight_Workout() {
		return IsWeight_Workout;
	}

	public void set_IsWeight_Workout(String IsWeight) {
		this.IsWeight_Workout = IsWeight;
	}

	public String get_Workout_Name() {
		return Workout_Name;
	}

	public void set_Workout_Name(String WN) {
		this.Workout_Name = WN;
	}

	public String get_Comments() {
		return Comments;
	}

	public void set_Comments(String comments) {
		this.Comments = comments;
	}

	public void delete_movement(Integer childPosition) {
		Movement_Container_Holder.remove(childPosition);

	}

	public Integer get_Movement_size() {
		return Movement_Container_Holder.size();
	}
	
}