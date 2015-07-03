package com.floridaseabee.wodjournal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class File_Restore {
// I was thinking that this would be easier and a decent way for the user to copy out the database if they needed to
	// but it was a mess as the copied database file out of the apps directory didn't keep the ownership nor file permissions
	// and i ended up having 
	private SQLiteDatabase current_db;
	private SQLiteDatabase restore_db;

	public File_Restore(SQLiteDatabase restore_db, SQLiteDatabase current_db) {
		this.current_db = current_db;
		this.restore_db = restore_db;
	}

	public void drop_and_create() {
		try {
			current_db.beginTransaction();
			current_db.execSQL("DROP TABLE Main;");
			current_db.execSQL("DROP TABLE WOD_Container;");
			current_db.execSQL("DROP TABLE Movement_Template;");
			current_db.execSQL("DROP TABLE VARIABLES;");
			current_db.setTransactionSuccessful();
		} finally {
			current_db.endTransaction();
		}

		// now add them back in, just that they are empty.
		try {
			current_db.beginTransaction();
			current_db.execSQL("CREATE TABLE Main (_id INTEGER PRIMARY KEY AUTOINCREMENT, WOD_Date TEXT);");
			current_db
					.execSQL("CREATE TABLE WOD_Container (_id INTEGER PRIMARY KEY AUTOINCREMENT, Date_ID INTEGER, Container_Order INTEGER, Timed INTEGER, Timed_Type TEXT, Rounds INTEGER, Finish_Time TEXT, Rounds_Finished REAL, IsWeight_Workout TEXT, Workout_Name TEXT, Comments TEXT, Staggered_Rounds TEXT);");
			current_db
					.execSQL("CREATE TABLE Movement_Template (_id INTEGER PRIMARY KEY AUTOINCREMENT, WOD_Container_ID INTEGER, Movement_Order INTEGER, Sets INTEGER, Reps INTEGER, AMRAP Integer, Movement_Number Integer, Movement TEXT, Rep_Max INTEGER, Time_of_Movement TEXT, Time_of_Movement_Units Text, Length INTEGER, Length_Units TEXT, Weight INTEGER, Weight_Units TEXT, Percentage INTEGER, Comments TEXT, Staggered_Rounds INTEGER, Reps_Dynamic TEXT);");
			current_db.execSQL("CREATE TABLE VARIABLES (_id INTEGER PRIMARY KEY AUTOINCREMENT, Application_Title TEXT, Database_Name TEXT, Schema_Version INTEGER);");
			current_db.setTransactionSuccessful();
		} finally {
			current_db.endTransaction();
		}

	}

	public void execute() {
		Cursor Main_restore;
		Cursor WOD_Container_restore;
		Cursor Movement_Template_restore;
		Cursor VARIABLES_restore;
		Cursor Main_current;
		Cursor WOD_Container_current;
		Cursor Movement_Template_current;
		Cursor VARIABLES_current;
		Integer record_restore_count = 0;
		Integer Main_restore_column_counter = 0;

		Main_restore = restore_db.rawQuery("SELECT * FROM Main", null);
		Main_restore.moveToFirst();
		while (!Main_restore.isAfterLast()) {

			current_db.execSQL("INSERT INTO MAIN (WOD_Date) VALUES ('" + Main_restore.getString(Main_restore.getColumnIndex("WOD_Date")) + "');");
			Main_current = current_db.rawQuery("SELECT _id FROM MAIN WHERE WOD_Date = '" + Main_restore.getString(Main_restore.getColumnIndex("WOD_Date")) + "');", null);
			WOD_Container_restore = restore_db.rawQuery("SELECT * FROM WOD_Container;", null);
			WOD_Container_restore.moveToFirst();
			while (!WOD_Container_restore.isAfterLast()) {
				
			}

		}
		Main_restore.close();
	}

}
