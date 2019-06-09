package com.floridaseabee.wodjournal;

import android.database.Cursor;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLExporter {

	private static final String OPEN_XML_STANZA = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	private static final String CLOSE_WITH_TICK = "</";
	private static final String CLOSE_WITH_TICK_AND_QUOTE = "'</";
	private static final String DB_OPEN = "<database_name>";
	private static final String DB_CLOSE = "</database_name>";
	private static final String OPEN_SCHEMA_VERSION = "<schema_version>";
	private static final String CLOSE_SCHEMA_VERSION = "</schema_version>";
	private static final String OPEN_WOD_DATE = "<WOD_Date>";
	private static final String CLOSE_WOD_DATE = "</WOD_Date>";
	private static final String OPEN_MAIN = "<Main>";
	private static final String CLOSE_MAIN = "</Main>";
	private static final String APP_NAME = "<App_Name>WODJournal</App_Name>";
	private static final String OPEN_CONTAINER = "<WOD_Container>";
	private static final String CLOSE_CONTAINER = "</WOD_Container>";
	private static final String OPEN_MOVEMENT = "<Movement_Template>";
	private static final String CLOSE_MOVEMENT = "</Movement_Template>";
	private static final String CONTAINER_ORDER = "Container_Order";
	private static final String TIMED = "Timed";
	private static final String TIMED_TYPE = "Timed_Type";
	private static final String ROUNDS = "Rounds";
	private static final String FINISH_TIME = "Finish_Time";
	private static final String ISWEIGHT_WORKOUT = "IsWeight_Workout";
	private static final String WORKOUT_NAME = "Workout_Name";
	private static final String ROUNDS_FINISHED = "Rounds_Finished";
	private static final String COMMENTS = "Comments";
	private static final String STAGGERED_ROUNDS = "Staggered_Rounds";
	private static final String MOVEMENT_ORDER = "Movement_Order";
	private static final String SETS = "Sets";
	private static final String REPS = "Reps";
	private static final String AMRAP = "AMRAP";
	private static final String MOVEMENT_NUMBER = "Movement_Number";
	private static final String MOVEMENT = "Movement";
	private static final String REP_MAX = "Rep_Max";
	private static final String TIME_OF_MOVEMENT = "Time_of_Movement";
	private static final String TIME_OF_MOVEMENT_UNITS = "Time_of_Movement_Units";
	private static final String LENGTH = "Length";
	private static final String LENGTH_UNITS = "Length_Units";
	private static final String WEIGHT = "Weight";
	private static final String WEIGHT_UNITS = "Weight_Units";
	private static final String PERCENTAGE = "Percentage";
	private static final String REPS_DYNAMIC = "Reps_Dynamic";

	private BufferedOutputStream bos;

	public XMLExporter(String EXPORT_FILE_NAME) {
		try {
			// create a file on the sdcard to export the
			// database contents to
			Log.v("XMLExporter", "file name: " + EXPORT_FILE_NAME);
			File myFile = new File(EXPORT_FILE_NAME);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			this.bos = new BufferedOutputStream(fOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	void start_XML(String dbName, int Schema_Version) throws IOException {
		bos.write((OPEN_XML_STANZA + "\r\n").getBytes());
		bos.write((APP_NAME + "\r\n").getBytes());
		String stg = DB_OPEN + dbName +  DB_CLOSE + "\r\n";
		bos.write(stg.getBytes());
		stg = OPEN_SCHEMA_VERSION + Schema_Version + CLOSE_SCHEMA_VERSION + "\r\n";
		bos.write(stg.getBytes());
	}

	void write_main_table_start() throws IOException {
		bos.write((OPEN_MAIN + "\r\n").getBytes());
	}

	void write_main_table_end() throws IOException {
		bos.write((CLOSE_MAIN + "\r\n").getBytes());
	}

	void write_container_table_start() throws IOException {
		bos.write(("\t" + OPEN_CONTAINER + "\r\n").getBytes());
	}

	void write_container_table_end() throws IOException {
		bos.write(("\t" + CLOSE_CONTAINER + "\r\n").getBytes());
	}

	void write_movement_table_start() throws IOException {
		bos.write(("\t\t" + OPEN_MOVEMENT + "\r\n").getBytes());
	}

	void write_movement_table_end() throws IOException {
		bos.write(("\t\t" + CLOSE_MOVEMENT + "\r\n").getBytes());
	}

	void write_main_entry(Cursor c) throws IOException {
		String stg = OPEN_WOD_DATE + c.getString(c.getColumnIndex("WOD_Date")) +  CLOSE_WOD_DATE + "\r\n";
		bos.write(stg.getBytes());
	}

	void write_container_entry(Cursor c) throws IOException {
		String stg = "\t\t<" + CONTAINER_ORDER + ">" + c.getInt(c.getColumnIndex("Container_Order")) + CLOSE_WITH_TICK + CONTAINER_ORDER + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + TIMED + ">" + c.getInt(c.getColumnIndex("Timed")) + CLOSE_WITH_TICK + TIMED + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + TIMED_TYPE + ">'" + c.getString(c.getColumnIndex("Timed_Type")) + CLOSE_WITH_TICK_AND_QUOTE + TIMED_TYPE + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + ROUNDS + ">" + c.getInt(c.getColumnIndex("Rounds")) + CLOSE_WITH_TICK + ROUNDS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + FINISH_TIME + ">'" + c.getString(c.getColumnIndex("Finish_Time")) + CLOSE_WITH_TICK_AND_QUOTE + FINISH_TIME + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + ROUNDS_FINISHED + ">" + c.getFloat(c.getColumnIndex("Rounds_Finished")) + CLOSE_WITH_TICK + ROUNDS_FINISHED + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + ISWEIGHT_WORKOUT + ">'" + c.getString(c.getColumnIndex("IsWeight_Workout")) + CLOSE_WITH_TICK_AND_QUOTE + ISWEIGHT_WORKOUT + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + WORKOUT_NAME + ">'" + c.getString(c.getColumnIndex("Workout_Name")) + CLOSE_WITH_TICK_AND_QUOTE + WORKOUT_NAME + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + COMMENTS + ">'" + c.getString(c.getColumnIndex("Comments")) + CLOSE_WITH_TICK_AND_QUOTE + COMMENTS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t<" + STAGGERED_ROUNDS + ">'" + c.getString(c.getColumnIndex("Staggered_Rounds")) + CLOSE_WITH_TICK_AND_QUOTE + STAGGERED_ROUNDS + ">\r\n";
		bos.write(stg.getBytes());
	}

	void write_movement_entry(Cursor c) throws IOException {
		String stg = "\t\t\t<"  + MOVEMENT_ORDER + ">" + c.getInt(c.getColumnIndex("Movement_Order")) + CLOSE_WITH_TICK + MOVEMENT_ORDER + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + SETS + ">" + c.getInt(c.getColumnIndex("Sets")) + CLOSE_WITH_TICK + SETS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + REPS + ">" + c.getInt(c.getColumnIndex("Reps")) + CLOSE_WITH_TICK + REPS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + AMRAP + ">" + c.getInt(c.getColumnIndex("AMRAP")) + CLOSE_WITH_TICK + AMRAP + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + MOVEMENT_NUMBER + ">" + c.getInt(c.getColumnIndex("Movement_Number")) + CLOSE_WITH_TICK + MOVEMENT_NUMBER + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + MOVEMENT + ">'" + c.getString(c.getColumnIndex("Movement")) + CLOSE_WITH_TICK_AND_QUOTE + MOVEMENT + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + REP_MAX + ">" + c.getInt(c.getColumnIndex("Rep_Max")) + CLOSE_WITH_TICK + REP_MAX + ">\r\n";
		bos.write(stg.getBytes());
		// we store this as a text, which we should probably fix later on and store as a integer in the database, but not sure what other operations we
		// have right now that depend on the text, so leaving as is.
		stg = "\t\t\t<" + TIME_OF_MOVEMENT + ">" + c.getString(c.getColumnIndex("Time_of_Movement")) + CLOSE_WITH_TICK + TIME_OF_MOVEMENT + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + TIME_OF_MOVEMENT_UNITS + ">'" + c.getString(c.getColumnIndex("Time_of_Movement_Units")) + CLOSE_WITH_TICK_AND_QUOTE + TIME_OF_MOVEMENT_UNITS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + LENGTH + ">" + c.getInt(c.getColumnIndex("Length")) + CLOSE_WITH_TICK + LENGTH + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + LENGTH_UNITS + ">'" + c.getString(c.getColumnIndex("Length_Units")) + CLOSE_WITH_TICK_AND_QUOTE + LENGTH_UNITS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + WEIGHT + ">" + c.getInt(c.getColumnIndex("Weight")) + CLOSE_WITH_TICK + WEIGHT + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + WEIGHT_UNITS + ">'" + c.getString(c.getColumnIndex("Weight_Units")) + CLOSE_WITH_TICK_AND_QUOTE + WEIGHT_UNITS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + PERCENTAGE + ">" + c.getInt(c.getColumnIndex("Percentage")) + CLOSE_WITH_TICK + PERCENTAGE + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + COMMENTS + ">'" + c.getString(c.getColumnIndex("Comments")) + CLOSE_WITH_TICK_AND_QUOTE + COMMENTS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + STAGGERED_ROUNDS + ">" + c.getInt(c.getColumnIndex("Staggered_Rounds")) + CLOSE_WITH_TICK + STAGGERED_ROUNDS + ">\r\n";
		bos.write(stg.getBytes());
		stg = "\t\t\t<" + REPS_DYNAMIC + ">'"  + c.getString(c.getColumnIndex("Reps_Dynamic")) + CLOSE_WITH_TICK_AND_QUOTE + REPS_DYNAMIC + ">\r\n";
		bos.write(stg.getBytes());
	}

	void close() throws IOException {
		if (bos != null) {
			bos.close();
		//	MediaScannerConnection msc = new MediaScannerConnection();
			
		}
	}
}