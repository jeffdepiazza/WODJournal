package com.floridaseabee.wodjournal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import android.util.Xml;

public class XMLParser {
	// this is the class that will parse the opened up xml, check some basic
	// settings to ensure that its
	// an XML for this application, and then beging to parse in the data. care
	// should be taken in the
	// future to create the import database based upon the schema that the XML
	// has...import the data
	// and then upgrade the database
	private InputStream in;
	private Integer database_version =0;
	private static final String MAIN = "Main";
	private static final String WOD_DATE = "WOD_Date";
	private static final String WOD_CONTAINER = "WOD_Container";
	private static final String CONTAINER_ORDER = "Container_Order";
	private static final String TIMED = "Timed";
	private static final String TIMED_TYPE = "Timed_Type";
	private static final String ROUNDS = "Rounds";
	private static final String FINISH_TIME = "Finish_Time";
	private static final String ROUNDS_FINISHED = "Rounds_Finished";
	private static final String ISWEIGHT_WORKOUT = "IsWeight_Workout";
	private static final String WORKOUT_NAME = "Workout_Name";
	private static final String COMMENTS = "Comments";
	private static final String STAGGERED_ROUNDS = "Staggered_Rounds";
	private static final String MOVEMENT_TEMPLATE = "Movement_Template";
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

	private ArrayList<Date_Holder> date_holder;
	private String error_to_return = null;
	private XML_Imported_Database XMLid;
	private static final String ns = null;
	private XmlPullParser parser;

	XMLParser(String IMPORT_FILE_NAME) throws XmlPullParserException, IOException {
		//Log.v("XMLParser", " xml parser constructor");
		// date_holder = new ArrayList<Date_Holder>();
		//Log.v("XML Parser", "" + IMPORT_FILE_NAME);
		in = new FileInputStream(new File(IMPORT_FILE_NAME));
		parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(in, null);

	}

	public XML_Imported_Database start_parse() throws IOException, XmlPullParserException {
		parser.nextTag();
		//Log.v("XML Parser", "opened file and about to parse");
		try {
			if (verify_app(parser)) {
				if (verify_database(parser)) {
					get_schema(parser);
					date_holder = new ArrayList<Date_Holder>();
					while (parser.next() != XmlPullParser.END_DOCUMENT) {
						String name = parser.getName();
						if (name.equals(MAIN)) {
							get_main(parser);
						}
					}
				}
			}
		} catch (XmlPullParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error_to_return = Log.getStackTraceString(e);
		}
		in.close();
		// we did all the work with a regular date holder, and now we need to get ready to return
		// major 
		XMLid = new XML_Imported_Database(error_to_return, database_version);
		XMLid.set_date_holder_array(date_holder);
		return XMLid;
	}

