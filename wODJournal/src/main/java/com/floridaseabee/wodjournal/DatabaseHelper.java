package com.floridaseabee.wodjournal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "WODS.db";
	private static final int SCHEMA_VERSION = 3;
	private static DatabaseHelper singleton = null;
	private Context ctxt = null;

	synchronized static DatabaseHelper getInstance(Context ctxt) {
		if (singleton == null) {
			singleton = new DatabaseHelper(ctxt.getApplicationContext());
		}
		return (singleton);
	}

	private DatabaseHelper(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, SCHEMA_VERSION);
		this.ctxt = ctxt;
	}

	// only listener needed during the calandar activity to let the
	// activity/fragment know that the Hashmap for the dates
	// are ready to be passed to the Calendar for proper display. Nothing else
	interface WOD_DB_Listener {
		// will need to sent to the fragment setBackgroundResourceForDates
		void CALLsetBackgroundResourceForDates(HashMap<Date, Integer> backgroundForDateMap);
        void done_day_delete();
	}

	// Used for the main WOD area that will need a lot of functions to allow the
	// activity to know when items are uploaded and what to do.

	interface WOD_Main_Listener {
		void returnNoWorkoutsforDayFound(); // no workouts for the day
													// found. so we have custom
													// display to unhide

		void pulledWODsforDay(Date_Holder result); // got the data, now
															// need to display

		void success_add_edit_delete(); // whether or not its an
												// add/edit/or delete, we will
												// make the activity repull that
												// days workout and reattach the
												// adapter
	}

	// for the personal records activity and allows communication to the
	// holder fragment that will keep a hook into the database as its running

	interface Personal_Records_Listener {
		void return_no_PRs_found();

		void return_PR_results(ArrayList<Personal_Record_Holder> prh);

	}

	interface Workout_Search_Listener {
		void return_no_results_found();

		void return_search_results(ArrayList<Search_Results_Holder> results);
	}

	interface Rep_Max_Item_Listener {
		void return_Rep_Max_Items(ArrayList<String> rep_max_items);

		void return_Analytics_data(ArrayList<Analytics_Data_Holder> result);

	}

	interface Utilities_Listener {
		void return_progress(Integer progress);

		void return_create_xml_asynctask(create_xml_async my_task);
		
		void return_restore_xml_asynctask(restore_xml_async my_task);
		
		void return_task_complete();

	}

	public void deleteEntireDay(Integer year, Integer month, Integer day,  WOD_DB_Listener listener) {
		Utilities_Holder_Fragment.executeAsyncTask(new delete_Entire_day(year, month, day, listener));
	}

	public void createXmlAsync(Utilities_Holder_Fragment listener, Context ctxt, String DATASUBDIRECTORY, String EXPORT_FILE_NAME) {
		Utilities_Holder_Fragment.executeAsyncTask(new create_xml_async(listener, ctxt, DATASUBDIRECTORY, EXPORT_FILE_NAME));
	}

	public void createFileAsync(Utilities_Holder_Fragment listener, Context ctxt, String DATASUBDIRECTORY, String EXPORT_FILE_NAME) {
		Utilities_Holder_Fragment.executeAsyncTask(new create_file_async(listener, ctxt, DATASUBDIRECTORY, EXPORT_FILE_NAME));
	}

	public void restoreXmlAsync(Utilities_Holder_Fragment listener, Context ctxt, String filename) {
		Utilities_Holder_Fragment.executeAsyncTask(new restore_xml_async(listener, ctxt, filename));
	}

	public void restoreFileAsync(Utilities_Holder_Fragment listener, Context ctxt, String filename) {
		Utilities_Holder_Fragment.executeAsyncTask(new restore_file_async(listener, ctxt, filename));
	}

	public void GetAnalyticsData(Analytics_Holder_Fragment listener, String movement) {
		Analytics_Holder_Fragment.executeAsyncTask(new get_analytics_data_async(listener, movement));
	}

	public void GetRepMaxItems(Analytics_Holder_Fragment listener) {
		Analytics_Holder_Fragment.executeAsyncTask(new get_rep_max_async(listener));
	}

	void getWODAsync(Integer year, Integer month, WOD_DB_Listener listener) {
		WODCalendarFragment.executeAsyncTask(new Get_Wods_in_the_month(listener, year, month));
	}

	public void DeleteChildAsync(Date_Holder date_holder, Integer groupPosition, Integer childPosition, Calendar_Holder_Fragment listener) {
		WODCalendarFragment.executeAsyncTask(new delete_child_async(listener, date_holder, childPosition, groupPosition));

	}

	public void CopyChildAsync(Date_Holder date_holder, Integer groupPosition, Integer childPosition, Calendar_Holder_Fragment listener) {
		WODCalendarFragment.executeAsyncTask(new copy_child_async(listener, date_holder, childPosition, groupPosition));

	}
	public void DeleteParentAsync(Date_Holder date_holder, Integer groupPosition, Calendar_Holder_Fragment listener) {
		Log.v("Database helper", "delete parent Async ");
		Calendar_Holder_Fragment.executeAsyncTask(new delete_parent_async(listener, date_holder, groupPosition));

	}

	public void EditParentAsync(WOD_Container_Holder WOD_container_edit, Calendar_Holder_Fragment listener) {
		Log.v("Database Helper", "edit parents");
		Calendar_Holder_Fragment.executeAsyncTask(new edit_parent_async(listener, WOD_container_edit));
	}

	public void EditChildAsync(Movement_Container_Holder movement_container_holder, Calendar_Holder_Fragment listener) {
		Log.v("Database Helper", "edit child");
		Calendar_Holder_Fragment.executeAsyncTask(new edit_child_async(listener, movement_container_holder));
	}

	public void AddInsertParentAsync(Date_Holder date_holder, WOD_Container_Holder WOD_container_edit, Integer groupPosition, Calendar_Holder_Fragment listener, String type,
			String current_date) {
		Log.v("Database Helper", "add/insert Parent");
		Calendar_Holder_Fragment.executeAsyncTask(new addinsert_parent_async(date_holder, WOD_container_edit, groupPosition, listener, type, current_date));

	}

	public void AddInsertChildAsync(Date_Holder date_holder, Movement_Container_Holder movement_container_edit, Integer groupPosition, Integer childPosition,
			Calendar_Holder_Fragment listener, String current_date, String type) {
		Log.v("Database Helper", "add/insert child");
		Calendar_Holder_Fragment.executeAsyncTask(new addinsert_child_async(date_holder, movement_container_edit, groupPosition, childPosition, listener, current_date, type));
	}

	public void getWODsinDayAsync(String current_date, Calendar_Holder_Fragment listener) {
		Log.v("Database helper", "About to get the Wods in the Day");
		Calendar_Holder_Fragment.executeAsyncTask(new getWODsinDayAsync(current_date, listener));
	}

	public void GetPRs(Personal_Records_Holder_Fragment listener) {
		Log.v("Database helper", "about to launch async task to grab PRs");
		Personal_Records_Holder_Fragment.executeAsyncTask(new GetPRsAsync(listener));
	}

	public void getSearchResultsAsync(String search_string, String search_type, Workout_Search_Holder_Fragment listener) {
		Log.v("Database helper", "about to launch async task to grab PRs");
		Workout_Search_Holder_Fragment.executeAsyncTask(new GetSearchResultsAsync(search_string, search_type, listener));
	}

	public class delete_Entire_day extends AsyncTask<Void, Void, Void> {
        private WOD_DB_Listener listener = null;
        private Integer year;
        private Integer month;
        private Integer day;

        delete_Entire_day(Integer year, Integer month, Integer day, WOD_DB_Listener listener) {
            Log.v("delete entire day", "in constructor");
            this.listener = listener;
            this.year = year;
            this.month = month;
            this.day = day;

        }
        @Override
        protected Void doInBackground(Void... params) {
			String sqlstatement;
			Integer date_id;
			Integer container_id;
			// in order to delete the entire workout, we need to grab the date_id of the Main table,
			// and the container_id of the Container table before we delete
            if (month <10 ) {
                sqlstatement = "SELECT _id FROM MAIN WHERE WOD_Date = '" + year + "-0" + month + "-" + day + "';";
            }
            else {
            sqlstatement = "SELECT _id FROM MAIN WHERE WOD_Date = '" + year + "-" + month + "-" + day + "';";
                }
            Log.v("delete day", sqlstatement);
			Cursor c = getReadableDatabase().rawQuery(sqlstatement, null);
			c.moveToFirst();
            if (!c.isAfterLast()) {
                date_id = c.getInt(c.getColumnIndex("_id"));
                c.close();
                if (month <10 ) {
                    sqlstatement = "DELETE FROM MAIN WHERE WOD_Date = '" + year + "-0" + month + "-" + day + "';";
                }
                else {
                    sqlstatement = "DELETE FROM MAIN WHERE WOD_Date = '" + year + "-" + month + "-" + day + "';";
                }
				Log.v("Databasehelper", sqlstatement);
                getWritableDatabase().execSQL(sqlstatement);

                sqlstatement = "SELECT _id FROM WOD_Container WHERE Date_ID = " + date_id + ";";
                c = getReadableDatabase().rawQuery(sqlstatement, null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    container_id = c.getInt(c.getColumnIndex("_id"));
                    sqlstatement = "DELETE FROM Movement_Template WHERE WOD_Container_ID = " + container_id + ";";
                    getWritableDatabase().execSQL(sqlstatement);
                    sqlstatement = "DELETE FROM WOD_Container WHERE Date_ID = " + date_id + ";";
                    getWritableDatabase().execSQL(sqlstatement);
                    c.moveToNext();
                }
            }
			c.close();

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.v("finishing day delete", "heading back to activity");
            listener.done_day_delete();
        }
    }

	public class create_file_async extends AsyncTask<Void, Void, Void> {

		Utilities_Listener listener;
		Context ctxt;
		Boolean completed = false;
		String DATASUBDIRECTORY = "";
		String EXPORT_FILE_NAME = "";

		public create_file_async(Utilities_Holder_Fragment listener, Context ctxt, String DATASUBDIRECTORY, String EXPORT_FILE_NAME) {
			this.listener = listener;
			this.ctxt = ctxt;
			Log.v("database helper", "DATASUBDIRECTORY " + DATASUBDIRECTORY);
			Log.v("database helper", "EXPORT_FILE_NAME " + EXPORT_FILE_NAME);
			this.DATASUBDIRECTORY = DATASUBDIRECTORY;
			this.EXPORT_FILE_NAME = EXPORT_FILE_NAME;
		}

		@Override
		protected Void doInBackground(Void... params) {

			File directorycheck = new File(DATASUBDIRECTORY);

			// this will check to see if the directory is already created,
			// and if not, will do so...if it does already, it won't care.
			// just by calling the mkdir, it will do all the logic for us,
			// so the only thing we need to do is look at the result, which
			// will tell us what happened with the directory.

			if (!directorycheck.mkdir())
				Log.v("database file creating", "directory already existed");
			else
				Log.v("database file creating", "directory did not exist previsouly, creating directory");
			try {
				File currentDB = ctxt.getDatabasePath(DATABASE_NAME);
				File backupDB = new File(DATASUBDIRECTORY, EXPORT_FILE_NAME);
				Log.v("database helper", " creating files - " + DATASUBDIRECTORY + EXPORT_FILE_NAME);
				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			} catch (Exception e) {
				Log.v("database utilities", "exception in file export - " + e.toString());
				// Toast.makeText(ctxt, e.toString(), Toast.LENGTH_LONG).show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.v("finishing file output", " dismissing dialog");
			super.onPostExecute(result);
			completed = true;
			listener.return_task_complete();
			// rest of the code
		}

		public void reset_listener(Utilities_Holder_Fragment listener) {
			this.listener = listener;
		}
	}

	public class restore_file_async extends AsyncTask<Void, Void, String> {
		Utilities_Listener listener;
		Context ctxt;
		Boolean completed = false;
		String filename;

		public restore_file_async(Utilities_Holder_Fragment listener, Context ctxt, String filename) {
			this.listener = listener;
			this.ctxt = ctxt;
			this.filename = filename;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// listener.return_asynctask(this); when we clean up the listener
			// stuff
			Log.v("Database file restore", "creating progress dialog and sending hook for task ");
		}

		@Override
		protected String doInBackground(Void... params) {
			int restore_schema_version;
			Cursor c;
			SQLiteDatabase restore_db;
			SQLiteDatabase current_db;
			try {
				File restorefile = new File(filename);
				restore_db = SQLiteDatabase.openOrCreateDatabase(restorefile, null);
				System.out.println("Its open? " + restore_db.isOpen());

			} catch (Throwable t) {
				// in case the user gives us an invalid file or database, we
				// need to catch it upon opening, and exit the task
				t.printStackTrace();
                return null;
			}

			restore_schema_version = restore_db.getVersion();
			Log.v("database restore file", "restore database version = " + restore_schema_version);
			if (restore_schema_version > SCHEMA_VERSION) {
                // schema version is higher than what we have, so this must be the wrong database.
				 return (ctxt.getResources().getString(R.string.schema_too_high) + SCHEMA_VERSION);
			}

			try {
				c = restore_db.rawQuery("SELECT * FROM VARIABLES", null);
				c.moveToFirst();
				if ((!(c.getString(c.getColumnIndex("Application_Name")) == "WODJOURNAL")) && (!(c.getString(c.getColumnIndex("Database_Name")) == "WODS.db"))) {
                    return null; // has a variables table, but not the
											// correct entries.... after this
											// check, we can finally try to
											// restore.
				}
			} catch (Throwable t) {
				// in case the user gives us an invalid file or database, we
				// need to catch it upon opening, and exit the task
				t.printStackTrace();
                return null;
			}

			current_db = getWritableDatabase();
			// We've done a lot of testing now, and we are ready to wipe the
			// current database and re-insert all we need.
			// first we need to drop the current tables and recreate them.
			File_Restore FR = new File_Restore(restore_db, current_db);
			onUpgrade(restore_db, restore_schema_version, SCHEMA_VERSION);
			FR.drop_and_create(); // drop the tables in our WODS.db and recreate
									// them.
			FR.execute();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.v("done file restore", " dismissing dialog");
			super.onPostExecute(result);
			completed = true;
			listener.return_task_complete();
			// rest of the code
		}

	}

	public class restore_xml_async extends AsyncTask<Void, Void, Void> {
		Utilities_Listener listener;
		Context ctxt;
		Boolean completed = false;
		String filename;
		XMLParser parse;
		XML_Imported_Database XMLid;

		public restore_xml_async(Utilities_Holder_Fragment listener, Context ctxt, String filename) {
			this.listener = listener;
			this.ctxt = ctxt;
			this.filename = filename;

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// listener.return_asynctask(this); when we clean up the listener
			// stuff
			Log.v("file restore", "creating progress dialog and sending hook for task ");
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				parse = new XMLParser(filename);
				XMLid = parse.start_parse();
				// if there is no error, continue with populating the database but make sure we drop the old copies
				// in case they are left over from a program crash or something;
				
				getWritableDatabase().execSQL("DROP TABLE IF EXISTS 'MAIN_Copy';");
				getWritableDatabase().execSQL("DROP TABLE IF EXISTS 'WOD_Container_Copy';");
				getWritableDatabase().execSQL("DROP TABLE IF EXISTS 'Movement_Template_Copy';");
				if (XMLid.read_error() == null) {
					Log.v("Database helper" , "got the XML imported database back and now creating copies");
					// create the copy tables that we will populate...and will
					// move over
					getWritableDatabase().execSQL("CREATE TABLE Main_COPY (_id INTEGER PRIMARY KEY AUTOINCREMENT, WOD_Date TEXT);");
					getWritableDatabase().execSQL("CREATE TABLE WOD_Container_COPY (_id INTEGER PRIMARY KEY AUTOINCREMENT, Date_ID INTEGER, Container_Order INTEGER, Timed INTEGER, Timed_Type TEXT, Rounds INTEGER, Finish_Time TEXT, Rounds_Finished REAL, IsWeight_Workout TEXT, Workout_Name TEXT, Comments TEXT, Staggered_Rounds TEXT);");
					getWritableDatabase().execSQL("CREATE TABLE Movement_Template_COPY (_id INTEGER PRIMARY KEY AUTOINCREMENT, WOD_Container_ID INTEGER, Movement_Order INTEGER, Sets INTEGER, Reps INTEGER, AMRAP Integer, Movement_Number Integer, Movement TEXT, Rep_Max INTEGER, Time_of_Movement TEXT, Time_of_Movement_Units Text, Length INTEGER, Length_Units TEXT, Weight INTEGER, Weight_Units TEXT, Percentage INTEGER, Comments TEXT, Staggered_Rounds INTEGER, Reps_Dynamic TEXT);");
					// temp tables created, now lets step through the XMLid
					// object and insert into...well...everything.
					// we will be doing a lot of sql calls to not only insert,
					// but to look at what we JUST inserted, as
					// this is how we are keeping track of what containers go to
					// what dates, and what movements go to
					// what containers, etc.
					Integer date_counter = 0;
					Integer container_counter = 0;
					Integer movement_counter = 0;
					Integer date_id;
					Integer container_id;
					String sqlstatement;
					ArrayList<WOD_Container_Holder> wch;
					ArrayList<Movement_Container_Holder> mch;
					Cursor date_cursor;
					Cursor container_cursor;
					Log.v("database import xml", "date holder size = " + XMLid.get_date_holder_size());
					while (XMLid.get_date_holder_size() >= date_counter + 1) {
						// write the date and give the progress back to the listeners...
						//Log.v("database helper" , "" + (float) (date_counter + 1) + "  " + (float) XMLid.get_date_holder_size());
						listener.return_progress((int) ((((float) (date_counter + 1)) / (float) XMLid.get_date_holder_size()) *100));
						getWritableDatabase().beginTransaction();
						getWritableDatabase().execSQL("INSERT INTO Main_Copy (WOD_Date) VALUES ('" + XMLid.return_single_date_holder(date_counter).get_date() + "');");
						getWritableDatabase().setTransactionSuccessful();
						getWritableDatabase().endTransaction();
						// now we need to grab the unique row id from what we
						// just inserted in order to keep it as this
						// will be a reference on the wod container we are about
						// to create.
						date_cursor = getReadableDatabase().rawQuery(
								"SELECT DISTINCT _id, WOD_Date FROM Main_Copy WHERE WOD_Date = '" + XMLid.return_single_date_holder(date_counter).get_date() + "';", null);
						date_cursor.moveToFirst();
						//Log.v("Database helper xml import", "importing date- " + XMLid.return_single_date_holder(date_counter).get_date() + " with " + XMLid.return_single_date_holder(date_counter).return_containers().size() + " containers");
						date_id = date_cursor.getInt(date_cursor.getColumnIndex("_id"));
						wch = XMLid.return_single_date_holder(date_counter).return_containers();
						container_counter = 0; 
						while (wch.size() >= container_counter + 1) {
							
							// we look at the comments now and see if there are any apostrophes in there we will remove the left and
							// right most apostrophes, as we need to double up any other apstrophies so the sql doesn't choke on it.
							// we also look to see if its an empty string, as we will need to skip the below and just set the string to empty.
							
							if (!(wch.get(container_counter).get_Comments().equals("''"))) {
								wch.get(container_counter).set_Comments(wch.get(container_counter).get_Comments().substring(1, wch.get(container_counter).get_Comments().length() -1 ));
								wch.get(container_counter).set_Comments(wch.get(container_counter).get_Comments().replace("'", "''"));

							} else {
								wch.get(container_counter).set_Comments("");
							}

							//Log.v("database helper ", "container comments " + wch.get(container_counter).get_Comments());
							//Log.v("database helper ", "container count - " + container_counter);
							// had to do this as we will crash when we have apostrophes in the
							// actual text that the user puts in...so we have to double up in
							// order for sqlite to not crash.
							sqlstatement = "INSERT INTO WOD_Container_Copy (Date_ID, Timed, Timed_Type, Rounds, Finish_Time, Rounds_Finished, IsWeight_Workout, "
									+ "Workout_name, Comments, Staggered_Rounds, Container_Order) VALUES ("
									+ date_id
									+ ", "
									+ wch.get(container_counter).get_Timed()
									+ ", "
									+ wch.get(container_counter).get_Timed_Type()
									+ ", "
									+ wch.get(container_counter).get_Rounds()
									+ ", "
									+ wch.get(container_counter).get_Finish_Time()
									+ ", "
									+ wch.get(container_counter).get_Rounds_Finished()
									+ ", "
									+ wch.get(container_counter).get_IsWeight_Workout()
									+ ", "
									+ wch.get(container_counter).get_Workout_Name()
									+ ", '"
									+ wch.get(container_counter).get_Comments()
									+ "', "
									+ wch.get(container_counter).get_Staggered_Rounds() + ", " + wch.get(container_counter).get_WOD_Container_Order() + ");";
							getWritableDatabase().beginTransaction();
							getWritableDatabase().execSQL(sqlstatement);
							getWritableDatabase().setTransactionSuccessful();
							getWritableDatabase().endTransaction();
							container_cursor = getReadableDatabase().rawQuery(
									"SELECT DISTINCT _id FROM WOD_Container_Copy WHERE Date_ID = " + date_id + " AND Container_Order = "
											+ wch.get(container_counter).get_WOD_Container_Order() + ";", null);
							container_cursor.moveToFirst();
							container_id = container_cursor.getInt(container_cursor.getColumnIndex("_id"));
							mch = wch.get(container_counter).return_movements();
							movement_counter = 0;
							//Log.v("Database helper", "mch size is " + mch.size());
							while (mch.size()  >= movement_counter + 1) {
								//Log.v("database helper ", "movement count - " + movement_counter);
								if (!(mch.get(movement_counter).return_Comments().equals("''"))) {
								mch.get(movement_counter).set_Comments(mch.get(movement_counter).return_Comments().substring(1, mch.get(movement_counter).return_Comments().length() -1 ));
								mch.get(movement_counter).set_Comments(mch.get(movement_counter).return_Comments().replace("'", "''"));
								} else {
									mch.get(movement_counter).set_Comments("");
								}
                                if (!(mch.get(movement_counter).return_Movement().equals("''"))) {
                                    mch.get(movement_counter).set_Movement(mch.get(movement_counter).return_Movement().substring(1, mch.get(movement_counter).return_Movement().length() - 1));
                                    mch.get(movement_counter).set_Movement(mch.get(movement_counter).return_Movement().replace("'", "''"));
                                } else {
                                    mch.get(movement_counter).set_Movement("");
                                }

								// had to do this as we will crash when we have apostrophes in the
								// actual text that the user puts in...so we have to double up in
								// order for sqlite to not crash.
								sqlstatement = "INSERT INTO Movement_Template_Copy (WOD_Container_ID, Movement_Order, Sets, Reps, AMRAP, Movement_Number, Movement, Rep_Max, Time_of_Movement, "
										+ "Time_of_Movement_Units, Length, Length_Units, Weight, Weight_Units, Percentage, Comments, Staggered_Rounds, Reps_Dynamic) VALUES ("
										+ container_id
										+ ", "
										+ mch.get(movement_counter).return_Movement_Order()
										+ ","
										+ mch.get(movement_counter).return_sets()
										+ ", "
										+ mch.get(movement_counter).return_reps()
										+ ", "
										+ mch.get(movement_counter).return_AMRAP()
										+ ", "
										+ mch.get(movement_counter).return_Movement_Number()
										+ ", "
										+ mch.get(movement_counter).return_Movement()
										+ ", "
										+ mch.get(movement_counter).return_Rep_Max()
										+ ", "
										+ mch.get(movement_counter).return_Time_of_Movement()
										+ ", "
										+ mch.get(movement_counter).return_Time_of_Movement_Units()
										+ ", "
										+ mch.get(movement_counter).return_length()
										+ ", "
										+ mch.get(movement_counter).return_length_units()
										+ ", "
										+ mch.get(movement_counter).return_weight()
										+ ", "
										+ mch.get(movement_counter).return_weight_units()
										+ ", "
										+ mch.get(movement_counter).return_Percentage()
										+ ", '"
										+ mch.get(movement_counter).return_Comments()
										+ "', "
										+ mch.get(movement_counter).return_Staggered_Rounds()
										+ ", "
										+ mch.get(movement_counter).return_reps_dynamic() + ");";
								getWritableDatabase().beginTransaction();
								getWritableDatabase().execSQL(sqlstatement);
								getWritableDatabase().setTransactionSuccessful();
								getWritableDatabase().endTransaction();
								movement_counter++;
							}
							container_counter++;
						}
					date_counter++;
					}
				// done with the import
				
				// drop the old tables
					Log.v("database helper", "dropping old tables and altering");
					getWritableDatabase().beginTransaction();
					getWritableDatabase().execSQL("DROP TABLE IF EXISTS 'MAIN';");
					getWritableDatabase().execSQL("DROP TABLE IF EXISTS 'WOD_Container';");
					getWritableDatabase().execSQL("DROP TABLE IF EXISTS 'Movement_Template';");
					getWritableDatabase().execSQL("ALTER TABLE 'Main_Copy' RENAME TO 'Main';");
					getWritableDatabase().execSQL("ALTER TABLE 'WOD_Container_Copy' RENAME TO 'WOD_Container';");
					getWritableDatabase().execSQL("ALTER TABLE 'Movement_Template_Copy' RENAME TO 'Movement_Template';");
					getWritableDatabase().setTransactionSuccessful();
					getWritableDatabase().endTransaction();
					// next we need to test if the schema that we just imported needs to be updated.
					if (XMLid.read_database_version() < SCHEMA_VERSION) {
						Log.v("Database helper" , "imported xml and need to update schema");
						onUpgrade(getWritableDatabase(), XMLid.read_database_version(), SCHEMA_VERSION);
					}
				// rename the copy table to the new ones
				
				} else {
					
					Log.v("Database xml import" , "null error or something...not good");
				}
					

			} catch (XmlPullParserException | IOException e) {
				// TODO Auto-generated catch block
				Log.v("Database helper", "error in XML parsing");
				e.printStackTrace();

			}

			return null;
		}

		public void reset_listener(Utilities_Holder_Fragment listener) {
			Log.v("Datatbase Helper", "restore xml async - reset listener");
			this.listener = listener;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.v("finishing xml restore", " dismissing dialog");
			super.onPostExecute(result);
			completed = true;
			listener.return_task_complete();
			// rest of the code
		}

	}

	public class create_xml_async extends AsyncTask<Void, Void, Void> {

		Utilities_Listener listener;
		Context ctxt;
		Boolean completed = false;
		String DATASUBDIRECTORY = "";
		String EXPORT_FILE_NAME = "";
		XMLExporter xml_exporter;

		public create_xml_async(Utilities_Holder_Fragment listener, Context ctxt, String DATASUBDIRECTORY, String EXPORT_FILE_NAME) {
			this.listener = listener;
			this.ctxt = ctxt;
			this.DATASUBDIRECTORY = DATASUBDIRECTORY;
			this.EXPORT_FILE_NAME = EXPORT_FILE_NAME;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			listener.return_create_xml_asynctask(this);
			Log.v("Database for .xml dump", "creating progress dialog ");
		}

		@Override
		protected Void doInBackground(Void... params) {
			Cursor main_cursor;
			Cursor container_cursor;
			Cursor movement_cursor;
			Log.v("Database xml export", "directory = " + DATASUBDIRECTORY + " filename = " + EXPORT_FILE_NAME);
			String sqlstatement = "SELECT * FROM MAIN";

			xml_exporter = new XMLExporter(DATASUBDIRECTORY + EXPORT_FILE_NAME);
			File directorycheck = new File(DATASUBDIRECTORY);

			// this will check to see if the directory is already created,
			// and if not, will do so...if it does already, it won't care.
			// just by calling the mkdir, it will do all the logic for us,
			// so the only thing we need to do is look at the result, which
			// will tell us what happened with the directory.

			if (!directorycheck.mkdir())
				Log.v("database XML creating", "directory already existed");
			else
				Log.v("database XML creating", "directory did not exist previsouly, creating directory");

			main_cursor = getReadableDatabase().rawQuery(sqlstatement, null);

			main_cursor.moveToFirst();
			Log.v("Database-xml export", "Main Cursor count = " + main_cursor.getCount());
			try {
				if (main_cursor.isAfterLast()) {
					Log.v("DatabaseHelper ", "no main entries found");

					return null;
				} else {

					xml_exporter.start_XML(DATABASE_NAME, SCHEMA_VERSION);
				}
				while (!main_cursor.isAfterLast()) {
					listener.return_progress((int) (((float) main_cursor.getPosition() / (float) main_cursor.getCount()) * 100));
					xml_exporter.write_main_table_start();
					xml_exporter.write_main_entry(main_cursor);

					sqlstatement = "SELECT * FROM WOD_Container WHERE Date_ID=" + main_cursor.getInt(main_cursor.getColumnIndex("_id")) + ";";
					container_cursor = getReadableDatabase().rawQuery(sqlstatement, null);
					container_cursor.moveToFirst();
					if (container_cursor.isAfterLast()) {
						Log.v("DatabaseHelper ", "no container entries found" + main_cursor.getString(main_cursor.getColumnIndex("WOD_Date")));
					} else {
						while (!container_cursor.isAfterLast()) {

							xml_exporter.write_container_table_start();
							xml_exporter.write_container_entry(container_cursor);
							sqlstatement = "SELECT * FROM Movement_Template WHERE WOD_Container_ID=" + container_cursor.getInt(container_cursor.getColumnIndex("_id")) + ";";
							movement_cursor = getReadableDatabase().rawQuery(sqlstatement, null);
							movement_cursor.moveToFirst();
							if (movement_cursor.isAfterLast()) {
								Log.v("DatabaseHelper ", "no movement entries found for date " + main_cursor.getString(main_cursor.getColumnIndex("WOD_Date")));
							} else {
								while (!movement_cursor.isAfterLast()) {
									xml_exporter.write_movement_table_start();
									xml_exporter.write_movement_entry(movement_cursor);
									xml_exporter.write_movement_table_end();
									movement_cursor.moveToNext();
								}
								movement_cursor.close();
							}
							xml_exporter.write_container_table_end();
							container_cursor.moveToNext();
						}
					}
					container_cursor.close();
					xml_exporter.write_main_table_end();
					main_cursor.moveToNext();
				}
				main_cursor.close();
				xml_exporter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.v("finishing xml output", " dismissing dialog");
			super.onPostExecute(result);
			completed = true;
			listener.return_task_complete();
			// rest of the code
		}

		public void reset_listener(Utilities_Holder_Fragment listener) {
			this.listener = listener;
		}
	}

	private class addinsert_child_async extends AsyncTask<Void, Void, Void> {

		private Movement_Container_Holder mch;
		private WOD_Main_Listener listener = null;
		private String type = "'";
		private Integer groupPosition = 0;
		private Integer _id = 0;
		private Integer childPosition = 0;
		private String current_date = "";
		private String sqlstatement = "";
		private Date_Holder date_holder;

		public addinsert_child_async(Date_Holder date_holder, Movement_Container_Holder movement_container_edit, Integer groupPosition, Integer childPosition,
				Calendar_Holder_Fragment listener, String current_date, String type) {
			this.mch = movement_container_edit;
			this.listener = listener;
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
			this.current_date = current_date;
			this.date_holder = date_holder;
			this.type = type;
			_id = date_holder.return_single_container(groupPosition).get_id();
		}

		@Override
		protected Void doInBackground(Void... params) {

			mch.set_Comments(mch.return_Comments().replace("'", "''"));
            mch.set_Movement(mch.return_Movement().replace("'", "''"));
            // had to do this as we will crash when we have apostrophes in the
			// actual text that the user puts in...so we have to double up in
			// order for sqlite to not crash.
			if (type == "Add") {

				sqlstatement = "INSERT INTO Movement_Template (WOD_Container_ID, Movement_Order, Sets, Reps, AMRAP, Movement_Number, Movement, Rep_Max, Time_of_Movement, "
						+ "Time_of_Movement_Units, Length, Length_Units, Weight, Weight_Units, Percentage, Comments, Staggered_Rounds, Reps_Dynamic) VALUES ("
						+ _id
						+ ", "
						+ (date_holder.return_single_container(groupPosition).get_Movement_size() + 1)
						+ ","
						+ mch.return_sets()
						+ ", "
						+ mch.return_reps()
						+ ", "
						+ mch.return_AMRAP()
						+ ", "
						+ mch.return_Movement_Number()
						+ ", '"
						+ mch.return_Movement()
						+ "', "
						+ mch.return_Rep_Max()
						+ ", "
						+ mch.return_Time_of_Movement()
						+ ", '"
						+ mch.return_Time_of_Movement_Units()
						+ "', "
						+ mch.return_length()
						+ ", '"
						+ mch.return_length_units()
						+ "', "
						+ mch.return_weight()
						+ ", '"
						+ mch.return_weight_units()
						+ "', "
						+ mch.return_Percentage()
						+ ", '"
						+ mch.return_Comments()
						+ "', "
						+ mch.return_Staggered_Rounds() + ", '" + mch.return_reps_dynamic() + "');";
				Log.v("sql for add child", sqlstatement);
				getWritableDatabase().execSQL(sqlstatement);
			} else if (type == "Insert") {
				// if this is an insert function, we will need to move up all
				// the containers so we can put this one into the space between.
				Log.v("database helper", "Inserting child");
				Integer i = date_holder.return_single_container(groupPosition).get_Movement_size();
				while (i > childPosition) {
					Log.v("Mvmt container iterator", "");

					sqlstatement = "UPDATE Movement_Template SET Movement_Order = " + (i + 1) + " WHERE Movement_Order = " + i + " AND WOD_Container_ID = " + _id + ";";
					Log.v("increasing childs", sqlstatement);
					getWritableDatabase().execSQL(sqlstatement);
					i--;
				}
				// done with moving the containers around...
				sqlstatement = "INSERT INTO Movement_Template (WOD_Container_ID, Movement_Order, Sets, Reps, AMRAP, Movement_Number, Movement, Rep_Max, Time_of_Movement, "
						+ "Time_of_Movement_Units, Length, Length_Units, Weight, Weight_Units, Percentage, Comments, Staggered_Rounds, Reps_Dynamic) VALUES ("
						+ _id
						+ ", "
						+ (childPosition + 1)
						+ ","
						+ mch.return_sets()
						+ ", "
						+ mch.return_reps()
						+ ", "
						+ mch.return_AMRAP()
						+ ", "
						+ mch.return_Movement_Number()
						+ ", '"
						+ mch.return_Movement()
						+ "', "
						+ mch.return_Rep_Max()
						+ ", "
						+ mch.return_Time_of_Movement()
						+ ", '"
						+ mch.return_Time_of_Movement_Units()
						+ "', "
						+ mch.return_length()
						+ ", '"
						+ mch.return_length_units()
						+ "', "
						+ mch.return_weight()
						+ ", '"
						+ mch.return_weight_units()
						+ "', "
						+ mch.return_Percentage() + ", '" + mch.return_Comments() + "', " + mch.return_Staggered_Rounds() + ", '" + mch.return_reps_dynamic() + "');";
				Log.v("sql for insert child", sqlstatement);
				getWritableDatabase().execSQL(sqlstatement);
			}
			return null;
		}

		protected void onPostExecute(Void params) {
			Log.v("done add/insert child ", "in onPostExceute");
			// done adding/inserting the child entry, lets go back so we can
			// requery the adapter with fresh data.
			// we could just edit the adapter as well, but I would rather
			// requery the adapter and make sure
			// that the Order of WODs are the same, which I'm not sure i
			// could guarantee by editing them normally.
			listener.success_add_edit_delete();
		}

	}

	private class addinsert_parent_async extends AsyncTask<Void, Void, Void> {
		private WOD_Container_Holder wch;
		private WOD_Main_Listener listener = null;
		private String type = "'";
		private Integer groupPosition = 0;
		private String current_date = "";
		private Integer _id = 0;
		private String sqlstatement = "";
		private Date_Holder date_holder;

		public addinsert_parent_async(Date_Holder date_holder, WOD_Container_Holder WOD_container_addinsert, Integer groupPosition, Calendar_Holder_Fragment listener, String type,
				String current_date) {
			this.listener = listener;
			this.wch = WOD_container_addinsert;
			this.type = type;
			this.groupPosition = groupPosition;
			this.current_date = current_date;
			this.date_holder = date_holder;
		}

		@Override
		protected Void doInBackground(Void... params) {

			wch.set_Comments(wch.get_Comments().replace("'", "''"));
			// had to do this as we will crash when we have apostrophes in the
			// actual text that the user puts in...so we have to double up in
			// order for sqlite to not crash.

			Log.v("Database GetWodsinaDay", "SELECT DISTINCT _id, WOD_Date FROM Main WHERE WOD_Date = '" + current_date + "';");
			Cursor c = getReadableDatabase().rawQuery("SELECT DISTINCT _id, WOD_Date FROM Main WHERE WOD_Date = '" + current_date + "';", null);
			c.moveToFirst();
			Log.v("Database-GetWodsInDay", "Cursor count" + c.getCount());
			// If there is nothing in the cursor, then this is a new add for a
			// date that has nothing, so we have to do our add operation into
			// the main table, and then grab the _id afterwards and set it into
			// our_id.

			if (c.isAfterLast()) {
				Log.v("Database-GetWodsinDay", "nothing found for date_id, so we much create one");
				c.close();

				getWritableDatabase().execSQL("INSERT INTO Main (WOD_Date) VALUES ('" + current_date + "');");
				c = getReadableDatabase().rawQuery("SELECT DISTINCT _id, WOD_Date FROM Main WHERE WOD_Date = '" + current_date + "';", null);
				c.moveToFirst();
				_id = c.getInt(0);
			} else {
				_id = c.getInt(0);
			}

			if (type == "Add") {
				Log.v("database helper", "Adding parent");
				sqlstatement = "INSERT INTO WOD_CONTAINER (Date_ID, Timed, Timed_Type, Rounds, Finish_Time, Rounds_Finished, IsWeight_Workout, "
						+ "Workout_name, Comments, Staggered_Rounds, Container_Order) VALUES ("
						+ _id
						+ ", "
						+ +wch.get_Timed()
						+ ", '"
						+ wch.get_Timed_Type()
						+ "', "
						+ wch.get_Rounds()
						+ ", '"
						+ wch.get_Finish_Time()
						+ "', "
						+ wch.get_Rounds_Finished()
						+ ", '"
						+ wch.get_IsWeight_Workout()
						+ "', '"
						+ wch.get_Workout_Name()
						+ "', '"
						+ wch.get_Comments()
						+ "', '"
						+ wch.get_Staggered_Rounds() + "', " + (groupPosition + 1) + ");";

				Log.v("sql for edit parent", sqlstatement);
				getWritableDatabase().execSQL(sqlstatement);
			} else if (type == "Insert") {
				// if this is an insert function, we will need to move up all
				// the containers so we can put this one into the space between.
				Log.v("database helper", "Inserting parent");
				Integer i = date_holder.get_WOD_Container_size();
				while (i > groupPosition) {
					Log.v("WOD container iterator", "");

					sqlstatement = "UPDATE WOD_Container SET Container_Order = " + (i + 1) + " WHERE Container_Order = " + i + " AND Date_ID = " + _id + ";";
					Log.v("increasing parent", sqlstatement);
					getWritableDatabase().execSQL(sqlstatement);
					i--;
				}
				// done with moving the containers around...

				sqlstatement = "INSERT INTO WOD_CONTAINER (Date_ID, Timed, Timed_Type, Rounds, Finish_Time, Rounds_Finished, IsWeight_Workout, "
						+ "Workout_name, Comments, Staggered_Rounds, Container_Order) VALUES ("
						+ _id
						+ ", "
						+ wch.get_Timed()
						+ ", '"
						+ wch.get_Timed_Type()
						+ "', "
						+ wch.get_Rounds()
						+ ", '"
						+ wch.get_Finish_Time()
						+ "', "
						+ wch.get_Rounds_Finished()
						+ ", '"
						+ wch.get_IsWeight_Workout()
						+ "', '"
						+ wch.get_Workout_Name()
						+ "', '"
						+ wch.get_Comments()
						+ "', '"
						+ wch.get_Staggered_Rounds() + "', " + (groupPosition + 1) + ");";
				Log.v("sql statement", sqlstatement);
				getWritableDatabase().execSQL(sqlstatement);

			}

			return null;

		}

		protected void onPostExecute(Void params) {
			Log.v("done add/insert parent ", "in onPostExceute");
			// done adding/inserting the parent entry, lets go back so we can
			// requery the adapter with fresh data.
			// we could just edit the adapter as well, but I would rather
			// requery the adapter and make sure
			// that the Order of WODs are the same, which I'm not sure i
			// could guarantee by editing them normally.
			listener.success_add_edit_delete();
		}

	}

	private class edit_parent_async extends AsyncTask<Void, Void, Void> {

		private WOD_Container_Holder wch;
		private WOD_Main_Listener listener = null;

		public edit_parent_async(Calendar_Holder_Fragment listener, WOD_Container_Holder WOD_container_edit) {
			this.listener = listener;
			this.wch = WOD_container_edit;
		}

		@Override
		protected Void doInBackground(Void... params) {
			String sqlstring;

			wch.set_Comments(wch.get_Comments().replace("'", "''"));
			// had to do this as we will crash when we have apostrophes in the
			// actual text that the user puts in...so we have to double up in
			// order for sqlite to not crash.

			Log.v("database helper", " edit parent do in background with ID of " + wch.get_id());
			Log.v("checking stagger rounds", "-" + wch.get_Staggered_Rounds() + "-");
			sqlstring = "UPDATE WOD_CONTAINER SET Timed = " + wch.get_Timed() + ", Timed_Type = '" + wch.get_Timed_Type() + "', Rounds = " + wch.get_Rounds() + ", Finish_Time = '"
					+ wch.get_Finish_Time() + "', Rounds_Finished = " + wch.get_Rounds_Finished() + ", IsWeight_Workout = '" + wch.get_IsWeight_Workout() + "', Workout_name = '"
					+ wch.get_Workout_Name() + "', Comments = '" + wch.get_Comments() + "', Staggered_Rounds = '" + wch.get_Staggered_Rounds() + "' WHERE _id = " + wch.get_id()
					+ ";";
			Log.v("sql for edit parent", sqlstring);
			getWritableDatabase().execSQL(sqlstring);

			return null;
		}

		protected void onPostExecute(Void params) {
			Log.v("finished edit parent ", "in onPostExceute");
			// done editing the parent entry, lets go back so we can requery the
			// adapter with fresh data.
			// we could just edit the adapter as well, but I would rather
			// requery the adapter and make sure
			// that the Order of WODs are the same, which I'm not sure i
			// could guarantee by editing them normally.
			listener.success_add_edit_delete();
		}
	}

	private class edit_child_async extends AsyncTask<Void, Void, Void> {

		private Movement_Container_Holder mch;
		private WOD_Main_Listener listener = null;

		public edit_child_async(Calendar_Holder_Fragment listener, Movement_Container_Holder movement_container_holder) {
			this.listener = listener;
			this.mch = movement_container_holder;
			Log.v("database helper", " edit child constructor");
		}

		@Override
		protected Void doInBackground(Void... params) {
			String sqlstring;
			mch.set_Comments(mch.return_Comments().replace("'", "''"));
            mch.set_Movement(mch.return_Movement().replace("'", "''"));
            Log.v("database helper", " edit child do in background with ID of " + mch.return_id());
			sqlstring = "UPDATE Movement_Template SET Reps = " + mch.return_reps() + ", Reps_Dynamic = '" + mch.return_reps_dynamic() + "', Sets = " + mch.return_sets()
					+ ", AMRAP = " + mch.return_AMRAP() + ", Movement = '" + mch.return_Movement() + "', Time_of_Movement = " + mch.return_Time_of_Movement()
					+ ", Time_of_Movement_Units = '" + mch.return_Time_of_Movement_Units() + "', Rep_Max = " + mch.return_Rep_Max() + ", Length = " + mch.return_length()
					+ ", Length_units = '" + mch.return_length_units() + "', Weight = " + mch.return_weight() + ", Weight_Units = '" + mch.return_weight_units()
					+ "', Percentage = " + mch.return_Percentage() + ", Movement_Number = " + mch.return_Movement_Number() + ", Comments = '" + mch.return_Comments()
					+ "', Staggered_Rounds =" + mch.return_Staggered_Rounds() + " WHERE _id = " + mch.return_id() + ";";
			Log.v("sql for edit child", sqlstring);
			getWritableDatabase().execSQL(sqlstring);
			return null;
		}

		protected void onPostExecute(Void params) {
			Log.v("finished edit child ", "in onPostExceute");
			// done editing the child entry, lets go back so we can requery the
			// adapter with fresh data.
			// we could just edit the adapter as well, but I would rather
			// requery the adapter and make sure
			// that the Order of WODs are the same, which I'm not sure i
			// could guarantee by editing them normally.
			listener.success_add_edit_delete();
		}

	}

	private class delete_parent_async extends AsyncTask<Void, Void, Void> {
		private Date_Holder date_holder;
		private Integer groupPosition;
		private WOD_Main_Listener listener = null;

		public delete_parent_async(Calendar_Holder_Fragment listener, Date_Holder date_holder, Integer groupPosition) {
			Log.v("delete parent async", "in constructor");
			this.listener = listener;
			this.groupPosition = groupPosition;
			this.date_holder = date_holder;
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.v("delete parent async", " do in background");
			WOD_Container_Holder container_holder;
			String sqlstatement;
			container_holder = date_holder.WOD_Container_Holder.get(groupPosition);
			// before we go nuts on this, we need to test and see if there is
			// only one WOD container left
			// and if there is, we need to delete the corresponding entry in the
			// main table as well.
			// so the calendar doesn't show a workout when none exists.

			sqlstatement = "SELECT _id FROM WOD_Container WHERE Date_ID = " + date_holder.get_id() + ";";

			Cursor c = getReadableDatabase().rawQuery(sqlstatement, null);

			c.moveToFirst();
			if (c.getCount() == 1) {
				Log.v("database helper", "found the last count");
				sqlstatement = "DELETE FROM MAIN WHERE _id =" + date_holder.get_id() + ";";
				getWritableDatabase().execSQL(sqlstatement);
			}
			c.close();
			sqlstatement = "DELETE FROM Movement_Template WHERE WOD_Container_ID = " + container_holder.get_id() + ";";
			Log.v("delete parent", sqlstatement);
			getWritableDatabase().execSQL(sqlstatement);
			sqlstatement = "DELETE FROM WOD_Container WHERE _id = " + container_holder.get_id() + ";";
			Log.v("delete parent", sqlstatement);
			getWritableDatabase().execSQL(sqlstatement);
			date_holder.delete_container(groupPosition);
			for (WOD_Container_Holder container_holder_iterator : date_holder.return_containers()) {
				Log.v("WOD container iterator", "");
				if (container_holder_iterator.get_WOD_Container_Order() > (groupPosition + 1)) {
					sqlstatement = "UPDATE WOD_Container SET Container_Order = " + (container_holder_iterator.get_WOD_Container_Order() - 1) + " WHERE Container_Order = "
							+ (container_holder_iterator.get_WOD_Container_Order()) + ";";
					Log.v("sql statement", sqlstatement);
					getWritableDatabase().execSQL(sqlstatement);

				}

			}
			return null;
		}

		protected void onPostExecute(Void params) {
			Log.v("delete parent ", "in onPostExceute");
			// done deleting that entry, lets go back so we can requery the
			// adapter with fresh data.
			// we could just edit the adapter as well, but I would rather
			// requery the adapter and make sure
			// that the Order of WODs are the same, which I'm not sure i
			// could guarantee by editing them normally.
			listener.success_add_edit_delete();
		}
	}

	private class copy_child_async extends AsyncTask<Void, Void, Void> {
		private WOD_Main_Listener listener = null;
		private Integer groupPosition = 0;
		private Integer childPosition = 0;
		private Date_Holder date_holder;

		copy_child_async(Calendar_Holder_Fragment listener, Date_Holder date_holder, Integer childPosition, Integer groupPosition) {
			Log.v("copy child async", "in constructor");
			this.listener = listener;
			this.childPosition = childPosition;
			this.groupPosition = groupPosition;
			this.date_holder = date_holder;
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.v("copy child async", " do in background");
			WOD_Container_Holder container_holder;
			Movement_Container_Holder mch;
			String sqlstatement;
			container_holder = date_holder.WOD_Container_Holder.get(groupPosition);
			mch = container_holder.return_single_movement(childPosition);

			sqlstatement = "INSERT INTO Movement_Template_Copy (WOD_Container_ID, Movement_Order, Sets, Reps, AMRAP, Movement_Number, Movement, Rep_Max, Time_of_Movement, "
					+ "Time_of_Movement_Units, Length, Length_Units, Weight, Weight_Units, Percentage, Comments, Staggered_Rounds, Reps_Dynamic) VALUES ("
					+ mch.return_WOD_Container_ID()
					+ ", "
					+ (container_holder.get_Movement_size() + 1)
					+ ","
					+ mch.return_sets()
					+ ", "
					+ mch.return_reps()
					+ ", "
					+ mch.return_AMRAP()
					+ ", "
					+ mch.return_Movement_Number()
					+ ", "
					+ mch.return_Movement()
					+ ", "
					+ mch.return_Rep_Max()
					+ ", "
					+ mch.return_Time_of_Movement()
					+ ", "
					+ mch.return_Time_of_Movement_Units()
					+ ", "
					+ mch.return_length()
					+ ", "
					+ mch.return_length_units()
					+ ", "
					+ mch.return_weight()
					+ ", "
					+ mch.return_weight_units()
					+ ", "
					+ mch.return_Percentage()
					+ ", '"
					+ mch.return_Comments()
					+ "', "
					+ mch.return_Staggered_Rounds()
					+ ", "
					+ mch.return_reps_dynamic() + ");";
			getWritableDatabase().beginTransaction();
			getWritableDatabase().execSQL(sqlstatement);
			getWritableDatabase().setTransactionSuccessful();
			getWritableDatabase().endTransaction();

			Log.v("copy child", "about to exit do in background");
			return null;
		}

		protected void onPostExecute(Void params) {
			Log.v("copy child ", "in onPostExceute");
			// done copy that entry, lets go back so we can requery the
			// adapter with fresh data.
			// we could just edit the adapter as well, but I would rather
			// requery the adapter and make sure
			// that the Order of Movements are the same, which I'm not sure i
			// could guarantee by editing them normally.
			listener.success_add_edit_delete();

		}

	}

	private class delete_child_async extends AsyncTask<Void, Void, Void> {
		private WOD_Main_Listener listener = null;
		private Integer groupPosition = 0;
		private Integer childPosition = 0;
		private Date_Holder date_holder;

		delete_child_async(Calendar_Holder_Fragment listener, Date_Holder date_holder, Integer childPosition, Integer groupPosition) {
			Log.v("delete child async", "in constructor");
			this.listener = listener;
			this.childPosition = childPosition;
			this.groupPosition = groupPosition;
			this.date_holder = date_holder;
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.v("delete child async", " do in background");
			WOD_Container_Holder container_holder;
			Movement_Container_Holder movement_holder;
			String sqlstatement;
			container_holder = date_holder.WOD_Container_Holder.get(groupPosition);
			movement_holder = container_holder.return_single_movement(childPosition);

			sqlstatement = "DELETE FROM Movement_Template WHERE _id = " + movement_holder.return_id() + ";";
			Log.v("past movment holder", sqlstatement);
			getWritableDatabase().execSQL(sqlstatement);
			container_holder.delete_movement(childPosition);
			// for each movement container, we are going to go through the
			// movement container object and use that as our template for
			// deleting the child we want to delete (done a couple of lines
			// above)
			// and decrementing each child above that movement order in order to
			// ensure that
			// there are no holes in the movement orders.
			for (Movement_Container_Holder movement_holder_iterator : container_holder.return_movements()) {
				Log.v("Mvmt container iterator", "");
				if (movement_holder_iterator.return_Movement_Order() > (childPosition + 1)) {
					sqlstatement = "UPDATE Movement_Template SET Movement_Order = " + (movement_holder_iterator.return_Movement_Order() - 1) + " WHERE Movement_Order = "
							+ (movement_holder_iterator.return_Movement_Order()) + " AND WOD_Container_Id = " + date_holder.return_single_container(groupPosition).get_id() + ";";
					Log.v("sql statement", sqlstatement);
					getWritableDatabase().execSQL(sqlstatement);

				}

			}
			Log.v("delete child", "out of everything and about to exit do in background");
			return null;

		}

		protected void onPostExecute(Void params) {
			Log.v("delete child ", "in onPostExceute");
			// done deleting that entry, lets go back so we can requery the
			// adapter with fresh data.
			// we could just edit the adapter as well, but I would rather
			// requery the adapter and make sure
			// that the Order of Movements are the same, which I'm not sure i
			// could guarantee by editing them normally.
			listener.success_add_edit_delete();

		}

	}

	private class getWODsinDayAsync extends AsyncTask<Void, Void, Date_Holder> {

		private WOD_Main_Listener listener = null;
		private String current_date;

		getWODsinDayAsync(String current_date, WOD_Main_Listener listener) {
			Log.v("getWODsinDayAsync", "in constructor");
			this.listener = listener;
			this.current_date = current_date;
			// we have to do this as the integer won't have two digits unless
			// its above 9, and we will need two digit month and date for the
			// SQL portion
			// had to put a bit of math in to figure out the proper format of
			// the months and days and put them into string variables instead of
			// integer

		}

		@Override
		protected Date_Holder doInBackground(Void... params) {
			Date_Holder date_holder = new Date_Holder(current_date);
			Integer number_of_containers = 0;
			Integer date_holder_id = 0;
			Cursor container_cursor; // to figure out the WOD_Container Entries
			Cursor movement_cursor; // to figure out the Movement Container
									// Entries
			// We couldn't reuse the cursors as they were both being used at the
			// same time.

			Log.v("Database-GetWodsinaDay", "in doinbackground");
			Log.v("Database-GetWodsinaDay", "SELECT DISTINCT _id, WOD_Date FROM Main WHERE WOD_Date = '" + current_date + "';");
			Cursor c = getReadableDatabase().rawQuery("SELECT DISTINCT _id, WOD_Date FROM Main WHERE WOD_Date = '" + current_date + "';", null);
			c.moveToFirst();
			Log.v("Database-GetWodsInDay", "Cursor count" + c.getCount());

			// we need to figure out if we have any data to display first...and
			// if not, we exit out with a null
			// but if we do, we need to grab the _id of that row, as the
			// Container ID will be keyed to this row and
			// will be part of the SELECT statement out of WOD_container.

			if (c.isAfterLast()) {
				Log.v("Database-GetWodsinDay", "no workouts for the day found");
				c.close();
				return null;
			}
			date_holder_id = c.getInt(0);
			date_holder.set_id(date_holder_id); // send date to the Date holder.
			c.close(); // close it as we don't need it anymore

			// now that we have the _id of the Date row in Main, we save it and
			// pass it to the Select Statement
			// in the WOD Container query.
			container_cursor = getReadableDatabase().rawQuery("SELECT * FROM WOD_Container WHERE Date_ID = " + date_holder_id + " ORDER BY Container_Order;", null);
			container_cursor.moveToFirst();

			// if there are no containers, we still will return null as there
			// are no containers (and no movements)
			// for this day. We shouldnt' see this unless we did something in
			// the code that breaks. This is because
			// if there are no containers present, then there shouldn't be an
			// entry in the Main table to begin with.

			if (container_cursor.isAfterLast()) {
				Log.v("Database-GetWodsinDay", "no WOD Containers for the day found");
				container_cursor.close();
				return null;
				// put something here that returns the database object with
				// ...essentially nothing once you change the return types
				// around

			}
			// stepping through the container cursors in case there are multiple
			// ones, and then getting the movements for each
			while (!container_cursor.isAfterLast()) {
				Log.v("Database Helper", "about to query movement cursor with Container ID of " + container_cursor.getInt(0));
				movement_cursor = getReadableDatabase().rawQuery(
						"SELECT * FROM Movement_Template WHERE WOD_Container_ID = " + container_cursor.getInt(0) + " ORDER BY Movement_Order;", null);
				Log.v("Database Helper", "about to send cursors to WOD container and movement");
				movement_cursor.moveToFirst();
				// Both the Container and Movement cursors are sent at the same
				// time. This is because we already grabbed
				// the container _id and used it in the Movement SELECT
				// statement.

				date_holder.sendContainerCursor(container_cursor, number_of_containers, movement_cursor);
				number_of_containers++;
				movement_cursor.close();
				container_cursor.moveToNext();
				// this loop will iterate over a given number of WOD Container
				// entries, grabbing ALL movements associated with each
				// container on each loop, closing the movement cursor to
				// prepare it for requery.

			}

			container_cursor.close();
			return date_holder;
		}

		protected void onPostExecute(Date_Holder result) {
			Log.v("Database-GetWodsinDay", "in onPostExceute");
			// once we find out what we have, we return
			if (result == null) {
				listener.returnNoWorkoutsforDayFound();
			} else {
				listener.pulledWODsforDay(result);
			}
		}
	}

	private class Get_Wods_in_the_month extends AsyncTask<Void, Void, HashMap<Date, Integer>> {
		private WOD_DB_Listener listener = null;
		private Integer year;
		private Integer month;

		Get_Wods_in_the_month(WOD_DB_Listener listener, Integer year, Integer month) {
			Log.v("Database-GetWodsinMonth", "in constructor");
			this.listener = listener;
			this.year = year;
			this.month = month;
		}

		// Have to grab all the dates in the month/year range and return them in
		// a hashmap, so we can redo our background resources

		@Override
		protected HashMap<Date, Integer> doInBackground(Void... params) {

			HashMap<Date, Integer> result = null;
			String[] args = null;
			Date d = null;
			String startDate = null;
			Log.v("Database-GetWodsinMonth", "in doinbackground");
			Log.v("Database-GetWodsinMonth", "Month =" + month);
			Log.v("Database-GetWodsinMonth", "Year =" + year);
			args = new String[2];
			if (month < 10) {
				args[0] = "0" + month.toString();
			} else {
				args[0] = month.toString();
			}
			args[1] = year.toString();
			startDate = args[1] + "-" + args[0] + "-01";
			Log.v("Cursor Query", "SELECT WOD_Date FROM Main WHERE WOD_Date >= '" + startDate + "' AND WOD_Date <= date('" + startDate + "','+1 month','-1 day');");
			Cursor c = getReadableDatabase().rawQuery(
					"SELECT WOD_Date FROM Main WHERE WOD_Date >= '" + startDate + "' AND WOD_Date <= date('" + startDate + "','+1 month','-1 day');", null);

			c.moveToFirst();
			Log.v("Database-GetWodsInMonth", "Cursor count" + c.getCount());
			if (c.isAfterLast()) {
				Log.v("Database-GetWodsinMonth", "no workouts for the month found");
				c.close();
				return null;
			}
			// needed to get a format for the date so we have a format for
			// converting the string in the cursor to a Date that
			// we need to put into the hash map

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			result = new HashMap<Date, Integer>();
			// step through the cursor and add each date to the hash map
			while (!(c.isAfterLast())) {
				// Log.v("DatabaseHelper-GetWodsinMonth",
				// "Datefound " + c.getString(0));
				try {
					d = sdf.parse(c.getString(0));
				} catch (ParseException e) {

					e.printStackTrace();
				}
				result.put(d, R.drawable.kettlebell); // color calendar cell
														// violet
				c.moveToNext();
			}
			c.close();
			return result;
		}

		protected void onPostExecute(HashMap<Date, Integer> result) {
			// Log.v("DatabaseHelper-GetWodsinMonth", "in onPostExceute");
			listener.CALLsetBackgroundResourceForDates(result);
		}

	}

	private class GetPRsAsync extends AsyncTask<Void, Void, ArrayList<Personal_Record_Holder>> {
		private Personal_Records_Listener listener = null;
		private ArrayList<Personal_Record_Holder> prh = null;
		private String sqlstatement;
		private Cursor c;

		public GetPRsAsync(Personal_Records_Listener listener) {
			Log.v("database helper", " get PRs async constructor");
			this.listener = listener;

		}

		@Override
		protected ArrayList<Personal_Record_Holder> doInBackground(Void... params) {
			Log.v("database helper", " get PRs async do in background");

			sqlstatement = "SELECT Main.WOD_Date, Movement_Template.Movement, Movement_Template.Weight, Movement_Template.Weight_Units, Movement_Template.Comments FROM (("
					+ " Main INNER JOIN WOD_Container ON Main._id = WOD_Container.Date_ID) INNER JOIN Movement_Template ON WOD_Container._id = Movement_Template.WOD_Container_ID) "
					+ "WHERE Rep_Max = 1 GROUP BY Movement, Weight, WOD_Date;";

			Log.v("database helper", sqlstatement);
			c = getReadableDatabase().rawQuery(sqlstatement, null);
			Log.v("database helper", " after getting the cursor");
			c.moveToFirst();
			if (c.isAfterLast()) {
				Log.v("DatabaseHelper-PRS", "no PRs found");
				c.close();
				return null;
			}
			prh = new ArrayList<Personal_Record_Holder>();

			Log.v("Database helper", "personal record holder cursor count " + c.getCount());
			while (!(c.isAfterLast())) {
				if (prh.size() == 0) {
					Log.v("database helper", "adding from empty prh");
					prh.add(new Personal_Record_Holder());
					prh.get(0).set_date(c.getString(0));
					prh.get(0).set_movement(c.getString(1).trim());
					prh.get(0).set_weight(c.getInt(2));
					prh.get(0).set_weight_units(c.getString(3));
					prh.get(0).set_comments(c.getString(4));
				} else {
					if (prh.get(prh.size() - 1).get_movement().equals(c.getString(1).trim())) {
						if (prh.get(prh.size() - 1).get_weight() < c.getInt(2)) {
							prh.get(prh.size() - 1).set_weight(c.getInt(2));
							prh.get(prh.size() - 1).set_date(c.getString(0));
							prh.get(prh.size() - 1).set_comments(c.getString(4));
						}

					} else {
						prh.add(new Personal_Record_Holder());
						prh.get(prh.size() - 1).set_date(c.getString(0));
						prh.get(prh.size() - 1).set_movement(c.getString(1));
						prh.get(prh.size() - 1).set_weight(c.getInt(2));
						prh.get(prh.size() - 1).set_weight_units(c.getString(3));
						prh.get(prh.size() - 1).set_comments(c.getString(4));
					}
				}
				c.moveToNext();
			}
			c.close();
			return prh;
		}

		protected void onPostExecute(ArrayList<Personal_Record_Holder> result) {
			Log.v("DatabaseHelper", "in onPostExceute");
			if (result == null) {
				Log.v("database helper", " no PRS found");
				listener.return_no_PRs_found();

			} else {
				Log.v("database helper", " we did find PRs");
				listener.return_PR_results(result);
			}
		}
	}

	private class GetSearchResultsAsync extends AsyncTask<Void, Void, ArrayList<Search_Results_Holder>> {
		private Workout_Search_Listener listener = null;
		private ArrayList<Search_Results_Holder> srh = null;
		private String sqlstatement;
		private String search_string;
		private String search_type;
		private Cursor c;

		public GetSearchResultsAsync(String search_string, String search_type, Workout_Search_Listener listener) {
			Log.v("database helper", " get PRs async constructor");
			this.listener = listener;
			this.search_string = search_string;
			this.search_type = search_type;

		}

		@Override
		protected ArrayList<Search_Results_Holder> doInBackground(Void... params) {
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
			// Locale.ENGLISH);

			// we query the database separately for the container and movement
			// template tables as this allows us to look at purely each one and
			// add
			// If we find a match/non-empty cursor, we start to add the
			// information. First we have to get a new holder, and then add the
			// first entry
			// before we add the date. We add the date up front before we start
			// examining what kind of container or movement template we are
			// dealing
			// with as we don't care...we just grab the date up front and roll
			// with it.
			Log.v("database helper", "workout search background with " + search_type + " and " + search_string);

			if (search_type.equals("General") || search_type.equals("WOD_Container")) {
				if (search_type.equals("General")) {
					sqlstatement = "SELECT Main.WOD_Date, WOD_Container.Timed, WOD_Container.Timed_Type, WOD_Container.Rounds, WOD_Container.Finish_Time, WOD_Container.Rounds_Finished, "
							+ "WOD_Container.IsWeight_Workout, WOD_Container.Workout_Name, WOD_Container.Comments, WOD_Container.Staggered_Rounds FROM Main INNER JOIN WOD_Container ON Main._id = WOD_Container.Date_ID "
							+ "WHERE WOD_Container.Workout_Name LIKE ? OR WOD_Container.Comments LIKE ? ;";
					Log.v("database helper", "setting general search string");
					c = getReadableDatabase().rawQuery(sqlstatement, new String[] { "%" + search_string + "%", "%" + search_string + "%" });
				} else {
					sqlstatement = "SELECT Main.WOD_Date, WOD_Container.Timed, WOD_Container.Timed_Type, WOD_Container.Rounds, WOD_Container.Finish_Time, WOD_Container.Rounds_Finished, "
							+ "WOD_Container.IsWeight_Workout, WOD_Container.Workout_Name, WOD_Container.Comments, WOD_Container.Staggered_Rounds FROM Main INNER JOIN WOD_Container ON Main._id = WOD_Container.Date_ID "
							+ "WHERE " + search_string + ";";
					c = getReadableDatabase().rawQuery(sqlstatement, null);
				}

				Log.v("database workout search", " after getting the cursor with count " + c.getCount());
				c.moveToFirst();
				if (!(c.isAfterLast())) {
					Log.v("Database workout search", "records found");

					srh = new ArrayList<Search_Results_Holder>();

					while (!c.isAfterLast()) {
						srh.add(new Search_Results_Holder());
						srh.get(srh.size() - 1).set_date(c.getString(c.getColumnIndex("WOD_Date")));
						if (!(c.getInt(c.getColumnIndex("Rounds")) == 0)) {
							srh.get(srh.size() - 1).set_bigger_text("" + c.getString(c.getColumnIndex("Rounds")) + " Rounds");
							Log.v("database helper", "added rounds");

						} else if (c.getString(c.getColumnIndex("IsWeight_Workout")).equals("T")) {
							srh.get(srh.size() - 1).set_bigger_text("Accessory Only");
							Log.v("database helper", "accessory rounds");

						} else if (!(c.getInt(c.getColumnIndex("Timed")) == 0)) {
							// srh.get(srh.size() - 1).set_date(
							// c.getString(c.getColumnIndex("WOD_Date")));
							srh.get(srh.size() - 1).set_bigger_text("" + c.getString(c.getColumnIndex("Timed")) + " " + c.getString(c.getColumnIndex("Timed_Type")));

							Log.v("database helper", "timed rounds");
						}
						srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));
						if (!(c.getString(c.getColumnIndex("Workout_Name")).trim().length() == 0))
							srh.get(srh.size() - 1).set_smaller_text("Named Workout: " + c.getString(c.getColumnIndex("Workout_Name")));
						c.moveToNext();
					}
				}
				c.close();
			}
			if (search_type.equals("General") || search_type.equals("Movement")) {
				if (search_type.equals("General")) {
					sqlstatement = "SELECT Main.WOD_Date, Movement_Template.Sets, Movement_Template.Reps, "
							+ "Movement_Template.Reps_Dynamic, Movement_Template.AMRAP, Movement_Template.Movement_Number, Movement_Template.Movement, Movement_Template.Rep_Max, "
							+ "Movement_Template.Time_of_Movement, Movement_Template.Time_of_Movement_Units, Movement_Template.Weight, Movement_Template.Weight_Units, "
							+ " Movement_Template.Percentage, Movement_Template.Length, Movement_Template.Length_Units, Movement_Template.Staggered_Rounds, "
							+ "Movement_Template.Comments FROM ((Main INNER JOIN WOD_Container ON Main._id = WOD_Container.Date_ID) "
							+ "INNER JOIN Movement_Template ON WOD_Container._id = Movement_Template.WOD_Container_ID) "
							+ "WHERE Movement_Template.Movement LIKE ? OR Movement_Template.Comments LIKE ?;";

					c = getReadableDatabase().rawQuery(sqlstatement, new String[] { "%" + search_string + "%", "%" + search_string + "%" });

				} else {
					sqlstatement = "SELECT Main.WOD_Date, Movement_Template.Sets, Movement_Template.Reps, "
							+ "Movement_Template.Reps_Dynamic, Movement_Template.AMRAP, Movement_Template.Movement_Number, Movement_Template.Movement, Movement_Template.Rep_Max, "
							+ "Movement_Template.Time_of_Movement, Movement_Template.Time_of_Movement_Units, Movement_Template.Weight, Movement_Template.Weight_Units, "
							+ " Movement_Template.Percentage, Movement_Template.Length, Movement_Template.Length_Units, Movement_Template.Staggered_Rounds, "
							+ "Movement_Template.Comments FROM ((Main INNER JOIN WOD_Container ON Main._id = WOD_Container.Date_ID) "
							+ "INNER JOIN Movement_Template ON WOD_Container._id = Movement_Template.WOD_Container_ID) " + "WHERE " + search_string + ";";

					c = getReadableDatabase().rawQuery(sqlstatement, null);
				}
				Log.v("database workout search", " after getting workout cursor entries " + c.getCount());
				c.moveToFirst();
				if (!(c.isAfterLast())) {

					if (srh == null) {
						srh = new ArrayList<Search_Results_Holder>();
					}
					Log.v("database helper", " srh count is " + srh.size());
					while (!c.isAfterLast()) {
						// sets reps type of workout to display
						// step through the cursor and add each date to the hash
						// map
						srh.add(new Search_Results_Holder());
						srh.get(srh.size() - 1).set_date(c.getString(c.getColumnIndex("WOD_Date")));
						if (!(c.getInt(c.getColumnIndex("Sets")) == 0)
								&& ((!(c.getInt(c.getColumnIndex("Reps")) == 0) || (!(c.getString(c.getColumnIndex("WOD_Date")).length() == 0))))) {

							srh.get(srh.size() - 1).set_bigger_text("" + c.getInt(c.getColumnIndex("Sets")) + "X");
							// we know its a reps/sets, so we need to see if its
							// dynamic
							// or static
							if (!(c.getInt(c.getColumnIndex("Reps")) == 0)) {
								srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + c.getInt(c.getColumnIndex("Reps")));
							} else {
								srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + c.getString(c.getColumnIndex("Reps_Dynamic")));
							}
							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + " " + c.getString(c.getColumnIndex("Movement")));
							srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));
						} else if (!(c.getInt(c.getColumnIndex("Movement_Number")) == 0)) {

							// movement type workout to display
							srh.get(srh.size() - 1).set_bigger_text("" + c.getInt(c.getColumnIndex("Movement_Number")) + " Reps");

							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + " " + c.getString(c.getColumnIndex("Movement")));
							srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));

						} else if (!(c.getInt(c.getColumnIndex("AMRAP")) == 0)) {

							srh.get(srh.size() - 1).set_bigger_text("AMRAP ");

							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + "" + c.getString(c.getColumnIndex("Movement")));
							srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));
						} else if (!(c.getInt(c.getColumnIndex("Rep_Max")) == 0)) {
							srh.get(srh.size() - 1).set_bigger_text("" + c.getInt(c.getColumnIndex("Rep_Max")) + " Rep Max");

							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + " " + c.getString(c.getColumnIndex("Movement")));
							srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));
						} else if (!(c.getInt(c.getColumnIndex("Time_of_Movement")) == 0)) {
							srh.get(srh.size() - 1).set_bigger_text(
									"" + c.getString(c.getColumnIndex("Time_of_Movement")) + c.getString(c.getColumnIndex("Time_of_Movement_Units")));

							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + " " + c.getString(c.getColumnIndex("Movement")));
							srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));
						} else if (!(c.getInt(c.getColumnIndex("Length")) == 0)) {
							srh.get(srh.size() - 1).set_bigger_text("" + c.getString(c.getColumnIndex("Length")) + c.getString(c.getColumnIndex("Length_Units")));

							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + " " + c.getString(c.getColumnIndex("Movement")));
							srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));
						} else if (!(c.getInt(c.getColumnIndex("Staggered_Rounds")) == 0)) {

							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + "" + c.getString(c.getColumnIndex("Movement")));
							srh.get(srh.size() - 1).set_smaller_text(c.getString(c.getColumnIndex("Comments")));
						}

						if (!(c.getInt(c.getColumnIndex("Weight")) == 0)) {
							srh.get(srh.size() - 1).set_bigger_text(
									srh.get(srh.size() - 1).get_bigger_text() + " " + c.getInt(c.getColumnIndex("Weight")) + c.getString(c.getColumnIndex("Weight_Units")));
						}

						if (!(c.getInt(c.getColumnIndex("Percentage")) == 0)) {
							srh.get(srh.size() - 1).set_bigger_text(srh.get(srh.size() - 1).get_bigger_text() + " @" + c.getInt(c.getColumnIndex("Percentage")) + "%");
						}

						c.moveToNext();
					}
				}
				c.close();
			}
			if (srh == null) {
				return null;
			}
			Collections.sort(srh, Collections.reverseOrder());

			return srh;
		}

		protected void onPostExecute(ArrayList<Search_Results_Holder> result) {
			Log.v("DatabaseHelper", "in onPostExceute");
			if (result == null) {
				Log.v("database helper", " no searchs found going to holder listener");
				listener.return_no_results_found();

			} else {
				Log.v("database helper", " we did find search records going to holder listener");
				listener.return_search_results(result);
			}
		}
	}

	private class get_rep_max_async extends AsyncTask<Void, Void, ArrayList<String>> {

		private Rep_Max_Item_Listener listener;

		public get_rep_max_async(Rep_Max_Item_Listener listener) {
			Log.v("database helper", " get rep max items for spinner constructor");
			this.listener = listener;
		}

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			ArrayList<String> results = null;
			String sqlstatement;

			sqlstatement = "SELECT DISTINCT Movement FROM Movement_Template WHERE Rep_Max = 1 ORDER BY Movement;";
			Cursor c = getReadableDatabase().rawQuery(sqlstatement, null);

			c.moveToFirst();
			if (!(c.isAfterLast())) {
				results = new ArrayList<String>();
				while (!(c.isAfterLast())) {
					results.add(c.getString(0));
					c.moveToNext();
				}
				Log.v("Database helper", "found rep maxes for spinner, returning with count of " + results.size());
				return results;
			} else {
				Log.v("Database helper", "didn't find rep maxes for spinner, returning");
				return null;
			}

		}

		protected void onPostExecute(ArrayList<String> result) {
			if (result == null)
				listener.return_Rep_Max_Items(null);
			else
				listener.return_Rep_Max_Items(result);
		}
	}

	private class get_analytics_data_async extends AsyncTask<Void, Void, ArrayList<Analytics_Data_Holder>> {

		private Rep_Max_Item_Listener listener;
		private String movement;

		public get_analytics_data_async(Rep_Max_Item_Listener listener, String movement) {
			Log.v("database helper", " get rep max items for spinner constructor");
			this.listener = listener;
			this.movement = movement;
		}

		@Override
		protected ArrayList<Analytics_Data_Holder> doInBackground(Void... params) {

			ArrayList<Analytics_Data_Holder> adh = null;
			String sqlstatement;
			sqlstatement = "SELECT Main.WOD_Date, Movement_Template.Movement, Movement_Template.Weight, Movement_Template.Weight_Units FROM (("
					+ " Main INNER JOIN WOD_Container ON Main._id = WOD_Container.Date_ID) INNER JOIN Movement_Template ON WOD_Container._id = Movement_Template.WOD_Container_ID) "
					+ "WHERE Movement = '" + movement + "' AND Rep_Max = 1 GROUP BY Movement, Weight, WOD_Date;";

			Cursor c = getReadableDatabase().rawQuery(sqlstatement, null, null);
			c.moveToFirst();
			Log.v("Database helper", "got the cursor with " + c.getCount() + " records");
			if (!(c.isAfterLast())) {
				adh = new ArrayList<Analytics_Data_Holder>();
				while (!(c.isAfterLast())) {
					adh.add(new Analytics_Data_Holder(movement));
					adh.get(adh.size() - 1).set_date(c.getString(c.getColumnIndex("WOD_Date")));
					adh.get(adh.size() - 1).set_weight(c.getInt(c.getColumnIndex("Weight")));
					Log.v("Database Helper", " " + c.getString(c.getColumnIndex("WOD_Date")) + " - " + c.getInt(c.getColumnIndex("Weight")));
					c.moveToNext();
				}

			}
			Collections.sort(adh);
			return adh;
		}

		protected void onPostExecute(ArrayList<Analytics_Data_Holder> result) {
			if (result == null)
				listener.return_Analytics_data(null);
			else
				listener.return_Analytics_data(result);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.v("In Database Helper", "on Create of the Database");
		try {
			db.beginTransaction();
			db.execSQL("CREATE TABLE Main (_id INTEGER PRIMARY KEY AUTOINCREMENT, WOD_Date TEXT);");
			db.execSQL("CREATE TABLE WOD_Container (_id INTEGER PRIMARY KEY AUTOINCREMENT, Date_ID INTEGER, Container_Order INTEGER, Timed INTEGER, Timed_Type TEXT, Rounds INTEGER, Finish_Time TEXT, Rounds_Finished REAL, IsWeight_Workout TEXT, Workout_Name TEXT, Comments TEXT, Staggered_Rounds TEXT);");
			db.execSQL("CREATE TABLE Movement_Template (_id INTEGER PRIMARY KEY AUTOINCREMENT, WOD_Container_ID INTEGER, Movement_Order INTEGER, Sets INTEGER, Reps INTEGER, AMRAP Integer, Movement_Number Integer, Movement TEXT, Rep_Max INTEGER, Time_of_Movement TEXT, Time_of_Movement_Units Text, Length INTEGER, Length_Units TEXT, Weight INTEGER, Weight_Units TEXT, Percentage INTEGER, Comments TEXT, Staggered_Rounds INTEGER, Reps_Dynamic TEXT);");
			db.execSQL("CREATE TABLE VARIABLES (_id INTEGER PRIMARY KEY AUTOINCREMENT, Application_Title TEXT, Database_Name TEXT, Schema_Version INTEGER);");
			db.execSQL("INSERT INTO VARIABLES (Application_Title, Database_Name, Schema_Version) VALUES ('WODJournal', 'WODS.db'," + SCHEMA_VERSION + ");");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			Log.v("In Database Helper", "Successfully created all values");
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// using an integer to gives us an idea of how we are doing an
		// incremental update.
		// Instead of having all sorts of if statements that test all manners of
		// schema combinations, we merely
		// start at the bottom and work our way up, incrementally updating the
		// database, and using the 'upgraded_to"
		// variable to help us keep track of the incremental update as the
		// oldVersion and newVersion are the min/max
		// of the current database schema we are operating on.

		int upgraded_to = oldVersion;
		Log.v("Database helper", "On upgrade");
		// Shelly was coming across workouts that had a set/reps format, but it
		// was an AMRAP with a 2RM, so its possible that every set could have
		// different reps.
		// We needed to create another entry in the database that allowed for
		// the user to
		// put in multiple reps, separated by a '-', much like the staggered
		// workouts.
		// Shelly would structure the workout to be a ARMAP Bench Press, but
		// will put 2RM
		// into the comments

		if (upgraded_to == 1 && newVersion <= 3) {
			try {
				db.beginTransaction();
				db.execSQL("ALTER TABLE Movement_Template ADD Reps_Dynamic TEXT;");
				db.execSQL("UPDATE Movement_Template SET Reps_Dynamic= '';");
				db.setTransactionSuccessful();
			} finally {
				// make sure the above statements execute before committing, as
				// we will roll back if the statements fail to work.
				db.endTransaction();
				upgraded_to++;
				Log.v("database helper", "succsessfully upgraded to schema version " + upgraded_to);
			}
		}
		// this version was done to handle the import of an actual DB file and
		// the fact that we need to embed information about the database
		// do make it easier to determine if the user gave us a real WODJOURNAL
		// database or clicked on a different database or invalid file.
		// By incorporating information into the database that we KNOW came from
		// us, it makes it easier to do some upfront error checking.

		if (upgraded_to == 2 && newVersion <= 3) {
			try {
				db.beginTransaction();

				db.execSQL("CREATE TABLE VARIABLES (_id INTEGER PRIMARY KEY AUTOINCREMENT, Application_Title TEXT, Database_Name TEXT, Schema_Version INTEGER);");
				db.execSQL("INSERT INTO VARIABLES (Application_Title, Database_Name, Schema_Version) VALUES ('WODJournal', 'WODS.db'," + (upgraded_to + 1) + ");");
				db.setTransactionSuccessful();
			} finally {
				// make sure the above statements execute before committing, as
				// we will roll back if the statements fail to work.
				db.endTransaction();
				upgraded_to++;
				Log.v("database helper", "succsessfully upgraded to schema version " + upgraded_to);
			}
		}

	}
}