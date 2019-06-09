package com.floridaseabee.wodjournal;

import android.database.Cursor;

public class Movement_Container_Holder {

	private Integer _id = 0;
	private Integer WOD_Container_ID = 0;
	private Integer Sets = 0;
	private Integer Reps = 0;
	private String Reps_Dynamic ="";
	private Integer AMRAP = 0;
	private Integer Staggered_Rounds = 0;
	private String Movement = "";
	private Integer Time_of_Movement = 0;
	private String Time_of_Movement_Units = "sec";
	private Integer Rep_Max = 0;
	private Integer Length = 0;
	private String Length_Units= "m";
	private Integer Weight = 0;
	private String Weight_Units="lbs";
	private Integer Percentage = 0;
	private Integer Movement_Order = 0;
	private Integer Movement_Number = 0;
	private String Comments = "";

	public Movement_Container_Holder(Cursor c) {

		this._id = c.getInt(0);
		this.WOD_Container_ID = c.getInt(1);
		this.Movement_Order = c.getInt(2);
		this.Sets = c.getInt(3);
		this.Reps = c.getInt(4);
		this.AMRAP = c.getInt(5);
		this.Movement_Number = c.getInt(6);
		this.Movement = c.getString(7);
		this.Rep_Max = c.getInt(8);
		this.Time_of_Movement = c.getInt(9);
		this.Time_of_Movement_Units = c.getString(10);
		this.Length = c.getInt(11);
		this.Length_Units = c.getString(12);
		this.Weight = c.getInt(13);
		this.Weight_Units = c.getString(14);
		this.Percentage = c.getInt(15);
		this.Comments = c.getString(16);
		this.Staggered_Rounds = c.getInt(17);
		this.Reps_Dynamic = c.getString(18);
	}

	public Movement_Container_Holder() {
	}

	public Integer return_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public Integer return_WOD_Container_ID() {
		return WOD_Container_ID;
	}

	public void set_WOD_Container_ID(Integer wcid) {
		this.WOD_Container_ID = wcid;
	}

	public Integer return_Movement_Order() {
		return Movement_Order;
	}

	public void set_Staggered_Rounds(Integer SR) {
		this.Staggered_Rounds = SR;
	}

	public Integer return_Staggered_Rounds() {
		return Staggered_Rounds;
	}
	
	public void set_Movement_Order(Integer movement_order) {
		this.Movement_Order = movement_order;
	}

	public Integer return_sets() {
		return Sets;
	}

	public void set_sets(Integer sets) {
		this.Sets = sets;
	}

	public Integer return_reps() {
		return Reps;
	}

	public void set_reps(Integer reps) {
		this.Reps = reps;
	}

	public String return_reps_dynamic() {
		return Reps_Dynamic;
	}

	public void set_reps_dynamic(String RD) {
		this.Reps_Dynamic = RD;
	}
	
	public Integer return_AMRAP() {
		return AMRAP;
	}

	public void set_AMRAP(Integer AMRAP) {
		this.AMRAP = AMRAP;
	}

	public Integer return_Movement_Number() {
		return Movement_Number;
	}

	public void set_Movement_Number(Integer mn) {
		this.Movement_Number = mn;
	}

	public String return_Movement() {
		return Movement;
	}

	public void set_Movement(String movement) {
		this.Movement = movement;
	}

	public Integer return_Rep_Max() {
		return Rep_Max;
	}

	public void set_Rep_Max(Integer rm) {
		this.Rep_Max = rm;
	}

	public Integer return_Time_of_Movement() {
		return Time_of_Movement;
	}

	public void set_Time_of_Movement(Integer tom) {
		this.Time_of_Movement = tom;
	}

	public String return_Time_of_Movement_Units() {
		return Time_of_Movement_Units;
	}

	public void set_Time_of_Movement_Units(String tomu) {
		this.Time_of_Movement_Units = tomu;
	}
	
	public Integer return_length() {
		return Length;
	}

	public void set_length(Integer length) {
		this.Length = length;
	}

	public String return_length_units() {
		return Length_Units;
	}

	public void set_length_units(String tomu) {
		this.Length_Units = tomu;
	}
	
	public Integer return_weight() {
		return Weight;
	}

	public void set_weight(Integer weight) {
		this.Weight = weight;
	}

	public String return_weight_units() {
		return Weight_Units;
	}

	public void set_weight_units(String tomu) {
		this.Weight_Units = tomu;
	}
	
	public Integer return_Percentage() {
		return Percentage;
	}

	public void set_percentage(Integer percentage) {
		this.Percentage = percentage;
	}

	public String return_Comments() {
		return Comments;
	}

	public void set_Comments(String comments) {
		this.Comments = comments;
	}

}