	private boolean verify_app(XmlPullParser parser) throws IOException, XmlPullParserException {
		//Log.v("XML parser", " current name = " + parser.getName());
		parser.require(XmlPullParser.START_TAG, ns, "App_Name");
		parser.next();
		//Log.v("XML Parser ", "verify app = " + parser.getText());
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, ns, "App_Name");
		parser.nextTag();
		return true;
	}

	private boolean verify_database(XmlPullParser parser) throws IOException, XmlPullParserException {
		//Log.v("XML parser", " current name = " + parser.getName());
		parser.require(XmlPullParser.START_TAG, ns, "database_name");
		parser.next();
		//Log.v("XML Parser", "database_name= " + parser.getText());
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, ns, "database_name");
		parser.nextTag();
		return true;
	}

	private boolean get_schema(XmlPullParser parser) throws IOException, XmlPullParserException {
		//Log.v("XML parser", " current name = " + parser.getName());
		parser.require(XmlPullParser.START_TAG, ns, "schema_version");
		parser.next();
		database_version = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "schema_version= " + database_version);
		parser.nextTag();
		parser.require(XmlPullParser.END_TAG, ns, "schema_version");
		return true;
	}

	private void get_main(XmlPullParser parser) throws XmlPullParserException, IOException {
		// now onto the recurisive part, and grabbing the first main.
		String date_entry;
		while (parser.getEventType() != XmlPullParser.END_TAG) {
			//Log.v("XML parser", " got Main with = " + parser.getName());
			parser.nextTag();
			String name = parser.getName();
			if (name.equals(WOD_DATE)) {
				//Log.v("XML parser", " got WOD_DATE tag");
				parser.next();
				date_entry = parser.getText();
				date_holder.add(new Date_Holder(date_entry));
				read_WOD_Date(parser);
			}
		}

	}

	private void read_WOD_Date(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "WOD_Date = " + parser.getText()); // get WOD_Date
																// from here!
		parser.next(); // skip the end tag for WOD Date
		parser.next();
		while (parser.nextTag() != XmlPullParser.END_TAG) {
			String name = parser.getName();
			//Log.v("XML Parser", "read WOD Date container = " + name);
			if (name.equals(WOD_CONTAINER)) {
				//Log.v("XML Parser", "Got the wod container tag");
				date_holder.get(date_holder.size() - 1).WOD_Container_Holder.add(new WOD_Container_Holder());
				read_container(parser);
				//Log.v("XML Parser", "done reading container");
			}
		}

	}

	private void read_container(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read container tag = " + parser.getName());
		parser.nextTag(); // skip the end tag for WOD Date

		// the container tag leads the entire container item, so we now need to
		//Log.v("XML Parser", "container contents =" + parser.getName());
		while (parser.getEventType() != XmlPullParser.END_TAG) {
			//Log.v("XML Parser", " getEventType = " + parser.getEventType());
			String name = parser.getName();
			if (name.equals(CONTAINER_ORDER)) {
				read_container_order(parser);
			} else if (name.equals(TIMED)) {
				read_timed(parser);
			} else if (name.equals(TIMED_TYPE)) {
				read_timed_type(parser);
			} else if (name.equals(ROUNDS)) {
				read_rounds(parser);
			} else if (name.equals(FINISH_TIME)) {
				read_finish_time(parser);
			} else if (name.equals(ROUNDS_FINISHED)) {
				read_rounds_finished(parser);
			} else if (name.equals(ISWEIGHT_WORKOUT)) {
				read_isweight_workout(parser);
			} else if (name.equals(WORKOUT_NAME)) {
				read_workout_name(parser);
			} else if (name.equals(COMMENTS)) {
				read_comments(parser);
			} else if (name.equals(STAGGERED_ROUNDS)) {
				read_staggered_rounds(parser);
			} else if (name.equals(MOVEMENT_TEMPLATE)) {
				date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
						.add(new Movement_Container_Holder());
				read_movement_template(parser);
			}
			parser.nextTag();
			while ((parser.getEventType() != XmlPullParser.START_TAG) && (parser.getEventType() != XmlPullParser.END_TAG)) {
				parser.nextTag();
				//Log.v("XML Parser", "skipping whitespace in wod container entry");
			}
		}
		//Log.v("XML Parser", "End of wod container entry");
	}

	private void read_movement_template(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read container tag = " + parser.getName());
		parser.nextTag(); // skip the end tag for WOD Date

		// the movement template container tag leads the entire container item,
		// so we now need to
		//Log.v("XML Parser", "movement template contents =" + parser.getName());
		while (parser.getEventType() != XmlPullParser.END_TAG) {
			//Log.v("XML Parser", " getEventType = " + parser.getEventType());
			String name = parser.getName();
			if (name.equals(MOVEMENT_ORDER)) {
				read_movement_order(parser);
			} else if (name.equals(SETS)) {
				read_sets(parser);
			} else if (name.equals(REPS)) {
				read_reps(parser);
			} else if (name.equals(AMRAP)) {
				read_amrap(parser);
			} else if (name.equals(MOVEMENT_NUMBER)) {
				read_movement_number(parser);
			} else if (name.equals(MOVEMENT)) {
				read_movement(parser);
			} else if (name.equals(REP_MAX)) {
				read_rep_max(parser);
			} else if (name.equals(TIME_OF_MOVEMENT)) {
				read_time_of_movement(parser);
			} else if (name.equals(TIME_OF_MOVEMENT_UNITS)) {
				read_time_of_movement_units(parser);
			} else if (name.equals(LENGTH)) {
				read_length(parser);
			} else if (name.equals(LENGTH_UNITS)) {
				read_length_units(parser);
			} else if (name.equals(WEIGHT)) {
				read_weight(parser);
			} else if (name.equals(WEIGHT_UNITS)) {
				read_weight_units(parser);
			} else if (name.equals(PERCENTAGE)) {
				read_percentage(parser);
			} else if (name.equals(COMMENTS)) {
				read_movement_comments(parser);
			} else if (name.equals(STAGGERED_ROUNDS)) {
				read_movement_staggered_rounds(parser);
			} else if (name.equals(REPS_DYNAMIC)) {
				read_reps_dynamic(parser);
			}
			parser.nextTag();
			while ((parser.getEventType() != XmlPullParser.START_TAG) && (parser.getEventType() != XmlPullParser.END_TAG)) {
				parser.nextTag();
				//Log.v("XML Parser", "skipping whitespace in movement template entry");
			}
		}
		//Log.v("XML Parser", "End of movement container entry/entries");
	}

	private void read_container_order(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read container order tag = " + parser.getName());
		parser.next();
		Integer container_order = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "container order = " + container_order);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_WOD_Container_Order(
				container_order);
		parser.nextTag();
	}

	private void read_timed(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read timed tag = " + parser.getName());
		parser.next();
		Integer timed = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "timed  = " + timed);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Timed(timed);
		parser.nextTag();
	}

	private void read_timed_type(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read timed type tag = " + parser.getName());
		parser.next();
		String timed_type = parser.getText();
		//Log.v("XML Parser", "timed typed = " + timed_type);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Timed_Type(timed_type);
		parser.nextTag();
	}

	private void read_rounds(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read rounds tag = " + parser.getName());
		parser.next();
		Integer rounds = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "rounds = " + rounds);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Rounds(rounds);
		parser.nextTag();
	}

	private void read_finish_time(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read finished time tag = " + parser.getName());
		parser.next();
		String finished_time = parser.getText();
		//Log.v("XML Parser", "finished time = " + finished_time);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Finish_Time(finished_time);
		parser.nextTag();
	}

	private void read_rounds_finished(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read rounds finished tag = " + parser.getName());
		parser.next();
		Float rounds_finished = Float.parseFloat(parser.getText());
		//Log.v("XML Parser", "rounds finished = " + rounds_finished);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Rounds_Finished(rounds_finished);
		parser.nextTag();
	}

	private void read_isweight_workout(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read is weight workout tag = " + parser.getName());
		parser.next();
		String isweight = parser.getText();
		//Log.v("XML Parser", "is weight workout = " + isweight);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_IsWeight_Workout(isweight);
		parser.nextTag();
	}

	private void read_workout_name(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read workout name tag = " + parser.getName());
		parser.next();
		String workout_name = parser.getText();
		//Log.v("XML Parser", "workout name = " + workout_name);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Workout_Name(workout_name);
		parser.nextTag();
	}

	private void read_comments(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read comments tag = " + parser.getName());
		parser.next();
		String comments = parser.getText();
		//Log.v("XML Parser", "comments = " + comments);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Comments(comments);
		parser.nextTag();
	}

	private void read_staggered_rounds(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read staggered rounds  tag = " + parser.getName());
		parser.next();
		String staggered_rounds = parser.getText();
		//Log.v("XML Parser", "staggered rounds = " + staggered_rounds);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).set_Staggered_Rounds(
				staggered_rounds);
		parser.nextTag();
	}

	private void read_movement_order(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read movement order  tag = " + parser.getName());
		parser.next();
		Integer movement_order = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "movement_order = " + movement_order);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
				.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Movement_Order(movement_order);

		parser.nextTag();
	}

	private void read_sets(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read sets tag = " + parser.getName());
		parser.next();
		Integer sets = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "sets = " + sets);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_sets(sets);
		parser.nextTag();
	}

	private void read_reps(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read reps  tag = " + parser.getName());
		parser.next();
		Integer reps = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "reps = " + reps);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_reps(reps);
		parser.nextTag();
	}

	private void read_amrap(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read amrap  tag = " + parser.getName());
		parser.next();
		Integer AMRAP = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "amrap = " + AMRAP);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_AMRAP(AMRAP);
		parser.nextTag();
	}

	private void read_movement_number(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read movement number  tag = " + parser.getName());
		parser.next();
		Integer movement_number = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "movement number = " + movement_number);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Movement_Number(movement_number);
		parser.nextTag();
	}

	private void read_movement(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read movement tag = " + parser.getName());
		parser.next();
		String movement = parser.getText();
		//Log.v("XML Parser", "movement = " + movement);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Movement(movement);
		parser.nextTag();
	}

	private void read_rep_max(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read rep max  tag = " + parser.getName());
		parser.next();
		Integer rep_max = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "rep max = " + rep_max);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Rep_Max(rep_max);
		parser.nextTag();
	}

	private void read_time_of_movement(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read time of movement tag = " + parser.getName());
		parser.next();
		// in this case, the database has a text value for the 
		Integer time_of_movement = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "time of movement = " + time_of_movement);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Time_of_Movement(time_of_movement);
		parser.nextTag();
	}

	private void read_time_of_movement_units(XmlPullParser parser) throws XmlPullParserException, IOException {
		////Log.v("XML Parser", "read time_of_movement_units tag = " + parser.getName());
		parser.next();
		String time_of_movement_units = parser.getText();
		////Log.v("XML Parser", "time_of_movement_units = " + time_of_movement_units);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Time_of_Movement_Units(time_of_movement_units);
		parser.nextTag();
	}

	private void read_length(XmlPullParser parser) throws XmlPullParserException, IOException {
		////Log.v("XML Parser", "read length tag = " + parser.getName());
		parser.next();
		Integer length = Integer.parseInt(parser.getText());
		////Log.v("XML Parser", "length = " + length);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_length(length);
		parser.nextTag();
	}

	private void read_length_units(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read length units tag = " + parser.getName());
		parser.next();
		String length_units = parser.getText();
		//Log.v("XML Parser", "length units = " + length_units);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_length_units(length_units);
		parser.nextTag();
	}

	private void read_weight(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read weight tag = " + parser.getName());
		parser.next();
		Integer weight = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "weight  = " + weight);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_weight(weight);
		parser.nextTag();
	}

	private void read_weight_units(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read weight units tag = " + parser.getName());
		parser.next();
		String weight_units = parser.getText();
		//Log.v("XML Parser", "weight units = " + weight_units);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_weight_units(weight_units);
		parser.nextTag();
	}

	private void read_percentage(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read percentage tag = " + parser.getName());
		parser.next();
		Integer percentage = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "percentage  = " + percentage);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_percentage(percentage);
		parser.nextTag();
	}

	private void read_movement_comments(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read movement comments tag = " + parser.getName());
		parser.next();
		String comments = parser.getText();
		//Log.v("XML Parser", "movement comments  = " + comments);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Comments(comments);
		parser.nextTag();
	}

	private void read_movement_staggered_rounds(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read movement staggered rounds tag = " + parser.getName());
		parser.next();
		Integer staggered_rounds = Integer.parseInt(parser.getText());
		//Log.v("XML Parser", "staggered rounds comments  = " + staggered_rounds);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_Staggered_Rounds(staggered_rounds);
		parser.nextTag();
	}

	private void read_reps_dynamic(XmlPullParser parser) throws XmlPullParserException, IOException {
		//Log.v("XML Parser", "read reps dynamic tag = " + parser.getName());
		parser.next();
		String reps_dynamic = parser.getText();
		//Log.v("XML Parser", "reps dynamic comments  = " + reps_dynamic);
		date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).Movement_Container_Holder
		.get(date_holder.get(date_holder.size() - 1).WOD_Container_Holder.get(date_holder.get(date_holder.size() - 1).get_WOD_Container_size() - 1).get_Movement_size() - 1).set_reps_dynamic(reps_dynamic);
		parser.nextTag();
	}
}
