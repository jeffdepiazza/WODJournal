package com.floridaseabee.wodjournal;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;

public class Utilities_Activity extends Activity implements Utilities_File_Picker_Dialog.Send_back_clicks, Utilities_Holder_Fragment.Dialog_Dismiss {

	public Utilities_First_Display_Fragment utilities_first_fragment;
	public Utilities_Second_Display_Fragment utilities_second_fragment;
	public Utilities_Holder_Fragment utilities_holder_fragment;
	public String[] headers_title;
	public String[] headers_summary;
	public String[] section_title;
	public String[] section_summary;
	public int showing_second_type = 0;
	public static Boolean show_progress_dialog = false;
	private String EXPORT_FILE_NAME = "";
	private static final int REQUEST_CODE = 6384; // onActivityResult request
													// code
	private static final String file_select_status = "FILE_SELECT_STATUS";
	private static int file_status = 0; // 0= no selection, 1=xml restore,
										// 2=file restore
	private String directory;
	private String type_backup;
	private Utilities_Send_Task_Listener task_up;
	private static final int ACTIVITY_CHOOSE_FILE = 3;
	private static final String SHOWING_SECOND = "showing_second";
	private Utilities_File_Picker_Dialog file_picker_dialog;
	private Utilities_Dialog_Listener dialog_listener;
	static ProgressDialog progress_dialog;

	protected void onCreate(Bundle savedInstanceState) {
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		task_up = new Utilities_Send_Task_Listener() {

			public void sent_task(int backup_restore, int where, String directory) {
				switch (backup_restore) {

				case 0:
					if (where == 0) {
						Log.v("Utilities Activity", "got filebackup listener with " + backup_restore + ", " + where);
						/*
						 * // Use the GET_CONTENT intent from the utility class
						 * Intent target = FileUtils.createGetContentIntent();
						 * // Create the chooser Intent Intent intent =
						 * Intent.createChooser( target,
						 * getString(R.string.enter_filename)); try {
						 * startActivityForResult(intent, REQUEST_CODE); } catch
						 * (ActivityNotFoundException e) { // The reason for the
						 * existence of aFileChooser }
						 */
						file_export(directory);

					} else if (where == 1) {
						Log.v("utilities activity", "back up .xml");
						xmlexport(directory);
					}

					break;

				case 1:
					if (where == 0) {
						Log.v("utilities activity", "restore from file");
						file_status = 1;
						Log.v("Utilities Activity", "got listener with " + backup_restore + ", " + where);
						// Use the GET_CONTENT intent from the utility class
						Intent target = FileUtils.createGetContentIntent();
						// Create the chooser Intent
						Intent intent = Intent.createChooser(target, getString(R.string.enter_filename));
						try {
							startActivityForResult(intent, REQUEST_CODE);
						} catch (ActivityNotFoundException e) {
							// The reason for the existence of aFileChooser
						}
					} else if (where == 1) {
						Log.v("utilities activity", "restore from .xml");
						file_status = 1;
						Log.v("Utilities Activity", "got listener with " + backup_restore + ", " + where);
						// Use the GET_CONTENT intent from the utility class
						Intent target = FileUtils.createGetContentIntent();
						// Create the chooser Intent
						Intent intent = Intent.createChooser(target, getString(R.string.enter_filename));
						try {
							startActivityForResult(intent, REQUEST_CODE);
						} catch (ActivityNotFoundException e) {
							// The reason for the existence of aFileChooser
						}

					}

				}
			}

			public void spawn_second_fragment(int type) {
				// had to send the rest of the code outside of the listener
				// setup, as we are going to send this listener to the second
				// fragment when its spawned, but we can't do so while we are
				// still in the middle of creating it (kinda like a circular
				// reference/paradox). we could still generate the second
				// fragment and now show it unless we get the event, but figured
				// this was just as easy to implement
				spawn_second(type);

			}
		};

		if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
			Log.v(" utilities activity", "null holder");
			utilities_holder_fragment = Utilities_Holder_Fragment.newInstance();
			getFragmentManager().beginTransaction().add(android.R.id.content, utilities_holder_fragment, "utilities_holder").commit();
			utilities_first_fragment = Utilities_First_Display_Fragment.newInstance();
			getFragmentManager().beginTransaction().add(android.R.id.content, utilities_first_fragment, "utilities_first").commit();
		} else {
			Log.v(" utilities activity", "non-null holder");
			utilities_holder_fragment = (Utilities_Holder_Fragment) getFragmentManager().findFragmentByTag("utilities_holder");
			utilities_first_fragment = (Utilities_First_Display_Fragment) getFragmentManager().findFragmentByTag("utilities_first");
			if (getFragmentManager().findFragmentByTag("utilities_second") != null) {
				// Since the activity is being created, we need to reset the
				// state if the second fragment was being displayed... which
				// means that if the first fragment is showing, we need to also
				// hide it here, BUT do it before we reattach the second
				// fragment. ALSO, do not add this transaction to the back stack
				// as the activity already somehow knows what is going on as I
				// tried to remove the fragment first in onsaveinstancestate,
				// but it didn't let me do that there, so I tried to redo the
				// entire transaction including adding it to the back stack, and
				// it did that, but when pressing back, both the first and
				// second fragments were on top of each other and showing.
				// Turned out that all we needed to do was to remove the
				// addtobackstack, and just hide the first fragment.

				getFragmentManager().beginTransaction().hide(utilities_first_fragment).commit();
				utilities_second_fragment = (Utilities_Second_Display_Fragment) getFragmentManager().findFragmentByTag("utilities_second");
				utilities_second_fragment.setListener(task_up);
				Log.v("Utilties Activity", "non null holder and spawinging second fragment");
			}

		}

		utilities_first_fragment.setListener(task_up);

		dialog_listener = new Utilities_Dialog_Listener() {

			public void dismiss_progress_dialog() {
				dismiss_dialog();
			}

			public void update_dialog(int progress) {
				set_progress_dialog(progress);
			}

		};

		utilities_holder_fragment.setListener(dialog_listener);

		if (savedInstanceState != null) {
			Log.v("utilities activity", " non null savedinstancestate");
			file_picker_dialog = (Utilities_File_Picker_Dialog) getFragmentManager().findFragmentByTag("dialog_filename");
			if (file_picker_dialog != null) {
				Log.v("utilties activity", " resetting dialog parent listener");
				file_picker_dialog.setListener(this);
			}
			if (savedInstanceState.getBoolean("PROGRESS_BAR_UP")) {
				Log.v("utilities activity", "resetting progress dialog");
				progress_dialog = utilities_holder_fragment.get_progress_dialog();
				progress_dialog.show();
			}
			file_status = savedInstanceState.getInt(file_select_status);
		}

	}

	private void spawn_second(int type) {
		utilities_second_fragment = Utilities_Second_Display_Fragment.newInstance(type);
		getFragmentManager().beginTransaction().hide(utilities_first_fragment).add(android.R.id.content, utilities_second_fragment, "utilities_second").addToBackStack(null)
				.commit();
		showing_second_type = type;
		utilities_second_fragment.setListener(task_up);
		Log.v("utilities activity", "showing second fragment");

	}

	@Override
	public void onResume() {
		super.onResume();
		progress_dialog = utilities_holder_fragment.get_progress_dialog();
		if (!(progress_dialog == null) && show_progress_dialog) {
            Log.v("Utilities Activity", "on Resume - restoring dialog");
            progress_dialog.show();
		}
		Log.v("Utilities Activity", "on Resume");
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.v("Utilties Activity", " on Save instance state");
		if (getFragmentManager().findFragmentByTag("utilities_second") != null) {
			state.putInt(SHOWING_SECOND, showing_second_type);
			Log.v("Utilties Activity", "On rotate, with second screen type of " + showing_second_type);
		} else
			state.putInt(SHOWING_SECOND, 3); // 3 will be the standard of "not
												// showing the second screen, so
												// don't bother
		if (progress_dialog != null) {
			Log.v("utilities activity", "storing progress dialog");
			state.putBoolean("PROGRESS_BAR_UP", show_progress_dialog);
			utilities_holder_fragment.set_progress_dialog(progress_dialog);
			progress_dialog.dismiss();
			progress_dialog = null;
		}
		state.putInt(file_select_status, 0);
	}

	public void xmlexport(String DATASUBDIRECTORY) {
		Log.v("utilities activity", " xml export");
		this.directory = DATASUBDIRECTORY;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_filename");
		if (prev != null) {
			ft.remove(prev);
		}
		type_backup = "xml";
		file_picker_dialog = Utilities_File_Picker_Dialog.newInstance(getResources().getString(R.string.xml_dialog_title), DATASUBDIRECTORY, "xml");
		file_picker_dialog.setListener(this);
		file_picker_dialog.show(ft, "dialog_filename");

	}

	public void file_export(String DATASUBDIRECTORY) {
		Log.v("utilities activity", " xml export");
		this.directory = DATASUBDIRECTORY;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog_filename");
		if (prev != null) {
			ft.remove(prev);
		}
		type_backup = "file";
		file_picker_dialog = Utilities_File_Picker_Dialog.newInstance(getResources().getString(R.string.file_dialog_title), DATASUBDIRECTORY, "file");
		file_picker_dialog.setListener(this);
		file_picker_dialog.show(ft, "dialog_filename");

	}

	@Override
	public void send_back_ok(String filename) {
		
		// chosen the filename and now we are exporting either a database file or xml.
		
		spawn_progress_dialog(getResources().getString(R.string.utilities_xml_export_progress_title),
				getResources().getString(R.string.utilities_xml_export_progress_message) + " " + filename);
		if (type_backup == "file") {
			utilities_holder_fragment.backup_to_file(Utilities_Activity.this, directory, filename);
		} else
			// xml backup
			utilities_holder_fragment.backup_to_xml(Utilities_Activity.this, directory, filename);
	}

	public void spawn_progress_dialog(String title, String message) {
		progress_dialog = new ProgressDialog(this);
		progress_dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress_dialog.setProgress(0);
		progress_dialog.setMax(100);
		progress_dialog.setIndeterminate(false);
		progress_dialog.setTitle(title);
		progress_dialog.setMessage(message);
        progress_dialog.setCanceledOnTouchOutside(false);
        progress_dialog.setCancelable(false);
        progress_dialog.show();
		show_progress_dialog = true;
		utilities_holder_fragment.set_progress_dialog(progress_dialog);
	}

	@Override
	public void send_back_cancel() {
		// TODO Auto-generated method stub

	}

	public static void set_progress_dialog(int current_progress) {
		// Log.v("utilities activity", "current progress " + current_progress);
		// we test for null here in case the screen is off and we get a null ponter exception
		// as the progress dialog does not exist anymore when the screen is off as the activity
		// has been halted.
		if (!(progress_dialog == null )) {
			progress_dialog.setProgress(current_progress);
		}
	}

	@Override
	public void dismiss_dialog() {
		Log.v("utilities activity", "dismissing dialog");
		if (!(progress_dialog == null)) {
			progress_dialog.dismiss();
		}
		show_progress_dialog = false;
		utilities_holder_fragment.remove_progress_dialog();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:
			// If the file selection was successful
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();
					Log.i("Utilities Activity", "Uri = " + uri.toString());
					try {
						// Get the file path from the URI
						final String path = FileUtils.getPath(this, uri);
						if (file_status == 0) {
							Log.v("utilities activity", "got filename for file restore");
							spawn_progress_dialog("file restore title", "file restore message");
							utilities_holder_fragment.restore_from_file(this, path);
						} else if (file_status == 1) {
							Log.v("utilities activity", "got filename for xml restore");
							spawn_progress_dialog(getResources().getString(R.string.utilities_xml_restore_progress_title),
									getResources().getString(R.string.utilities_xml_restore_progress_message) + " " + path);
							utilities_holder_fragment.restore_from_xml(this, path);
						}
						Toast.makeText(this, "File Selected: " + path, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Log.v("FileSelectorTesActivity", "File select error", e);
					}
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
